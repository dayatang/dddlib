/*- 				History
 **********************************************
 *  ID      DATE           PERSON       REASON
 *  1     2013-4-7     Administrator    Created
 **********************************************
 */

/*
 * Copyright (c) openkoala 2011 All Rights Reserved
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
package org.openkoala.jbpm.core.jbpm.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;


/**
 * 类 名：BaseAction.java
 * 
 * 功能描述：struts2 aciton 基类
 * 
 * 创建日期：2013-4-7下午2:01:15
 * 
 * 版本信息：v1.0
 * 
 * 版权信息：Copyright (c) 2011 openkoala All Rights Reserved
 * 
 * 作 者：vakinge(chiang.www@gmail.com)
 * 
 * 修改记录： 修 改 者 修改日期 文件版本 修改说明
 */

public class BaseAction extends ActionSupport {

    private static final long serialVersionUID = 8096997943592879454L;

    protected Map<String, Object> dataMap = new HashMap<String, Object>();
    
    protected final static String JSON = "JSON";

    public int page;

    public int pagesize;

    /**
     * Convenience method to get the request
     * 
     * @return current request
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * Convenience method to get the response
     * 
     * @return current response
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    /**
     * Convenience method to get the session. This will create a session if one
     * doesn't exist.
     * 
     * @return the session from the request (request.getSession()).
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 输出json数据到web页面
     * @param response
     * @param jsons
     * @throws IOException 
     */
    public static  void writeJSON(String json){
        try {
            writeObject(json, "text/x-json;charset=UTF-8"); 
        } catch (Exception e) {
        }
        
    }
    
    /**
     * 
     * @param key
     * @param value
     */
    public static void writeJSON(String key, String value) {
        try {
            String template = "{\"{key}\":\"{value}\"}";
            writeObject(template.replace("{key}", key)
                    .replace("{value}", value), "text/x-json;charset=UTF-8");
        } catch (Exception e) {
        }
    }

    /**
     * 直接输出HTML.
     * 
     * @param response
     * @param text
     * @throws IOException
     */
    public static void writeHTML(String text) {
        try {
            writeObject(text == null ? "" : text, "text/html;charset=UTF-8");
        } catch (Exception e) {
        }
    }

    /**
     * 输出.
     * 
     * @param contentType
     *            内容的类型.html,text,xml的值见后，json为"text/x-json;charset=UTF-8"
     * @throws IOException
     */
    private static void writeObject(String text, String contentType)
            throws IOException {
        PrintWriter writer = null;
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType(contentType);
            writer = response.getWriter();
            writer.write(text);
        } finally {
            try {
                writer.flush();
            } catch (Exception e2) {
            }
        }

    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

}
