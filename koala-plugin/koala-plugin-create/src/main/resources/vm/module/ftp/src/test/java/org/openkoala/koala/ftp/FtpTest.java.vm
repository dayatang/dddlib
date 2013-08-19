package org.openkoala.koala.ftp; 

import java.io.File;
import java.util.List;

import org.openkoala.koala.ftp.exception.FtpException;
import org.openkoala.koala.ftp.util.FtpUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * FTP测试类
 * @author zyb
 *
 */
public class FtpTest {
	
	private FtpUtil ftpUtil;
	
	@Before
	public void setUp() {
		ftpUtil = FTPFacotry.getFtpUtil("ftp.properties");
	}
	
	/**
	 * 输出一个FTP路径下的所有文件名
	 */
	@Test
	public void testListFiles() throws Exception {
		List<String> files = ftpUtil.listFiles("/");
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
	 * @throws FtpException 
	 */
	@Test
	public void testDownloadFile() throws FtpException {
		ftpUtil.downLoadFile("/", "test.txt", "F:");
	}
	
	/**
	 * 上传一个ZIP文件，并进行解压
	 * @throws FtpException
	 */
	@Test
	public void testUploadZipFile() throws FtpException {
		/**
		 * 第1个参数：表示FTP服务器上的目录
		 * 第2个参数：表示本地文件系统中的一个ZIP文件
		 * 第3个参数：表示ZIP文件上传到FTP服务器上的哪个目录
		 */
		ftpUtil.uploadZipFile("/", new File("F:/t1.zip"), "上传的文件");
	}
	
	/**
	 * 删除FTP上的一个文件
	 */
	@Test
	public void testDeleteFile() throws FtpException {
		Assert.assertTrue(ftpUtil.deleteFile("f/test.txt"));
	}
	
	/**
	 * 删除FTP上的一个目录，只能删除空目录
	 * @throws FtpException
	 */
	@Test
	public void testDeleteDirectory() throws FtpException {
		Assert.assertTrue(ftpUtil.deleteDirectory("f"));
	}
	
	/**
	 * 重命名FTP上的文件或目录
	 * @throws FtpException 
	 */
	@Test
	public void testRenameFTPFile() throws FtpException {
		Assert.assertTrue(ftpUtil.renameFTPFile("t", "f"));
	}
	
	/**
	 * 查看一个文件在FTP服务器中是否已经存在
	 * @throws FtpException 
	 */
	@Test
	public void testIsFileExits() throws FtpException {
		Assert.assertTrue("文件已经存在！", ftpUtil.isFileExists("test"));
	}
	
	/**
	 * 在FTP服务器上创建目录
	 * @throws FtpException
	 */
	@Test
	public void testMakeDiretory() throws FtpException {
		Assert.assertTrue(ftpUtil.makeDirectory("/ftp/test/demo"));
	}
}
