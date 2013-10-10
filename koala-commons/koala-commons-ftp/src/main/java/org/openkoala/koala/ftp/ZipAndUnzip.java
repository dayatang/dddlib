package org.openkoala.koala.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;


public class ZipAndUnzip {
	
	public void unZipFile(File file,String path) throws IOException{
		 unZip(file,path);
	}
	
	 /** 
     * 解压缩zipFile 
     * @param file 要解压的zip文件对象 
     * @param outputDir 要解压到某个指定的目录下 
     * @throws IOException 
     */  
    private void unZip(File file,String outputDir) throws IOException {
        org.apache.tools.zip.ZipFile zipFile = null; 
        try {
            zipFile = new org.apache.tools.zip.ZipFile(file.getPath());
            
            createDirectory(outputDir,null);//创建输出目录  
       
            Enumeration<?> enums = zipFile.getEntries();  
              
            while (enums.hasMoreElements()) {
                  
                ZipEntry entry = (ZipEntry) enums.nextElement();  
                System.out.println(zipFile.getEncoding());
                System.out.println("解压." +  entry.getName());  
                  
                if (entry.isDirectory()) {//是目录  
                      
                    createDirectory(outputDir, entry.getName());//创建空目录  
                      
                } else {//是文件  
                      
                    File tmpFile = new File(outputDir + "/" + entry.getName());  
                      
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录  
                      
                    InputStream in = null;  
                      
                    OutputStream out = null; 
                    try{  
                        in = zipFile.getInputStream(entry);
                        out = new FileOutputStream(tmpFile);  
                          
                        int length = 0;  
                          
                        byte[] b = new byte[2048];  
                          
                        while ((length = in.read(b)) != -1) {  
                            out.write(b, 0, length);  
                        }
                      
                    } catch (IOException ex) {  
                        throw ex;  
                    } finally {  
                        if(in!=null) {
                        	in.close();  
                        }
                        if(out!=null) {
                        	out.close();  
                        }
                    }  
                      
                }  
                  
            }  
        } catch (IOException e) {
            throw new IOException("解压缩文件出现异常");
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭zipFile出现异常");
            }
        }
    }
    
    /** 
     * 构建目录 
     * @param outputDir 
     * @param subDir 
     */  
    public void createDirectory(String outputDir,String subDir){
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空  
            file = new File(outputDir + "/" + subDir);  
        }  
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
