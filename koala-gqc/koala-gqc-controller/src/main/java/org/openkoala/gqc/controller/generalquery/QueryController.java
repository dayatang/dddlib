package org.openkoala.gqc.controller.generalquery;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openkoala.gqc.application.GqcApplication;
import org.openkoala.gqc.core.domain.DynamicQueryCondition;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

/**
 * 查询控制器
 * 
 * @author zyb
 * @since 2013-7-11 下午8:00:25
 */
@Controller
public class QueryController {

	/**
	 * 查询通道应用层接口实例
	 */
    @Autowired
	private GqcApplication gqcApplication;
	
	/**
	 * 生成查询页面
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/query/{id}")
	public String queryPage(@PathVariable Long id, ModelMap modelMap) {
		GeneralQuery generalQuery = gqcApplication.getById(id);
		modelMap.addAttribute("gq", generalQuery);
		modelMap.addAttribute("gqId", id);
		return "query";
	}
	
	/**
	 * 生成查询页面
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
    @ResponseBody
	@RequestMapping("/preview/{id}")
	public Map<String, Object> preview(@PathVariable Long id) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("generalQuery", gqcApplication.getById(id));
		return dataMap;
	}

	/**
	 * 分页查询数据
	 * 
	 * @param id
	 * @param page
	 * @param pagesize
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/search/{id}")
	public Map<String, Object> search(@PathVariable Long id, @RequestParam int page,
			@RequestParam int pagesize, HttpServletRequest request) {
		GeneralQuery generalQuery = gqcApplication.getById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object[]> params = request.getParameterMap();
		String startValueTag = "Start@";
		String endValueTag = "End@";
		
		Set<Entry<String,Object[]>> keyValues = params.entrySet();
		for(Entry<String,Object[]> keyValue: keyValues){
			String key = keyValue.getKey();
			if (!"page".equals(key) && !"pagesize".equals(key)) {
				DynamicQueryCondition dqc = null;
				if (key.endsWith(startValueTag)) {
					String fieldName = key.replace(startValueTag, "");
					dqc = generalQuery.getDynamicQueryConditionByFieldName(fieldName);
					if (dqc != null) {
						dqc.setStartValue((String) keyValue.getValue()[0]);
					}
					continue;
				}
				
				if (key.endsWith(endValueTag)) {
					String fieldName = key.replace(endValueTag, "");
					dqc = generalQuery.getDynamicQueryConditionByFieldName(fieldName);
					if (dqc != null) {
						dqc.setEndValue((String) keyValue.getValue()[0]);
					}
					continue;
				}
				
				dqc = generalQuery.getDynamicQueryConditionByFieldName(key);
				if (dqc != null) {
					dqc.setValue((String) keyValue.getValue()[0]);
				}
			}
		}
		
		Page<Map<String, Object>> data = gqcApplication.pagingQuery(generalQuery, page, pagesize);

		result.put("Rows", data.getResult());
		result.put("start", page * pagesize - pagesize);
		result.put("limit", pagesize);
		result.put("Total", data.getTotalCount());
		return result;
	}

}
