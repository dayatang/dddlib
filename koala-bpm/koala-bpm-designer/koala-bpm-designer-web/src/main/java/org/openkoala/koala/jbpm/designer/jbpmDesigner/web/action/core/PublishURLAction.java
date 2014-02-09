
package org.openkoala.koala.jbpm.designer.jbpmDesigner.web.action.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import javax.inject.Inject;

import org.openkoala.koala.jbpm.designer.application.core.BpmDesignerApplication;
import org.openkoala.koala.jbpm.designer.application.vo.PublishURLVO;

import com.dayatang.querychannel.support.Page;

public class PublishURLAction extends ActionSupport {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 975065129891804970L;

	private PublishURLVO publishURLVO = new PublishURLVO();
	
	private Map<String, Object> dataMap = new HashMap<String, Object>();
		
	private List<Long> urlIds;
	
	@Inject
	private BpmDesignerApplication publishURLApplication;
	
	private int page;
	
	private int pagesize;
	
	public String add() {
		try {
			publishURLApplication.savePublishURL(publishURLVO);
			dataMap.put("result", "success");
		} catch (Exception e) {
		    e.printStackTrace();
			dataMap.put("result", "error");
		}
		return "JSON";
	}
	
	public String update() {
		try {
			publishURLApplication.updatePublishURL(publishURLVO);
			dataMap.put("result", "success");
		} catch (Exception e) {
		    e.printStackTrace();
			dataMap.put("result", "error");
		}
		return "JSON";
	}
	
	public String pageJson() {
		try {
			Page<PublishURLVO> all = publishURLApplication.pageQueryPublishURL(publishURLVO, page, pagesize);
			dataMap.put("Rows", all.getResult());
			dataMap.put("start", page * pagesize - pagesize);
			dataMap.put("limit", pagesize);
			dataMap.put("Total", all.getTotalCount());
		} catch (Exception e) {
		    e.printStackTrace();
			dataMap.put("result", "error");
		}
		return "JSON";
	}
	
	
	
	public String delete() {
		try {
			int size = urlIds.size();
			Long[] ids = new Long[size];
			for(int i=0; i<size; i++){
				ids[i] = urlIds.get(i);
			}
			publishURLApplication.removePublishURL(ids);
			dataMap.put("result", "success");
		} catch (Exception e) {
		    e.printStackTrace();
			dataMap.put("result", "error");
		}
		return "JSON";
	}
	
	public String get() {
		try {
			dataMap.put("data", publishURLApplication.getPublishURL(publishURLVO.getId()));
		} catch (Exception e) {
		    e.printStackTrace();
			dataMap.put("result", "error");
		}
		return "JSON";
	}
	
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPage() {
		return this.page;
	}
	
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	
	public int getPagesize() {
		return this.pagesize;
	}
	
	public void setPublishURLVO(PublishURLVO publishURLVO) {
		this.publishURLVO = publishURLVO;
	}
	
	public PublishURLVO getPublishURLVO() {
		return this.publishURLVO;
	}
	
	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public List<Long> getUrlIds() {
		return urlIds;
	}

	public void setUrlIds(List<Long> urlIds) {
		this.urlIds = urlIds;
	}
	
	
}