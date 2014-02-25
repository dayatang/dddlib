package org.dayatang.querychannel;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一个辅助类，处理查询语句，根据原始查询语句生成计算查询结果总数的查询语句
 * 因为客户代码不会直接使用该类，所以设置为包级可见性。
 * Created by yyang on 14-2-25.
 */
class CountQueryStringBuilder {
    private final String queryString;

    public CountQueryStringBuilder(String queryString) {
        this.queryString = queryString;
    }

    /**
     * 构造一个查询数据条数的语句,不能用于union
     * @return 查询数据条数的语句
     */
    public String buildQueryStringOfCount() {
        String result = removeOrderByClause();

        int index = StringUtils.indexOfIgnoreCase(result, " from ");

        StringBuilder builder = new StringBuilder("select count(" + stringInCount(result, index) + ") ");

        if (index != -1) {
            builder.append(result.substring(index));
        } else {
            builder.append(result);
        }
        return builder.toString();
    }

    /**
     * 去除查询语句的orderby 子句
     *
     * @return
     */
    public String removeOrderByClause() {
        Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE).matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String stringInCount(String queryString, int fromIndex) {
        int distinctIndex = getPositionOfDistinct(queryString);
        if (distinctIndex == -1) {
            return "*";
        }
        String distinctToFrom = queryString.substring(distinctIndex, fromIndex);

        // 除去“,”之后的语句
        int commaIndex = distinctToFrom.indexOf(",");
        String strMayBeWithAs = commaIndex == -1 ? distinctToFrom : distinctToFrom.substring(0, commaIndex);

        // 除去as语句
        int asIndex = StringUtils.indexOfIgnoreCase(strMayBeWithAs, " as ");
        String strInCount = asIndex == -1 ? strMayBeWithAs : strMayBeWithAs.substring(0, asIndex);

        // 除去()，因为HQL不支持 select count(distinct (...))，但支持select count(distinct ...)
        return strInCount.replace("(", " ").replace(")", " ");
    }

    private static int getPositionOfDistinct(String queryString) {
        int result = StringUtils.indexOfIgnoreCase(queryString, "distinct(");
        return result == -1 ? StringUtils.indexOfIgnoreCase(queryString, "distinct (") : result;
    }

    /**
     * 判断查询语句中是否包含group by子句。
     * @return 如果查询语句中包含group by子句，返回true，否则返回false
     */
    public boolean containsGroupByClause() {
        Matcher m = Pattern.compile("group\\s*by[\\w|\\W|\\s|\\S]*",
                Pattern.CASE_INSENSITIVE).matcher(queryString);
        return m.find();
    }
}
