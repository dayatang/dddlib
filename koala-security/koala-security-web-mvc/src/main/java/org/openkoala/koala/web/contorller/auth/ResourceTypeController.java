package org.openkoala.koala.web.contorller.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceTypeVO;
import org.openkoala.exception.extend.ApplicationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/auth/ResourceType")
public class ResourceTypeController {

	@Inject
	private ResourceTypeApplication resourceTypeApplication;

	@ResponseBody
	@RequestMapping("/save")
	public Map<String, Object> save(ParamsPojo params) {
		ResourceTypeVO resourceTypeVO = params.getResourceTypeVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		resourceTypeApplication.save(resourceTypeVO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/delete")
	public Map<String, Object> delete(ParamsPojo params) {
		List<ResourceTypeVO> resourceTypeVOs = params.getResourceTypeVOs();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		resourceTypeApplication.delete(resourceTypeVOs
				.toArray(new ResourceTypeVO[resourceTypeVOs.size()]));
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(ParamsPojo params) {
		ResourceTypeVO resourceTypeVO = params.getResourceTypeVO();
		Map<String, Object> dataMap = new HashMap<String,Object>();
		dataMap.put("result", "success");
		try{
			resourceTypeApplication.update(resourceTypeVO);
		}catch(ApplicationException e){
			dataMap.put("result", "该资源类型已存在");
		}
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/pageJson")
	public Map<String, Object> pageJson(int page,int pagesize) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Page<ResourceTypeVO> result = resourceTypeApplication.pageQuery(page,
				pagesize);
		dataMap.put("Rows", result.getResult());
		dataMap.put("start", page * page - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", result.getTotalCount());
		return dataMap;
	}

	@RequestMapping("/list")
	public String list() {
		return "auth/ResourceType-list";
	}
}
