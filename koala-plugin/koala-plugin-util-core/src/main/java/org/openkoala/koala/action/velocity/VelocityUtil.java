package org.openkoala.koala.action.velocity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.JarURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.util.EclipseUrlParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将模板生成文件
 * 
 * @author lingen.liu
 * 
 */
@SuppressWarnings("rawtypes")
public class VelocityUtil {

	// 编码格式
	private static final String ENCODING = "UTF-8";
	
	private static final Logger logger = LoggerFactory.getLogger(VelocityUtil.class);

	/**
	 * 将模板转化成为文件
	 * 
	 * @param content		内容对象
	 * @param template		模板文件
	 * @param path			文件生成的目录
	 * @throws IOException
	 */
	public static void vmToFile(VelocityContext context, String templatePath, String path) throws IOException {
		File parent = new File(path).getParentFile();
		File file = new File(path);
		if (parent.exists() == false) {
			parent.mkdirs();
		}
		if (file.exists() == false) {
			file.createNewFile();
		}
		VelocityEngine velocity = VelocityHelper.getVelocityEngine();
		Template template = velocity.getTemplate(templatePath, ENCODING);
		FileOutputStream fos = new FileOutputStream(path);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ENCODING)); // 设置写入的文件编码,解决中文问题
		template.merge(context, writer);
		writer.close();

		logger.info("模板:" + templatePath + ";文件:" + path);
	}

	/**
	 * 计算表达式的值
	 * 
	 * @param context
	 * @param el
	 * @return
	 */
	public static String evaluateEl(String el, VelocityContext context) {
		Velocity.init();
		StringWriter w = new StringWriter();
		Velocity.evaluate(context, w, "", el);
		return w.toString();
	}
	
	/**
	 * 判断一个表达式EXPR，使用Velocity来判断是否正确来判断是不是正确的
	 * 
	 * @param express
	 * @return
	 */
	public static boolean isExpressTrue(String express, VelocityContext context) {
		//xml配置表达式不支持“&”故用“and”替代 ---addby jiang  
		express = express.replace(" and ", " && ");
		String evalVal = MessageFormat.format("#if({0})true#end", express);
		StringWriter w = new StringWriter();
		Velocity.evaluate(context, w, "", evalVal);
		if ("true".equals(w.toString())) {
			return true;
		}
		return false;
	}

	/**
	 * 将一个VelocityObject对象生成模板
	 * 
	 * @param velocityObject
	 * @param obj
	 * @throws IOException
	 */
	public static void velocityObjectParse(String vm, String path, VelocityContext context) throws IOException {
		VelocityUtil.vmToFile(context, getPath(vm,context), getPath(path, context));
	}

	/**
	 * 获取通过计算后的path
	 * @param path
	 * @param context
	 * @return
	 */
	private static String getPath(String path, VelocityContext context) {
		// VM的PATH是表达式，需要使用Velocity进行解析
		return VelocityUtil.evaluateEl(path, context);
	}

	public static void fileCopy(File source, String path) {
		source.renameTo(new File(path));
	}

	public static void velocityDirParse(String vmDir, String pathDir, VelocityContext context) throws KoalaException{
		try{
				Enumeration resources = Thread.currentThread().getContextClassLoader().getResources(vmDir);
				while (resources.hasMoreElements()) {
					URL url = (URL) resources.nextElement();
					if ("bundleresource".equals(url.getProtocol())) {
						url = EclipseUrlParseUtils.parseUrl(url);
					}

					if ("jar".equals(url.getProtocol())) {
						JarFile jarFile = null;
						try {
							JarURLConnection conn = (JarURLConnection) url.openConnection();
							jarFile = conn.getJarFile();
							for (Enumeration entries = jarFile.entries(); entries.hasMoreElements();) {
								JarEntry entry = (JarEntry) entries.nextElement();
								String entryName = entry.getName();
								if (entryName.endsWith("vm") && entryName.startsWith(vmDir)) {
									String newPath = getPath(pathDir, context)
											+ entryName.substring(vmDir.length(),
													entryName.lastIndexOf(".vm"));
									VelocityUtil.vmToFile(context, entryName, newPath);
								}
							}
						} catch (IOException e) {
							continue;
						}
					} else if ("file".equals(url.getProtocol())) {
						File file = new File(url.getFile());
						List<String> files = queryVmFiles(file);
						for (String fileString : files) {
							fileString = fileString.replaceAll("\\\\", "/");
							String vmPath = vmDir
									+ fileString.substring(fileString.indexOf(vmDir)
											+ vmDir.length());
							String newPath = getPath(pathDir, context)
									+ fileString.substring(fileString.indexOf(vmDir)
											+ vmDir.length(),
											fileString.lastIndexOf(".vm"));
							VelocityUtil.vmToFile(context, vmPath, newPath);
						}
					}
				}
		}catch(Exception e){
			e.printStackTrace();
			throw new KoalaException(e.getMessage());
		}
	}

	private static List<String> queryVmFiles(File file) {
		List<String> filelist = new ArrayList<String>();
		if (file.isFile() && file.getName().endsWith(".vm")) {
			filelist.add(file.getPath());
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File singleFile : files) {
				filelist.addAll(queryVmFiles(singleFile));
			}
		}
		return filelist;
	}
	
	/**
	 * 传入lists参数，构造一个VelocityContext
	 * @param lists
	 * @return
	 */
	public static VelocityContext getVelocityContext(List lists){
		VelocityContext context = new VelocityContext();
		if(lists==null)lists = new ArrayList();
		for (Object obj : lists) {
			context.put(obj.getClass().getSimpleName(), obj);
		}
		return context;
	}
	
	/**
	 * 传入maps参数，构造一个VelocityContext
	 * @param maps
	 * @return
	 */
	public static VelocityContext getVelocityContext(Map<String,Object> maps){
		VelocityContext context = new VelocityContext();
		Set<String> keys = maps.keySet();
		for (String key : keys) {
			context.put(key, maps.get(key));
		}
		return context;
	}
}
