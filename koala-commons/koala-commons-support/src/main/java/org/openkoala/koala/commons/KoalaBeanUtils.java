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
package org.openkoala.koala.commons;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class KoalaBeanUtils {
    
    
    /**
     * <b>实体值复制</b> <br>
     * @author vakin jiang <br />
           mailto:<a href="mailto:chiang.www@gmail.com">chiang.www@gmail.com</a>
     * @version 1.0
                at 2010-11-15
     */
    public static void copyProperties(Object from, Object to,boolean ignoreNull) {
        try {
            if(from == null || to == null) {
            	return;
            }
            BeanInfo toBI = Introspector.getBeanInfo(to.getClass());
            PropertyDescriptor[] toPD = toBI.getPropertyDescriptors();

            BeanInfo fromBI = Introspector.getBeanInfo(from.getClass());
            PropertyDescriptor[] fromPD = fromBI.getPropertyDescriptors();

            HashMap<String, PropertyDescriptor> fromMap = new HashMap<String, PropertyDescriptor>();
            for (PropertyDescriptor pd : fromPD) {
                fromMap.put(pd.getName(), pd);
            }

            for (PropertyDescriptor toP : toPD) {

                Method setMethod = toP.getWriteMethod();

                PropertyDescriptor formP = fromMap.get(toP.getName());
                if (formP == null)// 如果from没有此属性的get方法，跳过
                    continue;

                Method getMethod = fromMap.get(toP.getName()).getReadMethod();

                if (getMethod != null && setMethod != null) {
                    Object result = getMethod.invoke(from);
                    //
                    if (ignoreNull && (result == null || "".equals(result.toString())))continue;
                    
                    try {
                    	//属性名同，属性类型不同的情况
                        Class<?> valParameterTypes = result.getClass();
                        Class<?> targetParameterTypes = setMethod.getParameterTypes()[0];
                        if(targetParameterTypes == java.util.Date.class && valParameterTypes == String.class){
                        	result = KoalaDateUtils.parseDate(result.toString());
                        }else if(targetParameterTypes == String.class && valParameterTypes == java.util.Date.class){
                        	result = KoalaDateUtils.format((java.util.Date)result);
                        }
                        //
                    	setMethod.invoke(to, result);
                    } catch (Exception e) {}
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T getNewInstance(Object from, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            copyProperties(from, instance,true);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> List<T> getNewList(Collection<?> src, Class<T> clazz) {
        try {
            List<T> result = new ArrayList<T>();
            for (Object object : src) {
                result.add(getNewInstance(object, clazz));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
