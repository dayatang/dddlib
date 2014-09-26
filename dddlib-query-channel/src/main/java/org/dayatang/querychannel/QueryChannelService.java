package org.dayatang.querychannel;


import org.dayatang.utils.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 查询通道接口。可以针对仓储进行分页查询。
 */
public interface QueryChannelService extends Serializable {

    /**
     * 创建JPQL查询
     *
     * @param jpql JPQL语句
     * @return 一个JPQL查询
     */
    ChannelQuery createJpqlQuery(String jpql);

    /**
     * 创建命名查询
     *
     * @param queryName 命名查询的名字
     * @return 一个命名查询
     */
    ChannelQuery createNamedQuery(String queryName);

    /**
     * 创建原生SQL查询
     *
     * @param sql SQL语句
     * @return 一个原生SQL查询
     */
    ChannelQuery createSqlQuery(String sql);

    /**
     * 执行查询，返回符合条件的结果列表
     *
     * @param query 要执行的查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果列表
     */
    <T> List<T> list(ChannelQuery query);

    /**
     * 执行查询，分页返回符合条件的结果列表
     *
     * @param query 要执行的查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果页
     */
    <T> Page<T> pagedList(ChannelQuery query);

    /**
     * 执行查询，返回符合条件的单个实体
     *
     * @param query 要执行的查询
     * @param <T> 返回结果类型
     * @return 符合查询条件的单个结果
     */
    <T> T getSingleResult(ChannelQuery query);

    /**
     * 获取查询结果的总数。
     *
     * @param query 要执行的查询
     * @return 符合查询条件的结果的总数
     */
    long getResultCount(ChannelQuery query);

}
