package org.openkoala.framework.cache;

import java.util.Date;
import java.util.Map;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


public class EhCacheImpl implements com.dayatang.cache.Cache  {

	private net.sf.ehcache.Cache cache;
	
	private String name;

	public Object get(String key) {
		Element el = (Element)cache.get(key);
		return el.getObjectValue();
	}

	public Map<String, Object> get(String... arg0) {
		return null;
	}
	

	public boolean isKeyInCache(String key) {
		return cache.isKeyInCache(key);
	}

	public void put(String key, Object obj) {
		cache.put( new Element(key, obj) );
	}

	public void put(String arg0, Object arg1, Date arg2) {
		
	}

	public void put(String arg0, Object arg1, long arg2) {
		
	}

	public boolean remove(String key) {
		return cache.remove(key);
	}
	
	public void removeCache()
	{
		if (CacheManager.getInstance().cacheExists(name)) {
			CacheManager.getInstance().removeCache(name);
		}
	}
	
	
	public EhCacheImpl(String name){
		cache = CacheManager.getInstance().getCache(name);
	}
	
	public EhCacheImpl(String name, int maxElementsInMemory, boolean overflowToDisk, boolean eternal, long timeToLiveSeconds, long timeToIdleSeconds)
	{
		if (!CacheManager.getInstance().cacheExists(name)) {
			this.name = name;
			cache = new Cache(name, maxElementsInMemory, overflowToDisk, eternal,
					timeToLiveSeconds, timeToIdleSeconds);
			CacheManager.getInstance().addCache(cache);
		}
	}
}
