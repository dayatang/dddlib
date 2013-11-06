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
		Assert.notNull(html);
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
		apply.setValidationType(ValidateRule.Email.name());
		//apply.setValidationExpr("/^[\u0391-\uFFE5]+$/");
		DynaProcessKey date = new DynaProcessKey("date","申请日期",DynaType.Date.name());
		date.setShowOrder(2);
		
		DynaProcessKey type = new DynaProcessKey("type","请假类型",DynaType.DropDown.name());
		type.setShowOrder(3);
		type.setKeyOptions("{'A':'AA','B':'BB'}");
		
		DynaProcessKey tiaoxiu = new DynaProcessKey("tiaoxiu","是否调休",DynaType.Radio.name());
		tiaoxiu.setShowOrder(4);
		tiaoxiu.setKeyOptions("{'是':'Y','否':'N'}");
		
		DynaProcessKey comment = new DynaProcessKey("comment","申请理由",DynaType.TextArea.name());
		comment.setShowOrder(6);
		
		DynaProcessKey options = new DynaProcessKey("comment","附加选项",DynaType.Checkbox.name());
		options.setKeyOptions("{'选项1':'1','选项2':'2'}");
		options.setShowOrder(5);
		
		keys.add(apply);
		keys.add(date);
		keys.add(comment);
		keys.add(type);
		keys.add(tiaoxiu);
		keys.add(options);
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
