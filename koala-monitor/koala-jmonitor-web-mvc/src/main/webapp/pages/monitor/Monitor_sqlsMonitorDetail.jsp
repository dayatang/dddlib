<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String methodId = request.getParameter("methodId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SQLS明细</title>
<!-- base jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/layer.js"></script>
<!-- ligerUI -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerUI/js/plugins/ligerTree.js"></script>
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/koala/css/style-all.css" rel="stylesheet" type="text/css" /> 
</head>
<body bgcolor="#FAF4FF">
	<input type="hidden" id="methodId" name="methodId" value="<%=methodId%>"/>
	<div id="maingrid"></div>
	<script src="${pageContext.request.contextPath}/js/monitor/sqlsMonitorDetail.js" type="text/javascript"></script>
</body>
</html>