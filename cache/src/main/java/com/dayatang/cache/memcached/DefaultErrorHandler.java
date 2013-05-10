package com.dayatang.cache.memcached;

import com.danga.MemCached.ErrorHandler;
import com.danga.MemCached.MemCachedClient;

public class DefaultErrorHandler implements ErrorHandler {

	@Override
	public void handleErrorOnDelete(MemCachedClient memcachedclient,
			Throwable throwable, String s) {
		throw new RuntimeException(throwable);

	}

	@Override
	public void handleErrorOnFlush(MemCachedClient memcachedclient,
			Throwable throwable) {
		throw new RuntimeException(throwable);

	}

	@Override
	public void handleErrorOnGet(MemCachedClient memcachedclient,
			Throwable throwable, String s) {
		throw new RuntimeException(throwable);
	}

	@Override
	public void handleErrorOnGet(MemCachedClient memcachedclient,
			Throwable throwable, String[] as) {
		throw new RuntimeException(throwable);
	}

	@Override
	public void handleErrorOnInit(MemCachedClient memcachedclient,
			Throwable throwable) {
		throw new RuntimeException(throwable);
	}

	@Override
	public void handleErrorOnSet(MemCachedClient memcachedclient,
			Throwable throwable, String s) {
		throw new RuntimeException(throwable);
	}

	@Override
	public void handleErrorOnStats(MemCachedClient memcachedclient,
			Throwable throwable) {
		throw new RuntimeException(throwable);
	}

}
