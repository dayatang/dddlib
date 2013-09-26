package org.jwebap.cfg.persist;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.jwebap.cfg.exception.BeanWriteException;
import org.jwebap.cfg.exception.JwebapDefNotFoundException;
import org.jwebap.cfg.exception.JwebapDefParseException;
import org.jwebap.core.RuntimeContext;
import org.jwebap.util.Assert;
import org.openkoala.koala.monitor.jwebap.ComponentDef;
import org.openkoala.koala.monitor.jwebap.DataPolicyDef;
import org.openkoala.koala.monitor.jwebap.NodeDef;
import org.openkoala.koala.monitor.jwebap.TaskDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.DefaultMapper;

/**
 * 配置工厂，完成配置的加载和持久化，无状态
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2007-12-5
 */
public class PersistManager {

	/**
	 * 
	 */
	private static final String XML_CHARSET = "utf-8";
	private static final Logger LOG = LoggerFactory.getLogger(PersistManager.class);
    private String configPath;

	public PersistManager(String configPath) {
		try {
			this.configPath = configPath; 
		} catch (Exception e) {}
	}
	
	public String getConfigPath() {
		return configPath;
	}

	/**
	 * 持久化jwebap配置
	 * 
	 * @param def
	 * @throws JwebapDefNotFoundException 
	 * @throws BeanWriteException 
	 */
	public void save(NodeDef def) throws JwebapDefNotFoundException, BeanWriteException {
		Assert.assertNotNull(def, "jwebap def is null.");
		BufferedWriter output = null;
		try {
			XStream xStream = initXStream();

			/*PrettyPrintWriter writer = new PrettyPrintWriter(new PrintWriter(configPath));
			ObjectOutputStream outputStream = xStream.createObjectOutputStream(writer);
			outputStream.writeObject(def);*/
			
			output = new BufferedWriter(new FileWriter(configPath));
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + xStream.toXML(def);
			output.write(xml);
			output.flush();
			
			LOG.info("刷新{}文件成功",configPath);
		} catch (Exception e) {
			LOG.warn("更新JwebAp配置文件失败，但是不会影响系统正常运行",e);
		}finally{
			try {output.close();} catch (Exception e2) {}
		}
		
		
	}

	/**
	 * 加载jwebap配置
	 * 
	 * @param path
	 * @return
	 * @throws JwebapDefNotFoundException 
	 * @throws JwebapDefParseException 
	 */
	public NodeDef get() throws Exception {
		
		XStream xStream  = initXStream();
		
		FileInputStream inputStream = new FileInputStream(configPath);
		NodeDef def = (NodeDef)xStream.fromXML(inputStream);
		try {
			inputStream.close();
		} catch (Exception e) {
			
		}
		
		return def;
	}
	
	private XStream initXStream(){
		XStream xStream  =   new  XStream( new  DomDriver(XML_CHARSET));
		xStream.registerConverter(new MapEntryConverter(new DefaultMapper(Thread.currentThread().getContextClassLoader())));   

		xStream.alias( "monitorNode" , NodeDef.class );  
		xStream.alias( "component" , ComponentDef.class );
		xStream.alias( "task" , TaskDef.class );
		xStream.alias( "dataPolicy" , DataPolicyDef.class );
		return xStream;
	}
	
	/**
	 * 更新组件配置
	 * @param def
	 */
	public void updateComponentConfig(ComponentDef def){
		ComponentDef origDef = RuntimeContext.getContext().getComponentDef(def.getType());
		if(origDef != null){
			origDef.setActive(def.isActive());
			origDef.putProperties(def.getProperties());
			RuntimeContext.getContext().getComponentContext(def.getType()).putProperties(def.getProperties());
			NodeDef rootDef = RuntimeContext.getContext().getNodeDef();
			try {
				save(rootDef);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
