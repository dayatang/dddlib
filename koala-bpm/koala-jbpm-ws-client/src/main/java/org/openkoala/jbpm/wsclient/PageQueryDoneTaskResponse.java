
package org.openkoala.jbpm.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pageQueryDoneTaskResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pageQueryDoneTaskResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://application.jbpm.openkoala.org/}pageTaskVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pageQueryDoneTaskResponse", propOrder = {
    "_return"
})
public class PageQueryDoneTaskResponse {

    @XmlElement(name = "return")
    protected PageTaskVO _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link PageTaskVO }
     *     
     */
    public PageTaskVO getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageTaskVO }
     *     
     */
    public void setReturn(PageTaskVO value) {
        this._return = value;
    }

}
