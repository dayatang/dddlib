package org.openkoala.koala.action.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.action.velocity.VelocityUtil;
/**
 * 对web.xml的修改辅助类
 * @author lingen
 *
 */
public class WebXmlUtil implements Serializable {

	private static final long serialVersionUID = -30468063007446676L;

	private final static String WEB_XMLNS= "http://java.sun.com/xml/ns/javaee";
	
	private final static String WEB_APP = "/xmlns:web-app";
	
	private List<String> filters;//指定要删除的filters列表
	
	private List<String> servlets;//指定要删除的servlet列表
	
	private String webXmlFile;//指定要xml所在的文件
	
	public void process(VelocityContext context){
	    webXmlFile = VelocityUtil.evaluateEl(webXmlFile, context);
	    Document webXmlDocument = DocumentUtil.readDocument(webXmlFile);
	    for(String filter:this.getFilters()){
	        filter = VelocityUtil.evaluateEl(filter, context);
	        WebXmlUtil.removeFilter(filter, webXmlDocument);
	    }
	    for(String servlet:this.getServlets()){
	        servlet = VelocityUtil.evaluateEl(servlet, context);
	        WebXmlUtil.removeServlet(servlet, webXmlDocument);
	    }
	    DocumentUtil.document2Xml(webXmlFile, webXmlDocument);
	}
	
	
	/**
	 * 移除WEB.XML中指定的filter
	 * @param name
	 */
	public static void removeFilter(String name,Document document){
		Element parent = XPathQueryUtil.querySingle(WEB_XMLNS, WEB_APP, document);
		String filterSearch = "/xmlns:web-app/xmlns:filter[xmlns:filter-class='"+name+"']";
		Element filter = XPathQueryUtil.querySingle(WEB_XMLNS, filterSearch, document);
		if(filter==null)return;
		String filterMapSearch = "/xmlns:web-app/xmlns:filter-mapping[xmlns:filter-name='"+filter.elementText("filter-name")+"']";
		Element filterMapping = XPathQueryUtil.querySingle(WEB_XMLNS, filterMapSearch, document);
		parent.remove(filter);
		parent.remove(filterMapping);
	}
	
	/**
	 * 移除WEB.XML中指定的filter
	 * @param name
	 * @param document
	 */
	public static void removeListener(String name,Document document){
		
	}
	
	
	
	/**
	 * 移除WEB.XML中指定的Servlet
	 * @param name
	 */
	public static void removeServlet(String name,Document document){
		Element parent = XPathQueryUtil.querySingle(WEB_XMLNS, WEB_APP, document);
		String servletSearch = "/xmlns:web-app/xmlns:servlet[xmlns:servlet-class='"+name+"']";
		Element servlet = XPathQueryUtil.querySingle(WEB_XMLNS, servletSearch, document);
		if(servlet==null)return;
		String servletMapSearch = "/xmlns:web-app/xmlns:servlet-mapping[xmlns:servlet-name='"+servlet.elementText("servlet-name")+"']";
		Element servletMapping = XPathQueryUtil.querySingle(WEB_XMLNS, servletMapSearch, document);
		parent.remove(servlet);
		parent.remove(servletMapping);
	}

    public List<String> getFilters() {
        if(filters==null)filters=new ArrayList<String>();
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<String> getServlets() {
        if(servlets==null)return new ArrayList<String>();
        return servlets;
    }

    public void setServlets(List<String> servlets) {
        this.servlets = servlets;
    }

    public String getWebXmlFile() {
        return webXmlFile;
    }

    public void setWebXmlFile(String webXmlFile) {
        this.webXmlFile = webXmlFile;
    }
    
}
