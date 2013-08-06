package org.openkoala.koala.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.openkoala.koala.ftp.exception.FtpException;
import org.openkoala.koala.ftp.impl.FtpUtilImpl;
import org.openkoala.koala.ftp.util.FtpUtil;

/**
 * 通过此类获取FTPutil
 * 
 * @author lingen.liu
 * 
 */
public class FTPFacotry {

	private static String username;

	private static String password;

	private static String serverIp;

	private static int port;
	
	private static String encoding;
	
	private static String tmpDir;
	
	private static int retryCount;
	
	private static String mode;

	public static FtpUtil getFtpUtil(String properties) {
		initConfig(properties);
		FtpUtilImpl ftpUtil = new FtpUtilImpl(serverIp, port, username, password,encoding,retryCount,mode);
		ftpUtil.initTmpDir(tmpDir);
		//ftpUtil.initMaxPoolSize(maxPoolSize);
		return ftpUtil;
	}
	
	private static FtpUtil getFtpUtil(String serverIp,int port,String username,String password,String encoding) {
		FtpUtilImpl ftpUtil = new FtpUtilImpl(serverIp, port, username, password,encoding,3,"PORT");
		ftpUtil.initTmpDir(tmpDir);
		//ftpUtil.initMaxPoolSize(maxPoolSize);
		return ftpUtil;
	}

	private static void initConfig(String properties) {
		String proFilePath = null;
		InputStream in = null;
		try {
			proFilePath  = FTPFacotry.class.getClassLoader().getResource("").getPath() + properties;
			Properties prop = new Properties(); 
			in =new FileInputStream(new File(proFilePath));
			prop.load(in);
			username = prop.getProperty("ftp.username");
			password = prop.getProperty("ftp.password");
			serverIp = prop.getProperty("ftp.serverIp");
			port = Integer.parseInt(prop.getProperty("ftp.port"));
			encoding = prop.getProperty("ftp.encoding");
			tmpDir = prop.getProperty("ftp.tmpDir");
			try{retryCount = Integer.parseInt(prop.getProperty("ftp.retry.count"));}catch(Exception e){retryCount = 3;}
			mode = prop.getProperty("ftp.mode");
			if(tmpDir!=null)initTmpDir();
		} catch (Exception e) {
			System.out.println("===Properties Error======");
			System.out.println(proFilePath);
			e.printStackTrace();
		}finally{
		  try{
		    if(in!=null)in.close();
			}catch(Exception e1)
			{e1.printStackTrace();}
		}
	}
	
	private static void initTmpDir(){
		//创建临时目录
		File tmpDirFile = new File(tmpDir);
		if(!tmpDirFile.exists())tmpDirFile.mkdirs();
	}
	
	public static void main(String args[]){ 
		long begin = System.currentTimeMillis();
		FtpUtil ftpUtil = FTPFacotry.getFtpUtil("ftp.properties");
		try {
			//ftpUtil.deleteFile("/SL通讯录.xls");
			//ftpUtil.updateFile("/", new File("d:/你好.txt"));
			//ftpUtil.updateFile("/", new File("d:/你好.txt"), "a.txt");
			//ftpUtil.updateDirFiles(new File("d:/upload"), "/", "upload");
			//System.out.println(ftpUtil.isDirectory("/a.txt"));
			//System.out.println(ftpUtil.isFile("/a.txt"));
			//System.out.println(ftpUtil.isFileExists("/b.txt"));
			//System.out.println(ftpUtil.makeDirectory("/a/b/c/d"));
			//System.out.println(ftpUtil.listDirectory("/"));
			//System.out.println(ftpUtil.listFiles("/"));
//			List<FTPFile> files = ftpUtil.listFTPFiles("/");
//			for(FTPFile file:files){
//				System.out.println(file.getName()+":"+file.isDirectory()+":"+file.getSize()+":");
//			}
			//ftpUtil.renameFTPFile("/a/b", "/a/c");
			//ftpUtil.updateZipFile("/", new File("d:/Archive.zip"), "dre");
			//System.out.println(ftpUtil.downLoadFile("/", "a.txt"));
			//ftpUtil.downLoadFile("/", filename);
			//System.out.println(ftpUtil.isFile("/News"));
			//ftpUtil.listFTPFiles("/");
			//File file = new File("e:/文件不存在.jpg");
			//System.out.println(file.lastModified());
			System.out.println(ftpUtil.downLoadFile("/", "文件不存在.jpg","e:/"));
		} catch (FtpException e) {
			e.printStackTrace();
		}finally{
		}
		System.out.println(System.currentTimeMillis()-begin);
	}
}
