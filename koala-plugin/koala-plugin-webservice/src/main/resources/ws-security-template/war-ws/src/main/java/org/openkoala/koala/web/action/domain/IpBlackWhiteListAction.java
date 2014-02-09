package org.openkoala.koala.web.action.domain;

import java.util.HashMap;
import java.util.Map;
import com.opensymphony.xwork2.ActionSupport;
import javax.inject.Inject;
import com.dayatang.querychannel.support.Page;
import org.apache.struts2.ServletActionContext;
import org.openkoala.auth.application.domain.IpBlackWhiteListApplication;
import org.openkoala.auth.application.vo.*;

@SuppressWarnings("serial")
public class IpBlackWhiteListAction extends ActionSupport {

	private IpBlackWhiteListVO ipBlackWhiteListVO;

	private Map<String, Object> dataMap;

	private int page;

	private int pagesize;

	@Inject
	private IpBlackWhiteListApplication ipBlackWhiteListApplication;
	
	public IpBlackWhiteListAction() {
		this.ipBlackWhiteListVO = new IpBlackWhiteListVO();
		this.dataMap = new HashMap<String, Object>();
	}

	public String add() {
		ipBlackWhiteListApplication.saveIpBlackWhiteList(ipBlackWhiteListVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String update() {
		ipBlackWhiteListApplication.updateIpBlackWhiteList(ipBlackWhiteListVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String pageJson() {
		Page<IpBlackWhiteListVO> all = ipBlackWhiteListApplication.pageQueryIpBlackWhiteList(ipBlackWhiteListVO, page, pagesize);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}

	public String delete() {
		String idsString = ServletActionContext.getRequest().getParameter("ids");
		if (idsString != null) {
			String[] idArrs = idsString.split(",");
			Long[] ids = new Long[idArrs.length];
			for (int i = 0; i < idArrs.length; i++) {
				ids[i] = Long.parseLong(idArrs[i]);
			}
			ipBlackWhiteListApplication.removeIpBlackWhiteLists(ids);
		}

		dataMap.put("result", "success");
		return "JSON";
	}

	public String get() {
		dataMap.put("data", ipBlackWhiteListApplication.getIpBlackWhiteList(ipBlackWhiteListVO.getId()));
		return "JSON";
	}

	public void setIpBlackWhiteListVO(IpBlackWhiteListVO ipBlackWhiteListVO) {
		this.ipBlackWhiteListVO = ipBlackWhiteListVO;
	}

	public IpBlackWhiteListVO getIpBlackWhiteListVO() {
		return this.ipBlackWhiteListVO;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
}