package org.dayatang.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 抽象遗留实体类。适用于那些数据库先于程序存在的对象。它的ID的类型是未知的，而且可能没有version属性。
 *
 * @author yang
 *
 */
@MappedSuperclass
public abstract class LegacyEntity extends BaseEntity {

    private static final long serialVersionUID = 8882145540383345037L;

    /**
     * 将实体本身持久化到数据库
     */
    public void save() {
        getRepository().save(this);
    }

    /**
     * 将实体本身从数据库中删除
     */
    public void remove() {
        getRepository().remove(this);
    }

    /**
     * 根据实体类型和ID从仓储中获取实体
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @param id 实体的ID
     * @return 类型为T或T的子类型，ID为id的实体。
     */
    public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
        return getRepository().get(clazz, id);
    }

    /**
     * 查找实体在数据库中的未修改版本
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @param entity  实体
     * @return 实体的未修改版本。
     */
    public static <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
        return getRepository().getUnmodified(clazz, entity);
    }

    /**
     * 根据实体类型和ID从仓储中加载实体(与get()方法的区别在于除id外所有的属性值都未填充)
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @param id 实体的ID
     * @return 类型为T或T的子类型，ID为id的实体。
     */
    public static <T extends Entity> T load(Class<T> clazz, Serializable id) {
        return getRepository().load(clazz, id);
    }

    /**
     * 查找指定类型的所有实体
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @return 符合条件的实体列表
     */
    public static <T extends Entity> List<T> findAll(Class<T> clazz) {
        return getRepository().createCriteriaQuery(clazz).list();
    }

    /**
     * 根据单个属性值以“属性=属性值”的方式查找实体
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @param propName 属性名
     * @param value 匹配的属性值
     * @return 符合条件的实体列表
     */
    public static <T extends Entity> List<T> findByProperty(Class<T> clazz, String propName, Object value) {
        return getRepository().findByProperty(clazz, propName, value);
    }

    /**
     * 根据多个属性值以“属性=属性值”的方式查找实体，例如查找name="张三", age=35的员工。
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @param propValues 属性值匹配条件
     * @return 符合条件的实体列表
     */
    public static <T extends Entity> List<T> findByProperties(Class<T> clazz, Map<String, Object> propValues) {
        return getRepository().findByProperties(clazz, MapParameters.create(propValues));
    }
}
