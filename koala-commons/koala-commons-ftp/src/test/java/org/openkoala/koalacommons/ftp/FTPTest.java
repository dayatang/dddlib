package org.openkoala.koalacommons.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.koala.ftp.FTPFacotry;
import org.openkoala.koala.ftp.exception.FtpException;
import org.openkoala.koala.ftp.util.FtpUtil;

/**
 * FTP测试类
 * 
 * @author lingen lingen.liu@gmail.com
 * 
 */
public class FTPTest {

	
	private static FtpUtil ftpUtil;

	
	private static FtpServer server;
	/**
	 * 初始化一个嵌入式的FTP Server
	 */
	@BeforeClass
	public static void createEmbeddingFtp() {
		try {
			FtpServerFactory serverFactory = new FtpServerFactory();
			ListenerFactory factory = new ListenerFactory();
			// set the port of the listener
	
			factory.setPort(2021);
			// replace the default listener
			serverFactory.addListener("default", factory.createListener());
			PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
			userManagerFactory.setFile(new File("users.properties"));
			UserManager um = userManagerFactory.createUserManager();
			List<Authority> auths = new ArrayList();
	        Authority auth = new WritePermission();
	        auths.add(auth);
			BaseUser user = new BaseUser();
			user.setEnabled(true);
			user.setName("andy");
			user.setPassword("andy");
			user.setHomeDirectory(System.getProperty("java.io.tmpdir"));
			user.setAuthorities(auths);
			um.save(user);
			serverFactory.setUserManager(um);
			// start the server
			server = serverFactory.createServer();
			server.start();
		} catch (org.apache.ftpserver.ftplet.FtpException e) {
			e.printStackTrace();
		}
		ftpUtil = FTPFacotry.getFtpUtil("ftp.properties");
	}
	
	@AfterClass
	public static void close(){
		server.stop();
	}
	
	/**
	 * 上传一个文件
	 * @throws FtpException
	 */
	@Test
	public void testUploadFile() throws FtpException{
		String proFilePath = null;
		proFilePath  = FTPFacotry.class.getClassLoader().getResource("").getPath() + "test.txt";
		ftpUtil.uploadFile("/", new File(proFilePath));
	}
	

	/**
	 * 查看一个文件在FTP服务器中是否已经存在
	 * 
	 * @throws FtpException
	 */
	@Test
	public void testIsFileExits() throws FtpException {
		Assert.assertTrue("文件已经存在！", ftpUtil.isFileExists("test.txt"));
	}

	/**
	 * 输出一个FTP路径下的所有文件名
	 */
	@Test
	public void testListFiles() throws Exception {
		List<String> files = ftpUtil.listFiles("/");
		System.out.println(files);
		Assert.assertNotNull(files);
	}

	/**
	 * 输出一个FTP远程路径下所有的目录
	 */
	@Test
	public void testListDiretory() throws Exception {
		List<String> directories = ftpUtil.listDirectory("/");
		Assert.assertNotNull(directories);
	}

	/**
	 * 下载文件
	 * 
	 * @throws FtpException
	 */
	@Test
	public void testDownloadFile() throws FtpException {
		ftpUtil.downLoadFile("/", "test.txt",System.getProperty("java.io.tmpdir")+"testnew");
	}

	/**
	 * 上传一个ZIP文件，并进行解压
	 * 
	 * @throws FtpException
	 */
	@Test
	public void testUploadZipFile() throws FtpException {
		/**
		 * 第1个参数：表示FTP服务器上的目录 第2个参数：表示本地文件系统中的一个ZIP文件
		 * 第3个参数：表示ZIP文件上传到FTP服务器上的哪个目录
		 */
		String proFilePath = null;
		proFilePath  = FTPFacotry.class.getClassLoader().getResource("").getPath() + "t1.zip";
		ftpUtil.uploadZipFile("/", new File(proFilePath), "t1");
	}
	
	/**
	 * 在FTP服务器上创建目录
	 * 
	 * @throws FtpException
	 */
	@Test
	public void testMakeDiretory() throws FtpException {
		Assert.assertTrue(ftpUtil.makeDirectory("/tmp"));
	}
	
	/**
	 * 删除FTP上的一个目录，只能删除空目录
	 * 
	 * @throws FtpException
	 */
	@Test
	public void testDeleteDirectory() throws FtpException {
		Assert.assertTrue(ftpUtil.deleteDirectory("/tmp"));
	}
	
	/**
	 * 重命名FTP上的文件或目录
	 * 
	 * @throws FtpException
	 */
	@Test
	public void testRenameFTPFile() throws FtpException {
		Assert.assertTrue(ftpUtil.renameFTPFile("/test.txt", "/test1.txt"));
	}

	/**
	 * 删除FTP上的一个文件
	 */
	@Test
	public void testDeleteFile() throws FtpException {
		Assert.assertTrue(ftpUtil.deleteFile("/test1.txt"));
	}
	
}
