package org.openkoala.koala.config;

import java.util.List;

import org.openkoala.koala.config.vo.DBConfigVO;
import org.openkoala.koala.parse.XML2ObjectUtil;

/**
 * 数据库配置的辅助类,返回默认配置的数据库
 * @author lingen
 *
 */
public class DbConfigUtil {

	private List<DBConfigVO> dBConfigVOs;
	
	private static DbConfigUtil dbconfigUtil;
	
	/**
	 * 返回所有数据库的默认配置
	 * @return
	 * @throws Exception
	 */
	public static List<DBConfigVO> getDBConfig() throws Exception{
		if(dbconfigUtil==null)dbconfigUtil = (DbConfigUtil)XML2ObjectUtil.getInstance().processParse("xml/db/db_config.xml");
		return dbconfigUtil.getDBConfigVOs();
	}


	
	public List<DBConfigVO> getDBConfigVOs() {
		return dBConfigVOs;
	}



	public void setDBConfigVOs(List<DBConfigVO> dBConfigVOs) {
		this.dBConfigVOs = dBConfigVOs;
	}
	
}
