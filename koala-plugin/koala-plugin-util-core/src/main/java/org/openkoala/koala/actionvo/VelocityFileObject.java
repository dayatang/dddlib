package org.openkoala.koala.actionvo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;


/**
 * 使用Velocity进行单个文件式的解析生成
 * @author lingen.liu
 *
 */
public class VelocityFileObject {

	private String vm;
	
	private String path;
	
	private String express;
	
	public void process(VelocityContext context) throws IOException{
		if(express!=null && VelocityUtil.isExpressTrue(express, context)==false)return;
		VelocityUtil.velocityObjectParse(vm,path,context);
	}

	public VelocityFileObject(String vm, String path) {
		super();
		this.vm = vm;
		this.path = path;
	}

	public VelocityFileObject() {
		super();
	}

	public String getVm() {
		return vm;
	}

	public void setVm(String vm) {
		this.vm = vm;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}
	
}
