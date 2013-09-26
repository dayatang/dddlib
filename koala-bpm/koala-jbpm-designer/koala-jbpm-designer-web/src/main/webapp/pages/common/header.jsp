<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ss3" uri="http://www.springframework.org/security/tags" %>
<%
 String contextPath = request.getContextPath();
%>
<!-- base jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<!-- ligerUI -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerUI/js/plugins/ligerTree.js"></script>
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/koala/css/style-all.css" rel="stylesheet" type="text/css" />  
 <!-- jquery validation -->    
<script src="${pageContext.request.contextPath}/js/jquery-validation/jquery.validate.min.js" type="text/javascript"></script> 
<script src="${pageContext.request.contextPath}/js/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery-validation/messages_cn.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/common/validateform.js" type="text/javascript"></script>
<!-- common js --> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/koala/koala-ui.plugin.js"></script>
<script src="${pageContext.request.contextPath}/js/koala/Koala.js" type="text/javascript"></script>
<!-- common css -->
<link href="${pageContext.request.contextPath}/css/koala-common.css" rel="stylesheet" type="text/css" /> 
<!-- auth -->
<script src="${pageContext.request.contextPath}/js/common/common.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/css/auth-common.css" rel="stylesheet" type="text/css" /> 

<script type="text/javascript">var rootPath = "";</script>

<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 
