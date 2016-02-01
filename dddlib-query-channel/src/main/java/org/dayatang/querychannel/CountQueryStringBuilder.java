//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dayatang.querychannel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

class CountQueryStringBuilder {
    private final String queryString;

    public CountQueryStringBuilder(String queryString) {
        this.queryString = queryString;
    }

    public String buildQueryStringOfCount() {
        String result = this.removeOrderByClause();
        int index = StringUtils.indexOfIgnoreCase(result, " from ");
        StringBuilder builder = new StringBuilder("select count(" + stringInCount(result, index) + ") ");
        if(index != -1) {
            builder.append(result.substring(index));
        } else {
            builder.append(result);
        }

        return builder.toString();
    }

    public String removeOrderByClause() {
        Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2).matcher(this.queryString);
        StringBuffer sb = new StringBuffer();

        while(m.find()) {
            m.appendReplacement(sb, "");
        }

        m.appendTail(sb);
        return sb.toString();
    }

    private static String getStringIncountWithoutDistinct(String queryString){
        String substringBetween = StringUtils.substringBetween(queryString.toLowerCase(), "select".toLowerCase(), "FROM".toLowerCase());
        return substringBetween;
    }

    private static String stringInCount(String queryString, int fromIndex) {
        int distinctIndex = getPositionOfDistinct(queryString);
        if(distinctIndex == -1) {
            return getStringIncountWithoutDistinct(queryString);
        } else {
            String distinctToFrom = queryString.toLowerCase().substring(distinctIndex, fromIndex);
            int commaIndex = distinctToFrom.indexOf(",");
            String strMayBeWithAs = commaIndex == -1?distinctToFrom:distinctToFrom.substring(0, commaIndex);
            int asIndex = StringUtils.indexOfIgnoreCase(strMayBeWithAs, " as ");
            String strInCount = asIndex == -1?strMayBeWithAs:strMayBeWithAs.substring(0, asIndex);
            return strInCount.replace("(", " ").replace(")", " ");
        }
    }


    private static int getPositionOfDistinct(String queryString) {
        return StringUtils.indexOfIgnoreCase(queryString, "distinct");
    }

    public boolean containsGroupByClause() {
        Matcher m = Pattern.compile("group\\s*by[\\w|\\W|\\s|\\S]*", 2).matcher(this.queryString);
        return m.find();
    }
}
