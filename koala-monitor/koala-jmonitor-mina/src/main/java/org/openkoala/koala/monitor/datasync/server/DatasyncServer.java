/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.monitor.datasync.server;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.openkoala.koala.monitor.datasync.base.ClientRequestListener;
import org.openkoala.koala.monitor.remote.Commond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：mina数据同步服务端<br />
 * 
 * 创建日期：2013-5-21 下午3:57:11 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class DatasyncServer extends IoHandlerAdapter{

	private static final Logger LOG = LoggerFactory.getLogger(DatasyncServer.class);

	private ClientRequestListener listener;
	private int port;
	
	private final Map<String, IoSession> sessions = new ConcurrentHashMap<String, IoSession>();

	private boolean runNormal = false;
	/**
	 * @param listener
	 * @param port
	 */
	public DatasyncServer(int port,ClientRequestListener listener) {
		this.listener = listener;
		this.port = port;
	}
	
	public boolean isRunNormal() {
		return runNormal;
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if(listener != null){
			listener.afterClientClosed(session);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		String clientKey = (String) session.getAttribute("clientKey");
		LOG.error(session + "[{}]==>"+cause.getMessage(),clientKey);
	}

	@Override
	public void messageReceived(IoSession session, Object message){
		try {
			if (message.getClass() == Commond.class) {
				if(listener != null){
					listener.afterClientRequest(session, (Commond)message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startup() {
		try {
			IoAcceptor acceptor = new NioSocketAcceptor();
			// acceptor.getFilterChain().addLast( "logger", new LoggingFilter()
			// );
			ObjectSerializationCodecFactory factory = new ObjectSerializationCodecFactory();
			factory.setDecoderMaxObjectSize(1048576 * 10);
			acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(factory));
			acceptor.setHandler(this);
			acceptor.getSessionConfig().setReadBufferSize(4096);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 120);
			acceptor.bind(new InetSocketAddress(port));
			LOG.info("监控同步服务服务端启动,端口："+port);
			runNormal = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			for (IoSession session : sessions.values()) {
				if (session.isConnected()) {
					session.close(true);
				}
			}
		} catch (Exception e) {
			
		}
		sessions.clear();
	}

	/**
	 * 发送数据
	 * @param clientId
	 * @param data
	 */
	public void sendData(String clientId, Object data) {
		IoSession session = sessions.get(clientId);
		if (session != null && session.isConnected()) {
			WriteFuture future = session.write(data);
			future.awaitUninterruptibly();
		}
	}

	/**
	 * 广播
	 * @param data
	 */
	public void broadcast(Object data) {
		synchronized (sessions) {
			for (IoSession session : sessions.values()) {
				if (session.isConnected()) {
					session.write(data);
				}
			}
		}
	}


	public Map<String, IoSession> getSessions() {
		return sessions;
	}
	
	public static void main(String[] args) {
		DatasyncServer server = new DatasyncServer(8000, null);
		server.startup();
	}
	
}
