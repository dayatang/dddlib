package org.dayatang.security.domain;

import org.dayatang.domain.BaseEntity;
import org.dayatang.domain.CriteriaQuery;
import org.dayatang.domain.NamedParameters;
import org.dayatang.utils.DateUtils;

import javax.persistence.*;
import java.util.*;

/**
 * 所有实体的基类，定义实体的公共属性和行为。
 * Created by yyang on 15/1/6.
 */
@MappedSuperclass
public abstract class AbstractEntity extends BaseEntity {

    public static final String ID_FIELD = "id";

    public static final String VERSION_FIELD = "version";

    @Id
    private String id = UUID.randomUUID().toString();

    @Version
    private int version;

    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    //最后修改时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    //失效时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date expired;

    //是否已经失效
    private boolean disabled;

    /**
     * 构造新的实体对象，设置创建时间和最后修改时间为当前时间，失效状态为false
     */
    public AbstractEntity() {
        this(new Date());
    }

    public AbstractEntity(Date created) {
        this.created = new Date(created.getTime());
        lastModified = new Date(created.getTime());
        disabled = false;
        expired = DateUtils.MAX_DATE;
    }

    /**
     * 获得实体的标识
     *
     * @return 实体的标识
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 设置实体的标识
     *
     * @param id 要设置的实体标识
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获得实体的版本号。持久化框架以此实现乐观锁。
     *
     * @return 实体的版本号
     */
    public int getVersion() {
        return version;
    }

    /**
     * 设置实体的版本号。持久化框架以此实现乐观锁。
     *
     * @param version 要设置的版本号
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * 获取实体创建时间
     * @return 实体的创建时间
     */
    public Date getCreated() {
        return new Date(created.getTime());
    }

    /**
     * 设置实体创建时间
     * @param created 要设置的创建时间
     */
    public void setCreated(Date created) {
        this.created = new Date(created.getTime());
    }

    /**
     * 获取实体最后修改的时间
     * @return 实体最后修改时间
     */
    public Date getLastModified() {
        return new Date(lastModified.getTime());
    }

    /**
     * 设置实体最后修改时间
     * @param lastModified 要设置的最后修改时间
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = new Date(lastModified.getTime());
    }

    public Date getExpired() {
        return expired;
    }

    /**
     * 判断实体是否已经失效
     * @return 如果实体已经失效则返回true，否则返回false
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * 使实体失效，对系统来说，等价于实体已经在逻辑上被删除
     */
    public void disable(Date date) {
        disabled = true;
        expired = date;
        save();
    }

    /**
     * 使实体重新生效，等价于恢复实体的有效状态
     */
    public void enable() {
        disabled = false;
        expired = DateUtils.MAX_DATE;
        save();
    }

    /**
     * 将实体本身持久化到数据库
     */
    public void save() {
        lastModified = new Date();
        getRepository().save(this);
    }

    /**
     * 将实体本身从数据库中删除
     */
    public void remove() {
        getRepository().remove(this);
    }

    /**
     * 根据ID获取指定类型的实体，所有的属性已填充好
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends AbstractEntity> T get(Class<T> entityClass, String id) {
        return getRepository().get(entityClass, id);
    }

    /**
     * 根据ID获取指定类型的实体，除ID外，其余属性均未填充，实际上没访问数据库。此方法主要为性能考虑
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends AbstractEntity> T load(Class<T> entityClass, String id) {
        return getRepository().load(entityClass, id);
    }

    /**
     * 查找指定类型的所有实体
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @return 符合条件的实体列表
     */
    public static <T extends AbstractEntity> List<T> findAll(Class<T> clazz) {
        return createCriteriaQuery(clazz).list();
    }

    /**
     * 根据单个属性值以“属性=属性值”的方式查找符合条件的实体集合
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @param propName 属性名
     * @param value 匹配的属性值
     * @return 符合条件的实体列表
     */
    public static <T extends AbstractEntity> List<T> findByProperty(Class<T> clazz, String propName, Object value) {
        Map<String, Object> propValues = new HashMap<String, Object>();
        //propValues.put("disabled", false);
        propValues.put(propName, value);
        return findByProperties(clazz, propValues);
    }

    /**
     * 根据多个属性值以“属性=属性值”的方式查找符合条件的实体集合，例如查找name="张三", age=35的员工。
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @param propValues 属性值匹配条件
     * @return 符合条件的实体列表
     */
    public static <T extends AbstractEntity> List<T> findByProperties(Class<T> clazz, Map<String, Object> propValues) {
        Map<String, Object> newProps = new HashMap<String, Object>();
        //newProps.put("disabled", false);
        newProps.putAll(propValues);
        return getRepository().findByProperties(clazz, NamedParameters.create(newProps));
    }

    /**
     * 根据单个属性值以“属性=属性值”的方式查找符合条件的单个实体，通常用于根据业务主键找到唯一实体
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @param propName 属性名
     * @param value 匹配的属性值
     * @return 符合条件的实体列表
     */
    public static <T extends AbstractEntity> T getByProperty(Class<T> clazz, String propName, Object value) {
        List<T> entities = findByProperty(clazz, propName, value);
        return entities == null || entities.isEmpty() ? null : entities.get(0);
    }

    /**
     * 根据多个属性值以“属性=属性值”的方式查找符合条件的单个实体，通常用于根据业务主键找到唯一实体，例如查找name="张三", age=35的员工。
     * @param <T> 实体所属的类型
     * @param clazz 实体所属的类
     * @param propValues 属性值匹配条件
     * @return 符合条件的实体列表
     */
    public static <T extends AbstractEntity> T getByProperties(Class<T> clazz, Map<String, Object> propValues) {
        List<T> entities = findByProperties(clazz, propValues);
        return entities == null || entities.isEmpty() ? null : entities.get(0);
    }

    /**
     * 生成针对某个实体类的条件查询，缺省加入“disabled = false”的条件，以过滤掉已经逻辑删除的实体
     * @param entityClass 实体类
     * @param <T> 实体的类型
     * @return 针对某种实体类的条件查询
     */
    public static <T extends AbstractEntity> CriteriaQuery createCriteriaQuery(Class<T> entityClass) {
        return getRepository().createCriteriaQuery(entityClass).isFalse("disabled");
    }
}
