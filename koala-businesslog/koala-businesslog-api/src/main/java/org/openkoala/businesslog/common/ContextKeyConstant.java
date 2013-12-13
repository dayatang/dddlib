package org.openkoala.businesslog.common;

/**
 * User: zjzhai
 * Date: 12/11/13
 * Time: 11:42 AM
 */
public interface ContextKeyConstant {

    /**
     * 业务方法返回值，在模板中使用的key
     */
    final static String BUSINESS_METHOD_RETURN_VALUE_KEY = "_methodReturn";

    /**
     * 业务方法的参数
     */
    final static String PRE_OPERATOR_OF_METHOD_KEY = "_param";

    /**
     * 业务方法执行失败的异常信息
     */
    final static String BUSINESS_METHOD_EXECUTE_ERROR = "_executeError";


    /**
     * 业务方法
     */
    final static String BUSINESS_METHOD = "_businessMethod";

    /**
     * 操作人
     */
    final static String BUSINESS_OPERATION_USER = "_user";

    /**
     * 操作时间
     */
    final static String BUSINESS_OPERATION_TIME = "_time";


    /**
     * ip地址
     */
    final static String BUSINESS_OPERATION_IP = "_ip";
}
