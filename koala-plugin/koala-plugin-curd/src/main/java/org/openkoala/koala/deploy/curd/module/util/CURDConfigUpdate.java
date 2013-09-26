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
package org.openkoala.koala.deploy.curd.module.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.action.xml.DocumentUtil;
import org.openkoala.koala.action.xml.XPathQueryUtil;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.core.model.RelationFieldModel;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 类    名：CURDConfigUpdate.java
 *   
 * 功能描述：生成CURD后的配置文件的更新
 *  
 * 创建日期：2013-2-5上午10:41:31     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class CURDConfigUpdate {
    
    private static final String JPA_XMLS = "http://java.sun.com/xml/ns/persistence";
    
    /**
     * 更新数据库的配置文件
     * @param entityModel
     * @param project
     */
    public static void UpdateDBConfig(EntityModel entityModel, MavenProject project){
        //找到JPA的DB配置文件
        List<MavenProject> impProjects =  project.getImplProjects();
        String jpaPersistencePath =null;
        for(MavenProject impl:impProjects){
            String config = project.getPath()+"/"+impl.getName()+"/src/main/resources/META-INF/conf/jpa-persistence.xml";
            if(new File(config).exists()){
                jpaPersistencePath = config;
            }
        }
        if(jpaPersistencePath!=null){
            updateJpaDBConfig(entityModel,jpaPersistencePath);
        }
    }
    
    private static void updateJpaDBConfig(EntityModel entityModel,String config){
        List<String> entitys = new ArrayList<String>();
        entitys.add(entityModel.getClassName());
        for(RelationFieldModel relation:entityModel.getRelationFieldModel()){
            entitys.add(relation.getRelationModel().getEntityModel().getClassName());
        }
        jpaDBUpdate(entitys,config);
    }
    
    private static void jpaDBUpdate(List<String> entitys,String config){
        Document jpaDocument = DocumentUtil.readDocument(config);
        String parent = "/xmlns:persistence/xmlns:persistence-unit";
        Element parentElement = XPathQueryUtil.querySingle(JPA_XMLS, parent, jpaDocument);
        for(String entity:entitys){
            String entityQueryString = "/xmlns:persistence/xmlns:persistence-unit[xmlns:class='"+entity+"']";
            Element entityElement = XPathQueryUtil.querySingle(JPA_XMLS, entityQueryString, jpaDocument);
           
            if(entityElement==null){
                entityElement = parentElement.addElement("class", JPA_XMLS);
                entityElement.setText(entity);
                Element add = entityElement;
                parentElement.remove(entityElement);
                parentElement.content().add(0,add);
            }
        }
        DocumentUtil.document2Xml(config, jpaDocument);
    }
}

class PersistenceComparator implements Comparator{

    /*
     *@see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        Element e1 = (Element)o1;
        Element e2 = (Element)o2;
        return getInt(e1.getName()) - getInt(e2.getName());
    }
    
    private int getInt(String type){
        if(type.equals("description"))return 0;
        if(type.equals("provider"))return 1;
        if(type.equals("jta-data-source"))return 2;
        if(type.equals("non-jta-data-source"))return 3;
        if(type.equals("mapping-file"))return 4;
        if(type.equals("jar-file"))return 5;
        if(type.equals("class"))return 6;
        if(type.equals("exclude-unlisted-classes"))return 7;
        if(type.equals("shared-cache-mode"))return 8;
        if(type.equals("validation-mode"))return 9;
        if(type.equals("properties"))return 10;
        if(type.equals("name"))return 11;
        if(type.equals("transaction-type"))return 12;
        if(type.equals("version"))return 13;
        
        return 9999;
    }
   }
