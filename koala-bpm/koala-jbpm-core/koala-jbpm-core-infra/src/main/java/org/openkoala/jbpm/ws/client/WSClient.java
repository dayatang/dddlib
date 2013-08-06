package org.openkoala.jbpm.ws.client;

import org.openkoala.jbpm.ws.WebServiceApplicationService;

public class WSClient {

	private static WebServiceApplicationService service;
	
	public static WebServiceApplicationService getWebServiceApplicationService(){
		if(service==null){
			service  = new WebServiceApplicationService();
		}
		return service;
	}
	
	public static String getTaskUser(String user){
		return getWebServiceApplicationService().getWebServiceApplicationImplPort().getTaskUser(user);
	}
	
	public static void logMessage(String user,String message){
		getWebServiceApplicationService().getWebServiceApplicationImplPort().logMessage(user, message);
	}
	
	public static void main(String args[]){
		System.out.println(WSClient.getTaskUser("lingen"));
	}
}
