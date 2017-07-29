package org.dayatang.cache.ehcache;

public class UnExistedCacheNameException extends RuntimeException {
    public UnExistedCacheNameException() {
        super();
    }

    public UnExistedCacheNameException(String message) {
        super(message);
    }

    public UnExistedCacheNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnExistedCacheNameException(Throwable cause) {
        super(cause);
    }
}
