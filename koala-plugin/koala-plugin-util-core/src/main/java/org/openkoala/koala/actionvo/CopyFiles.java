package org.openkoala.koala.actionvo;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.file.FileCopyAction;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @description  文件或目录的复制支持VO
 *  
 * @date：      2013-8-27   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class CopyFiles {

	private static final Logger logger = LoggerFactory.getLogger(CopyFiles.class);

	private String srcDir;

	private String destDir;
	
	private String express;

	public void process(VelocityContext context) throws Exception {
		if(express!=null && VelocityUtil.isExpressTrue(express, context)==false)return;
		destDir = VelocityUtil.evaluateEl(destDir,context);
		srcDir = VelocityUtil.evaluateEl(srcDir,context);
		FileCopyAction.copyDir(srcDir, destDir);
		logger.info("目录复制:from " + srcDir + " to " + destDir);
	}

	public CopyFiles() {
		super();
	}

	public CopyFiles(String srcDir, String destDir) {
		super();
		this.srcDir = srcDir;
		this.destDir = destDir;
	}

	public String getSrcDir() {
		return srcDir;
	}

	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}

	public String getDestDir() {
		return destDir;
	}

	public void setDestDir(String destDir) {
		this.destDir = destDir;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}
	
}
