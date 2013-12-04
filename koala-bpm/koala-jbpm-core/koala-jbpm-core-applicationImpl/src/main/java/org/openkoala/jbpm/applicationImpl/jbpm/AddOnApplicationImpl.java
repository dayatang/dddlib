package org.openkoala.jbpm.applicationImpl.jbpm;

import javax.inject.Named;

import org.openkoala.jbpm.application.AddOnApplication;
import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.core.HistoryLog;
import org.springframework.beans.BeanUtils;

@Named("addOnApplication")
public class AddOnApplicationImpl implements AddOnApplication {

	public void jbpmLog(HistoryLogVo log) {
		HistoryLog history  = new HistoryLog();
		BeanUtils.copyProperties(log, history);
		history.save();
	}

}
