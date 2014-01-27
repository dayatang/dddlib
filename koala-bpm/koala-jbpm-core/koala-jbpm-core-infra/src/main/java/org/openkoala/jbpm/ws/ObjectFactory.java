
package org.openkoala.jbpm.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openkoala.jbpm.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetTaskUser_QNAME = new QName("http://application.jbpmDemo.jbpm.openkoala.org/", "getTaskUser");
    private final static QName _LogMessageResponse_QNAME = new QName("http://application.jbpmDemo.jbpm.openkoala.org/", "logMessageResponse");
    private final static QName _LogMessage_QNAME = new QName("http://application.jbpmDemo.jbpm.openkoala.org/", "logMessage");
    private final static QName _GetTaskUserResponse_QNAME = new QName("http://application.jbpmDemo.jbpm.openkoala.org/", "getTaskUserResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openkoala.jbpm.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LogMessage }
     * 
     */
    public LogMessage createLogMessage() {
        return new LogMessage();
    }

    /**
     * Create an instance of {@link LogMessageResponse }
     * 
     */
    public LogMessageResponse createLogMessageResponse() {
        return new LogMessageResponse();
    }

    /**
     * Create an instance of {@link GetTaskUserResponse }
     * 
     */
    public GetTaskUserResponse createGetTaskUserResponse() {
        return new GetTaskUserResponse();
    }

    /**
     * Create an instance of {@link GetTaskUser }
     * 
     */
    public GetTaskUser createGetTaskUser() {
        return new GetTaskUser();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaskUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://application.jbpmDemo.jbpm.openkoala.org/", name = "getTaskUser")
    public JAXBElement<GetTaskUser> createGetTaskUser(GetTaskUser value) {
        return new JAXBElement<GetTaskUser>(_GetTaskUser_QNAME, GetTaskUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://application.jbpmDemo.jbpm.openkoala.org/", name = "logMessageResponse")
    public JAXBElement<LogMessageResponse> createLogMessageResponse(LogMessageResponse value) {
        return new JAXBElement<LogMessageResponse>(_LogMessageResponse_QNAME, LogMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://application.jbpmDemo.jbpm.openkoala.org/", name = "logMessage")
    public JAXBElement<LogMessage> createLogMessage(LogMessage value) {
        return new JAXBElement<LogMessage>(_LogMessage_QNAME, LogMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaskUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://application.jbpmDemo.jbpm.openkoala.org/", name = "getTaskUserResponse")
    public JAXBElement<GetTaskUserResponse> createGetTaskUserResponse(GetTaskUserResponse value) {
        return new JAXBElement<GetTaskUserResponse>(_GetTaskUserResponse_QNAME, GetTaskUserResponse.class, null, value);
    }

}
