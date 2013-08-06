package org.openkoala.gqc.controller.datasource;



import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.openkoala.gqc.application.DataSourceApplication;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.vo.DataSourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;


@Controller
@RequestMapping("/dataSource")
public class DataSourceController{
		
	@Autowired
	private DataSourceApplication dataSourceApplication;
	
	//Json对象
	private Map<String, Object> dataMap;
	
	/**
	 * 增加数据源
	 * @param dataSourceVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String,Object> add(DataSourceVO dataSourceVO) {
		try {
		    this.initMap();
		    
            String errorMsg = dataSourceApplication.saveDataSource(dataSourceVO);
            if(errorMsg == null){
                dataMap.put("result", "success");
            }else{
                dataMap.put("result", errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "保存失败！");
        }
        
		return dataMap;
	}
	
	/**
	 * 更新数据源
	 * @param dataSourceVO
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/update")
	public Map<String,Object> update(DataSourceVO dataSourceVO) {
        this.initMap();
        
		dataSourceApplication.updateDataSource(dataSourceVO);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	/**
	 * 分页查询数据源列表
	 * @param page
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/pageJson")
	public Map<String,Object> pageJson(int page, int pagesize) {
        this.initMap();
        
        Page<DataSourceVO> all = dataSourceApplication.pageQueryDataSource(new DataSourceVO(), page, pagesize);
        dataMap.put("Rows", all.getResult());
        dataMap.put("start", page * pagesize - pagesize);
        dataMap.put("limit", pagesize);
        dataMap.put("Total", all.getTotalCount());
		return dataMap;
	}

	/**
	 * 删除数据源
	 * @param ids
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/delete")
	public Map<String,Object> delete(String ids) {
        this.initMap();
        
        if(ids != null){
            String[] idArrs = ids.split(",");
            Long[] idsLong = new Long[idArrs.length];
            for (int i = 0; i < idArrs.length; i ++) {
                idsLong[i] = Long.parseLong(idArrs[i]);
            }
            dataSourceApplication.removeDataSources(idsLong);
        }
        
		dataMap.put("result", "success");
		return dataMap;
	}
	
    /**
     * 查询指定数据源
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/get/{id}")
	public Map<String,Object> get(@PathVariable("id") Long id) {
        this.initMap();
        
		dataMap.put("data", dataSourceApplication.getDataSource(id));
		return dataMap;
	}
    
    /**
     * 检测数据源是否可用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkDataSourceById")
    public Map<String,Object> checkDataSourceById(Long id) {
        this.initMap();
        
        boolean result = dataSourceApplication.testConnection(id);
        if(result){
            dataMap.put("result", "该数据源可用");
        }else{
            dataMap.put("result", "该数据源不可用");
        }
        
        return dataMap;
    }
    
    /**
     * 检测数据源是否可用
     * @param dataSourceVO
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkDataSource")
    public Map<String,Object> checkDataSource(DataSource dataSource) {
        this.initMap();
        
        if (dataSource.getDataSourceType().equals(DataSourceType.SYSTEM_DATA_SOURCE)) {
        	try {
				dataSource = DataSource.getSystemDataSource(dataSource.getDataSourceId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        
        boolean result = dataSource.testConnection();
        if(result){
            dataMap.put("result", "该数据源可用");
        }else{
            dataMap.put("result", "该数据源不可用");
        }
        
        return dataMap;
    }
    
    /**
     * 创建Json对象
     */
    private void initMap(){
        dataMap = new HashMap<String, Object>();
    }
	
}