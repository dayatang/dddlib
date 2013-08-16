package org.openkoala.koala.actionvo;

import java.io.IOException;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.exception.KoalaException;


/**
 * 使用Velocity进行目录式的模板生成
 * 
 * @author lingen.liu
 * 
 */
public class VelocityDirObject {

	private String vmDir;

	private String pathDir;
	
	private String express;

	public VelocityDirObject() {
		super();
	}

	public VelocityDirObject(String vmDir, String pathDir) {
		super();
		this.vmDir = vmDir;
		this.pathDir = pathDir;
	}

	public String getVmDir() {
		return vmDir;
	}

	public void setVmDir(String vmDir) {
		this.vmDir = vmDir;
	}

	public String getPathDir() {
		return pathDir;
	}

	public void setPathDir(String pathDir) {
		this.pathDir = pathDir;
	}
	
	public void process(VelocityContext context) throws KoalaException{
		if(express!=null && VelocityUtil.isExpressTrue(express, context)==false)return;
		VelocityUtil.velocityDirParse(vmDir,pathDir, context);
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}
	
}
