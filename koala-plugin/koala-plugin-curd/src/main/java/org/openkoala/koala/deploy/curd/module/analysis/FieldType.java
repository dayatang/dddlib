package org.openkoala.koala.deploy.curd.module.analysis;

/**
 * 
 * 类    名：FieldType.java
 *   
 * 功能描述：领域实体类中的属性类型 
 *  
 * 创建日期：2013-1-21下午3:52:55     
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
public enum FieldType {
    ID {
        public String toString() {
            return "主键";
        }
    },
    EmbeddedId{
        public String toString() {
            return "联合主键";
        }
    },
    Column {
        public String toString() {
            return "数据库字段";
        }
    },
    OneToOne {
        public String toString() {
            return "一对一关联";
        }
    },
    OneToMany {
        public String toString() {
            return "一对多关联";
        }
    },
    ManyToOne {
        public String toString() {
            return "多对一关联";
        }
    },
    ManyToMany {
        public String toString() {
            return "多对多关联";
        }
    },
    Other {
        public String toString() {
            return "其它";
        }
    }
}
