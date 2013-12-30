package org.openkoala.businesslog.common;

import org.openkoala.businesslog.BusinessLogXmlConfigIOException;
import org.openkoala.businesslog.BusinessLogXmlConfigNotFoundException;
import org.openkoala.businesslog.BusinessLogXmlConfigParseException;
import org.openkoala.businesslog.BusinessLogXmlConfigSAXException;
import org.openkoala.businesslog.config.BusinessLogContextQuery;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQuery;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 12/6/13
 * Time: 2:20 PM
 */
public class BusinessLogConfigXmlParser {


    /**
     * 业务日志节点的节点名s
     */
    private final static String BUSINESS_LOG_CONFIG_NODE_NAME = "businessLogConfig";


    /**
     * 业务操作的节点名
     */
    private final static String BUSINESS_OPERATOR_NODE_NAME = "method";

    /**
     * 模板的节点名
     */
    private final static String TEMPLATE_NODE_NAME = "template";


    /**
     * 查询定义子节点
     */
    private final static String TARGET_BEAN_NODE_NAME = "target";

    private final static String TARGET_BEAN_CLASS_ATTR_NAME = "class";


    /**
     * 查询定义子节点
     */
    private final static String QUERY_NODE_NAME = "query";

    private final static String QUERIES_NODE_NAME = "queries";


    private final static String TARGET_METHOD_NODE_NAME = "method";

    private final static String TARGET_METHOD_ARGS_NODE_NAME = "args";

    private final static String TARGET_METHOD_ARG_NODE_NAME = "arg";
    /**
     * 业务操作分类
     */
    private final static String TARGET_BUSINESS_METHOD_CATEGORY = "category";

    /**
     * 查询定义的key
     */
    private final static String QUERY_CONTEXT_KEY = "key";

    private Document xmlDoc;

    private List<Node> configs;


    private BusinessLogConfigXmlParser() {
    }

    private BusinessLogConfigXmlParser(Document xmlDoc) {
        this.xmlDoc = xmlDoc;
        this.configs = getConfigsFrom(xmlDoc);
    }

    public static BusinessLogConfigXmlParser parsing(String xmlConfigPath) {
        File file = new File(xmlConfigPath);
        if (!file.exists()) {
            return null;
        }
        return parsing(file);
    }

    public static BusinessLogConfigXmlParser parsing(File xmlConfigPath) {
        Document doc = loadXmlDocument(xmlConfigPath);
        return new BusinessLogConfigXmlParser(doc);
    }





    public synchronized static Document loadXmlDocument(File xmlConfigPath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlConfigPath);
        } catch (ParserConfigurationException e) {
            throw new BusinessLogXmlConfigParseException(e);
        } catch (FileNotFoundException e) {
            throw new BusinessLogXmlConfigNotFoundException(e);
        } catch (SAXException e) {
            throw new BusinessLogXmlConfigSAXException(e);
        } catch (IOException e) {
            throw new BusinessLogXmlConfigIOException(e);
        }
    }


    public synchronized String getTemplateFrom(String businessOperator) {
        Node configNode = findConfigByBusinessOperator(businessOperator);
        if (null == configNode) {
            return "";
        }
        NodeList nodeList = configNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (TEMPLATE_NODE_NAME.equals(node.getNodeName())) {
                return node.getTextContent();
            }
        }
        return "";
    }

    public synchronized static List<Node> getConfigsFrom(Document doc) {
        List<Node> results = new ArrayList<Node>();
        Element root = doc.getDocumentElement();
        NodeList rootChildren = root.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++) {
            Node node = rootChildren.item(i);
            if (BUSINESS_LOG_CONFIG_NODE_NAME.equals(node.getNodeName())) {
                results.add(node);
            }
        }

        return results;
    }

    /**
     * 得到业务操作的所属分类
     *
     * @param businessOperator
     * @return
     */
    public synchronized String getCategory(String businessOperator) {
        Node configNode = findConfigByBusinessOperator(businessOperator);
        if (null == configNode) {
            return "";
        }
        NodeList nodeList = configNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (TARGET_BUSINESS_METHOD_CATEGORY.equals(node.getNodeName())
                    && Node.ELEMENT_NODE == node.getNodeType()) {
                return node.getTextContent();
            }
        }
        return "";
    }


    public synchronized List<BusinessLogContextQuery> getQueriesFrom(String businessOperator) {
        List<BusinessLogContextQuery> result = new ArrayList<BusinessLogContextQuery>();
        Node config = findConfigByBusinessOperator(businessOperator);
        if (null == config) {
            return result;
        }
        Node queriesNode = findQueriesNode(config);
        if (null == queriesNode) {
            return result;
        }
        NodeList nodeList = queriesNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node queryNode = nodeList.item(i);
            if (QUERY_NODE_NAME.equals(queryNode.getNodeName())) {
                result.add(createQueryBy(queryNode));
            }
        }
        return result;
    }


    private synchronized Node findQueriesNode(Node nodeConfig) {
        NodeList children = nodeConfig.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node queryNode = children.item(i);
            if (QUERIES_NODE_NAME.equals(queryNode.getNodeName())) {
                /*只能是第一个queries节点返回*/
                return queryNode;
            }
        }
        return null;
    }


    private Node findConfigByBusinessOperator(String businessOperator) {

        for (Node eachNode : configs) {
            NodeList nodeList = eachNode.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (BUSINESS_OPERATOR_NODE_NAME.equals(node.getNodeName())
                        && (Node.ELEMENT_NODE == node.getNodeType())
                        && businessOperator.equals(node.getTextContent())) {

                    return eachNode;
                }
            }
        }
        return null;
    }


    private  synchronized  BusinessLogDefaultContextQuery createQueryBy(Node queryNode) {

        String beanName = null;
        String beanClass = null;
        String targetMethod = "";
        List<String> args = new ArrayList<String>();

        NodeList children = queryNode.getChildNodes();

        String contextKey = queryNode.getAttributes().getNamedItem(QUERY_CONTEXT_KEY).getNodeValue();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (TARGET_BEAN_NODE_NAME.equals(node.getNodeName())) {
                beanName = node.getTextContent();
                beanClass = node.getAttributes().getNamedItem(TARGET_BEAN_CLASS_ATTR_NAME).getNodeValue();
            }
            if (TARGET_METHOD_NODE_NAME.equals(node.getNodeName())) {
                targetMethod = (node.getTextContent() != null ? node.getTextContent().trim() : null);
                if (null == targetMethod || "".equals(targetMethod)) {
                    continue;
                }
            }

            if (TARGET_METHOD_ARGS_NODE_NAME.equals(node.getNodeName())) {
                args = getBeanMethodArgsFrom(node.getChildNodes());
            }
        }
        BusinessLogDefaultContextQuery query = new BusinessLogDefaultContextQuery();
        query.setContextKey(contextKey);
        query.setBeanName(beanName);
        query.setBeanClassName(beanClass);
        query.setMethodSignature(targetMethod);
        query.setArgs(args);
        return query;
    }


    private List<String> getBeanMethodArgsFrom(NodeList argsNode) {
        if (argsNode.getLength() == 0) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < argsNode.getLength(); i++) {
            Node node = argsNode.item(i);
            if (TARGET_METHOD_ARG_NODE_NAME.equals(node.getNodeName())) {
                result.add(node.getTextContent());
            }
        }
        return result;
    }

    public synchronized boolean exists(String businessOperation) {
        return findConfigByBusinessOperator(businessOperation) != null;
    }
}