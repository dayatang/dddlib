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
package org.openkoala.koala.monitor.datasync.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.openkoala.koala.monitor.datasync.base.ServerCommondListener;
import org.openkoala.koala.monitor.remote.Commond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：<br />
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
public class DatasyncClient extends IoHandlerAdapter {

	private static final Logger _LOG = LoggerFactory.getLogger(DatasyncClient.class);

	private String serverHost;

	private int serverPort = 9999;
	private long connectTimeout = 30 * 1000L;

	private IoSession session = null;
	
	private ServerCommondListener listener;
	
	/**
	 * 
	 */
	public DatasyncClient(String serverHost,int serverPort,long connectTimeout,ServerCommondListener listener) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.connectTimeout = connectTimeout;
		this.listener = listener;
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.close(true);
		session = null;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		String clientKey = (String) session.getAttribute("clientKey");
		_LOG.error(session + "[{}]==>"+cause.getMessage(),clientKey);

	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message.getClass() == Commond.class) {
			Commond cmd = (Commond) message;
			if(listener != null)listener.execute(cmd);
		}
	}

	public void startup() {
		connect();
	}

	/**
	 * 连接
	 * @return
	 */
	public IoSession connect() {
		try {
			NioSocketConnector connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(connectTimeout);
			ObjectSerializationCodecFactory factory = new ObjectSerializationCodecFactory();
			factory.setDecoderMaxObjectSize(1048576 * 10);
			connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(factory));

			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.setHandler(this);

			ConnectFuture future = connector.connect(new InetSocketAddress(serverHost, serverPort));
			future.awaitUninterruptibly();
			session = future.getSession();
			_LOG.info("与服务端建立连接成功");
			return session;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 中断连接
	 */
	public void disConnect(){
		if (session != null && session.isConnected()) {
			CloseFuture close = session.close(true);
			close.awaitUninterruptibly();
		}
	}
	
	public boolean resetConnect(){
		try {
			disConnect();
			try {
			   Thread.sleep(1000 * 5);	
			} catch (Exception e) {}
			connect();
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	/**
	 * 发送数据
	 * @param data
	 */
	public void sendData(Object data) {
		session.write(data);
	}
	/**
	 * 检查是否处于链接状态
	 * @return
	 */
	public boolean isConnected(){
		return session != null && session.isConnected();
	}
	
	public static void main(String[] args) {
		DatasyncClient client = new DatasyncClient("localhost", 8000, 60000000, null);
		client.startup();
		System.out.println(client.session);
		client.sendData("jjjj");
	}
}
