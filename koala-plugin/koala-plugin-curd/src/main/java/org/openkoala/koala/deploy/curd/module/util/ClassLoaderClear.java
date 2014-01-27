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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import java.util.jar.JarFile;

/**
 * 类    名：ClassLoaderClear.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-3-27上午11:26:12     
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
public class ClassLoaderClear {
    
    private static ClassLoaderClear classClear = new ClassLoaderClear();

    private ClassLoaderClear(){}
    
    public static ClassLoaderClear getClassLoaderClear() {
        return classClear;
    }

    public void close(ClassLoader cl) {
        setJarFileNames2Close.clear();
        closeClassLoader(cl);
        finalizeNativeLibs(cl);
    }

    protected HashSet<String> setJarFileNames2Close = new HashSet<String>();

    public boolean closeClassLoader(ClassLoader cl) {
        boolean res = false;
        if (cl == null) {
            return res;
        }
        Class classURLClassLoader = URLClassLoader.class;
        Field f = null;
        try {
            f = classURLClassLoader.getDeclaredField("ucp");
        } catch (NoSuchFieldException e1) {
            //ignore
        }
        if (f != null) {
            f.setAccessible(true);
            Object obj = null;
            try {
                obj = f.get(cl);
            } catch (IllegalAccessException e1) {
                //ignore
            }
            if (obj != null) {
                final Object ucp = obj;
                f = null;
                try {
                    f = ucp.getClass().getDeclaredField("loaders");
                } catch (NoSuchFieldException e1) {
                    //ignore
                }
                if (f != null) {
                    f.setAccessible(true);
                    ArrayList loaders = null;
                    try {
                        loaders = (ArrayList) f.get(ucp);
                        res = true;
                    } catch (IllegalAccessException e1) {
                        //ignore
                    }
                    for (int i = 0; loaders != null && i < loaders.size(); i++) {
                        obj = loaders.get(i);
                        f = null;
                        try {
                            f = obj.getClass().getDeclaredField("jar");
                        } catch (NoSuchFieldException e) {
                            //ignore
                        }
                        if (f != null) {
                            f.setAccessible(true);
                            try {
                                obj = f.get(obj);
                            } catch (IllegalAccessException e1) {
                                // ignore
                            }
                            if (obj instanceof JarFile) {
                                final JarFile jarFile = (JarFile) obj;
                                setJarFileNames2Close.add(jarFile.getName());
                                //try {
                                //  jarFile.getManifest().clear();
                                //} catch (IOException e) {
                                //  // ignore
                                //}
                                try {
                                    jarFile.close();
                                } catch (IOException e) {
                                    // ignore
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * finalize native libraries
     * @param cl
     * @return
     */
    @SuppressWarnings({"nls", "unchecked" })
    public boolean finalizeNativeLibs(ClassLoader cl) {
        boolean res = false;
        Class classClassLoader = ClassLoader.class;
        java.lang.reflect.Field nativeLibraries = null;
        try {
            nativeLibraries = classClassLoader.getDeclaredField("nativeLibraries");
        } catch (NoSuchFieldException e1) {
            //ignore
        }
        if (nativeLibraries == null) {
            return res;
        }
        nativeLibraries.setAccessible(true);
        Object obj = null;
        try {
            obj = nativeLibraries.get(cl);
        } catch (IllegalAccessException e1) {
            //ignore
        }
        if (!(obj instanceof Vector)) {
            return res;
        }
        res = true;
        Vector java_lang_ClassLoader_NativeLibrary = (Vector) obj;
        for (Object lib : java_lang_ClassLoader_NativeLibrary) {
            java.lang.reflect.Method finalize = null;
            try {
                finalize = lib.getClass().getDeclaredMethod("finalize", new Class[0]);
            } catch (NoSuchMethodException e) {
                //ignore
            }
            if (finalize != null) {
                finalize.setAccessible(true);
                try {
                    finalize.invoke(lib, new Object[0]);
                } catch (IllegalAccessException e) {} catch (InvocationTargetException e) {
                    //ignore
                }
            }
        }
        return res;
    }
}
