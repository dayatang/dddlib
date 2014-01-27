package org.openkoala.koala.deploy.db2entity;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.db2entity.vo.DBColumnVO;
import org.openkoala.koala.deploy.db2entity.vo.PrimaryKeyColumnVO;
import org.openkoala.koala.util.StringUtils;

/**
 * 从表格生成实体
 * @author lingen
 *
 */
public class EntityGenerateUtil {

	/**
	 * 生成一个实体
	 * @param path 项目路径
	 * @param tableName  表格名称
	 * @param packageName 包名称
	 * @param columns 表格的所拥有的列
	 * @throws IOException 
	 */
	public static void process(String path,String tableName,String packageName,List<DBColumnVO> columns) throws IOException{
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("package", packageName);
		params.put("tableName", tableName);
		String entityName = StringUtils.generateJavaClass(tableName);
		params.put("EntityName", entityName);
		List<DBColumnVO> commons = getNoPrimaryKey(columns);
		params.put("Columns",commons);
		List<DBColumnVO> primarys = getPrimaryKey(columns);
		params.put("PrimayKeys", primarys);
		
		if(primarys.size()==1 && isAbstractEntity(primarys,commons)){
			//生成一个AbstractEntity的实体
			//如果是AbstractEntity，要去除ID以及Version
			params.put("Columns",removeIdAndVersionForColumns(commons));
			String generatePath = path.replaceAll("\\\\", "/")+"/src/main/java/"+ packageName.replaceAll("\\.", "/")+"/"+entityName+".java";
			VelocityUtil.vmToFile(VelocityUtil.getVelocityContext(params), "vm/AbstractEntity.vm", generatePath);
		}else{
			if(primarys.size()>1){
				
				String generatePath = path.replaceAll("\\\\", "/")+"/src/main/java/"+ packageName.replaceAll("\\.", "/")+"/"+entityName+"PK.java";
				VelocityUtil.vmToFile(VelocityUtil.getVelocityContext(params), "vm/EntityPK.vm", generatePath);
				
				generatePath = path.replaceAll("\\\\", "/")+"/src/main/java/"+ packageName.replaceAll("\\.", "/")+"/"+entityName+".java";
				VelocityUtil.vmToFile(VelocityUtil.getVelocityContext(params), "vm/BaseLegacyEntityWithPK.vm", generatePath);
				
			}else{
				
				String generatePath = path.replaceAll("\\\\", "/")+"/src/main/java/"+ packageName.replaceAll("\\.", "/")+"/"+entityName+".java";
				VelocityUtil.vmToFile(VelocityUtil.getVelocityContext(params), "vm/BaseLegacyEntity.vm", generatePath);
			}
		}
		
	}
	
	public static void main(String args[]){
		String value = "org.forever.oss.koala";
		System.out.println(value.replaceAll("\\.", "/"));
	}
	
	private static List<DBColumnVO> getNoPrimaryKey(List<DBColumnVO> columns){
		List<DBColumnVO> cos = new ArrayList<DBColumnVO>();
		  for(DBColumnVO column:columns){
			  if(column instanceof PrimaryKeyColumnVO == false){
				  cos.add(column);
			  }
		  }
		return cos;
	}
	
	private static boolean isAbstractEntity(List<DBColumnVO> primary,List<DBColumnVO> columns){
		boolean isAbstractEntity = true;
		
		PrimaryKeyColumnVO id = (PrimaryKeyColumnVO)primary.get(0);
		
		if(!id.getColumnName().toUpperCase().equals("ID") && !id.isAutoIncrement()){
			isAbstractEntity = false;
		}
		
		//查询Version字段
		DBColumnVO version = null;
		  for(DBColumnVO column:columns){
			  if(column.getColumnName().toUpperCase().equals("VERSION")){
				  version = column;
			  }
		  }
		if(version==null || version.getDataType()!=Types.INTEGER){
			isAbstractEntity = false;
		}
		return isAbstractEntity;
	}
	
	private static List<DBColumnVO> getPrimaryKey(List<DBColumnVO> columns){
		List<DBColumnVO> cos = new ArrayList<DBColumnVO>();
		  for(DBColumnVO column:columns){
			  if(column instanceof PrimaryKeyColumnVO){
				  cos.add(column);
			  }
		  }
		return cos;
	}
	
	/**
	 * AbstractEntity中返回非ID以及非Version的字段
	 * @param commons
	 * @return
	 */
	private static List<DBColumnVO> removeIdAndVersionForColumns(List<DBColumnVO> commons){
		List<DBColumnVO> news = new ArrayList<DBColumnVO>();
		 for(DBColumnVO common:commons){
			 if(!common.getColumnName().toUpperCase().equals("ID") && !common.getColumnName().toUpperCase().equals("VERSION")){
				 news.add(common);
			 }
		 }
		return news;
	}
}
