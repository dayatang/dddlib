package org.openkoala.bpm.processdyna.core;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;

/**
 * 
 * @author lingen
 *
 */
public class TestDynaProcessValue extends KoalaBaseSpringTestCase {

	/**
	 * 测试从流程实例获取业务表单的值
	 */
	@Test
	public void testqueryDynaProcessValueByProcessInstanceId(){
		DynaProcessForm form1 =new DynaProcessForm("APPLY","请假单");
		form1.save();
		
		DynaProcessKey apply = new DynaProcessKey("apply_id","申请人",DynaType.Text.name());
		apply.setDynaTable(form1);
		DynaProcessKey date = new DynaProcessKey("date","申请日期",DynaType.Date.name());
		date.setDynaTable(form1);
		DynaProcessKey comment = new DynaProcessKey("commnet","申请理由",DynaType.TextArea.name());
		comment.setDynaTable(form1);
		
		apply.save();
		date.save();
		comment.save();
		
		DynaProcessValue value1 = new DynaProcessValue(1001l,apply,"lingen");
		value1.save();
		
		DynaProcessValue value2 = new DynaProcessValue(1001l,date,"2013-10-08");
		value2.save();
		
		DynaProcessValue value3 = new DynaProcessValue(1001l,comment,"我同意了");
		value3.save();
		
		List<DynaProcessValue> values = DynaProcessValue.queryDynaProcessValueByProcessInstanceId(1001l);
		for(DynaProcessValue value:values){
			Assert.assertEquals(value.getDynaProcessKey().getDynaTable().getBizName(), "请假单");
		}
		
	}
}
