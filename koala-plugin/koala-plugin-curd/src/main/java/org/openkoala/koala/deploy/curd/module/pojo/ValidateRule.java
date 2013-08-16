/**
 * @(#)ValidateType.java
 * 
 * Copyright csair.All rights reserved.
 * This software is the XXX system. 
 *
 * @Version: 1.0
 * @JDK: jdk jdk1.6.0_10
 * @Module: deployPlugin-curd
 */ 
 /*- 				History
 **********************************************
 *  ID      DATE           PERSON       REASON
 *  1     2013-3-28     Administrator    Created
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
package org.openkoala.koala.deploy.curd.module.pojo;
/**
 * 类    名：ValidateType.java
 *   
 * 功能描述：验证规则。	
 *  
 * 创建日期：2013-3-28下午4:02:17     
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

public enum ValidateRule {
    
    Email("电子邮箱"),
    Mobile("手机号码"),
    TelePhone("电话号码"),
    IdCard("身份证号码"),
    Number("数字类型"),
    URL("URL链接"),
    English("英文字符"),
    Chinese("中文字符"),
    Regex("正则表达式");
    
    private final String ruleName;
    
    private ValidateRule(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }
    
    public static ValidateRule string2Enum(String ruleName){
        if(ruleName == null || "".equals(ruleName)) {
        	return null;
        }
        
        ValidateRule[] rules = ValidateRule.values();
        for (ValidateRule validateRule : rules) {
            if(validateRule.getRuleName().equals(ruleName)) {
            	return validateRule;
            }
        }
        return null;
    }
    
}
