package org.openkoala.gqc.controller.generalquery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openkoala.gqc.application.DataSourceApplication;
import org.openkoala.gqc.application.GqcApplication;
import org.openkoala.gqc.core.domain.DynamicQueryCondition;
import org.openkoala.gqc.core.domain.FieldDetail;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.PreQueryCondition;
import org.openkoala.gqc.vo.DataSourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/generalquery")
public class GeneralQueryController {

    @Autowired
    private DataSourceApplication dataSourceApplication;
    
    @Autowired
    private GqcApplication gqcApplication;
    
    //Json对象
    private Map<String, Object> dataMap;
	
	/**
	 * 分页查询通用查询列表
	 * @param page
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/pageJson")
	public Map<String,Object> pageJson(int page, int pagesize, String queryName) {
        this.initMap();
        
        Page<GeneralQuery> all = null;
        if (queryName != null && !queryName.isEmpty()) {
        	all = gqcApplication.pagingQueryGeneralQueriesByQueryName(queryName, page, pagesize);
        } else {
        	all = gqcApplication.pagingQueryGeneralQueries(page, pagesize);
        }
        
        List<GeneralQueryVo> generalQueryVos = new ArrayList<GeneralQueryController.GeneralQueryVo>();
        for (GeneralQuery generalQuery : all.getResult()) {
        	generalQueryVos.add(new GeneralQueryVo(generalQuery.getId(), generalQuery.getDataSource().getDataSourceId(), generalQuery.getQueryName(), 
        			generalQuery.getTableName(), generalQuery.getDescription(), generalQuery.getCreateDate()));
        }
        
        dataMap.put("Rows", generalQueryVos);
        dataMap.put("start", page * pagesize - pagesize);
        dataMap.put("limit", pagesize);
        dataMap.put("Total", all.getTotalCount());
		return dataMap;
	}
    
    @ResponseBody
    @RequestMapping("/add")
    public Map<String,Object> add(GeneralQuery generalQuery) {
        try {
            this.initMap();
            generalQuery.setCreateDate(new Date());
            gqcApplication.saveEntity(generalQuery);
            dataMap.put("result", "success");
        } catch (Exception e) {
            dataMap.put("result", "保存失败！");
        }
        
        return dataMap;
    }
    
    @RequestMapping("/getById")
    public String getById(HttpServletRequest request, Long id) {
        try {
            this.initMap();
            //查询出该实体
            GeneralQuery generalQuery = gqcApplication.getById(id);
//            GeneralQueryVo generalQueryVo = new GeneralQueryVo(generalQuery.getId(), generalQuery.getDataSource().getDataSourceId(), 
//                    generalQuery.getQueryName(), generalQuery.getTableName(), generalQuery.getDescription(), generalQuery.getCreateDate());
//            generalQueryVo.setFieldDetails(generalQuery.getFieldDetails());
//            generalQueryVo.setPreQueryConditions(generalQuery.getPreQueryConditions());
//            generalQueryVo.setDynamicQueryConditions(generalQuery.getDynamicQueryConditions());

            //表中所有列，供查询条件选择
            Map<String, Integer> tableMapLeftDiv = dataSourceApplication.findAllColumn(
                    generalQuery.getDataSource().getId(), generalQuery.getTableName());
            
            //表中所有列，供显示列选择
            Map<String, Integer> tableMapRightDiv = getCloneMap(tableMapLeftDiv);
            
            //把已被选择了的列从列池中去除
            this.removeTableMapLeftDiv(generalQuery, tableMapLeftDiv);
            this.removeTableMapRightDiv(generalQuery, tableMapRightDiv);

            request.setAttribute("data", generalQuery);
            request.setAttribute("tableMapLeftDiv", tableMapLeftDiv);
            request.setAttribute("tableMapRightDiv", tableMapRightDiv);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取信息失败!");
        }
        
        return "generalquery/Generalquery-update";
    }
    
    @ResponseBody
    @RequestMapping("/update")
    public Map<String,Object> update(GeneralQuery generalQuery) {
        try {
            this.initMap();
            generalQuery.setCreateDate(new Date());
            gqcApplication.updateEntity(generalQuery);
            dataMap.put("result", "success");
        } catch (Exception e) {
            dataMap.put("result", "保存失败！");
        }
        
        return dataMap;
    }
    
    @ResponseBody
    @RequestMapping("/findAllDataSource")
    public Map<String,Object> findAllDataSource() {
        try {
            this.initMap();
            
            List<DataSourceVO> list = dataSourceApplication.findAllDataSource();
            dataMap.put("dataSourceList", list);
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "获取数据源列表失败！");
        }
        
        return dataMap;
    }

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/delete")
	public Map<String,Object> delete(String ids) {
        this.initMap();
        
        if(ids != null){
            String[] idArrs = ids.split(",");
            Set<GeneralQuery> generalQueries = new HashSet<GeneralQuery>();
            for (int i = 0; i < idArrs.length; i ++) {
            	generalQueries.add(GeneralQuery.get(GeneralQuery.class, Long.parseLong(idArrs[i])));
            }
            gqcApplication.removeEntities(generalQueries);
        }
        
		dataMap.put("result", "success");
		return dataMap;
	}
   
    /**
     * 获取指定数据源所有的表
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAllTable")
    public Map<String,Object> findAllTable(Long id) {
        try {
            this.initMap();
            
            List<String> tableList = dataSourceApplication.findAllTable(id);
            dataMap.put("tableList", tableList);
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "获取表失败！");
        }
        
        return dataMap;
    }
    
    /**
     * 获取指定表的所有列
     * @return
     * @param id 数据源主键
     * @param tableName
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAllColumn")
    public Map<String,Object> findAllColumn(Long id, String tableName) {
        try {
            this.initMap();
            
            Map<String, Integer> tableMap = dataSourceApplication.findAllColumn(id, tableName);
            dataMap.put("tableMap", tableMap);
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "获取列失败！");
        }
        
        return dataMap;
    }
    
    /**
     * 创建Json对象
     */
    private void initMap(){
        dataMap = new HashMap<String, Object>();
    }
    

    private void removeTableMapLeftDiv(GeneralQuery generalQuery, Map<String, Integer> tableMapLeftDiv){
        //把已被选择了的列从列池中去除
        List<PreQueryCondition> list = generalQuery.getPreQueryConditions();
        for(PreQueryCondition bean : list){
            if(tableMapLeftDiv.containsKey(bean.getFieldName())){
                //把列类型赋值给PreQueryCondition
                bean.setFieldType(tableMapLeftDiv.get(bean.getFieldName()));
                tableMapLeftDiv.remove(bean.getFieldName());
            }
        }
        
        //把已被选择了的列从列池中去除
        List<DynamicQueryCondition> list2 = generalQuery.getDynamicQueryConditions();
        for(DynamicQueryCondition bean : list2){
            if(tableMapLeftDiv.containsKey(bean.getFieldName())){
                //把列类型赋值给DynamicQueryCondition
                bean.setFieldType(tableMapLeftDiv.get(bean.getFieldName()));
                tableMapLeftDiv.remove(bean.getFieldName());
            }
        }
    }
    
    private void removeTableMapRightDiv(GeneralQuery generalQuery, Map<String, Integer> tableMapRightDiv){
        //把已被选择了的列从列池中去除
        List<FieldDetail> list3 = generalQuery.getFieldDetails();
        for(FieldDetail bean : list3){
            if(tableMapRightDiv.containsKey(bean.getFieldName())){
                tableMapRightDiv.remove(bean.getFieldName());
            }
        }
    }
	
	
	class GeneralQueryVo {
		
		private Long id;
		
		private String dataSourceId;
		
		private String queryName;
		
		private String tableName;
		
		private String description;
		
		private Date createDate;
		
		private List<PreQueryCondition> preQueryConditions = new ArrayList<PreQueryCondition>();
		
		private List<DynamicQueryCondition> dynamicQueryConditions = new ArrayList<DynamicQueryCondition>();
		
		private List<FieldDetail> fieldDetails = new ArrayList<FieldDetail>();

		GeneralQueryVo(Long id, String dataSourceId, String queryName, String tableName, String description, Date createDate) {
			this.id = id;
			this.dataSourceId = dataSourceId;
			this.queryName = queryName;
			this.tableName = tableName;
			this.description = description;
			this.createDate = createDate;
		}


		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getDataSourceId() {

			return dataSourceId;
		}

		public void setDataSourceId(String dataSourceId) {
			this.dataSourceId = dataSourceId;
		}

		public String getQueryName() {
			return queryName;
		}

		public void setQueryName(String queryName) {
			this.queryName = queryName;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}


		public List<PreQueryCondition> getPreQueryConditions() {
			return preQueryConditions;
		}


		public void setPreQueryConditions(List<PreQueryCondition> preQueryConditions) {
			this.preQueryConditions = preQueryConditions;
		}


		public List<DynamicQueryCondition> getDynamicQueryConditions() {
			return dynamicQueryConditions;
		}


		public void setDynamicQueryConditions(
				List<DynamicQueryCondition> dynamicQueryConditions) {
			this.dynamicQueryConditions = dynamicQueryConditions;
		}


		public List<FieldDetail> getFieldDetails() {
			return fieldDetails;
		}


		public void setFieldDetails(List<FieldDetail> fieldDetails) {
			this.fieldDetails = fieldDetails;
		}
		
	}
	
	/**
	 * 克隆
	 * @param map
	 * @return
	 */
	public static Map<String,Integer> getCloneMap(Map<String,Integer> map){
        Map<String,Integer> mapClone = new HashMap<String,Integer>();
        for(Entry<String,Integer> e : map.entrySet()){
            mapClone.put(e.getKey(), e.getValue());
        }
        return mapClone;
    }
	
}
