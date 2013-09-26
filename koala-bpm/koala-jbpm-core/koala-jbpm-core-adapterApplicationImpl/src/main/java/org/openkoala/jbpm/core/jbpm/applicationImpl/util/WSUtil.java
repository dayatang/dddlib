package org.openkoala.jbpm.core.jbpm.applicationImpl.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.drools.runtime.process.ProcessContext;
import org.openkoala.jbpm.infra.XmlParseUtil;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;

public class WSUtil {
	
	public static void invoke(ProcessContext context, Object url,
			String method, String... args) throws Exception {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) context
				.getProcessInstance();
		String processParams = parseContext(context);
		String userParams = parseParams(args);
		Object[] returns = wsConnect(String.valueOf(url), method, processParams, userParams);

		for (Object value : returns) {
			String returnValue = (String) value;
			Map<String, Object> params = XmlParseUtil.xmlToPrams(returnValue);
			Set<String> keys = params.keySet();
			for (String key : keys) {
				in.setVariable(key, params.get(key));
			}
		}
	}
	
	/**
	 * 调用返回
	 * @param url
	 * @param user
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public static List<String> invokeUserGroup(String user,String url,String method) throws Exception{
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(url);
		Object[] returns = client.invoke(method, new Object[] {user});
		String value = (String)returns[0];
		if(value==null || value.trim().equals("")){
			return Collections.EMPTY_LIST;
		}else{
			return XmlParseUtil.parseListXml(value);
		}
		
	}

	private static Object[] wsConnect(String url, String method,
			String processParams, String userParams) throws Exception {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(url);
		Object[] returns = client.invoke(method, new Object[] { processParams,
				userParams });
		return returns;

	}


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		
		String url = "http://localhost:8080/ws/ksAdapter?wsdl";
		
		System.out.println(WSUtil.invokeUserGroup("aaa", url, "getGroupsByUser"));
	}

	private static String parseParams(String... strings) {
		Document document = DocumentHelper.createDocument();
		Element params = document.addElement("Params");
		for (String string : strings) {
			params.addElement("value").setText(string);
		}
		return document.asXML();
	}

	private static String parseContext(ProcessContext context) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) context
				.getProcessInstance();

		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Process");

		root.addElement("processId").setText(in.getProcessId());
		root.addElement("processInstanceId")
				.setText(String.valueOf(in.getId()));
		root.addElement("processName").setText(in.getProcessName());

		Map<String, Object> variables = in.getVariables();
		Set<String> keys = variables.keySet();
		Element variablesElements = root.addElement("Variables");

		for (String key : keys) {
			variablesElements.addElement(key).setText(
					String.valueOf(variables.get(key)));
		}
		return document.asXML();
	}
}
