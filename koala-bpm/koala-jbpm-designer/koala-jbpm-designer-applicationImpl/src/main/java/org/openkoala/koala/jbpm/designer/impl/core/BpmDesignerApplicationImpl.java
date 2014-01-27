
package org.openkoala.koala.jbpm.designer.impl.core;

import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;
import javax.inject.Named;
import org.springframework.transaction.annotation.Transactional;
import com.dayatang.querychannel.support.Page;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import org.openkoala.koala.jbpm.designer.application.vo.*;
import org.openkoala.koala.jbpm.designer.application.core.BpmDesignerApplication;
import org.openkoala.koala.jbpm.jbpmDesigner.core.*;
import org.apache.commons.beanutils.BeanUtils;

@Named
@Transactional
public class BpmDesignerApplicationImpl implements BpmDesignerApplication {

	private static QueryChannelService queryChannel;
	
	public static QueryChannelService queryChannel() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class);
		}
		return queryChannel;
	}
	
	public PublishURLVO getPublishURL(Long id) throws Exception {
			
	   	String jpql = "select _publishURL from PublishURL _publishURL  where _publishURL.id=?";
	   	PublishURL publishURL = (PublishURL) queryChannel().querySingleResult(jpql, new Object[] { id });
		PublishURLVO publishURLVO = new PublishURLVO();
		// 将domain转成VO
		BeanUtils.copyProperties(publishURLVO, publishURL);
																
		return publishURLVO;
	}
	
	public PublishURLVO savePublishURL(PublishURLVO publishURLVO) throws Exception {
		PublishURL publishURL = new PublishURL();
        BeanUtils.copyProperties(publishURL, publishURLVO);
		publishURL.save();
		return publishURLVO;
	}
	
	public void updatePublishURL(PublishURLVO publishURLVO) throws Exception {
		PublishURL publishURL = PublishURL.get(PublishURL.class, publishURLVO.getId());
		// 设置要更新的值
		BeanUtils.copyProperties(publishURL, publishURLVO);
	}
	
	public void removePublishURL(Long id) throws Exception {
		this.removePublishURL(new Long[] { id });
	}
	
	public void removePublishURL(Long[] ids) throws Exception {
		for (int i = 0; i < ids.length; i++) {
			PublishURL publishURL = PublishURL.load(PublishURL.class, ids[i]);
			publishURL.remove();
		}
	}
	
	public List<PublishURLVO> findAllPublishURL() throws Exception {
		List<PublishURLVO> list = new ArrayList<PublishURLVO>();
		List<PublishURL> all = PublishURL.findAll(PublishURL.class);
		for (PublishURL publishURL : all) {
			PublishURLVO publishURLVO = new PublishURLVO();
			// 将domain转成VO
			BeanUtils.copyProperties(publishURLVO, publishURL);
			list.add(publishURLVO);
		}
		return list;
	}
	
	public Page<PublishURLVO> pageQueryPublishURL(PublishURLVO queryVo, int currentPage, int pageSize) throws Exception {
		List<PublishURLVO> result = new ArrayList<PublishURLVO>();
		List<Object> conditionVals = new ArrayList<Object>();
						
	   	boolean hasWhere = false;
	   	StringBuilder jpql = new StringBuilder("select _publishURL from PublishURL _publishURL ");
	   		   		   		   		   		
	   	if (queryVo.getName() != null && !"".equals(queryVo.getName())) {
	   		hasWhere = appendWhereIfNeed(jpql, hasWhere);
	   		jpql.append("_publishURL.name like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getName()));
	   	}		
	   		   		   		   		   		   		   		
	   	if (queryVo.getUrl() != null && !"".equals(queryVo.getUrl())) {
	   		hasWhere = appendWhereIfNeed(jpql, hasWhere);
	   		jpql.append("_publishURL.url like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getUrl()));
	   	}		
	   		   		   		   		   		   		   		   	
        Page<PublishURL> pages = queryChannel().queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pageSize);
        for (PublishURL publishURL : pages.getResult()) {
            PublishURLVO publishURLVO = new PublishURLVO();
            // 将domain转成VO
            BeanUtils.copyProperties(publishURLVO, publishURL);
            																					
            result.add(publishURLVO);
        }
        return new Page<PublishURLVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}
	
	
	/**
	 * 根据需要添加where语句或and语句
	 * @param jpql
	 * @param hasWhere
	 * @return
	 */
	private boolean appendWhereIfNeed(StringBuilder jpql, boolean hasWhere) {
		if (hasWhere == false) {
			jpql.append(" where ");
			return true;
		}
		jpql.append(" and ");
		return false;
	}
}
