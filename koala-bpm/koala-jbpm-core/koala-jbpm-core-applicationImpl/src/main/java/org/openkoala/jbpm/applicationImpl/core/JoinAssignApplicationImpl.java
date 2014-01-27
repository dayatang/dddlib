
package org.openkoala.jbpm.applicationImpl.core;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.dayatang.querychannel.support.Page;
import com.dayatang.querychannel.service.QueryChannelService;
import org.openkoala.jbpm.application.vo.*;
import org.openkoala.jbpm.application.core.JoinAssignApplication;
import org.openkoala.jbpm.core.*;
import org.apache.commons.beanutils.BeanUtils;
import org.openkoala.exception.extend.ApplicationException;

@Named
public class JoinAssignApplicationImpl implements JoinAssignApplication {

	@Inject
	private QueryChannelService queryChannel;
	
	public JoinAssignVO getJoinAssign(Long id) {
			
	   	String jpql = "select _joinAssign from JoinAssign _joinAssign  where _joinAssign.id=?";
	   	JoinAssign joinAssign = (JoinAssign) queryChannel.querySingleResult(jpql, new Object[] { id });
	   	
	   	if (joinAssign == null) {
	   		throw new RuntimeException("This entity is not exist, id:" + id);
	   	}
	   	
		JoinAssignVO joinAssignVO = new JoinAssignVO();
		try {
			BeanUtils.copyProperties(joinAssignVO, joinAssign);
		} catch (Exception e) {
			e.printStackTrace();
		}
																												
		return joinAssignVO;
	}
	
	public JoinAssignVO saveJoinAssign(JoinAssignVO joinAssignVO) {
		JoinAssign joinAssign = new JoinAssign();
		try {
        	BeanUtils.copyProperties(joinAssign, joinAssignVO);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		joinAssign.save();
		joinAssignVO.setId(joinAssign.getId());
		return joinAssignVO;
	}
	
	public void updateJoinAssign(JoinAssignVO joinAssignVO) {
		JoinAssign joinAssign = JoinAssign.get(JoinAssign.class, joinAssignVO.getId());
		// 设置要更新的值
		try {
			BeanUtils.copyProperties(joinAssign, joinAssignVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeJoinAssign(Long id) {
		this.removeJoinAssigns(new Long[] { id });
	}
	
	public void removeJoinAssigns(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			JoinAssign joinAssign = JoinAssign.load(JoinAssign.class, ids[i]);
			joinAssign.remove();
		}
	}
	
	public List<JoinAssignVO> findAllJoinAssign() {
		List<JoinAssignVO> list = new ArrayList<JoinAssignVO>();
		List<JoinAssign> all = JoinAssign.findAll(JoinAssign.class);
		for (JoinAssign joinAssign : all) {
			JoinAssignVO joinAssignVO = new JoinAssignVO();
			// 将domain转成VO
			try {
				BeanUtils.copyProperties(joinAssignVO, joinAssign);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(joinAssignVO);
		}
		return list;
	}
	
	public Page<JoinAssignVO> pageQueryJoinAssign(JoinAssignVO queryVo, int currentPage, int pageSize) {
		List<JoinAssignVO> result = new ArrayList<JoinAssignVO>();
		List<Object> conditionVals = new ArrayList<Object>();
						
	   	StringBuilder jpql = new StringBuilder("select _joinAssign from JoinAssign _joinAssign   where 1=1 ");
	   		   		   		   		   		   		
	   	if (queryVo.getName() != null && !"".equals(queryVo.getName())) {
	   		jpql.append(" and _joinAssign.name like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getName()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getDescription() != null && !"".equals(queryVo.getDescription())) {
	   		jpql.append(" and _joinAssign.description like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDescription()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getKeyChoice() != null && !"".equals(queryVo.getKeyChoice())) {
	   		jpql.append(" and _joinAssign.keyChoice like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getKeyChoice()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getType() != null && !"".equals(queryVo.getType())) {
	   		jpql.append(" and _joinAssign.type like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getType()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getSuccessResult() != null && !"".equals(queryVo.getSuccessResult())) {
	   		jpql.append(" and _joinAssign.successResult like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getSuccessResult()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getMonitorVal() != null && !"".equals(queryVo.getMonitorVal())) {
	   		jpql.append(" and _joinAssign.monitorVal like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getMonitorVal()));
	   	}		
	   		   		   		   		   		   		   		   		   		   		   		   		   		   		   		   		   		   		   		   		   	
        Page<JoinAssign> pages = queryChannel.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pageSize);
        for (JoinAssign joinAssign : pages.getResult()) {
            JoinAssignVO joinAssignVO = new JoinAssignVO();
            try {
            	BeanUtils.copyProperties(joinAssignVO, joinAssign);
            } catch (Exception e) {
            	e.printStackTrace();
            }
            																																							
            result.add(joinAssignVO);
        }
        return new Page<JoinAssignVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}
	
	public JoinAssignVO getJoinAssignByName(String name){
		JoinAssign joinAssign = JoinAssign.getJoinAssignByName(name);
		JoinAssignVO joinAssignVO = new JoinAssignVO();
		try {
			BeanUtils.copyProperties(joinAssignVO, joinAssign);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return joinAssignVO;
	}
	
	
}
