package org.openkoala.jbpm.infra;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SOAPClient {
	
	public static String soapRequest(String url,String content){
		
		return null;
	}
	
	public static void copy(InputStream in, OutputStream out) 
	   throws IOException {
	    synchronized (in) {
	      synchronized (out) {
	        byte[] buffer = new byte[256];
	        while (true) {
	          int bytesRead = in.read(buffer);
	          if (bytesRead == -1) break;
	          out.write(buffer, 0, bytesRead);
	        }
	      }
	    }
	  } 
}
