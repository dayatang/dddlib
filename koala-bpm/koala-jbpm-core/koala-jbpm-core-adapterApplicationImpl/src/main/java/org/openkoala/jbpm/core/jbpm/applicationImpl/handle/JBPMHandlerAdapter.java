package org.openkoala.jbpm.core.jbpm.applicationImpl.handle;

import java.util.Map;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.drools.runtime.process.ProcessContext;
import org.openkoala.jbpm.core.jbpm.applicationInterface.vo.JbpmAdapterParameter;

public class JBPMHandlerAdapter extends IoHandlerAdapter {

	private ProcessContext context;
	
	private JbpmAdapterParameter adapter;
	
	public JBPMHandlerAdapter(ProcessContext context,
			JbpmAdapterParameter adapter) {
		super();
		this.context = context;
		this.adapter = adapter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(final IoSession session, final Object message)
			throws Exception {
		if(message == null){
			return;
		}else if(message instanceof String){
			this.context.getNodeInstance().setVariable("_TMP_"+adapter.getName()+"_RESULT", (String)message);
		}else if(message instanceof Map){
			Map<String,Object> result = (Map<String,Object>)message;
			Set<Map.Entry<String, Object>> entrySet = result.entrySet();
			for(Map.Entry<String, Object> entry:entrySet){
				context.getNodeInstance().setVariable(entry.getKey(),
						entry.getValue());
			}
		}else{
			throw new UnsupportedOperationException("只支持返回MAP,String,Void结果");
		}
		session.close(true);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
		super.messageSent(session, message);
	}
	
	@Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.write(adapter);
	}
	
	
	
}
