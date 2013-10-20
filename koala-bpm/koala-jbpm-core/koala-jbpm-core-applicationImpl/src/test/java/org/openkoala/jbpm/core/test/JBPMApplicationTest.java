package org.openkoala.jbpm.core.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.jbpm.application.JBPMApplication;
import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.JBPMNode;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.application.vo.ProcessInstanceVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.application.vo.TaskVO;
import org.openkoala.jbpm.infra.XmlParseUtil;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.support.Page;

/**
 * JBPMApplication的核心测试
 * 
 * @author lingen
 * 
 */
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class JBPMApplicationTest extends KoalaBaseSpringTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(JBPMApplicationTest.class);

	public JBPMApplication jbpmApplication;

	public boolean init;

	public JBPMApplication getJBPMApplication() {
		if (jbpmApplication == null) {
			jbpmApplication = InstanceFactory
					.getInstance(JBPMApplication.class);
		}
		return jbpmApplication;
	}

	@Before
	public void publishJbpm() throws IOException {
		if (init == false) {
			String packageName = "defaultPackage";

			String processId = "defaultPackage.Trade";

			int version = 1;

			String data = FileUtils.readFileToString(new File(
					JBPMApplicationTest.class.getClassLoader()
							.getResource("json.txt").getPath()));

			byte[] png = FileUtils.readFileToByteArray(new File(
					JBPMApplicationTest.class.getClassLoader()
							.getResource("defaultPackage.Trade-image.png")
							.getPath()));

			getJBPMApplication().addProcess(packageName, processId, version,
					data, png, true);

			init = true;
		}

	}

	/**
	 * 查询流程引擎中的所有流程
	 */
	@Test
	public void testGetProcesses() {

		List<ProcessVO> processes = getJBPMApplication().getProcesses();
		Assert.assertTrue(processes.size() == 1);

	}
	
	/**
	 * 测试回退一个流程
	 */
	@Test
	public void testRoolBack(){
		long i = getJBPMApplication()
				.startProcess("defaultPackage.Trade","aaa",null);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(1l, 1l, "fhjl",
				XmlParseUtil.paramsToXml(data), null);

		getJBPMApplication().roolBack(i, 0, "fwzy");
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size()==1);
	}
	
	@Test
	public void testFetchBack(){
		long i = getJBPMApplication()
				.startProcess("defaultPackage.Trade","aaa",null);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(1l, 1l, "fhjl",
				XmlParseUtil.paramsToXml(data), null);

		getJBPMApplication().fetchBack(1l, 0l, "fhjl");
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size()==1);
	}
	
	/**
	 * 测试回退一个流程
	 */
	@Test
	public void testRoolBack2(){
		long i = getJBPMApplication()
				.startProcess("defaultPackage.Trade","aaa",null);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(1l, 1l, "fhjl",
				XmlParseUtil.paramsToXml(data), null);

		getJBPMApplication().roolBack(i, 0, "fwzy");
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size()==1);
	}

	/**
	 * 启动一个流程
	 */
	@Test
	public void testStartProcesses() {

		long i = getJBPMApplication()
				.startProcess("defaultPackage.Trade","aaa",null);
		Assert.assertTrue(i == 1);

	}

	/**
	 * 查询待办任务
	 */
	@Test
	public void testQueryTodoList() {

		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size() > 0);

	}

	/**
	 * 完成某一个任务
	 */
	@Test
	public void testCompleteTask() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(1l, 1l, "fhjl",
				XmlParseUtil.paramsToXml(data), null);
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fwzy");
		Assert.assertTrue(tasks.size() > 0);

	}

	/**
	 * 某询已办任务
	 */
	@Test
	public void testQueryDoenTask() {
		List<TaskVO> tasks = getJBPMApplication().queryDoenTask("fhjl");
		Assert.assertTrue(tasks.size() > 0);
	}

	/**
	 * 查询一个流程的历史任务
	 */
	@Test
	public void testQueryHistoryLog() {
		List<HistoryLogVo> vos = getJBPMApplication().queryHistoryLog(1l);
		Assert.assertTrue(vos.size() > 0);
	}

	/**
	 * 查询一个流程上的所有人工节点
	 */
	@Test
	public void testGetProcessNodes() {
		List<JBPMNode> nodes = getJBPMApplication().getProcessNodes(
				"defaultPackage.Trade");
		
		Assert.assertTrue(nodes.size()>0);
		
	}
	
	@Test
	public void testGetProcessNodesFromPorcessInstnaceId(){
		long i = getJBPMApplication()
				.startProcess("defaultPackage.Trade","aaa",null);
		List<JBPMNode> nodes = getJBPMApplication().getProcessNodesFromPorcessInstnaceId(i);
		for(JBPMNode node:nodes){
			logger.debug(node.getName()+":"+node.getId());
		}
		Assert.assertTrue(nodes.size()>0);
	}
	
	/**
	 * 将节点转移到2 分行经理审批上
	 */
	@Test
	public void testAssignToNode(){
		//[id=2, name=分行经理审批]
		getJBPMApplication().assignToNode(1l, 2l);
		testQueryTodoList();
	}
	
	/**
	 * 查询当前激活的流程
	 */
	@Test
	public void testQueryAllActiveProcess(){
		List<ProcessInstanceVO> vos = getJBPMApplication().queryAllActiveProcess("defaultPackage.Trade");
		Assert.assertTrue(vos.size()==1);
	}
	
	/**
	 * 查询一个流程下的所有部署的版本
	 */
	@Test
	public void testGetProcessVersionByProcessId(){
		 List<KoalaProcessInfoVO> vos = getJBPMApplication().getProcessVersionByProcessId("defaultPackage.Trade");
		 Assert.assertTrue(vos.size()>0);
	}
	
	/**
	 * 查询正在运行的流程
	 */
	@Test
	public void testQueryRunningProcessInstances(){
		Page<ProcessInstanceVO> vos = getJBPMApplication().queryRunningProcessInstances("defaultPackage.Trade", "1", 0, 100);
		Assert.assertTrue(vos.getResult().size()==1);
	}
	
	/**
	 * 查询已经运行完成的流程
	 */
	@Test
	public void testQueryCompleteProcessInstances(){
		Page<ProcessInstanceVO> vos = getJBPMApplication().queryCompleteProcessInstances("defaultPackage.Trade", "1", 0, 10);
		Assert.assertTrue(vos.getResult().size()==0);
	}
	
	/**
	 * 查询一个流程实例
	 */
	@Test
	public void testGetProcessInstance(){
		ProcessInstanceVO vo = getJBPMApplication().getProcessInstance(1l);
		Assert.assertTrue(vo.getProcessId().equals("defaultPackage.Trade"));
	}

	/**
	 * 委托代理
	 */
	@Test
	public void testDelegate(){
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		for(TaskVO task:tasks){
			getJBPMApplication().delegate(task.getTaskId(), "fhjl", "aaa");
		}
		List<TaskVO> aaaTasks = getJBPMApplication().queryTodoList("aaa");
		Assert.assertTrue(aaaTasks.size() > 0);
		
	}
}
