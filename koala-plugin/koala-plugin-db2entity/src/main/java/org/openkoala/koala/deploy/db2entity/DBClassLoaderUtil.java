package org.openkoala.koala.deploy.db2entity;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * DB数据库驱动的加载辅助类
 * @author lingen
 *
 */
public class DBClassLoaderUtil {
	
    private static URLClassLoader classloader;
    
	private DBClassLoaderUtil(String jarPath){
		List<URL> urlList = new ArrayList<URL>();
		try {
			urlList.add(new URL("file:" + jarPath));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		URL[] urls = new URL[] {};
		classloader = new URLClassLoader(urlList.toArray(urls));
	}
	
	public static DBClassLoaderUtil getCURDClassLoader(String jarPath) {
		DBClassLoaderUtil classLoader = new DBClassLoaderUtil(jarPath);
        return classLoader;
    }
	
	public Class forName(String name) throws ClassNotFoundException{
		 return classloader.loadClass(name);
	}
	
}
