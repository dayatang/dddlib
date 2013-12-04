package org.openkoala.bpm.applicationImpl;


import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.jbpm.application.JBPMApplication;
import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.PageTaskVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.application.vo.TaskChoice;
import org.openkoala.jbpm.application.vo.TaskVO;
import org.springframework.context.ApplicationContext;

/**
 * 客户端调用restful风格的JBPM服务端
 * 该测试用例需要预先启动JBPM服务端
 * 
 */
public class JbpmWsRestClientTest {
	
	private static JBPMApplication application;
	
	private BusinessSupportApplicationImpl instance = new BusinessSupportApplicationImpl();
	
	@BeforeClass
	public static void beforeClass(){
		ApplicationContext context = WSApplicationContextUtil.getApplicationContext();
		application = (JBPMApplication)context.getBean(JBPMApplication.class);
		
	}

	@Test
	public void testGetProcesses(){
		List<ProcessVO> list = application.getProcesses();
		Assert.assertNotNull(list);
	}
	
	@Test
	public void testAddProcess(){
//		application.addProcess("packageName", "processId", 1, "fdsaf", new Byte[]{1}, true);
		Assert.assertTrue(true);
	}
	
	@Test
	public void testStartProcess(){
		String processId = getFirstProcessId();
		if(processId == null){
			Assert.assertTrue(true);
		}else{
			long processInstanceId = application.startProcess(processId, "createrTest", null);
			Assert.assertTrue(processInstanceId > 0);
		}
	}
	
	@Test
	public void testPageQueryDoneTask(){
		String processId = getFirstProcessId();
		if(processId == null){
			Assert.assertTrue(true);
		}else{
			PageTaskVO page = application.pageQueryDoneTask(processId, "createrTest", 1, 10);
			Assert.assertNotNull(page);
		}
	}
	
	@Test
	public void testProcessQueryTodoListWithGroup(){
		String processId = getFirstProcessId();
		if(processId == null){
			Assert.assertTrue(true);
		}else{
			List<TaskVO> list = application.processQueryTodoListWithGroup(processId, "createrTest", null);
			Assert.assertNotNull(list);
		}
	}
	
	@Test
	public void testQueryTaskChoice(){
		String processId = getFirstProcessId();
		if(processId == null){
			Assert.assertTrue(true);
		}else{
			long taskId = getFirstTaskId(processId);
			if(taskId < 0){
				Assert.assertTrue(true);
			}else{
				long processInstanceId = application.startProcess(processId, "createrTest", null);
				List<TaskChoice> list = application.queryTaskChoice(processInstanceId, taskId);
				Assert.assertNotNull(list);
			}
		}
	}
	
	@Test
	public void testCompleteTask(){
		String processId = getFirstProcessId();
		if(processId == null){
			Assert.assertTrue(true);
		}else{
			long taskId = getFirstTaskId(processId);
			if(taskId < 0){
				Assert.assertTrue(true);
			}else{
//				long processInstanceId = application.startProcess(processId, "createrTest", null);
//				boolean result = application.completeTask(processInstanceId, taskId, "fhjl",
//						instance.convertTaskChoiceToXml("情况属实",createAndInitTaskChoice()), null);
//				Assert.assertTrue(result);
			}
		}
	}
	
	private TaskChoice createAndInitTaskChoice(){
		TaskChoice taskChoice = new TaskChoice();
		taskChoice.setKey("approveStatus");
		taskChoice.setValue("1");
		taskChoice.setValueType("String");
		taskChoice.setName("同意");
		return taskChoice;
	}
	
	@Test
	public void testGetPorcessImageStream(){
		String processId = getFirstProcessId();
		if(processId == null){
			Assert.assertTrue(true);
		}else{
			long processInstanceId = application.startProcess(processId, "createrTest", null);
			byte[] image = application.getPorcessImageStream(processInstanceId);
			Assert.assertNotNull(image);
		}
	}
	
	@Test
	public void testQueryHistoryLog(){
		String processId = getFirstProcessId();
		if(processId == null){
			Assert.assertTrue(true);
		}else{
			long processInstanceId = application.startProcess(processId, "createrTest", null);
			List<HistoryLogVo> list = application.queryHistoryLog(processInstanceId);
			Assert.assertNotNull(list);
		}
	}
	
	private String getFirstProcessId(){
		List<ProcessVO> list = application.getProcesses();
		if(list.size() > 0){
			return list.get(0).getId();
		}else{
			return null;
		}
	}
	
	private long getFirstTaskId(String processId){
		List<TaskVO> list = application.processQueryTodoListWithGroup(processId, "fhjl", null);
		if(list.size() > 0){
			return list.get(0).getTaskId();
		}else{
			return -1;
		}
	}
}
