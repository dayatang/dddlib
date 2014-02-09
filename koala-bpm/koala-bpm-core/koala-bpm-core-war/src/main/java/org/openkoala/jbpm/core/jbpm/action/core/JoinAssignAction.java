
package org.openkoala.jbpm.core.jbpm.action.core;

import java.util.HashMap;
import java.util.Map;
import com.opensymphony.xwork2.ActionSupport;
import javax.inject.Inject;

import org.openkoala.jbpm.application.core.JoinAssignApplication;
import org.openkoala.jbpm.application.vo.JoinAssignVO;
import org.openkoala.jbpm.core.jbpm.action.BaseAction;

import com.dayatang.querychannel.support.Page;

public class JoinAssignAction extends BaseAction {
		
	private static final long serialVersionUID = 1L;
	
	private JoinAssignVO joinAssignVO = new JoinAssignVO();
		
	@Inject
	private JoinAssignApplication joinAssignApplication;
	
	public String add() {
		joinAssignApplication.saveJoinAssign(joinAssignVO);
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String update() {
		joinAssignApplication.updateJoinAssign(joinAssignVO);
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String pageJson() {
		Page<JoinAssignVO> all = joinAssignApplication.pageQueryJoinAssign(joinAssignVO, page, pagesize);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}
	
	public String delete() {
	    String idsString = getRequest().getParameter("ids");
        if(idsString != null){
            String[] idArrs = idsString.split(",");
            Long[] ids = new Long[idArrs.length];
            for (int i = 0; i < idArrs.length; i ++) {
            	ids[i] = Long.parseLong(idArrs[i]);
            }
            joinAssignApplication.removeJoinAssigns(ids);
        }
        
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String get() {
		dataMap.put("data", joinAssignApplication.getJoinAssign(joinAssignVO.getId()));
		return "JSON";
	}
	
	
	public void setJoinAssignVO(JoinAssignVO joinAssignVO) {
		this.joinAssignVO = joinAssignVO;
	}
	
	public JoinAssignVO getJoinAssignVO() {
		return this.joinAssignVO;
	}
}