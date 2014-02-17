package org.dayatang.querychannel.service;


import org.dayatang.querychannel.query.ChannelJpqlQuery;
import org.dayatang.querychannel.query.ChannelSqlQuery;
import org.dayatang.querychannel.query.ChannelNamedQuery;
import java.io.Serializable;
import java.util.List;

public interface QueryChannelService extends Serializable {

    /**
     * 创建JPQL查询
     *
     * @param jpql JPQL语句
     * @return 一个JPQL查询
     */
    ChannelJpqlQuery createJpqlQuery(String jpql);

    /**
     * 执行JPQL查询，返回符合条件的实体列表
     *
     * @param jpqlQuery 要执行的JPQL查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果列表
     */
    <T> List<T> list(ChannelJpqlQuery jpqlQuery);

    /**
     * 执行JPQL查询，分页返回符合条件的实体列表
     *
     * @param jpqlQuery 要执行的JPQL查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果页
     */
    <T> Page<T> pagedList(ChannelJpqlQuery jpqlQuery);

    /**
     * 执行JPQL查询，返回符合条件的单个实体
     *
     * @param jpqlQuery 要执行的JPQL查询
     * @param <T> 返回结果类型
     * @return 符合查询条件的单个结果
     */
    <T> T getSingleResult(ChannelJpqlQuery jpqlQuery);

    /**
     * 获取JPQL查询结果的总数。
     *
     * @param jpqlQuery 要执行的JPQL查询。
     * @return 符合查询条件的结果的总数
     */
    long getResultCount(ChannelJpqlQuery jpqlQuery);

    /**
     * 创建命名查询
     *
     * @param queryName 命名查询的名字
     * @return 一个命名查询
     */
    ChannelNamedQuery createNamedQuery(String queryName);

    /**
     * 执行命名查询，返回符合条件的实体列表
     *
     * @param namedQuery 要执行的命名查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果列表
     */
    <T> List<T> list(ChannelNamedQuery namedQuery);

    /**
     * 执行命名查询，分页返回符合条件的实体列表
     *
     * @param namedQuery 要执行的命名查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果页
     */
    <T> Page<T> pagedList(ChannelNamedQuery namedQuery);

    /**
     * 执行命名查询，返回符合条件的单个实体
     *
     * @param namedQuery 要执行的命名查询
     * @param <T> 返回结果类型
     * @return 符合查询条件的单个结果
     */
    <T> T getSingleResult(ChannelNamedQuery namedQuery);

    /**
     * 获得符合命名查询条件的结果的总数。
     *
     * @param namedQuery 要执行的命名查询。
     * @return 符合查询条件的结果的总数
     */
    long getResultCount(ChannelNamedQuery namedQuery);

    /**
     * 创建原生SQL查询
     *
     * @param sql SQL语句
     * @return 一个原生SQL查询
     */
    ChannelSqlQuery createSqlQuery(String sql);

    /**
     * 执行SQL查询，返回符合条件的实体列表
     *
     * @param sqlQuery 要执行的SQL查询。
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果列表
     */
    <T> List<T> list(ChannelSqlQuery sqlQuery);

    /**
     * 执行SQL查询，分页返回符合条件的实体列表
     *
     * @param sqlQuery 要执行的SQL查询。
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果页
     */
    <T> Page<T> pagedList(ChannelSqlQuery sqlQuery);

    /**
     * 执行SQL查询，返回符合条件的单个实体
     *
     * @param sqlQuery 要执行的SQL查询。
     * @param <T> 返回结果类型
     * @return 符合查询条件的单个结果
     */
    <T> T getSingleResult(ChannelSqlQuery sqlQuery);

    /**
     * 获取符合原生SQL查询条件的结果的总数。
     *
     * @param sqlQuery 要执行的原生SQL查询。
     * @return 符合查询条件的结果的总数
     */
    long getResultCount(ChannelSqlQuery sqlQuery);
}
