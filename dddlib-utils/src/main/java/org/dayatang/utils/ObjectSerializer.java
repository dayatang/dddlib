package org.dayatang.utils;

/**
 * 对象序列化器，将Java对象序列化为字符串形式，或者相反，从字符串反序列化为对象。
 * Created by yyang on 14-9-16.
 */
public interface ObjectSerializer {

    /**
     * 将对象序列化为字符串
     * @param anObject 要序列化的对象
     * @return 对象的序列化形式
     */
    String serialize(Object anObject);

    /**
     * 将字符串反序列化为对象
     * @param serializedString 对象的字符串序列化形式
     * @param objectClass 对象的类
     * @param <T> 对象的类型
     * @return 一个对象实例
     */
    <T> T deserialize(String serializedString, Class<T> objectClass);
}
