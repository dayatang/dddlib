
package org.openkoala.jbpm.applicationImpl.core;

import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.transaction.annotation.Transactional;
import com.dayatang.querychannel.support.Page;
import com.dayatang.querychannel.service.QueryChannelService;

import org.openkoala.exception.extend.KoalaException;
import org.openkoala.jbpm.application.vo.*;
import org.openkoala.jbpm.application.core.KoalaJbpmVariableApplication;
import org.openkoala.jbpm.core.*;
import org.apache.commons.beanutils.BeanUtils;

@Named
public class KoalaJbpmVariableApplicationImpl implements KoalaJbpmVariableApplication {

	@Inject
	private QueryChannelService queryChannel;
	
	public KoalaJbpmVariableVO getKoalaJbpmVariable(Long id) {
			
	   	String jpql = "select _koalaJbpmVariable from KoalaJbpmVariable _koalaJbpmVariable  where _koalaJbpmVariable.id=?";
	   	KoalaJbpmVariable koalaJbpmVariable = (KoalaJbpmVariable) queryChannel.querySingleResult(jpql, new Object[] { id });
		KoalaJbpmVariableVO koalaJbpmVariableVO = new KoalaJbpmVariableVO();
		// 将domain转成VO
		try {
			BeanUtils.copyProperties(koalaJbpmVariableVO, koalaJbpmVariable);
		} catch (Exception e) {
			e.printStackTrace();
		}
																				
		return koalaJbpmVariableVO;
	}
	
	public KoalaJbpmVariableVO saveKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariableVO) {
		KoalaJbpmVariable koalaJbpmVariable = new KoalaJbpmVariable();
		if(KoalaJbpmVariable.isVariableExists(koalaJbpmVariableVO.getScope(), koalaJbpmVariableVO.getKey())){
			throw new KoalaException("101","变量已存在，不能重复新增");
		}
		try {
        	BeanUtils.copyProperties(koalaJbpmVariable, koalaJbpmVariableVO);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		koalaJbpmVariable.save();
		return koalaJbpmVariableVO;
	}
	
	public void updateKoalaJbpmVariable(KoalaJbpmVariableVO koalaJbpmVariableVO) {
		KoalaJbpmVariable koalaJbpmVariable = KoalaJbpmVariable.get(KoalaJbpmVariable.class, koalaJbpmVariableVO.getId());
		// 设置要更新的值
		try {
			BeanUtils.copyProperties(koalaJbpmVariable, koalaJbpmVariableVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeKoalaJbpmVariable(Long id) {
		this.removeKoalaJbpmVariables(new Long[] { id });
	}
	
	public void removeKoalaJbpmVariables(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			KoalaJbpmVariable koalaJbpmVariable = KoalaJbpmVariable.load(KoalaJbpmVariable.class, ids[i]);
			koalaJbpmVariable.remove();
		}
	}
	
	public List<KoalaJbpmVariableVO> findAllKoalaJbpmVariable() {
		List<KoalaJbpmVariableVO> list = new ArrayList<KoalaJbpmVariableVO>();
		List<KoalaJbpmVariable> all = KoalaJbpmVariable.findAll(KoalaJbpmVariable.class);
		for (KoalaJbpmVariable koalaJbpmVariable : all) {
			KoalaJbpmVariableVO koalaJbpmVariableVO = new KoalaJbpmVariableVO();
			// 将domain转成VO
			try {
				BeanUtils.copyProperties(koalaJbpmVariableVO, koalaJbpmVariable);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(koalaJbpmVariableVO);
		}
		return list;
	}
	
	public Page<KoalaJbpmVariableVO> pageQueryKoalaJbpmVariable(KoalaJbpmVariableVO queryVo, int currentPage, int pageSize) {
		List<KoalaJbpmVariableVO> result = new ArrayList<KoalaJbpmVariableVO>();
		List<Object> conditionVals = new ArrayList<Object>();
						
	   	StringBuilder jpql = new StringBuilder("select _koalaJbpmVariable from KoalaJbpmVariable _koalaJbpmVariable   where 1=1 ");
	   		   		   		   		   		   		
	   	if (queryVo.getKey() != null && !"".equals(queryVo.getKey())) {
	   		jpql.append(" and _koalaJbpmVariable.key like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getKey()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getValue() != null && !"".equals(queryVo.getValue())) {
	   		jpql.append(" and _koalaJbpmVariable.value like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getValue()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getType() != null && !"".equals(queryVo.getType())) {
	   		jpql.append(" and _koalaJbpmVariable.type like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getType()));
	   	}		
	   		   		   		   		   		   		   		   		
	   	if (queryVo.getScope() != null && !"".equals(queryVo.getScope())) {
	   		jpql.append(" and _koalaJbpmVariable.scope like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getScope()));
	   	}		
	   		   		   		   		   		   		   		   		   	
        Page<KoalaJbpmVariable> pages = queryChannel.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pageSize);
        for (KoalaJbpmVariable koalaJbpmVariable : pages.getResult()) {
            KoalaJbpmVariableVO koalaJbpmVariableVO = new KoalaJbpmVariableVO();
            // 将domain转成VO
            try {
            	BeanUtils.copyProperties(koalaJbpmVariableVO, koalaJbpmVariable);
            	
            	String scope = koalaJbpmVariableVO.getScope();
            	if(scope.startsWith(KoalaJbpmVariable.KOALA_GLOBAL)){
            		koalaJbpmVariableVO.setScopeType(KoalaJbpmVariable.KOALA_GLOBAL);
            		koalaJbpmVariableVO.setScope("");
            	}
            	if(scope.startsWith(KoalaJbpmVariable.KOALA_PACKAGE)){
            		koalaJbpmVariableVO.setScopeType(KoalaJbpmVariable.KOALA_PACKAGE);
            		koalaJbpmVariableVO.setScope(scope.substring(KoalaJbpmVariable.KOALA_PACKAGE.length()));
            	}
            	if(scope.startsWith(KoalaJbpmVariable.KOALA_PROCESS)){
            		koalaJbpmVariableVO.setScopeType(KoalaJbpmVariable.KOALA_PROCESS);
            		koalaJbpmVariableVO.setScope(scope.substring(KoalaJbpmVariable.KOALA_PROCESS.length()));
            	}
            } catch (Exception e) {
            	e.printStackTrace();
            }
            																											
            result.add(koalaJbpmVariableVO);
        }
        return new Page<KoalaJbpmVariableVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}
	
	
}
