package com.dayatang.hibernate;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.util.StringHelper;

/**
 * Hibernate映射命名策略类。将表名和列名设定为大写字母形式，如果表名或列名由多个单词组成，单词之间用下划线隔开。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public class UppercaseAndUnderscoreNamingStrategy implements NamingStrategy {

	/**
	 * A convenient singleton instance
	 */
	public static final NamingStrategy INSTANCE = new UppercaseAndUnderscoreNamingStrategy();

	@Override
	public String classToTableName(String className) {
		return StringHelper.toUpperCase(addUnderscores(StringHelper.unqualify(className)));
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		return StringHelper.toUpperCase(addUnderscores(StringHelper.unqualify(propertyName)));
	}

	@Override
	public String tableName(String tableName) {
		return StringHelper.toUpperCase(addUnderscores(tableName));
	}

	@Override
	public String columnName(String columnName) {
		return StringHelper.toUpperCase(addUnderscores(columnName));
	}

	protected static String addUnderscores(String name) {
		StringBuffer buf = new StringBuffer(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++) {
			if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i))
					&& Character.isLowerCase(buf.charAt(i + 1))) {
				buf.insert(i++, '_');
			}
		}
		return buf.toString().toLowerCase();
	}

	@Override
	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity,
			String associatedEntityTable, String propertyName) {
		return tableName(ownerEntityTable + '_' + propertyToColumnName(propertyName));
	}

	@Override
	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return columnName(joinedColumn);
	}

	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		String header = propertyName != null ? StringHelper.unqualify(propertyName) : propertyTableName;
		if (header == null) {
			throw new AssertionFailure("NamingStrategy not properly filled");
		}
		return columnName(header); 
	}

	@Override
	public String logicalColumnName(String columnName, String propertyName) {
		return StringHelper.toUpperCase(StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName));
	}

	@Override
	public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable,
			String propertyName) {
		if (tableName != null) {
			return StringHelper.toUpperCase(tableName);
		} else {
			// use of a stringbuffer to workaround a JDK bug
			return new StringBuffer(ownerEntityTable)
					.append("_")
					.append(associatedEntityTable != null ? associatedEntityTable : StringHelper
							.unqualify(propertyName)).toString().toUpperCase();
		}
	}

	@Override
	public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
		return StringHelper.toUpperCase(StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName) + "_"
				+ referencedColumn);
	}

}
