
package org.openkoala.koala.jbpm.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for taskVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="taskVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actualName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="actualOwner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agents" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="creater" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processInstanceId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="processName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taskData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taskId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taskVO", propOrder = {
    "actualName",
    "actualOwner",
    "agents",
    "createDate",
    "creater",
    "lastUpdateDate",
    "processData",
    "processId",
    "processInstanceId",
    "processName",
    "taskData",
    "taskId"
})
public class TaskVO {

    protected String actualName;
    protected String actualOwner;
    protected String agents;
    protected String createDate;
    protected String creater;
    protected String lastUpdateDate;
    protected String processData;
    protected String processId;
    protected long processInstanceId;
    protected String processName;
    protected String taskData;
    protected long taskId;

    /**
     * Gets the value of the actualName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActualName() {
        return actualName;
    }

    /**
     * Sets the value of the actualName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActualName(String value) {
        this.actualName = value;
    }

    /**
     * Gets the value of the actualOwner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActualOwner() {
        return actualOwner;
    }

    /**
     * Sets the value of the actualOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActualOwner(String value) {
        this.actualOwner = value;
    }

    /**
     * Gets the value of the agents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgents() {
        return agents;
    }

    /**
     * Sets the value of the agents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgents(String value) {
        this.agents = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateDate(String value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the creater property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreater() {
        return creater;
    }

    /**
     * Sets the value of the creater property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreater(String value) {
        this.creater = value;
    }

    /**
     * Gets the value of the lastUpdateDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets the value of the lastUpdateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUpdateDate(String value) {
        this.lastUpdateDate = value;
    }

    /**
     * Gets the value of the processData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessData() {
        return processData;
    }

    /**
     * Sets the value of the processData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessData(String value) {
        this.processData = value;
    }

    /**
     * Gets the value of the processId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * Sets the value of the processId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessId(String value) {
        this.processId = value;
    }

    /**
     * Gets the value of the processInstanceId property.
     * 
     */
    public long getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * Sets the value of the processInstanceId property.
     * 
     */
    public void setProcessInstanceId(long value) {
        this.processInstanceId = value;
    }

    /**
     * Gets the value of the processName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Sets the value of the processName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessName(String value) {
        this.processName = value;
    }

    /**
     * Gets the value of the taskData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskData() {
        return taskData;
    }

    /**
     * Sets the value of the taskData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskData(String value) {
        this.taskData = value;
    }

    /**
     * Gets the value of the taskId property.
     * 
     */
    public long getTaskId() {
        return taskId;
    }

    /**
     * Sets the value of the taskId property.
     * 
     */
    public void setTaskId(long value) {
        this.taskId = value;
    }

}
