package org.openkoala.jbpm.application;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.JBPMNode;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.application.vo.PageTaskVO;
import org.openkoala.jbpm.application.vo.ProcessInstanceVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.application.vo.TaskChoice;
import org.openkoala.jbpm.application.vo.TaskVO;

import com.dayatang.querychannel.support.Page;

/**
 * 流程接口
 * 
 * @author lingen
 * 
 */
@WebService
public interface JBPMApplication {
	
	/**
	 * 返回所有流程定义
	 * 
	 * @return
	 */
	@GET
	@Path("processes")
	public List<ProcessVO> getProcesses();
	
	/**
	 * 新增一个流程
	 * 
	 * @param processId
	 * @param version
	 * @param data
	 * @param png
	 */
	@POST
	@Path("process")
	@Produces({"application/xml", "application/json"})
	public void addProcess(@FormParam("packageName")String packageName, @FormParam("processId")String processId, 
			@FormParam("version")int version, @FormParam("data")String data, 
			@FormParam("png")String png, @FormParam("isActive")boolean isActive);

	/**
	 * 发起一个流程
	 * @param processId 流程 ID
	 * @param creater   流程的创建者
	 * @param params    流程的初始化参数，以 XML格式
	 * <params>
	 *      <creater>lingen</creater> 
	 *      <isOpen>true</isOpen> 
	 * </params>
	 * @return
	 */
	@POST
	@Path("processInstance")
	@Produces({"application/xml", "application/json"})
	public long startProcess(@FormParam("processId")String processId, @FormParam("creater")String creater, @FormParam("params")String params);
	
	/**
	 * 分页查询已办任务
	 * @param user
	 * @param firstRow
	 * @param pageSize
	 * @return
	 */
	@GET
	@Path("doneTask/{userName}/{processId}")
	public PageTaskVO pageQueryDoneTask(@PathParam("processId")String processId, @PathParam("userName")String userName, @QueryParam("currentPage")int currentPage, @QueryParam("pageSize")int pageSize);
	
	/**
	 * 按流程查询待办任务
	 * @param process
	 * @param user
	 * @param groups
	 * @return
	 */
	@GET
	@Path("todoTask/{userName}/{processId}")
	public List<TaskVO> processQueryTodoListWithGroup(@PathParam("processId") String processId, @PathParam("userName") String userName, @QueryParam("groupName") String groupName);
	
	/**
	 * 查询一个流程任务的选择策略
	 * @param processInstanceId
	 * @param taskId
	 * @return
	 */  
	@GET
	@Path("choice/{processInstanceId}/{taskId}")
	public List<TaskChoice> queryTaskChoice(@PathParam("processInstanceId") long processInstanceId, @PathParam("taskId") long taskId);

	/**
	 * 完成一个工作
	 * 
	 * @param user
	 *            完成的用户
	 * @param taskId
	 *            任务ID
	 * @param params
	 *            流程级参数
	 * @param data
	 *            节点级参数 流程级参数与节点级参数均以XML形式传入，如 <params>
	 *            <creater>lingen</creater> <isOpen>true</isOpen> </params>
	 */
	@PUT
	@Path("task/{userName}/{processInstanceId}/{taskId}")
	public boolean completeTask(@PathParam("processInstanceId") long processInstanceId, 
			@PathParam("taskId") long taskId, @PathParam("userName") String userName,
			@FormParam("params") String params, @FormParam("data") String data);
	
	/**
	 * 返回一个流程图片
	 * 
	 * @param instanceId
	 * @return
	 */
	@GET
	@Path("processInstanceImage/{processInstanceId}")
	public byte[] getPorcessImageStream(@PathParam("processInstanceId") long processInstanceId);

	/**
	 * 返回流程定义的流程图
	 * 
	 * @param processId
	 * @return
	 */
	@GET
	@Path("processImage/{processId}")
	public byte[] getProcessImage(@PathParam("processId")String processId);
	
	/**
	 * 查询一个流程的历史记录
	 * 
	 * @param processId
	 * @return
	 */
	@GET
	@Path("history/{processInstanceId}")
	public List<HistoryLogVo> queryHistoryLog(@PathParam("processInstanceId") long processInstanceId);
	
	/**
	 * 查询一个用户的待办任务
	 * @param user     用户名
	 * @param groups   用户所属的group,group可以是角色，部门，岗位或其它，由业务决定 ，以 XML格式
	 * <params>
	 *      <creater>lingen</creater> 
	 *      <isOpen>true</isOpen> 
	 * </params>
	 * @return
	 */
	@GET
	@Path("todoTasks")
	public List<TaskVO> queryTodoListWithGroup(@QueryParam("userName")String userName, @QueryParam("groupName")String groupName);
	
	/**
	 * 查询一个用户的待办任务
	 * @param user     用户名
	 * @param groups   用户所属的group,group可以是角色，部门，岗位或其它，由业务决定 ，以 XML格式
	 * <params>
	 *      <creater>lingen</creater> 
	 *      <isOpen>true</isOpen> 
	 * </params>
	 * @return
	 */
	@GET
	@Path("todoTasks/{user}")
	public List<TaskVO> queryTodoList(@PathParam("user")String user);

	/**
	 * 查询一个用户的已完成任务
	 * 
	 * @param user
	 * @return
	 */
	@GET
	@Path("doneTasks/{user}")
	public List<TaskVO> queryDoenTask(@PathParam("user")String user);

	/**
	 * 返回错误的流程列表
	 * 
	 * @return
	 */
	@GET
	@Path("errorProcesses")
	public List<TaskVO> queryErrorList();

	/**
	 * 返回一个流程定义的所有人工节点
	 * 
	 * @param processId
	 * @return
	 */
	@GET
	@Path("processNodes/{processId}")
	public List<JBPMNode> getProcessNodes(@PathParam("processId")String processId);

	/**
	 * 返回一个流程实例的所有人工节点
	 * 
	 * @param processInstanceId
	 * @return
	 */
	@GET
	@Path("processInstanceNodes/{processInstanceId}")
	public List<JBPMNode> getProcessNodesFromPorcessInstnaceId(
			@PathParam("processInstanceId")long processInstanceId);

	/**
	 * 查询一个流程下的所有活动流程实例信息
	 * 
	 * @param processId
	 * @return
	 */
	@GET
	@Path("processInstances/{processId}")
	public List<ProcessInstanceVO> queryAllActiveProcess(@PathParam("processId")String processId);

	/**
	 * 查询一个流程下所部署的所有版本
	 * @param processId  流程的processId
	 * @return
	 */
	@GET
	@Path("ProcessVersions/{processId}")
	public List<KoalaProcessInfoVO> getProcessVersionByProcessId(
			@PathParam("processId")String processId);

	/**
	 * 查询所有正在运行的流程
	 * 
	 * @param processId
	 * @return
	 */
	@GET
	@Path("runningProcessInstances/{processId}/{versionNum}")
	public Page<ProcessInstanceVO> queryRunningProcessInstances(
			@PathParam("processId")String processId, @PathParam("versionNum")String versionNum,
			@QueryParam("firstRow")long firstRow, @QueryParam("endRow")int endRow);

	/**
	 * 查询已完成流程
	 * 
	 * @param processId
	 * @return
	 */
	@GET
	@Path("completedProcessInstances/{processId}/{versionNum}")
	public Page<ProcessInstanceVO> queryCompleteProcessInstances(
			@PathParam("processId")String processId, @PathParam("versionNum")String versionNum,
			@QueryParam("firstRow")long firstRow, @QueryParam("endRow")int endRow);

	/**
	 * 查询一个流程的实例信息
	 * 
	 * @param processInstaceId
	 * @return
	 */
	@GET
	@Path("processInstance/{processInstaceId}")
	public ProcessInstanceVO getProcessInstance(@PathParam("processInstaceId")long processInstaceId);

	/**
	 * 修复错误流程
	 * 
	 * @param taskId
	 */
	@PUT
	@Path("taskRepair/{taskId}")
	public void repairTask(@PathParam("taskId")long taskId);

	@GET
	@Path("processesByName/{processName}")
	public List<ProcessVO> getProcessesByProcessName(@PathParam("processName")String processName);

	@GET
	@Path("processesByPackage/{packageName}")
	public List<String> getProcessByPackage(String packageName);

	@GET
	@Path("packages")
	public List<String> getPakcages();

	/**
	 * 移除一个流程实例
	 * 
	 * @param processInstanceId
	 */
	@DELETE
	@Path("processInstance/{processInstanceId}")
	public void removeProcessInstance(long processInstanceId);

	/**
	 * 新增一个全局变量
	 * 
	 * @param key
	 *            全局变量的key值
	 * @param value
	 *            全局变量的value
	 * @param type
	 *            全局变量的类型 支持以下变量类型： STRING 字符型 BOOLEAN boolean型 INT 整形 LONG 长整形
	 *            DOUBLE 浮点型
	 */
	@POST
	@Path("globalVariable")
	public void setGlobalVariable(@FormParam("key")String key, 
			@FormParam("value")String value,@FormParam("type")String type);

	/**
	 * 删除一个全局变量
	 * 
	 * @param key
	 */
	@DELETE
	@Path("globalVariable/{key}")
	public void removeGlobalVariable(@PathParam("key")String key);

	/**
	 * 新增一个Package级的变量
	 * 
	 * @param packageName
	 *            package变量的名称
	 * @param key
	 *            package变量的key值
	 * @param value
	 *            package变量的value
	 * @param type
	 *            package变量的类型 支持以下变量类型： STRING 字符型 BOOLEAN boolean型 INT 整形 LONG
	 *            长整形 DOUBLE 浮点型
	 */
	@POST
	@Path("packageVariable")
	public void setPackageVariable(@FormParam("packageName")String packageName, @FormParam("key")String key,
			@FormParam("value")String value, @FormParam("type")String type);

	/**
	 * 移除一个Package变量
	 * 
	 * @param packageName
	 * @param key
	 */
	@DELETE
	@Path("packageVariable/{packageName}/{key}")
	public void removePackageVariable(@PathParam("packageName")String packageName, @PathParam("key")String key);

	/**
	 * 添加一个流程定义级的变量
	 * 
	 * @param packageName
	 *            流程定义所有的包名
	 * @param processId
	 *            流程定义的名称
	 * @param key
	 *            变量的KEY值
	 * @param value
	 *            变量的VALUE值
	 * @param type
	 *            变量的类型 支持以下变量类型： STRING 字符型 BOOLEAN boolean型 INT 整形 LONG 长整形
	 *            DOUBLE 浮点型
	 */
	@POST
	@Path("processVariable")
	public void setProcessVariable(@FormParam("processId")String processId, @FormParam("key")String key, 
			@FormParam("value")String value, @FormParam("type")String type);

	/**
	 * 移除一个流程定义级的变量
	 * 
	 * @param packageName
	 * @param processId
	 * @param key
	 */
	@DELETE
	@Path("processVariable/{processId}/{key}")
	public void removeProcessVariable(@PathParam("processId")String processId, @PathParam("key")String key);

	/**
	 * 退回功能，当前节点用户可进行退回
	 * 
	 * @param processInstanceId
	 * @param userId
	 * @param userId
	 */
	public void roolBack(long processInstanceId, long taskId, String userId);

	/**
	 * 取回功能，如果下一步的人员未进行任何操作，前一步的用户可以主动取回这个任务，重新决策
	 * 
	 * @param TaskId
	 * @param userId
	 */
	public void fetchBack(long processInstanceId, long taskId, String userId);
	
	/**
	 * 流程节点的人工转移
	 * 
	 * @param processInstanceId
	 * @param nodeId
	 */
	public void assignToNode(long processInstanceId, long nodeId);

	/**
	 * 流程委托
	 * 
	 * @param taskId
	 *            任务ID
	 * @param userId
	 *            委托者
	 * @param targetUserId
	 *            被委托者
	 */
	public void delegate(long taskId, String userId, String targetUserId);

	/**
	 * 设置流程实例变量值
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @param key
	 *            流程实例变量KEY值
	 * @param value
	 *            流程实例变量VALUE值
	 * @param type
	 *            流程实例变量类型
	 */
	public void setProcessInstanceVariable(long processInstanceId, String key,
			String value, String type);

	/**
	 * 查询一个流程下的所有流程实例信息
	 * 
	 * @param processId
	 * @return
	 */
	@Deprecated
	public List<ProcessInstanceVO> queryAllProcess(String processId);
}
