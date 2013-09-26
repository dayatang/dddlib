package org.openkoala.koala.ftp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.openkoala.koala.ftp.ZipAndUnzip;
import org.openkoala.koala.ftp.exception.FtpException;
import org.openkoala.koala.ftp.util.FtpUtil;


public class FtpUtilImpl implements FtpUtil {
	
	private String serverIp;
	
	private int port;
	
	private String username;
	
	private String password;
	
	private String encoding;
	
	private String tmpDir;
	
	private int retryCount;
	
	private String mode;
	
	public FtpUtilImpl(String serverIp,int port,String username,String password,String encoding,int retryCount,String mode){
		this.serverIp = serverIp;
		this.port = port;
		this.username = username;
		this.password = password;
		this.encoding = encoding;
		this.retryCount = retryCount;
		this.mode = mode;
	}
	
	public File downLoadFile(String path,String filename,String localPath) throws FtpException{
		File file = null;
		FileOutputStream fos = null;
		if(path.endsWith("/")==false)path = path + "/";
		if(this.isFileExists(path+filename)==false)return null;
		FTPClient ftpClient = null;
		long last = 0l;
		try {
			ftpClient = connectFtpClient();
			ftpClient.cwd("/");
			ftpClient.cwd(path);
			ftpClient.sendCommand("MDTM "+filename);
			String dateStr = ftpClient.getReplyString();
			if(dateStr.startsWith("213")){
				System.out.println(dateStr);
			last =parseFileLastMofify(dateStr).getTime().getTime();
			//先查找本地是否存在这个文件，如果存在且时间一致，则直接返回本地文件，不再从FTP下载
			File localFile = new File(localPath+path+filename);
			if(localFile.exists()){
				if(localFile.lastModified()==last)return localFile;
			}
			}
			file = new File(localPath+"/"+path+"/");
			if(!file.exists())file.mkdirs();
			file = new File(localPath+"/"+path+"/"+filename);
			fos = new FileOutputStream(file);
			ftpClient.retrieveFile(filename, fos);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		} finally {
				try {
					if (fos != null){
						fos.flush();
						fos.close();
					}
					ftpClient.disconnect();
					ftpClient = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		file.setLastModified(last);
		return file;
	}
	
	public File downLoadFile(String path, String filename) throws FtpException {
		if(tmpDir==null || "".equals(tmpDir))tmpDir = System.getProperty("java.io.tmpdir");
		return downLoadFile(path,filename,this.tmpDir);
	}

	
	public boolean renameFTPFile(String fromName,String toName) throws FtpException{
		FTPClient ftpClient = connectFtpClient();
		boolean result = false;
		try {
			result = ftpClient.rename(fromName, toName);
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public List<String> listFiles(String path) throws FtpException{
		FTPClient ftpClient = connectFtpClient();
		List<String> fileNames = new ArrayList<String>();
		try { 
			FTPFile[] files = ftpClient.listFiles(path);
			for(FTPFile file:files){
				if(file.isFile())fileNames.add(file.getName());
			}
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileNames;
	}
	
	public List<String> listDirectory(String path) throws FtpException{
		List<String> fileNames = new ArrayList<String>();
		FTPClient ftpClient = connectFtpClient();
		try {
			FTPFile[] files = ftpClient.listFiles(path);
			for(FTPFile file:files){
				if(file.isDirectory())fileNames.add(file.getName());
			}
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileNames;
	}
	
	/**
	 * 返回一个目录下所有的目录或文件
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public List<FTPFile> listFTPFiles(String path) throws FtpException{
		List<FTPFile> ftpFiles = new ArrayList<FTPFile>();
		FTPFile[] files = null;
		FTPClient ftpClient = connectFtpClient();
		try {
			files = ftpClient.listFiles(path);
			for(FTPFile ftpFile:files){
				ftpFiles.add(ftpFile);
			}
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ftpFiles;
	}
	
	
	/**
	 * 删除FTP上的一个文件
	 * @param path
	 * @throws FtpException
	 */
	public boolean deleteFile(String filePath) throws FtpException{
		boolean dele = false;
		FTPClient ftpClient = connectFtpClient();
		try {
			dele = ftpClient.deleteFile(filePath);
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dele;
	}
	
	
	/**
	 * 删除FTP上是目录
	 * @param path
	 * @throws FtpException
	 */
	public boolean deleteDirectory(String dirPath) throws FtpException{
		boolean dele = false;
		FTPClient ftpClient = connectFtpClient();
		try {
			dele = ftpClient.removeDirectory(dirPath);
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{try {
			ftpClient.disconnect();
			ftpClient = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		return dele;
	}
	
	public void uploadFile(String path, File file) throws FtpException {
		FileInputStream fis = null;
		FTPClient ftpClient = connectFtpClient();
		try {
			ftpClient.cwd("/");
			int code = ftpClient.cwd(path);
			if(code==550){
				ftpClient.makeDirectory(path);
				ftpClient.cwd(path);
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			fis = new FileInputStream(file);
			boolean upload = ftpClient.storeFile(file.getName(), fis);
			System.out.println(upload);
			ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
				try {
					if(fis!=null){fis.close();}
					ftpClient.disconnect();
					ftpClient = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public void uploadFile(String path, File file,String fileName) throws FtpException {
		FileInputStream fis = null;
		FTPClient ftpClient = connectFtpClient();
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.cwd("/");
			int code = ftpClient.cwd(path);
			if(code==550){
				ftpClient.makeDirectory(path);
				ftpClient.cwd(path);
			}
			fis = new FileInputStream(file);
			ftpClient.storeFile(fileName, fis);
			ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
				try {
					if(fis!=null){fis.close();}
					ftpClient.disconnect();
					ftpClient = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 上传一个ZIP文件，并进行解压
	 * @param path
	 * @param file
	 * @throws FtpException
	 */
	public void uploadZipFile(String path,File file,String fileName) throws FtpException{
		FileInputStream fis = null;
		FTPClient ftpClient = connectFtpClient();
		try {
			ftpClient.cwd("/");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			int code = ftpClient.cwd(path);
			if(code==550){
				ftpClient.makeDirectory(path);
				ftpClient.cwd(path);
			}
			String tmpPath = System.getProperty("java.io.tmpdir")+file.getName();
			ZipAndUnzip zipUtil = new ZipAndUnzip();
			zipUtil.unZipFile(file, tmpPath);
			File uploadFile = new File(tmpPath);
			this.uploadDirFiles(uploadFile, path, fileName);
			ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
				try {
					if(fis!=null){fis.close();}
					ftpClient.disconnect();
					ftpClient = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 
	 * @param file
	 * @param path
	 * @throws FtpException 
	 */
	public void uploadDirFiles(File file,String path,String dirName) throws FtpException{
		FTPClient ftpClient = connectFtpClient();
		try{ftpClient.cwd("/");}catch(IOException e){this.connectFtpClient();}
		String  sep=System.getProperty("file.separator"); 
		String remoteDir = path + "/" + dirName;
		this.makeDirectory(remoteDir);
		File[] files = file.listFiles();
		for(File uploadFile:files){
			if(uploadFile.isDirectory())uploadDirFiles(uploadFile,remoteDir,uploadFile.getName());
			else if(uploadFile.isFile())this.uploadFile(remoteDir, uploadFile);
		}
	}
	public FTPClient connectFtpClient() throws FtpException {
		FTPClient ftpClient = null;
		int i = 0;
		while(ftpClient ==null && i < retryCount){
			ftpClient = createConnection();
			i++;
		}
		return ftpClient;
	}
	
	private FTPClient createConnection(){
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding(encoding);
			ftpClient.connect(serverIp, port);
			ftpClient.login(username, password);
			int replay = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replay)) {
				throw new FtpException("connection error,replay for:"
						+ replay);
			}
			if("PASSVIE".equals(this.mode.trim()))ftpClient.enterLocalPassiveMode();
			else ftpClient.enterLocalActiveMode();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try{if(ftpClient!=null)ftpClient.disconnect();}catch(Exception e1){};
			ftpClient = null;
		}
		return ftpClient;
	}
	

	public void closeFtpClient() {
	}

	
	public boolean isFileExists(String inputPath) throws FtpException{
		FTPClient ftpClient = connectFtpClient();
		try {
			ftpClient.cwd("/");
			FTPFile[] files  = ftpClient.listFiles(inputPath);
			if(files.length>0)return true;
		}catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 创建一个目录，支持多级创建
	 * @param dir
	 * @return
	 * @throws FtpException
	 */
	public boolean makeDirectory(String dir) throws FtpException{
		FTPClient ftpClient = connectFtpClient();
		boolean make =  false;
		try {
		   make = ftpClient.makeDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return make;
	}
	
	/**
	 * 是否是目录
	 * @param path
	 * @return
	 * @throws FtpExcption
	 */
	public boolean isDirectory(String path) throws FtpException{
		FTPClient ftpClient = connectFtpClient();
		
		if(path.endsWith("/"))path = path.substring(0,path.lastIndexOf("/"));
		String fileName = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("/"));
		boolean dir = false;
		try{
			if(this.isFileExists(path)==false)throw new FtpException("file not exits:"+path);
		ftpClient.cwd("/");
		ftpClient.cwd(path);
		FTPFile files[] = ftpClient.listFiles();
		for(FTPFile file:files){
			if(file.getName().equals(fileName)){
				if(file.isDirectory())return true;
				else return false;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dir;
	}
	
	
	private void clearTmpDir(String path){
		Calendar currentDay = Calendar.getInstance();
		currentDay.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.DAY_OF_MONTH), 0, 0);
		File oldFile = new File(path);
		if(!oldFile.exists()){
			oldFile.mkdir();
		}
		File oldFileList[] = oldFile.listFiles();
		if(null != oldFileList){
			for(int s=0;s<oldFileList.length;s++){
				if(oldFileList[s].isFile() && oldFileList[s].lastModified()<=currentDay.getTimeInMillis()){
					oldFileList[s].delete();
				}
			}
		}
	}
	/**
	 * 是否是文件
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public boolean isFile(String path) throws FtpException{
		FTPClient ftpClient = connectFtpClient();
		if(path.endsWith("/"))path = path.substring(0,path.lastIndexOf("/"));
		String fileName = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("/"));
		boolean dir = false;
		try{
		ftpClient.cwd("/");
		ftpClient.cwd(path);
		FTPFile files[] = ftpClient.listFiles();
		for(FTPFile file:files){
			if(file.getName().equals(fileName)){
				if(file.isFile())return true;
				else return false;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			throw new FtpException(e.getMessage());
		}finally{
			try {
				ftpClient.disconnect();
				ftpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dir;
	}
	
	private Calendar parseFileLastMofify(String str){
		Calendar c = Calendar.getInstance();
		if(str.startsWith("550"))return c;
		String dateStr = str.split(" ")[1].trim();
		int year = Integer.parseInt(dateStr.substring(0,4));
		int month = Integer.parseInt(dateStr.substring(4,6))-1;
		int date = Integer.parseInt(dateStr.substring(6,8));
		int hourOfDay = Integer.parseInt(dateStr.substring(8,10));
		int minute = Integer.parseInt(dateStr.substring(10,12));
		int second = Integer.parseInt(dateStr.substring(12,14));
		c.clear();
		c.set(year, month, date, hourOfDay, minute, second);
		return c;
	}
	
	public void initTmpDir(String path){
		if(path==null){
			path = System.getProperty("java.io.tmpdir");
		}
		if(new File(path).exists()==false){
			new File(path).mkdirs();
		}
		this.tmpDir = path;
	}


	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTmpDir() {
		if(tmpDir==null)tmpDir = System.getProperty("java.io.tmpdir");
		return tmpDir;
	}
	
}
