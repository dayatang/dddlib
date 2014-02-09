package org.openkoala.application.impl;

import java.text.MessageFormat;

import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.QueryItemVO;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;

public class BaseImpl {
	private static QueryChannelService queryChannel;

	public static QueryChannelService queryChannel() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_ss");
		}
		return queryChannel;
	}

	protected String genQueryCondition(QueryConditionVO search) {
		StringBuilder result = new StringBuilder(MessageFormat.format("select m from {0} m where 1=1", //
				search.getObjectName()));
		for (QueryItemVO qi : search.getItems()) {
			result.append(MessageFormat.format(" and m.{0} {1} {2}", qi.getPropName(), //
					QueryConditionVO.genOperatorStirng(qi.getOperaType()), qi.getPropValue()));
		}
		result.append(" and m.abolishDate>?");
		return result.toString();
	}

}
