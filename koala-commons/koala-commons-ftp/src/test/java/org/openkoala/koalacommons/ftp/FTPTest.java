package org.openkoala.koalacommons.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
    private static String ftpUserDir = System.getProperty("java.io.tmpdir");

    /**
     * 初始化一个嵌入式的FTP Server
     */
    @BeforeClass
    public static void createEmbeddingFtp() throws Exception {
        // create and start the server
        server = createFtpServer();
        server.start();
        ftpUtil = FTPFacotry.getFtpUtil("ftp.properties");
    }

    @AfterClass
    public static void close() {
        server.stop();
    }

    private static FtpServer createFtpServer() throws Exception {
        FtpServerFactory serverFactory = createFtpServerFactory();
        serverFactory.setUserManager(createUserManager());
        return serverFactory.createServer();
    }

    private static FtpServerFactory createFtpServerFactory() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();
        // set the port of the listener
        listenerFactory.setPort(2021);
        // replace the default listener
        serverFactory.addListener("default", listenerFactory.createListener());
        return serverFactory;
    }

    private static UserManager createUserManager() throws org.apache.ftpserver.ftplet.FtpException {
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File("users.properties"));
        UserManager um = userManagerFactory.createUserManager();
        um.save(createFtpUser());
        return um;
    }

    private static BaseUser createFtpUser() {
        BaseUser user = new BaseUser();
        user.setEnabled(true);
        user.setName("andy");
        user.setPassword("andy");
        user.setHomeDirectory(ftpUserDir);
        user.setAuthorities(createAuths());
        return user;
    }

    private static List<Authority> createAuths() {
        List<Authority> auths = new ArrayList<Authority>();
        Authority auth = new WritePermission();
        auths.add(auth);
        return auths;
    }

    /**
     * 如果ftp服务器/dir1目录中不存在users.properties文件，复制一个到那里。
     * @throws Exception 
     */
    @Before
    public void beforeTest() throws Exception {
        File dir = new File(ftpUserDir, "dir1");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "users.properties");
        if (!file.exists()) {
            String filePath = FTPTest.class.getResource("/users.properties").getFile();
            FileUtils.copyFileToDirectory(new File(filePath), dir);
        }
    }
    
    /**
     * 上传一个文件
     *
     * @throws FtpException
     */
    @Test
    public void testUploadFile() throws Exception {
        String filePath = getClass().getResource("/test.txt").getFile();
        uploadFile("/", filePath);
        Assert.assertTrue("文件不存在！", ftpUtil.isFileExists("test.txt"));
    }

    /**
     * 文件已存在
     */
    @Test
    public void testIsFileExists() throws Exception {
        Assert.assertTrue("文件不存在！", ftpUtil.isFileExists("dir1/users.properties"));
    }
    /**
     * 输出一个FTP路径下的所有文件名
     */
    @Test
    public void testListFiles() throws Exception {
        List<String> files = ftpUtil.listFiles("/");
        Assert.assertTrue(files.contains("users.properties"));
        files = ftpUtil.listFiles("/dir1");
        Assert.assertTrue(files.contains("users.properties"));
    }

    /**
     * 输出一个FTP远程路径下所有的目录
     */
    @Test
    public void testListDiretory() throws Exception {
        List<String> directories = ftpUtil.listDirectory("/");
        Assert.assertTrue(directories.contains("dir1"));
    }

    /**
     * 下载文件
     *
     * @throws FtpException
     */
    @Test
    public void testDownloadFile() throws FtpException {
        ftpUtil.downLoadFile("/dir1", "users.properties", System.getProperty("java.io.tmpdir"));
    }

    /**
     * 上传一个ZIP文件，并进行解压
     *
     * @throws FtpException
     */
    @Ignore
    @Test
    public void testUploadZipFile() throws FtpException {
        /**
         * 第1个参数：表示FTP服务器上的目录 第2个参数：表示本地文件系统中的一个ZIP文件
         * 第3个参数：表示ZIP文件上传到FTP服务器上的哪个目录
         */
        String filePath = getClass().getResource("/t1.zip").getFile();
        ftpUtil.uploadZipFile("/", new File(filePath), "t1");
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
    @Ignore
    @Test
    public void testRenameFTPFile() throws FtpException {
        Assert.assertTrue(ftpUtil.renameFTPFile("/test.txt", "/test1.txt"));
    }

    /**
     * 删除FTP上的一个文件
     */
    @Ignore
    @Test
    public void testDeleteFile() throws FtpException {
        Assert.assertTrue(ftpUtil.deleteFile("/test1.txt"));
    }

    private void uploadFile(String dir, String filePath) throws FtpException, RuntimeException {
        String fileShortName = new File(filePath).getName();
        File file = new File(ftpUserDir, fileShortName);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("Cannot delete file: " + file);
            }
        }
        //String proFilePath = FTPFacotry.class.getClassLoader().getResource("").getPath() + "test.txt";
        ftpUtil.uploadFile(dir, new File(filePath));
    }
}
