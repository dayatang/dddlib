package org.openkoala.jbpm.core;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table
public class HistoryLog extends AbstractEntity {


	private static final long serialVersionUID = -2409238992583642261L;
	
	private String processId;

	private long processInstanceId;
	
	private String user;//用户
	
	private Date createDate;//日期
	
	private String nodeName;//步骤名称
	
	private long nodeId;
	
	private String result;//审批结果
	
	private String comment;//备注
	
	@Lob
	private String processData;//流程级参数
	
	@Lob
	private String taskData;//节点级参数
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessData() {
		return processData;
	}

	public void setProcessData(String processData) {
		this.processData = processData;
	}

	public String getTaskData() {
		return taskData;
	}

	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + (int) (nodeId ^ (nodeId >>> 32));
		result = prime * result
				+ ((nodeName == null) ? 0 : nodeName.hashCode());
		result = prime * result
				+ ((processData == null) ? 0 : processData.hashCode());
		result = prime * result
				+ (int) (processInstanceId ^ (processInstanceId >>> 32));
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result
				+ ((taskData == null) ? 0 : taskData.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryLog other = (HistoryLog) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (nodeId != other.nodeId)
			return false;
		if (nodeName == null) {
			if (other.nodeName != null)
				return false;
		} else if (!nodeName.equals(other.nodeName))
			return false;
		if (processData == null) {
			if (other.processData != null)
				return false;
		} else if (!processData.equals(other.processData))
			return false;
		if (processInstanceId != other.processInstanceId)
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (taskData == null) {
			if (other.taskData != null)
				return false;
		} else if (!taskData.equals(other.taskData))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	public void save(){
		this.processId = this.processId.substring(0,this.processId.indexOf("@"));
		super.save();
	}

	@Override
	public String toString() {
		return "HistoryLog [processInstanceId=" + processInstanceId + ", user="
				+ user + ", createDate=" + createDate + ", nodeName="
				+ nodeName + ", nodeId=" + nodeId + ", result=" + result
				+ ", comment=" + comment + ", processData=" + processData
				+ ", taskData=" + taskData + "]";
	}
	
	
	/**
	 * 查询一个流程的最后一个待办人及使用者
	 * @param processInstanceId
	 * @return
	 */
	public static HistoryLog queryLastActivedNodeId(long processInstanceId){
		String jpql = "from HistoryLog where processInstanceId = ? order by id desc";
		List<HistoryLog> historys = HistoryLog.getRepository().find(jpql, new Object[]{processInstanceId}, HistoryLog.class);
		if(historys==null){
			return null;
		}
		return historys.get(0);
	}

	
}