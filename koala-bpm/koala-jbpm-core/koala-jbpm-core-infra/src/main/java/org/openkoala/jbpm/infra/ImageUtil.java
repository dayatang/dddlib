package org.openkoala.jbpm.infra;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	public static String getProcessPictureSrc(String processId,byte[] png,List<Integer> nodes){
		String path = System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/");
		if(path.endsWith("/")==false)path=path+"/";
		String imagePath = path+processId+"";
		for(int i=0;i<nodes.size();i=i+2){
			imagePath = imagePath +"_" + nodes.get(i) +"_"+nodes.get(i+1);
		}
		imagePath+=".png";
		if(new File(imagePath).exists()){
			return new File(imagePath).getAbsolutePath();
		}else{
			InputStream imagein= null;
			InputStream imagein2 = null;
			try{
			imagein= new ByteArrayInputStream(png);
			imagein2=ImageUtil.class.getResourceAsStream("/png/role.gif");
	        BufferedImage image=ImageIO.read(imagein);
	        BufferedImage image2=ImageIO.read(imagein2);
	        Graphics g=image.getGraphics();
	        for(int i=0;i<nodes.size();i=i+2){
	        	System.out.println(image2.getWidth());
				 g.drawImage(image2,nodes.get(i)-5,nodes.get(i+1)-5,null);
			}
	        ImageIO.write(image, "png", new File(imagePath));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
		        try {
					imagein.close();
			        imagein2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return imagePath;
	}
	
	public static byte[] getProcessPictureByte(String processId,byte[] png,List<Integer> nodes){
		String dest = getProcessPictureSrc(processId,png,nodes);
		byte[] result = null;
		 try {
			result = readInputStream(new FileInputStream(dest));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 return result;
	}
	
	private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[1024]; 
        int length = -1; 
        try { 
            while ((length = is.read(buffer)) != -1) { 
                baos.write(buffer, 0, length); 
            } 
            baos.flush(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        byte[] data = baos.toByteArray(); 
        try { 
            is.close(); 
            baos.close(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return data; 
    }
}
