package org.openkoala.koala.action.velocity;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.junit.Test;

/**
 * VelocityUtil 测试.
 * @author xmfang
 *
 */
public class VelocityUtilTest {

	@Test
	public void testVmToFile() throws IOException {
		VelocityContext context = new VelocityContext();
		context.put("name", "testname");
		
		String destFilePath = "src/test/resources/test.file";
		File destFile = new File(destFilePath);
		assertFalse(destFile.exists());
		
		VelocityUtil.vmToFile(context, "test.vm", destFilePath);
		assertTrue(destFile.exists());
		assertEquals("testname", getFileContent(destFile));
		
		destFile.delete();
	}

	private String getFileContent(File file) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
        	result.append(temp);
        }
        bufferedReader.close();

        return result.toString();
	}

	@Test
	public void testEvaluateEl() {
		VelocityContext context = new VelocityContext();
		context.put("name", "testname");
		context.put("showName", true);
		
		String elString = "#if ($showName)"
				+ "$name"
				+ "#end";
		assertEquals("testname", VelocityUtil.evaluateEl(elString, context));
	}

	@Test
	public void testIsExpressTrue() {
		VelocityContext context = new VelocityContext();
		context.put("booleanType", true);
		context.put("intType", 1);
		context.put("stringType", "string");
		context.put("floatType", 1.1);

		String trueExpress1 = "$booleanType && $intType == 1 && $stringType == \"string\" && $floatType == 1.1";
		assertTrue(VelocityUtil.isExpressTrue(trueExpress1, context));
		
		String trueExpress2 = "$booleanType && $intType == 1 && $stringType == \"string\" && $floatType > 1";
		assertTrue(VelocityUtil.isExpressTrue(trueExpress2, context));
		
		String errorExpress = "$booleanType && $intType < 1 && $stringType == \"string\" && $floatType > 1";
		assertFalse(VelocityUtil.isExpressTrue(errorExpress, context));
	}

	@Test
	public void testVelocityObjectParse() throws IOException {
		String destFilePath = "src/test/resources/test.file";
		File destFile = new File(destFilePath);
		assertFalse(destFile.exists());
		
		VelocityContext context = new VelocityContext();
		context.put("name", "testname");
		context.put("srcVm", "test.vm");
		context.put("destFile", destFilePath);
		
		VelocityUtil.velocityObjectParse("$srcVm", "$destFile", context);
		assertTrue(destFile.exists());
		assertEquals("testname", getFileContent(destFile));
		
		destFile.delete();
	}

	@Test
	public void testFileCopy() {
		String srcFilePath = "src/test/resources/test.vm";
		String destFilePath = "src/test/resources/testvm.copy";
		File destFile = new File(destFilePath);
		assertFalse(destFile.exists());
		
		VelocityUtil.fileCopy(new File(srcFilePath), destFilePath);
		assertTrue(destFile.exists());
		
		VelocityUtil.fileCopy(new File(destFilePath), srcFilePath);
	}

	@Test
	public void testVelocityDirParse() {
//		fail("Not yet implemented");
		//TODO 未实现
	}

	@Test
	public void testGetVelocityContextList() {
		Person person = new Person("theName", 11);
		List<Object> objects = new ArrayList<Object>();
		objects.add(person);
		
		VelocityContext context = VelocityUtil.getVelocityContext(objects);
		assertEquals(person, context.get(person.getClass().getSimpleName()));
	}

	@Test
	public void testGetVelocityContextMapOfStringObject() {
		Person person = new Person("theName", 11);
		Map<String, Object> objects = new HashMap<String, Object>();
		objects.put("person", person);
		objects.put("name", "testname");
		
		VelocityContext context = VelocityUtil.getVelocityContext(objects);
		assertEquals(person, context.get("person"));
		assertEquals("testname", context.get("name"));
	}

	private class Person {
		private String name;
		private int age;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		Person (String name, int age) {
			this.name = name;
			this.age = age;
		}
	}
	
}
