/**
 * @(#)KoalaUISurpportApplicationImpl.java
 * 
 * Copyright csair.All rights reserved.
 * This software is the XXX system. 
 *
 * @Version: 1.0
 * @JDK: jdk jdk1.6.0_10
 * @Module: examine-application-Impl
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
package org.openkoala.ui.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.ui.base.datamodel.BaseVo;
import org.openkoala.ui.base.datamodel.TreeNodeVo;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;

/**
 * 类    名：KoalaUISurpportApplicationImpl.java
 *   
 * 功能描述：具体功能做描述。    
 *  
 * 创建日期：2013-1-23下午8:01:17     
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
//@Named
public class KoalaUISurpportApplicationImpl implements KoalaUISurpportApplication{

    private static QueryChannelService queryChannelService;
	
	public static QueryChannelService getQueryChannelService() {
		if (queryChannelService == null) {
			queryChannelService = InstanceFactory.getInstance(QueryChannelService.class);
		}
		return queryChannelService;
	}
    
//    @Inject
//    SessionFactory sessionFactory;

    @Override
    public List<BaseVo> queryAllOptions(String beanName, String beanFields,String parentId,String filterParams) {
        StringBuffer hqlBuf = new StringBuffer().append("select ").append(beanFields).append(" from ").append(beanName).append(" where 1=1");
        
      //idFieldName ,textFieldName,parentFieldName
        String[] beanFieldsArr = beanFields.split("\\,");

        String idFieldName = beanFieldsArr[0];
        Object[] params = new Object[0];
        if(beanFieldsArr.length > 2){
            String parentIdField = beanFieldsArr[2];
            hqlBuf.append(" and ").append(parentIdField);
            if(StringUtils.isNotBlank(parentId)){
                hqlBuf.append(" = ?"); 
                params = new Object[]{convertValueAsType(beanName, parentIdField, parentId)};
            }else{
                hqlBuf.append(" is NULL"); 
            }
        }
        
        if(StringUtils.isNotBlank(filterParams)){
            hqlBuf.append(" and (").append(filterParams).append(")");
        }
        
        hqlBuf.append(" order by ").append(idFieldName);
        
        List<Object[]> list = getQueryChannelService().queryResult(hqlBuf.toString(), params);
        
        List<BaseVo> options = new ArrayList<BaseVo>();
        
        for (Object[] objects : list) {
            options.add(new BaseVo(objects[0].toString(), objects[1].toString()));
        }
        return options;
    }


    @Override
    public TreeNodeVo queryTreeNodes(String beanName,String beanFields,String parentId,String filterParams,boolean lazyLoadChildNode) {
        
        TreeNodeVo topNode = new TreeNodeVo();
        try {
          //idFieldName ,textFieldName,parentFieldName
            String[] tmpArr = beanFields.split("\\,");
            //
            String idFieldName = tmpArr[0];
            Object[] params = null;
            //
            String selectFields = tmpArr.length == 2 ? beanFields : beanFields.substring(0, beanFields.lastIndexOf(","));
            StringBuffer hqlBuf = new StringBuffer().append("select ")//
                                    .append(selectFields)//
                                    .append(" from ").append(beanName).append(" where 1=1");
            if(tmpArr.length > 2){
                String parentIdField = tmpArr[2]; 
                hqlBuf.append(" and ").append(parentIdField);
                if(StringUtils.isNotBlank(parentId)){
                    hqlBuf.append(" = ?");
                    params = new Object[]{convertValueAsType(beanName, parentIdField, parentId)};
                }else{
                    hqlBuf.append(" is NULL");
                    params = new Object[0];
                }
            }

            if(StringUtils.isNotBlank(filterParams)){
                hqlBuf.append(" and (").append(filterParams).append(")");
            }
            
            //子节点个数
            long childNodeCount = getQueryChannelService().queryResultSize(hqlBuf.toString(), params);
            if(childNodeCount == 0)return topNode;
            
            hqlBuf.append(" order by ").append(idFieldName);
            List<Object[]> list = getQueryChannelService().queryResult(hqlBuf.toString(), params);
            
            for (Object[] objects : list) {
                TreeNodeVo childNode = null;
                if(lazyLoadChildNode){
                    childNode = new TreeNodeVo();
                }else{
                    parentId = objects[0].toString();
                    childNode = queryTreeNodes(beanName, beanFields, parentId,null, lazyLoadChildNode);
                }
                childNode.setParentID(parentId);
                childNode.setId(objects[0].toString());
                childNode.setText(objects[1].toString());
                topNode.getChildren().add(childNode);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return topNode;
    }
    
    /**
     * 转换指定属性类型的值
     * @param beanName
     * @param fieldName
     * @param value
     * @return
     */
    private Object convertValueAsType(String beanName, String fieldName, String value) {
        try {
            Object result = value;
            /*Type propertyType = sessionFactory.getClassMetadata(beanName).getPropertyType(fieldName);
            if(idPropertyType instanceof org.hibernate.type.IntegerType) {
                
            }else if(idPropertyType instanceof org.hibernate.type.LongType) {
                params = new Object[] { Long.parseLong(parentId) };
            }else {
                params = new Object[] { parentId };
            }*/
            Class<?> type = Class.forName(beanName).getDeclaredField(fieldName).getType();
            if(type == java.lang.Integer.class  || type == long.class){
                result = Integer.parseInt(value);
            }else if(type == java.lang.Long.class || type == long.class){
                result = Long.parseLong(value);
            }
            return result;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
