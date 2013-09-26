package org.openkoala.jbpm.core.jbpm.applicationImpl.util;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.drools.runtime.process.ProcessContext;
import org.openkoala.jbpm.core.jbpm.applicationImpl.handle.JBPMHandlerAdapter;
import org.openkoala.jbpm.core.jbpm.applicationInterface.vo.JbpmAdapterParameter;

public class AdapterClient {

	private IoConnector connector;

	private String ip;

	private int port;

	private AdapterClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public static AdapterClient getInstance(String ip, int port) {
		return new AdapterClient(ip, port);
	}

	public void connectToServer(ProcessContext context,
			JbpmAdapterParameter adapter) throws Exception {
		IoSession session = null;
		try {
			connector = new NioSocketConnector();
			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(
							new ObjectSerializationCodecFactory()));
			connector.getSessionConfig().setUseReadOperation(true);
			
			session = connector.connect(new InetSocketAddress(
					ip, port)).awaitUninterruptibly().getSession();
			
			session.write(adapter).awaitUninterruptibly();
			
			ReadFuture readFuture = session.read();
			readFuture.awaitUninterruptibly();
			Object message = readFuture.getMessage();

			if (message == null) {

			} else if (message instanceof String) {
				context.getNodeInstance().setVariable(
						"_TMP_" + adapter.getName() + "_RESULT",
						(String) message);
			} else if (message instanceof Map) {
				Map<String, Object> result = (Map<String, Object>) message;
				Set<String> keys = result.keySet();
				for (String key : result.keySet()) {
					context.getNodeInstance().setVariable(key,
							result.get(key));
				}
			} else {
				throw new UnsupportedOperationException(
						"只支持返回MAP,String,Void结果");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close(true);
			session.getService().dispose();
		}
	}

}
