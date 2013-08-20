/**
 * @(#)KoalaUISurpport.java
 * 
 * Copyright csair.All rights reserved.
 * This software is the XXX system. 
 *
 * @Version: 1.0
 * @JDK: jdk jdk1.6.0_10
 * @Module: ui-surpport
 */ 
 /*- 				History
 **********************************************
 *  ID      DATE           PERSON       REASON
 *  1     2013-1-23     Administrator    Created
 **********************************************
 */

/*
 * Copyright (c) OpenKoala 2011 All Rights Reserved
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
package org.openkoala.ui.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openkoala.ui.base.datamodel.BaseVo;
import org.openkoala.ui.base.datamodel.TreeNodeVo;
import org.openkoala.ui.service.KoalaUISurpportApplication;

import com.dayatang.domain.InstanceFactory;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;


/**
 * 类    名：KoalaUISurpport.java
 *   
 * 功能描述：UI组件数据访问支持	
 *  
 * 创建日期：2013-1-23上午10:53:21     
 * 
 * 版本信息：v1.0
 * 
 * 版权信息：Copyright (c) 2011 openkoala All Rights Reserved
 * 
 * 作    者：vakinge(chiang.www@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明	
 */

public class KoalaUISurpport extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private KoalaUISurpportApplication koalaUISurpportApplication;

    public KoalaUISurpportApplication getKoalaUISurpportApplication() {
        if (koalaUISurpportApplication == null) {
        	koalaUISurpportApplication = InstanceFactory.getInstance(KoalaUISurpportApplication.class);
        }
        return koalaUISurpportApplication;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置浏览器不缓存本页
        //response.addHeader("Pragma", "no-cache");
       // response.addHeader("Cache-Control", "no-cache");
       // response.addHeader("Expires", "0");
        String ui_type = request.getParameter("_type");
        if (ui_type == null || "".equals(ui_type.trim())) {
        	return;
        }
        if ("dropdown".equals(ui_type)) {
            buildDropdown(request,response);
        } else if("tree".equals(ui_type)) {
            buildTree(request,response);
        }
    }


    /**
     * 生成树
     * @param request
     * @param response
     */
    private void buildTree(HttpServletRequest request, HttpServletResponse response) {
        try {
            String parentId = request.getParameter("parentId");
            String beanName = request.getParameter("beanName");
            String beanFields = request.getParameter("beanFields");
            //是否延时加载子节点
            boolean lazy = Boolean.parseBoolean(request.getParameter("lazy"));
            String filterParams = request.getParameter("filter");
            TreeNodeVo treeNodeVo = getKoalaUISurpportApplication().queryTreeNodes(beanName, beanFields,parentId,filterParams,lazy);
            
            String json = JSONMapper.toJSON(treeNodeVo.getChildren()).render(false);
            
            if(!json.startsWith("["))json = "["+json+"]";
            writeJSON(response,json);
        } catch (Exception e) {
        	writeJSON(response, "{error}");
        }
    }


    /**
     * 生成下拉框
     * @param request
     * @param response
     */
    private void buildDropdown(HttpServletRequest request,HttpServletResponse response) {
        try {
            //
            String json = null;
            String dropdownStyle = request.getParameter("dropdownStyle");
            String beanName = request.getParameter("beanName");
            String beanFields = request.getParameter("beanFields");
            String parentId = request.getParameter("parentId");
            String filterParams = request.getParameter("filter");
            if ("list".equals(dropdownStyle)) {
                List<BaseVo> options = getKoalaUISurpportApplication().queryAllOptions(beanName, beanFields, parentId, filterParams);
                json = JSONMapper.toJSON(options).render(false);
            } else if ("tree".equals(dropdownStyle)) {
                buildTree(request, response);
            }
            
            writeJSON(response,json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 输出json数据到web页面
     * @param response
     * @param jsons
     * @throws IOException 
     */
    public static void writeJSON(HttpServletResponse response,String json){
        try {
            writeObject(response,json, "text/x-json;charset=UTF-8"); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    }
    
    /**
     * 输出.
     * 
     * @param contentType
     *            内容的类型.html,text,xml的值见后，json为"text/x-json;charset=UTF-8"
     * @throws IOException 
     */
    private static  void writeObject(HttpServletResponse response,String text, String contentType) throws IOException {
        PrintWriter writer = null;
        try {
            response.setContentType(contentType);
            writer = response.getWriter();
            writer.write(text);
        } finally {
            try {
            	writer.close();
            } catch (Exception e2) {
            	e2.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            List<BaseVo> options = new ArrayList<BaseVo>();
            options.add(new BaseVo("1000", "菜单1"));
            options.add(new BaseVo("2000", "菜单2"));
            String json = JSONMapper.toJSON(options).render(false);
            System.out.println(json);
        } catch (MapperException e) {
            e.printStackTrace();
        }
    }
}
