package org.openkoala.jbpm.core.jbpm.applicationImpl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drools.runtime.process.ProcessContext;
import org.openkoala.jbpm.core.jbpm.applicationInterface.vo.JbpmAdapterParameter;
import org.openkoala.jbpm.core.jbpm.util.MapSerializeUtil;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;

/**
 * 远程方法调用实现类
 * 
 * @author lingen
 * 
 */
public class AdapterUtil {

	/**
	 * 调用业务适配器，不返回任何
	 * 
	 * @param context
	 * @param method
	 * @param strings
	 * @throws Exception 
	 */
	public static void invoke(ProcessContext context, String name,
			String... strings) throws Exception {
		String type = "voidInvoke";
		List<String> userParams = parseString(strings);
		byte[] params = parseByte(context);
		JbpmAdapterParameter adapter = new JbpmAdapterParameter(name,params,userParams);
		
		AdapterClient.getInstance("127.0.0.1", 9123).connectToServer(context, adapter);
	}

	
	private static List<String> parseString(String... strings) {
		List<String> params = new ArrayList<String>();
		for (String string : strings) {
			params.add(string);
		}
		return params;
	}
	
	private static byte[] parseByte(ProcessContext context) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) context
				.getProcessInstance();
		Map<String, Object> vas = in.getVariables();
		byte[] bytes = MapSerializeUtil.serialize(vas);
		return bytes;
	}
}
