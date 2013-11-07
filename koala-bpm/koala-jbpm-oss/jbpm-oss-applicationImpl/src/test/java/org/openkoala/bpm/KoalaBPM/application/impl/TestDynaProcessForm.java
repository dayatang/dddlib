package org.openkoala.bpm.KoalaBPM.application.impl;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.bpm.application.ProcessFormOperApplication;
import org.openkoala.bpm.application.dto.DynaProcessFormDTO;
import org.openkoala.bpm.application.dto.DynaProcessKeyDTO;
import org.openkoala.bpm.processdyna.core.DynaProcessForm;
import org.openkoala.bpm.processdyna.core.DynaType;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;

/**
 * DynaProcessForm的测试类
 * @author lingen
 *
 */
public class TestDynaProcessForm extends KoalaBaseSpringTestCase{
	
	/*@Mock
	private JBPMApplication jbpmApplication;*/
	
	@Inject
	ProcessFormOperApplication application;
	/**
	 * 测试新增加一个流程的表单自定义，这个新增的表单自定义
	 */
	
	@Before
	public void setUp() throws Exception {
//		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testProcessFormOperApplication(){
		DynaProcessFormDTO form = new DynaProcessFormDTO();
		form.setBizDescription("Test form");
		form.setBizName("testForm");
		form.setProcessId("APPLY_BPM");
		
		DynaProcessKeyDTO apply = new DynaProcessKeyDTO("apply_id","申请人",DynaType.Text.name());
		DynaProcessKeyDTO date = new DynaProcessKeyDTO("date","申请日期",DynaType.Date.name());
		DynaProcessKeyDTO comment = new DynaProcessKeyDTO("commnet","申请理由",DynaType.TextArea.name());
		
		form.getProcessKeys().add(apply);
		form.getProcessKeys().add(date);
		form.getProcessKeys().add(comment);
		
		application.createDynaProcessForm(form);
		
		DynaProcessForm form1 = DynaProcessForm.queryActiveDynaProcessFormByProcessId("APPLY_BPM");
		Assert.assertNotNull(form1);
		
		//修改
		form.setId(form1.getId());
		form.setBizDescription("Test form2222");
		application.createDynaProcessForm(form);
		
		//删除
		application.deleteDynaProcessFormById(new Long[]{form1.getId()});
	}


}
