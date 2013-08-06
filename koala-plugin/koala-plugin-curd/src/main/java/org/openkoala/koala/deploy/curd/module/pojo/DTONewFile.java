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
package org.openkoala.koala.deploy.curd.module.pojo;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.curd.generator.PrimitiveType;
import org.openkoala.koala.deploy.curd.generator.VelocityContextUtils;
import org.openkoala.koala.deploy.curd.generator.WrapperType;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 类    名：VoNewFile.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-1-24上午11:59:39     
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
public class DTONewFile extends NewFile {
    
    private static final String DATE_TYPE = "java.util.Date";

	private static final String STRING_TYPE = "java.lang.String";
	
	private static final String BOOLEAN_TYPE = "java.lang.Boolean";

	private static final String TEMPLATE_PATH = "templates/dtoTemplate.vm";

    /**
     * 字段以及其对应的类型
     */
    private Map<String, String> fieldMap;
    
    private Set<String> imports = new HashSet<String>();
    
    private MavenProject project;
    
    private EntityModel entityModel;
    
    /**
     * @param name
     * @param projects
     * @param type
     */
    public DTONewFile(String name, List<MavenProject> projects, NewFileType type, MavenProject project, EntityModel entityModel) {
        super(name, projects, type);
        this.project = project;
        this.entityModel = entityModel;
    }

    
    public Map<String, String> getFieldMap() {
        return fieldMap;
    }


    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    /*
     *@see org.openkoala.koala.deploy.curd.module.pojo.NewFile#process()
     */
    @Override
    public void process() {
        VelocityContext context = VelocityContextUtils.getVelocityContext();
        context.put("dtoClass", this);
        context.put("project", project);
        try {
            VelocityUtil.vmToFile(context, TEMPLATE_PATH, getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String getPackageName() {
    	return super.getPackageName() + ".dto";
    }
    
    /**
     * 获取import列表
     * @return
     */
    public Set<String> getImports() {
        for (Entry<String, String> entry : fieldMap.entrySet()) {
            if (isReferenceType(entry.getValue())) {
                imports.add(entry.getValue());
            }
        }
        return this.imports;
    }
    
    /**
     * 是否是基本数字类型
     * @param type
     * @return
     */
    public boolean isPrimitiveType(String type) {
		for (PrimitiveType primitiveType : PrimitiveType.values()) {
			if (type.equals(primitiveType.toString())) {
				return true;
			}
		}
    	return false;
    }
    
    /**
     * 是否是包装类型
     * @param type
     * @return
     */
    public boolean isWrapperType(String type) {
    	for (WrapperType wrapperType : WrapperType.values()) {
    		if (type.equals(wrapperType.toString())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 将原是类型转成包装类型
     * @param type
     * @return
     */
    public String convertPrimitiveToWrapper(String type) {
    	if (type.indexOf(".") != -1) {
    		return type.substring(type.lastIndexOf(".") + 1);
    	}
        for (PrimitiveType primitiveType : PrimitiveType.values()) {
        	if (type.equals(primitiveType.toString())) {
        		return primitiveType.convertToWrapper();
        	}
        }
    	return null;
    }
    
    /**
     * 是否是String类型
     * @param type
     * @return
     */
    public boolean isStringType(String type) {
    	if (type.indexOf(STRING_TYPE) != -1) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * 是否是java.util.Date类型
     * @param type
     * @return
     */
    public boolean isDateType(String type) {
    	if (type.indexOf(DATE_TYPE) != -1) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * 是否是boolean类型
     * @return
     */
    public boolean isBooleanType(String type) {
    	if (type.indexOf(".") == -1 && type.equals("boolean")) {
    		return true;
    	}
    	if (type.indexOf(BOOLEAN_TYPE) != -1) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * 是否是引用类型（并且不是java.lang包下的类）
     * @param type
     * @return
     */
    private boolean isReferenceType(String type) {
        if (type.indexOf(".") != -1 && type.indexOf("java.lang") == -1) {
            return true;
        }
        return false;
    }


	public EntityModel getEntityModel() {
		return entityModel;
	}
    
}


