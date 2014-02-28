package org.dayatang.cache.ehcache;

/**
 * 、
 *
 * @author lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 * @Description: 表明指定name在缓存配置文件中不存在
 * @date 2013-8-16 下午10:54:28
 */
public class UnExistsedCacheNameException extends RuntimeException {
    public UnExistsedCacheNameException() {
        super();
    }

    public UnExistsedCacheNameException(String message) {
        super(message);
    }

    public UnExistsedCacheNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnExistsedCacheNameException(Throwable cause) {
        super(cause);
    }
}
