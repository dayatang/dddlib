package org.openkoala.businesslog.utils;

import org.openkoala.businesslog.config.BusinessLogContextQuery;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQuery;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
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
     * 前置模板的节点名
     */
    private final static String PRE_TEMPLATE_NODE_NAME = "preTemplate";

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
        Document doc = loadXmlDocument(xmlConfigPath);
        return new BusinessLogConfigXmlParser(doc);
    }

    public static Document loadXmlDocument(String xmlConfigPath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new FileInputStream(xmlConfigPath));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPreTemplate() {
        Element root = xmlDoc.getDocumentElement();
        NodeList rootChildren = root.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++) {
            Node node = rootChildren.item(i);
            if (PRE_TEMPLATE_NODE_NAME.equals(node.getNodeName())) {
                return node.getTextContent();
            }
        }
        return "";
    }

    public String getTemplateFrom(String businessOperator) {
        Node configNode = findConfigByBusinessOperator(businessOperator);
        NodeList nodeList = configNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (TEMPLATE_NODE_NAME.equals(node.getNodeName())) {
                return node.getTextContent();
            }
        }
        return "";
    }

    public static List<Node> getConfigsFrom(Document doc) {
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


    public List<BusinessLogContextQuery> getQueriesFrom(String businessOperator) {
        List<BusinessLogContextQuery> result = new ArrayList<BusinessLogContextQuery>();
        Node config = findConfigByBusinessOperator(businessOperator);

        Node queriesNode = findQueriesNode(config);
        NodeList nodeList = queriesNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node queryNode = nodeList.item(i);
            if (QUERY_NODE_NAME.equals(queryNode.getNodeName())) {
                result.add(createQueryBy(queryNode));
            }
        }
        return result;
    }


    private Node findQueriesNode(Node nodeConfig) {
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
                        && businessOperator.equals(node.getTextContent())) {
                    return eachNode;
                }
            }
        }
        return null;
    }


    private BusinessLogDefaultContextQuery createQueryBy(Node queryNode) {
        String contextKey = null;

        String beanName = null;
        String beanClass = null;
        String targetMethod = "";
        List<String> args = new ArrayList<String>();

        NodeList children = queryNode.getChildNodes();


        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (TARGET_BEAN_NODE_NAME.equals(node.getNodeName())) {
                beanName = node.getTextContent();
                beanClass = node.getAttributes().getNamedItem(TARGET_BEAN_CLASS_ATTR_NAME).getNodeValue();
            }

            if (QUERY_CONTEXT_KEY.equals(node.getNodeName())) {
                contextKey = node.getNodeValue();
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
        List<String> args = new ArrayList<String>();
        for (int i = 0; i < argsNode.getLength(); i++) {
            Node node = argsNode.item(i);
            if (TARGET_METHOD_ARG_NODE_NAME.equals(node.getNodeName())) {
                args.add(node.getTextContent());
            }
        }
        return args;
    }
}