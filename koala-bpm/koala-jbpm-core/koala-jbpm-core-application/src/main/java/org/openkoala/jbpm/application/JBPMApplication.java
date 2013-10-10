package org.openkoala.jbpm.application;

import java.util.List;

import org.openkoala.jbpm.application.vo.HistoryLogVo;
import org.openkoala.jbpm.application.vo.JBPMNode;
import org.openkoala.jbpm.application.vo.KoalaProcessInfoVO;
import org.openkoala.jbpm.application.vo.ProcessInstanceVO;
import org.openkoala.jbpm.application.vo.ProcessVO;
import org.openkoala.jbpm.application.vo.TaskVO;

import com.dayatang.querychannel.support.Page;

import javax.jws.*;

/**
 * 流程接口
 * 
 * @author lingen
 * 
 */
@WebService
public interface JBPMApplication {
	
	/**
	 * 启动一个流程
	 * @param processName 流程的名称
	 * @param params  流程的参数，以XML形式传入,以便兼容各种语言
	 * 如
	 * <params>
	 *    <creater>lingen</creater>
	 *    <isOpen>true</isOpen>
	 * </params>
	 * @return
	 */
	public long startProcess(String processName,String creater,String params);

	
	/**
	 * 查询指定用户的待办任务
	 * @param user
	 * @return
	 */
	public List<TaskVO> queryTodoList(String user);
	
	/**
	 * 完成一个工作
	 * @param user 完成的用户
	 * @param taskId 任务ID
	 * @param params 流程级参数
	 * @param data 节点级参数
	 * 流程级参数与节点级参数均以XML形式传入，如
	 *  <params>
	 *    <creater>lingen</creater>
	 *    <isOpen>true</isOpen>
	 * </params>
	 */
	public boolean completeTask(long processId,long taskId,String user,String params,String data);
	
	/**
	 * 查询一个用户的已完成任务
	 * @param user
	 * @return
	 */
	public List<TaskVO> queryDoenTask(String user);
	
	/**
	 * 查询一个流程的历史记录
	 * @param processId
	 * @return
	 */
	public List<HistoryLogVo> queryHistoryLog(long processId);
	
	/**
	 * 返回错误的流程列表
	 * @return
	 */
	public List<TaskVO> queryErrorList();
	
	
	/**
	 * 返回一个流程定义的所有人工节点
	 * @param processId
	 * @return
	 */
	public List<JBPMNode> getProcessNodes(String processId);
	
	
	/**
	 * 返回一个流程实例的所有人工节点
	 * @param processInstanceId
	 * @return
	 */
	public List<JBPMNode> getProcessNodesFromPorcessInstnaceId(long processInstanceId);
	
	
	/**
	 * 流程节点的人工转移
	 * @param processInstanceId
	 * @param nodeId
	 */
	public void assignToNode(long processInstanceId,long nodeId);
	
	/**
	 * 查询一个流程下的所有活动流程实例信息
	 * @param processId
	 * @return
	 */
	public List<ProcessInstanceVO> queryAllActiveProcess(String processId);
	
	
	
	public List<KoalaProcessInfoVO> getProcessVersionByProcessId(String processId);
	
	/**
	 * 查询所有正在运行的流程
	 * @param processId
	 * @return
	 */
	public Page<ProcessInstanceVO> queryRunningProcessInstances(String processId,String versionNum,long firstRow,int endRow);
	
	
	/**
	 * 查询已完成流程
	 * @param processId
	 * @return
	 */
	public Page<ProcessInstanceVO> queryCompleteProcessInstances(String processId,String versionNum,long firstRow,int endRow);
	
	/**
	 * 查询一个流程的实例信息
	 * @param processInstaceId
	 * @return
	 */
	public ProcessInstanceVO getProcessInstance(long processInstaceId);
	
	/**
	 * 流程委托
	 * @param taskId 任务ID
	 * @param userId 委托者
	 * @param targetUserId 被委托者
	 */
	public void delegate(long taskId,String userId,String targetUserId);
	
	
	/**
	 * 返回流程定义的流程图
	 * @param processId
	 * @return
	 */
	public byte[] getProcessImage(String processId);
	
	/**
	 * 返回一个流程图片
	 * @param instanceId
	 * @return
	 */
	public byte[] getPorcessImageStream(long processInstacenId);
	
	/**
	 * 修复错误流程
	 * @param taskId
	 */
	public void repairTask(long taskId);
	
	/**
	 * 新增一个流程
	 * @param processId
	 * @param version
	 * @param data
	 * @param png
	 */
	public void addProcess(String packageName,String processId,int version,String data,byte[] png,boolean isActive);
	
	/**
	 * 返回所有流程定义
	 * @return
	 */
	public List<ProcessVO> getProcesses();
	
	public List<ProcessVO> getProcessesByProcessName(String processName);
	
	public List<String> getProcessByPackage(String packageName);
	
	public List<String> getPakcages();
	
	/**
	 * 移除一个流程实例
	 * @param processInstanceId
	 */
	public void removeProcessInstance(long processInstanceId);
	
	

	
	/**
	 * 新增一个全局变量
	 * @param key 全局变量的key值
	 * @param value 全局变量的value
	 * @param type  全局变量的类型
	 * 支持以下变量类型：
	 * STRING 字符型
	 * BOOLEAN  boolean型
	 * INT  整形
	 * LONG 长整形
	 * DOUBLE 浮点型
	 */
	public void setGlobalVariable(String key,String value,String type);
	
	/**
	 * 删除一个全局变量
	 * @param key
	 */
	public void removeGlobalVariable(String key);
	
	
	/**
	 * 新增一个Package级的变量
	 * @param packageName package变量的名称
	 * @param key  package变量的key值
	 * @param value   package变量的value
	 * @param type package变量的类型
	 * 支持以下变量类型：
	 * STRING 字符型
	 * BOOLEAN  boolean型
	 * INT  整形
	 * LONG 长整形
	 * DOUBLE 浮点型
	 */
	public void setPackageVariable(String packageName,String key,String value,String type);
	
	
	/**
	 * 移除一个Package变量
	 * @param packageName
	 * @param key
	 */
	public void removePackageVariable(String packageName,String key);
	
	
	/**
	 * 添加一个流程定义级的变量
	 * @param packageName  流程定义所有的包名
	 * @param processId    流程定义的名称
	 * @param key          变量的KEY值
	 * @param value        变量的VALUE值
	 * @param type         变量的类型
	 * 支持以下变量类型：
	 * STRING 字符型
	 * BOOLEAN  boolean型
	 * INT  整形
	 * LONG 长整形
	 * DOUBLE 浮点型
	 */
	public void setProcessVariable(String processId,String key,String value,String type);
	
	/**
	 * 移除一个流程定义级的变量
	 * @param packageName
	 * @param processId
	 * @param key
	 */
	public void removeProcessVariable(String processId,String key);
	
	
	/**
	 * 设置流程实例变量值 
	 * @param processInstanceId 流程实例ID
	 * @param key 流程实例变量KEY值
	 * @param value 流程实例变量VALUE值
	 * @param type 流程实例变量类型
	 */
	public void setProcessInstanceVariable(long processInstanceId,String key,String value,String type);
	
	/**
	 * 查询一个流程下的所有流程实例信息
	 * @param processId
	 * @return
	 */
	@Deprecated
	public List<ProcessInstanceVO> queryAllProcess(String processId);
	
	/**
	 * 退回功能，当前节点用户可进行退回
	 * @param processInstanceId
	 * @param userId
	 * @param userId
	 */
	public void roolBack(long processInstanceId,long taskId,String userId);
	
	/**
	 * 取回功能，如果下一步的人员未进行任何操作，前一步的用户可以主动取回这个任务，重新决策
	 * @param TaskId
	 * @param userId
	 */
	public void fetchBack(long processInstanceId,long taskId,String userId);
	
	

}
