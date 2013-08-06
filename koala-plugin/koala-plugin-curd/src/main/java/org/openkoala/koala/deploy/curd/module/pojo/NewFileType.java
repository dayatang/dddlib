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
 * 类    名：NewFileType.java
 *   
 * 功能描述：新创建的文件的功能描述
 *  
 * 创建日期：2013-1-23下午5:30:39     
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
public enum NewFileType {
    /*数据转换对象DTO*/
    DTO {
        public String toString() {
            return "application.dto";
        }
    },
    /*APPLICATIO接口*/
    APPLICATION {
        public String toString() {
            return "application";
        }
    },
    /*IMPL实现*/
    IMPL {
        public String toString() {
            return "impl";
        }
    },
    /*WEB ACTION*/
    WEB_ACTION {
        public String toString() {
            return "web.action";
        }
    },
    /*WEB CONTROLLER*/
    WEB_CONTROLLER {
    	public String toString() {
    		return "web.controller";
    	}
    },
    /*LIST页面，包含查询，新增，修改及删除*/
    WEB_LIST {
        public String toString() {
            return "list";
        }
    },
    /*VIEW页面*/
    WEB_VIEW {
        public String toString() {
            return "view";
        }
    },
    
    /*新增页面*/
    WEB_ADD {
        public String toString() {
            return "form";
        }
    },
    /*修改页面*/
    WEB_MODIFY {
        public String toString() {
            return "form";
        }
    }
}
