
package org.openkoala.jbpm.core.jbpm.action.core;


import java.util.List;

import javax.inject.Inject;
import com.dayatang.querychannel.support.Page;

import org.openkoala.jbpm.application.JBPMApplication;
import org.openkoala.jbpm.application.core.KoalaAssignInfoApplication;
import org.openkoala.jbpm.application.vo.*;
import org.openkoala.jbpm.core.jbpm.action.BaseAction;

public class KoalaAssignInfoAction extends BaseAction {
		
	private static final long serialVersionUID = 1L;
	
	private KoalaAssignInfoVO koalaAssignInfoVO = new KoalaAssignInfoVO();
		
	@Inject
	private KoalaAssignInfoApplication koalaAssignInfoApplication;
	
	@Inject
	private JBPMApplication jbpmApplication;
	
	private List<ProcessVO> processes;
	
	public String add() {
		try{
		koalaAssignInfoApplication.saveKoalaAssignInfo(koalaAssignInfoVO);
		}catch(Exception e){
			e.printStackTrace();
		}
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String update() {
		koalaAssignInfoApplication.updateKoalaAssignInfo(koalaAssignInfoVO);
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String pageJson() {
		Page<KoalaAssignInfoVO> all = koalaAssignInfoApplication.pageQueryKoalaAssignInfo(koalaAssignInfoVO, page, pagesize);
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
            koalaAssignInfoApplication.removeKoalaAssignInfos(ids);
        }
        
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String get() {
		dataMap.put("data", koalaAssignInfoApplication.getKoalaAssignInfo(koalaAssignInfoVO.getId()));
		return "JSON";
	}
	
	public String queryProcess(){
		this.processes = jbpmApplication.getProcesses();
		return "PROCESS";
	}
	

	public String findJbpmNamesByKoalaAssignInfo() {
		Page<KoalaAssignDetailVO> all = koalaAssignInfoApplication.findJbpmNamesByKoalaAssignInfo(koalaAssignInfoVO.getId(), page, pagesize);
		dataMap.put("Rows", all.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getTotalCount());
		return "JSON";
	}		
	
	public void setKoalaAssignInfoVO(KoalaAssignInfoVO koalaAssignInfoVO) {
		this.koalaAssignInfoVO = koalaAssignInfoVO;
	}
	
	public KoalaAssignInfoVO getKoalaAssignInfoVO() {
		return this.koalaAssignInfoVO;
	}

	public List<ProcessVO> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessVO> processes) {
		this.processes = processes;
	}
	
}