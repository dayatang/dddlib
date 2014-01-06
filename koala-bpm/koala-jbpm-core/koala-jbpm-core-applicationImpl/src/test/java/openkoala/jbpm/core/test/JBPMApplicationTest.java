package openkoala.jbpm.core.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.jbpm.application.JBPMApplication;
import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.JBPMNode;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.application.vo.PageTaskVO;
import org.openkoala.jbpm.application.vo.ProcessInstanceVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.application.vo.TaskChoice;
import org.openkoala.jbpm.application.vo.TaskVO;
import org.openkoala.jbpm.infra.XmlParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.support.Page;
import com.dayatang.spring.factory.SpringInstanceProvider;

/**
 * JBPMApplication的核心测试
 * 
 * @author lingen
 * 
 */
public class JBPMApplicationTest {

	private static Logger logger = LoggerFactory
			.getLogger(JBPMApplicationTest.class);

	public static JBPMApplication jbpmApplication;

	public static boolean init;

	public static JBPMApplication getJBPMApplication() {
		if (jbpmApplication == null) {
			jbpmApplication = InstanceFactory
					.getInstance(JBPMApplication.class);
		}
		TransactionManager t;
		return jbpmApplication;
	}

	@BeforeClass
	public static void publishJbpm() throws IOException {
 		InstanceFactory.setInstanceProvider(new SpringInstanceProvider(
				new String[] { "classpath*:META-INF/spring/root.xml" }));

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
			
			String pngString = new String(png);

			getJBPMApplication().addProcess(packageName, processId, version,
					data, pngString, true);

			init = true;
		}
	}
	
	private static Byte[] convertToByteArray(byte[] pngs){
		Byte[] pngByte = new Byte[pngs.length];
		for(int i=0; i<pngs.length; i++){
			pngByte[i] = Byte.valueOf(pngs[i]);
		}
		return pngByte;
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
	 * 启动一个流程
	 */
	@Test
	public void testStartProcesses() {
		Map<String,Object> values = new HashMap<String,Object>();
		values.put("creater", "abc");
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", XmlParseUtil.paramsToXml(values));
	   getJBPMApplication().removeProcessInstance(i);

	}

	/**
	 * 查询待办任务
	 */
	@Test
	public void testQueryTodoList() {
		Map<String,Object> values = new HashMap<String,Object>();
		values.put("creater", "abc");
		getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", XmlParseUtil.paramsToXml(values));
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size() > 0);
		//getJBPMApplication().removeProcessInstance(i);
	}
	
	@Test
	public void testQueryTaskChoice(){
		long i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", null);
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		List<TaskChoice> choices = getJBPMApplication().queryTaskChoice(i, tasks.get(0).getTaskId());
		Assert.assertNotNull(choices);
	}
	

	/**
	 * 完成某一个任务
	 */
	@Test
	public void testCompleteTask() {
		
		long i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", null);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		
		
	
		getJBPMApplication().completeTask(i, tasks.get(0).getTaskId(), "fhjl",
				XmlParseUtil.paramsToXml(data), null);
		tasks = getJBPMApplication().queryTodoList("fwzy");
		Assert.assertTrue(tasks.size() > 0);
		
		//getJBPMApplication().removeProcessInstance(i);

	}

	/**
	 * 某询已办任务
	 */
	@Test
	public void testQueryDoenTask() {
		long  i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", null);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		
		List<TaskVO> tasks = getJBPMApplication().processQueryTodoListWithGroup("defaultPackage.Trade","fhjl",null);

		getJBPMApplication().completeTask(i, tasks.get(0).getTaskId(), "fhjl",
				XmlParseUtil.paramsToXml(data), null);
		tasks = getJBPMApplication().queryDoenTask("fhjl");
		Assert.assertTrue(tasks.size() > 0);
		//getJBPMApplication().removeProcessInstance(i);
	}
	
	/**
	 * 某询已办任务
	 */
	@Test
	public void testPageQueryDoenTask() {
		Map<String,Object> abc  = new HashMap<String,Object>();
		abc.put("a1", 456);
		long  i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", XmlParseUtil.paramsToXml(abc));

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");

		getJBPMApplication().completeTask(i, tasks.get(0).getTaskId(), "fhjl",
				XmlParseUtil.paramsToXml(data), null);
		PageTaskVO pages = getJBPMApplication().pageQueryDoneTask("defaultPackage.Trade","fhjl", 1, 10);
		Assert.assertTrue(pages.getTotalCount()>0);
		getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 查询一个流程的历史任务
	 */
	@Test
	public void testQueryHistoryLog() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", null);
		List<HistoryLogVo> vos = getJBPMApplication().queryHistoryLog(i);
		Assert.assertTrue(vos.size() > 0);
		//getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 查询一个流程上的所有人工节点
	 */
	@Test
	public void testGetProcessNodes() {
		List<JBPMNode> nodes = getJBPMApplication().getProcessNodes(
				"defaultPackage.Trade");

		Assert.assertTrue(nodes.size() > 0);

	}

	@Test
	public void testGetProcessNodesFromPorcessInstnaceId() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", null);
		List<JBPMNode> nodes = getJBPMApplication()
				.getProcessNodesFromPorcessInstnaceId(i);
		for (JBPMNode node : nodes) {
			logger.debug(node.getName() + ":" + node.getId());
		}
		Assert.assertTrue(nodes.size() > 0);
		//getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 将节点转移到2 分行经理审批上
	 */
	@Test
	public void testAssignToNode() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", null);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(i, 1l, "fhjl",
				XmlParseUtil.paramsToXml(data), null);
		
		// [id=2, name=分行经理审批]
		getJBPMApplication().assignToNode(i, 2l);
		
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size() > 0);
		
		data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(i, tasks.get(0).getTaskId(), "fhjl",
				XmlParseUtil.paramsToXml(data), null);
		
		tasks = getJBPMApplication().queryTodoList("fwzy");
		Assert.assertTrue(tasks.size() > 0);
//		
		//getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 查询当前激活的流程
	 */
	@Test
	public void testQueryAllActiveProcess() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", null);
		List<ProcessInstanceVO> vos = getJBPMApplication()
				.queryAllActiveProcess("defaultPackage.Trade");
		Assert.assertTrue(vos.size() > 0);
		getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 查询一个流程下的所有部署的版本
	 */
	@Test
	public void testGetProcessVersionByProcessId() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", null);
		List<KoalaProcessInfoVO> vos = getJBPMApplication()
				.getProcessVersionByProcessId("defaultPackage.Trade");
		Assert.assertTrue(vos.size() > 0);
		getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 查询正在运行的流程
	 */
	@Test
	public void testQueryRunningProcessInstances() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", null);
		Page<ProcessInstanceVO> vos = getJBPMApplication()
				.queryRunningProcessInstances("defaultPackage.Trade", "1", 0,
						100);
		Assert.assertTrue(vos.getResult().size() > 0);
		getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 查询已经运行完成的流程
	 */
	@Test
	public void testQueryCompleteProcessInstances() {
		Page<ProcessInstanceVO> vos = getJBPMApplication()
				.queryCompleteProcessInstances("defaultPackage.Trade", "1", 0,
						10);
		Assert.assertTrue(vos.getResult().size() == 0);
	}

	/**
	 * 查询一个流程实例
	 */
	@Test
	public void testGetProcessInstance() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", null);
		ProcessInstanceVO vo = getJBPMApplication().getProcessInstance(i);
		Assert.assertTrue(vo.getProcessId().equals("defaultPackage.Trade"));
		getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 委托代理
	 */
	@Test
	public void testDelegate() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", null);
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		for (TaskVO task : tasks) {
			getJBPMApplication().delegate(task.getTaskId(), "fhjl", "aaa");
		}
		List<TaskVO> aaaTasks = getJBPMApplication().queryTodoList("aaa");
		Assert.assertTrue(aaaTasks.size() > 0);
		getJBPMApplication().removeProcessInstance(i);

	}

	/**
	 * 测试回退一个流程
	 */
	@Test
	public void testRoolBack() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", null);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(i, 1l, "fhjl",
				XmlParseUtil.paramsToXml(data), null);

		getJBPMApplication().roolBack(i, 0, "fwzy");
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size() > 0);
		//getJBPMApplication().removeProcessInstance(i);
	}

	@Test
	public void testFetchBack() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		long i = getJBPMApplication().startProcess("defaultPackage.Trade", "aaa", null);
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		getJBPMApplication().completeTask(i,  tasks.get(0).getTaskId(), "fhjl",
				XmlParseUtil.paramsToXml(data), null);

		getJBPMApplication().fetchBack(i, 0l, "fhjl");
		tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size() > 0);
		//getJBPMApplication().removeProcessInstance(i);
	}

	/**
	 * 测试回退一个流程
	 */
	@Test
	public void testRoolBack2() {
		long i = getJBPMApplication().startProcess("defaultPackage.Trade",
				"aaa", null);
		List<TaskVO> tasks = getJBPMApplication().queryTodoList("fhjl");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("approveStatus", "1");
		getJBPMApplication().completeTask(i, tasks.get(0).getTaskId(), "fhjl",
				XmlParseUtil.paramsToXml(data), null);

		getJBPMApplication().roolBack(i, 0, "fwzy");
		tasks = getJBPMApplication().queryTodoList("fhjl");
		Assert.assertTrue(tasks.size() > 0);
		//getJBPMApplication().removeProcessInstance(i);
	}
	
}


