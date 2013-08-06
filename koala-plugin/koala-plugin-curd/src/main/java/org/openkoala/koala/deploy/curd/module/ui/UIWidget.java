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
package org.openkoala.koala.deploy.curd.module.ui;

import org.apache.commons.lang.StringUtils;
import org.openkoala.koala.deploy.curd.module.pojo.ValidateRule;

/**
 * 类    名：UIWidget.java
 *   
 * 功能描述：页面控件	
 *  
 * 创建日期：2013-1-24上午9:06:22     
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
public abstract class UIWidget {
    
    /**
     * 显示的中文名
     */
    protected String name;
    /**
     * 默认值
     */
    protected String defaultValue;
    
    /*表达式，如id,name这种字段，不可使用关联*/
    protected String express;
    
    protected String type;
    
    protected boolean required;//是否为空
    
    protected ValidateRule validateRule;//验证规则
    
    protected String validateExpress;//验证表达式 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
        this.required = required;
    }

	public ValidateRule getValidateRule() {
		return validateRule;
	}

    public void setValidateRule(ValidateRule validateRule) {
        this.validateRule = validateRule;
    }
    
    
    /**
     * 
     * @return
     */
    public String getValidateAttrs() {
        StringBuffer sb = new StringBuffer("");
        if(validateRule != null){
            sb.append(" dataType=\"").append(validateRule.name()).append("\" ");
            if(validateRule.equals(ValidateRule.Regex) && StringUtils.isNotBlank(validateExpress)){
                boolean warped = validateExpress.startsWith("/");
                sb.append(" validateExpr=\"").append(warped?"":"/").append(validateExpress).append(warped?"":"/").append("\" ");
            }
            if(!required)sb.append("require=\"false\"");
        }else{
            if(required)sb.append("dataType=\"Require\"");
        }
        
        return sb.toString();
    }
    

    public String getValidateExpress() {
        return validateExpress;
    }

    public void setValidateExpress(String validateExpress) {
        this.validateExpress = validateExpress;
    }

    /**
     * 控件类型
     * @return
     */
    public abstract String getWidgetType();
    
    public abstract String toString();
    
    public String getViewType(){
        return this.getClass().getSimpleName();
    }
}
