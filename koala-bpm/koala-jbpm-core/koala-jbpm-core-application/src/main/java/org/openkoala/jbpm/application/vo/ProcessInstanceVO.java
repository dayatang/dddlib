package org.openkoala.jbpm.application.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement()
public class ProcessInstanceVO implements Serializable {
	
	private static final long serialVersionUID = 2687088782968240015L;

	private String processId;//流程ID名称
	
	private int versionNum;//当前流程的版本号
	
	private long processInstanceId;//流程实例ID
	
	private String processName;//流程名称
	
	private String createDate;//流程创建日期
	
	private String lastUpdateDate;//流程最后更新时间
	
	private long parentProcessInstanceId;//父流程ID
	
	private int status;//流程状态
	
	private String creater;//流程的创建者
	
	private String data;
	
	
	public ProcessInstanceVO(String processId, Long processInstanceId,
			String processName, Object createDate, Object lastUpdateDate,
			String data) {
		super();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.processId = processId;
		this.processInstanceId = processInstanceId;
		this.processName = processName;
		this.createDate = df.format(createDate);
		if(lastUpdateDate!=null)this.lastUpdateDate = df.format(lastUpdateDate);
		this.data = data;
	}

	public ProcessInstanceVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public long getParentProcessInstanceId() {
		return parentProcessInstanceId;
	}

	public void setParentProcessInstanceId(long parentProcessInstanceId) {
		this.parentProcessInstanceId = parentProcessInstanceId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	/**
	* 字符串转换为Timestamp
	* @param dateStr
	* @return
	*/
	public static Timestamp stringToTimestamp(String dateStr){
	  
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Calendar cal = Calendar.getInstance();
	   try {
	    Date date = sdf.parse(dateStr);
	    date.getTime();
	    cal.setTime(date);
	    return new Timestamp(cal.getTimeInMillis());
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	  
	   cal.setTime(new Date());
	   return new Timestamp(cal.getTimeInMillis());
	}
	
}
