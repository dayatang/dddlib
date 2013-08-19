package org.openkoala.koala.actionvo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.file.FileCopyAction;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.util.EclipseUrlParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 目录COPY
 * 
 * @author lingen.liu
 * 
 */
public class CopyFiles {

	private static final int BUFFER_SIZE = 1024;

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
