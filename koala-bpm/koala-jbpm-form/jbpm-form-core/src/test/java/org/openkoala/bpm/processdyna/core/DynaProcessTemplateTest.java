package org.openkoala.bpm.processdyna.core;

import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.util.Assert;

/**
 * DynaProcessTemplate模板领域对象的测试类
 * @author lingen
 *
 */
public class DynaProcessTemplateTest extends KoalaBaseSpringTestCase{
	
	@Test
	public void testSave(){
		String name="测试模板";
		String description = "模板的测试";
		String data = "<body> hello：${user} </body>";
		
		DynaProcessTemplate template = new DynaProcessTemplate(name,description,data);
		
		template.save();
		
		String template2 = DynaProcessTemplate.getTemplateData("测试模板");
		Assert.isTrue(data.equals(template2));
	}
}
