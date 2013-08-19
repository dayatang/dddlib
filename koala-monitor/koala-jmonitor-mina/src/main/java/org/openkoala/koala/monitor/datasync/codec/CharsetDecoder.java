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
package org.openkoala.koala.monitor.datasync.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-5-21 下午3:14:57  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class CharsetDecoder implements ProtocolDecoder {
	 
    
    private final static Charset charset = Charset.forName("UTF-8");    
    // 可变的IoBuffer数据缓冲区
    private IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);
    
    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        System.out.println("#########decode#########");
        
        // 如果有消息
        while (in.hasRemaining()) {
            // 判断消息是否是结束符，不同平台的结束符也不一样；
            // windows换行符（\r\n）就认为是一个完整消息的结束符了； UNIX 是\n；MAC 是\r
            byte b = in.get();
            if (b == '\n') {
                buff.flip();
                byte[] bytes = new byte[buff.limit()];
                buff.get(bytes);
                String message = new String(bytes, charset);
                
                buff = IoBuffer.allocate(100).setAutoExpand(true);
                
                // 如果结束了，就写入转码后的数据
                out.write(message);
                //log.info("message: " + message);
            } else {
                buff.put(b);
            }
        }
    }
 
    @Override
    public void dispose(IoSession session) throws Exception {
    	System.out.println("#########dispose#########");
    }
 
    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
    	System.out.println("#########完成解码#########");
    }
}
