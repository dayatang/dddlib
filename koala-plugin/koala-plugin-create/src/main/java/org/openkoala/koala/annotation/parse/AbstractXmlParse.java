package org.openkoala.koala.annotation.parse;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.actionvo.CopyFiles;
import org.openkoala.koala.actionvo.VelocityDirObject;
import org.openkoala.koala.actionvo.VelocityFileObject;
import org.openkoala.koala.actionvo.XmlAdd;

/**
 * 通用XML解析
 * @author lingen
 *
 */
public class AbstractXmlParse {
	
	protected List<CopyFiles> copyFiless;
	
	protected List<VelocityDirObject> velocityDirObjects;
	
	protected List<VelocityFileObject> velocityFileObjects;
	
	protected List<XmlAdd> xmlAdds;

	public final void parse(VelocityContext context)
			throws Exception {

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

	
}
