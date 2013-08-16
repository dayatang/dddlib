package org.jwebap.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SQL语法高亮显示器
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date 2009-2-21
 */
public class SQLConvertor {

	// 关键字
	public static final String[] SQL_KEY_WORDS = { "select", "from", "update",
			"delete", "insert", "into", "where", "group", "by", "having",
			"values", "and", "or", "(", ")", "as", "like", "not", "in",
			"exists", "order", "asc", "desc", "on", "join", "outer", "set",
			"create", "table", "alter", "drop", "index" };

	// 关键字替换前缀
	private String keywordLightPrefix = "<font color='#000080'><b>";

	// 关键字替换后缀
	private String keywordLightSuffix = "</b></font>";

	// 表名替换前缀
	private String tablesLightPrefix = "<font color='#3F7F5F'><b>";

	// 表名替换后缀
	private String tablesLightSuffix = "</b></font>";

	/**
	 * 分析出表名列表
	 * 
	 * @param sql
	 * @return
	 */
	public Collection analyseSqlTables(String sql) {
		// 词法分析
		String[] words = splitWord(sql, null);

		Collection tables = new ArrayList();

		// 语法分析
		tableAnalyse(new Counter(), words, tables);

		return tables;
	}

	/**
	 * 高亮表名
	 * 
	 * @param sql
	 * @return
	 */
	public String highlightTables(String sql) {
		Collection tables = analyseSqlTables(sql);

		return null;
	}

	/**
	 * 高亮关键字
	 * 
	 * @param sql
	 * @return
	 */
	public String highlightKeywords(String sql) {
		String lightSql = this.replaceSql(sql, null, true);
		return lightSql;
	}

	/**
	 * 高亮sql,包括关键字和表名
	 * 
	 * @param sql
	 * @return
	 */
	public String highlightSql(String sql) {
		Collection tables = this.analyseSqlTables(sql);

		String lightSql = this.replaceSql(sql, tables, true);
		return lightSql;
	}

	/**
	 * 完成sql替换,包括
	 * 
	 * @return
	 */
	private String replaceSql(String sql, final Collection tables,
			final boolean replaceKeyword) {
		// 替换必须保证在一次词法扫描完成，否则，替换的内容会互相影响
		// @ TODO 这里splitWord还不能做到真正的一次扫描，只是模拟一次词法分析

		final StringBuffer replacement = new StringBuffer(" ");

		// 词法分析处理
		LexicalAnalysisProcessor processor = new LexicalAnalysisProcessor() {

			public void process(String word) {
				int placeType = 0;

				if (replaceKeyword) {
					for (int i = 0; i < SQL_KEY_WORDS.length; i++) {
						if (SQL_KEY_WORDS[i].equals(word.toLowerCase())) {
							placeType = 1;
							break;// 表名的优先级大于关键字，所以这里不return
						}
					}
				}

				if (tables != null && tables.contains(word)) {
					placeType = 2;
				}

				switch (placeType) {
				case 0://普通词汇
					replacement.append(word+" ");
					break;
				case 1://关键字
					replacement.append(keywordLightPrefix + word
							+ keywordLightSuffix + " ");
					break;
				case 2://表名
					replacement.append(tablesLightPrefix + word
							+ tablesLightSuffix + " ");
					break;
				default:
				}
			}

		};

		splitWord(sql, processor);

		return replacement.toString();
	}

	/**
	 * 分词，在做SQL分析之前，先进行词法分析，找出SQL里面有意义的词汇
	 * 
	 * @author leadyu
	 * @param sql
	 * @return
	 */
	private String[] splitWord(String sql, LexicalAnalysisProcessor processor) {

		List words = new ArrayList();

		String[] spaceSplit = StringUtil.split(sql," ");

		for (int i = 0; i < spaceSplit.length; i++) {
			String word = spaceSplit[i];
			word = StringUtil.replaceAll(word,",", "#,#");
			word = StringUtil.replaceAll(word,"\\(", "#(#");
			word = StringUtil.replaceAll(word,"\\)", "#)#");

			String[] ws = StringUtil.split(word,"#");
			for (int c = 0; c < ws.length; c++) {
				if (ws[c] != null && !"".equals(ws[c].trim())) {
					words.add(ws[c]);
					if (processor != null)
						processor.process(ws[c]);
				}
			}

		}

		String[] result = new String[words.size()];
		words.toArray(result);
		return result;
	}

	private static class Counter {
		int num = 0;
	}

	/**
	 * 对表名的语法分析
	 * 
	 * @param words
	 * @return
	 */
	private void tableAnalyse(Counter i, String[] words, Collection tables) {

		for (; i.num < words.length; i.num++) {
			// 如果遇到'('跳入下一个子句语法分析
			if ("(".equals(words[i.num].trim().toLowerCase())) {
				i.num++;
				tableAnalyse(i, words, tables);
				continue;
			}
			// 如果遇到')'退出当前子句语法分析
			if (")".equals(words[i.num].trim().toLowerCase())) {
				return;
			}

			// 如果遇到from
			if ("from".equals(words[i.num].trim().toLowerCase())) {
				// System.out.print("");
				for (i.num++; i.num < words.length; i.num++) {
					String fromNext = words[i.num];

					// 如果遇到'('跳入下一个子句语法分析
					if ("(".equals(fromNext.trim().toLowerCase())) {
						i.num++;
						tableAnalyse(i, words, tables);
						continue;
					}
					// 如果遇到')'退出当前子句语法分析
					if (")".equals(fromNext.trim().toLowerCase())) {
						return;
					}
					// 如果遇到'where,into'from表达式结束
					if ("where".equals(fromNext.trim().toLowerCase())
							|| "into".equals(fromNext.trim().toLowerCase())) {
						break;
					}
					// 如果是,则前面的就是表名
					if (",".equals(fromNext.trim())) {
						// 什么也不做
					} else {
						// 如果单词前面是from或者,以及join说明是表名
						if ("from".equals(words[i.num - 1])
								|| ",".equals(words[i.num - 1])
								|| "join".equals(words[i.num - 1])) {
							tables.add(words[i.num]);
							// System.out.print("");
						}
					}
				}
			}

			if (i.num >= words.length)
				break;

			// 如果遇到update
			if ("update".equals(words[i.num].trim().toLowerCase())) {
				i.num++;
				if (i.num >= words.length)
					break;
				tables.add(words[i.num]);

			}

			if (i.num >= words.length)
				break;

			// 如果遇到into
			if ("into".equals(words[i.num].trim().toLowerCase())) {
				i.num++;
				if (i.num >= words.length)
					break;
				tables.add(words[i.num]);

			}

		}

	}

	public String getKeywordLightPrefix() {
		return keywordLightPrefix;
	}

	public void setKeywordLightPrefix(String keywordLightPrefix) {
		this.keywordLightPrefix = keywordLightPrefix;
	}

	public String getKeywordLightSuffix() {
		return keywordLightSuffix;
	}

	public void setKeywordLightSuffix(String keywordLightSuffix) {
		this.keywordLightSuffix = keywordLightSuffix;
	}

	public String getTablesLightPrefix() {
		return tablesLightPrefix;
	}

	public void setTablesLightPrefix(String tablesLightPrefix) {
		this.tablesLightPrefix = tablesLightPrefix;
	}

	public String getTablesLightSuffix() {
		return tablesLightSuffix;
	}

	public void setTablesLightSuffix(String tablesLightSuffix) {
		this.tablesLightSuffix = tablesLightSuffix;
	}
}

/**
 * sql词法分析处理器，以便在一次词法扫描过程，嵌入额外的动作
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.6
 * @date 2009-2-21
 */
abstract class LexicalAnalysisProcessor {
	/**
	 * 
	 * @param word
	 *            词汇
	 */
	public abstract void process(String word);

}
