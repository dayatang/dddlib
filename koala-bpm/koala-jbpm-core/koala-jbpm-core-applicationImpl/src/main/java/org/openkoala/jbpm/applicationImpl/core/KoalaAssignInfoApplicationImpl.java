
package org.openkoala.jbpm.applicationImpl.core;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.dayatang.querychannel.support.Page;
import com.dayatang.querychannel.service.QueryChannelService;
import org.openkoala.jbpm.application.vo.*;
import org.openkoala.jbpm.application.core.KoalaAssignInfoApplication;
import org.openkoala.jbpm.core.*;
import org.apache.commons.beanutils.BeanUtils;

@Named
public class KoalaAssignInfoApplicationImpl implements KoalaAssignInfoApplication {

	@Inject
	private QueryChannelService queryChannel;
	
	public KoalaAssignInfoVO getKoalaAssignInfo(Long id) {
			
	   	String jpql = "select _koalaAssignInfo from KoalaAssignInfo _koalaAssignInfo left join _koalaAssignInfo.jbpmNames  where _koalaAssignInfo.id=?";
	   	KoalaAssignInfo koalaAssignInfo = (KoalaAssignInfo) queryChannel.querySingleResult(jpql, new Object[] { id });
		KoalaAssignInfoVO koalaAssignInfoVO = new KoalaAssignInfoVO();
		// 将domain转成VO
		try {
			BeanUtils.copyProperties(koalaAssignInfoVO, koalaAssignInfo);
			koalaAssignInfoVO.setRelativeProcess(koalaAssignInfo.getRelativeProcess());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return koalaAssignInfoVO;
	}
	
	public KoalaAssignInfoVO saveKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfoVO) {
		KoalaAssignInfo koalaAssignInfo = new KoalaAssignInfo();
		try {
        	BeanUtils.copyProperties(koalaAssignInfo, koalaAssignInfoVO);
        	if(koalaAssignInfo.getBeginTime()==null)koalaAssignInfo.setBeginTime(new Date());
        	for(String processId:koalaAssignInfoVO.getRelativeProcess().split(";")){
        		if(processId==null || "".equals(processId.trim()))continue;
        		KoalaAssignDetail detail = new KoalaAssignDetail(koalaAssignInfo,processId);
        		detail.save();
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return koalaAssignInfoVO;
	}
	
	public void updateKoalaAssignInfo(KoalaAssignInfoVO koalaAssignInfoVO) {
		KoalaAssignInfo koalaAssignInfo = KoalaAssignInfo.get(KoalaAssignInfo.class, koalaAssignInfoVO.getId());
		// 设置要更新的值
		try {
			BeanUtils.copyProperties(koalaAssignInfo, koalaAssignInfoVO);
			for(KoalaAssignDetail detail:koalaAssignInfo.getJbpmNames()){
				detail.remove();
			}
			koalaAssignInfo.setJbpmNames(null);
			for(String processId:koalaAssignInfoVO.getRelativeProcess().split(";")){
				if(processId==null || "".equals(processId.trim()))continue;
				KoalaAssignDetail detail = new KoalaAssignDetail(koalaAssignInfo,processId);
        		detail.save();
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeKoalaAssignInfo(Long id) {
		this.removeKoalaAssignInfos(new Long[] { id });
	}
	
	public void removeKoalaAssignInfos(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			KoalaAssignInfo koalaAssignInfo = KoalaAssignInfo.load(KoalaAssignInfo.class, ids[i]);
			koalaAssignInfo.remove();
		}
	}
	
	public List<KoalaAssignInfoVO> findAllKoalaAssignInfo() {
		List<KoalaAssignInfoVO> list = new ArrayList<KoalaAssignInfoVO>();
		List<KoalaAssignInfo> all = KoalaAssignInfo.findAll(KoalaAssignInfo.class);
		for (KoalaAssignInfo koalaAssignInfo : all) {
			KoalaAssignInfoVO koalaAssignInfoVO = new KoalaAssignInfoVO();
			// 将domain转成VO
			try {
				BeanUtils.copyProperties(koalaAssignInfoVO, koalaAssignInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(koalaAssignInfoVO);
		}
		return list;
	}
	
	public Page<KoalaAssignInfoVO> pageQueryKoalaAssignInfo(KoalaAssignInfoVO queryVo, int currentPage, int pageSize) {
		List<KoalaAssignInfoVO> result = new ArrayList<KoalaAssignInfoVO>();
		List<Object> conditionVals = new ArrayList<Object>();
								
				
	   	StringBuilder jpql = new StringBuilder("select _koalaAssignInfo from KoalaAssignInfo _koalaAssignInfo   where 1=1 ");
	   		   		   		   		   		   		
	   	if (queryVo.getName() != null && !"".equals(queryVo.getName())) {
	   		jpql.append(" and _koalaAssignInfo.name like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getName()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getAssigner() != null && !"".equals(queryVo.getAssigner())) {
	   		jpql.append(" and _koalaAssignInfo.assigner like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getAssigner()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getAssignerTo() != null && !"".equals(queryVo.getAssignerTo())) {
	   		jpql.append(" and _koalaAssignInfo.assignerTo like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getAssignerTo()));
	   	}		
	   		   		   		   		   		   		   		   		   		
	   	if (queryVo.getBeginTime() != null) {
	   		jpql.append(" and _koalaAssignInfo.beginTime between ? and ? ");
	   		conditionVals.add(queryVo.getBeginTime());
	   		conditionVals.add(queryVo.getBeginTimeEnd());
	   	}	
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getEndTime() != null) {
	   		jpql.append(" and _koalaAssignInfo.endTime between ? and ? ");
	   		conditionVals.add(queryVo.getEndTime());
	   		conditionVals.add(queryVo.getEndTimeEnd());
	   	}	
	   		   		   		   		   		   		   		
	   	if (queryVo.getCreater() != null && !"".equals(queryVo.getCreater())) {
	   		jpql.append(" and _koalaAssignInfo.creater like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getCreater()));
	   	}		
	   		   		   		   		   		   		   		   		   		   		   		   		   		   		   	
        Page<KoalaAssignInfo> pages = queryChannel.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pageSize);
        for (KoalaAssignInfo koalaAssignInfo : pages.getResult()) {
            KoalaAssignInfoVO koalaAssignInfoVO = new KoalaAssignInfoVO();
            // 将domain转成VO
            try {
            	BeanUtils.copyProperties(koalaAssignInfoVO, koalaAssignInfo);
            } catch (Exception e) {
            	e.printStackTrace();
            }
            																																							
            result.add(koalaAssignInfoVO);
        }
        return new Page<KoalaAssignInfoVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}
	
	public Page<KoalaAssignDetailVO> findJbpmNamesByKoalaAssignInfo(Long id, int currentPage, int pageSize) {
		List<KoalaAssignDetailVO> result = new ArrayList<KoalaAssignDetailVO>();
		String jpql = "select e from KoalaAssignInfo o right join o.jbpmNames e where o.id=?";
		Page<KoalaAssignDetail> pages = queryChannel.queryPagedResultByPageNo(jpql, new Object[] { id }, currentPage, pageSize);
        for (KoalaAssignDetail entity : pages.getResult()) {
            KoalaAssignDetailVO vo = new KoalaAssignDetailVO();
            // 将domain转成VO
            if (result != null) {
            	try {
            		BeanUtils.copyProperties(vo, entity);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
            }
            result.add(vo);
        }
        return new Page<KoalaAssignDetailVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}		
	
}
