package org.openkoala.koala.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.action.xml.WebXmlUtil;
import org.openkoala.koala.actionvo.CopyFiles;
import org.openkoala.koala.actionvo.VelocityDirObject;
import org.openkoala.koala.actionvo.VelocityFileObject;
import org.openkoala.koala.actionvo.XmlAdd;
import org.openkoala.koala.actionvo.XmlRemove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @description 通用XML解析，根据XML的配置，进行生成或修改
 *  
 * @date：      2013-8-26   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class XmlParse {
	
    
    private static final Logger logger = LoggerFactory.getLogger(XmlParse.class);
    
	protected String description;
	
	protected List<CopyFiles> copyFiless;
	
	protected List<VelocityDirObject> velocityDirObjects;
	
	protected List<VelocityFileObject> velocityFileObjects;
	
	protected List<XmlAdd> xmlAdds;
	
	protected List<XmlRemove> xmlRemoves;
	
	protected List<String> removeFiles;
	
	protected List<WebXmlUtil> webXmlUtils;

	protected final void parse(VelocityContext context)
			throws Exception {
	    logger.info("解析XML:"+description);
        
        for(XmlRemove remove:this.getXmlRemoves()){
            remove.process(context);
           
        }
        
        for(String file:this.getRemoveFiles()){
            file = VelocityUtil.evaluateEl(file, context);
            file=file.replaceAll("\\\\", "/");
            File delete = new File(file);
            if(delete.isFile())delete.delete();
            else FileUtils.deleteDirectory(delete);
        }
        
        for(WebXmlUtil webXmlUtil:this.getWebXmlUtils()){
            webXmlUtil.process(context);
        }

		for (VelocityFileObject velocityObject : this.getVelocityFileObjects()) {
			velocityObject.process(context);
		}

		for (VelocityDirObject velocityDirObject : this.getVelocityDirObjects()) {
			velocityDirObject.process(context);
		}

		for (CopyFiles copyFile : this.getCopyFiless()) {
			copyFile.process(context);
		}
		
		for(XmlAdd update:this.getXmlAdds()){
			update.process(context);
		}
		

		logger.info("解析完毕:"+description);
	}

	public List<CopyFiles> getCopyFiless() {
		if(copyFiless==null)copyFiless = new ArrayList<CopyFiles>();
		return copyFiless;
	}

	public void setCopyFiless(List<CopyFiles> copyFiless) {
		this.copyFiless = copyFiless;
	}

	public List<VelocityDirObject> getVelocityDirObjects() {
		if(velocityDirObjects==null)velocityDirObjects = new ArrayList<VelocityDirObject>();
		return velocityDirObjects;
	}

	public void setVelocityDirObjects(List<VelocityDirObject> velocityDirObjects) {
		this.velocityDirObjects = velocityDirObjects;
	}

	public List<VelocityFileObject> getVelocityFileObjects() {
		if(velocityFileObjects==null)velocityFileObjects = new ArrayList<VelocityFileObject>();
		return velocityFileObjects;
	}

	public void setVelocityFileObjects(List<VelocityFileObject> velocityFileObjects) {
		this.velocityFileObjects = velocityFileObjects;
	}

	public List<XmlAdd> getXmlAdds() {
		if(xmlAdds==null)xmlAdds = new ArrayList<XmlAdd>();
		return xmlAdds;
	}

	public void setXmlAdds(List<XmlAdd> xmlAdds) {
		this.xmlAdds = xmlAdds;
	}

    public List<XmlRemove> getXmlRemoves() {
        if(xmlRemoves==null)xmlRemoves = new ArrayList<XmlRemove>();
        return xmlRemoves;
    }

    public void setXmlRemoves(List<XmlRemove> xmlRemoves) {
        this.xmlRemoves = xmlRemoves;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public List<String> getRemoveFiles() {
        if(removeFiles==null)removeFiles=new ArrayList<String>();
        return removeFiles;
    }

    public void setRemoveFiles(List<String> removeFiles) {
        this.removeFiles = removeFiles;
    }

    public List<WebXmlUtil> getWebXmlUtils() {
        if(webXmlUtils==null)return new ArrayList<WebXmlUtil>();
        return webXmlUtils;
    }

    public void setWebXmlUtils(List<WebXmlUtil> webXmlUtils) {
        this.webXmlUtils = webXmlUtils;
    }
    
}
