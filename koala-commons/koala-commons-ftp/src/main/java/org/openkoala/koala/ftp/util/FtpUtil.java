package org.openkoala.koala.ftp.util;

import java.io.File;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.openkoala.koala.ftp.exception.FtpException;


public interface FtpUtil {
	
	/**
	 * 下载一个文件
	 * @param path
	 * @param filename
	 * @return
	 */
	public File downLoadFile(String path,String filename) throws FtpException;
	
	/**
	 * 使用Binary模式下载一个文件
	 * @param path
	 * @param filename
	 * @return
	 */
	public File downloadBinaryFile(String path,String filename,String localPath) throws FtpException;
	
	/**
	 * 下载一个文件,保存到{localPath}/{path}/{filename}下面
	 * @param path
	 * @param filename
	 * @return
	 */
	public File downLoadFile(String path,String filename,String localPath) throws FtpException;
	
	/**
	 * 输出一个FTP路径下的所有文件名
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public List<String> listFiles(String path) throws FtpException;
	
	/**
	 * 输出一个FTP远程路径下所有的目录
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public List<String> listDirectory(String path) throws FtpException;
	
	/**
	 * 返回一个目录下所有的目录或文件
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public List<FTPFile> listFTPFiles(String path) throws FtpException;
	
	/**
	 * 上传一个文件
	 * @param path
	 * @param file
	 */
	public void uploadFile(String path,File file) throws FtpException;
	
	/**
	 * 上传一个ZIP文件，并进行解压
	 * @param path
	 * @param file
	 * @throws FtpException
	 */
	public void uploadZipFile(String path,File file,String fileName) throws FtpException;
	
	/**
	 * 上传一个目录
	 * @param file
	 * @param path
	 * @param dirName
	 * @throws FtpException
	 */
	public void uploadDirFiles(File file,String path,String dirName) throws FtpException;
	
	/**
	 * 删除FTP上的一个文件
	 * @param path
	 * @throws FtpException
	 */
	public boolean deleteFile(String filePath) throws FtpException;
	
	/**
	 * 删除FTP上是目录
	 * @param path
	 * @throws FtpException
	 */
	public boolean deleteDirectory(String dirPath) throws FtpException;
	
	/**
	 * 上传一个文件
	 * @param path  上传到FTP的路径
	 * @param file   文件
	 * @param fileName  此文件重命名
	 * @throws FtpException
	 */
	public void uploadFile(String path, File file,String fileName) throws FtpException;
	
	/**
	 * 连接到FTP服务器
	
	public void connectFtpServe() throws FtpException; */
	
	/**
	 * 重命名一个文件或文件夹，使用FTP的文件描述方式
	 * 例：
	 * 重命名一个文件
	 * renameFTPFile("/uplo/a.txt","/uplo/b.txt");
	 * 重命名一个文件夹
	 * renameFTPFile("/upload/a","/upload/b");
	 * @param fromName
	 * @param toName
	 */
	public boolean renameFTPFile(String fromName,String toName) throws FtpException;
	
	/**
	 * 关闭同FTP服务器的连接
	
	public void closeFtpClient(); 
	
	public void connectFtpClient() throws FtpException;
	*/
	
	/**
	 * 查看一个文件是否存在
	 * @param inputPath
	 * @return
	 * @throws FtpException
	 */
	public boolean isFileExists(String inputPath) throws FtpException;
	
	/**
	 * 创建一个目录，支持多级创建
	 * @param dir
	 * @return
	 * @throws FtpException
	 */
	public boolean makeDirectory(String dir) throws FtpException;
	
	/**
	 * 是否是目录
	 * @param path
	 * @return
	 * @throws FtpExcption
	 */
	public boolean isDirectory(String path) throws FtpException;
	
	/**
	 * 是否是文件
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public boolean isFile(String path) throws FtpException;
	
	public void initTmpDir(String dir);
	
	public String getTmpDir();
	
}
