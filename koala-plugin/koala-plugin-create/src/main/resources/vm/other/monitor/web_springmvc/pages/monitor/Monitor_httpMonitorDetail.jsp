<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String unit = request.getParameter("unit");
	String requestDate = request.getParameter("requestDate");
	String system = request.getParameter("system");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- base jquery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/layer.js"></script>
<!-- ligerUI -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ligerUI/js/plugins/ligerTree.js"></script>
<link href="${pageContext.request.contextPath}/js/ligerUI/skins/koala/css/style-all.css" rel="stylesheet" type="text/css" /> 
<script type="text/javascript">
	/**
	 *弹出层，显示堆栈信息
	*/
	function showStack(id){
		$.layer({
			type : 2,
		    title : ['<h3>堆栈信息</h3>',true],
		    iframe : {src : '${pageContext.request.contextPath}/monitor/Monitor/stackTracesDetail.koala?monitorType=http&detailsId='+id},
		    area : ['900px' , '400px'],
		    offset : ['50%' , '']
		});
	}
</script>
</head>
<body>
	<input type="hidden" id="unit" name="unit" value="<%=unit%>"/>
	<input type="hidden" id="requestDate" name="requestDate" value="<%=requestDate%>"/>
	<input type="hidden" id="system" name="system" value="<%=system%>"/>
	<div id="maingrid"></div>
	<script src="${pageContext.request.contextPath}/js/monitor/httpMonitorDetail.js" type="text/javascript"></script>
</body>
</html>