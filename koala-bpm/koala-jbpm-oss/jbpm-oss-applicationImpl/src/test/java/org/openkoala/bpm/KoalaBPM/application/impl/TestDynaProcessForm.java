package org.openkoala.bpm.KoalaBPM.application.impl;

import org.junit.Test;
import org.openkoala.bpm.processdyna.core.DynaProcessForm;
import org.openkoala.bpm.processdyna.core.DynaProcessKey;
import org.openkoala.bpm.processdyna.core.DynaType;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.util.Assert;

/**
 * DynaProcessForm的测试类
 * @author lingen
 *
 */
public class TestDynaProcessForm extends KoalaBaseSpringTestCase{
	
	/**
	 * 测试新增加一个流程的表单自定义，这个新增的表单自定义
	 */
	@Test
	public void testAddNewDynaProcessForm(){
		DynaProcessKey apply = new DynaProcessKey("apply_id","申请人",DynaType.Text.name());
		DynaProcessKey date = new DynaProcessKey("date","申请日期",DynaType.Date.name());
		DynaProcessKey comment = new DynaProcessKey("commnet","申请理由",DynaType.TextArea.name());
		
		DynaProcessForm form1 =new DynaProcessForm("APPLY","请假单");
		form1.addDynaProcessKey(apply);
		form1.addDynaProcessKey(date);
		form1.addDynaProcessKey(comment);
		form1.save();
		DynaProcessForm from1 =  DynaProcessForm.queryActiveDynaProcessFormByProcessId("APPLY");
		Assert.isTrue(from1.getBizName().equals(from1.getBizName()));
	}

}
