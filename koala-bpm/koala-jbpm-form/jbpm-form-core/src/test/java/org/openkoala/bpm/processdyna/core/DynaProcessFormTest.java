package org.openkoala.bpm.processdyna.core;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.util.Assert;

/**
 * DynaProcessForm的测试类
 * @author lingen
 *
 */
public class DynaProcessFormTest extends KoalaBaseSpringTestCase{
	
	/**
	 * 流程ID
	 */
	private final String PROCESS_ID = "APPLY";
	
	@Test(expected=ConstraintViolationException.class)
	public void testProcessFormValidation(){
		DynaProcessForm form =new DynaProcessForm();
		form.save();
	}
	
	/**
	 * 测试新增加一个流程的表单自定义，这个新增的表单自定义
	 */
	@Test
	public void testAddNewDynaProcessForm(){
		DynaProcessForm form = this.createAndInitDynaProcessForm();
		
		form.save();
		form = this.assertFormSaveSuccessAndReturnFromExpected(form);
		
		form.remove();
		this.assertFormRemoveSuccess();
	}
	
	@Test
	public void testPackagingHtml(){
		DynaProcessForm form = this.createAndInitDynaProcessForm();
		form.save();
		form = this.assertFormSaveSuccessAndReturnFromExpected(form);
		String html = form.packagingHtml();
		System.out.println("--------html start--------");
		System.out.println(html);
		System.out.println("--------html end--------");
		Assert.isTrue(html.contains("<input type=\"text\"") && html.contains("data-role=\"date\"") && html.contains("textarea"));
		form.remove();
		this.assertFormRemoveSuccess();
	}
	
	private DynaProcessForm createAndInitDynaProcessForm(){
		DynaProcessForm form =new DynaProcessForm(PROCESS_ID,"请假单");
		form.setKeys(this.createAndInitDynaProcessKeySet());
		form.setTemplate(this.createAndInitDynaProcessTemplate());
		return form;
	}
	
	private DynaProcessForm assertFormSaveSuccessAndReturnFromExpected(DynaProcessForm form){
		DynaProcessForm fromExpected =  DynaProcessForm.queryActiveDynaProcessFormByProcessId(PROCESS_ID);
		Assert.isTrue(form.getBizName().equals(fromExpected.getBizName()));
		return fromExpected;
	}
	
	private void assertFormRemoveSuccess(){
		DynaProcessForm fromExpected =  DynaProcessForm.queryActiveDynaProcessFormByProcessId(PROCESS_ID);
		Assert.isNull(fromExpected);
	}
	
	/**
	 * 填充Set
	 * @return
	 */
	private Set<DynaProcessKey> createAndInitDynaProcessKeySet(){
		Set<DynaProcessKey> keys = new HashSet<DynaProcessKey>();
		
		DynaProcessKey apply = new DynaProcessKey("apply_id","申请人",DynaType.Text.name());
		apply.setShowOrder(1);
		DynaProcessKey date = new DynaProcessKey("date","申请日期",DynaType.Date.name());
		date.setShowOrder(2);
		DynaProcessKey comment = new DynaProcessKey("comment","申请理由",DynaType.TextArea.name());
		date.setShowOrder(3);
		
		keys.add(apply);
		keys.add(date);
		keys.add(comment);
		return keys;
	}
	
	/**
	 * 创建两列模版
	 * @return
	 */
	private DynaProcessTemplate createAndInitDynaProcessTemplate(){
		DynaProcessTemplate template = new DynaProcessTemplate();
		template.setTemplateName("two-columns");
		template.setTemplateDescription("两列模版");
		template.setTemplateData("<form class=\"form-horizontal processDetail\" role=\"form\"><#if params??><#assign i=0><#list params?keys as idKey><#if (i+1)%2==1><div class=\"form-group row\"></#if><div class=\"col-lg-5 form-group\"><label class=\"col-lg-4 control-label\">${params[idKey].keyName}:</label><div class=\"col-lg-8\">${params[idKey].widget}</div></div><#if (i+1)%2==0></div></#if><#if (i+1)==size && (i+1)%2==1></div></#if><#assign i=i+1></#list></#if></form>");
		return template;
	}
	
}
