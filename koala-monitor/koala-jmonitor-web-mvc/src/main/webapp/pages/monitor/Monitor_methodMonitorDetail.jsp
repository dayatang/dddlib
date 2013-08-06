<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String method = request.getParameter("method") == null ? "" : request.getParameter("method");
	String timeStart = request.getParameter("timeStart") == null ? "" : request.getParameter("timeStart");
	String timeEnd = request.getParameter("timeEnd") == null ? "" : request.getParameter("timeEnd");
	String nodeId = request.getParameter("nodeId") == null ? "" : request.getParameter("nodeId");
	String traceKey = request.getParameter("traceKey") == null ? "" : request.getParameter("traceKey");
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
	 *弹出层，显示SQLS信息
	*/
	function showSqls(id){
		//alert("id: " + id);
		$.layer({
			type : 2,
		    title : ['<h3>SQLS明细</h3>',true],
		    iframe : {src : '${pageContext.request.contextPath}/pages/monitor/Monitor_sqlsMonitorDetail.jsp?methodId='+id},
		    area : ['900px' , '400px'],
		    offset : ['50%' , '']
		});
	}

	/**
	 *弹出层，显示堆栈信息
	*/
	function showStack(id){
		$.layer({
			type : 2,
		    title : ['<h3>堆栈信息</h3>',true],
		    iframe : {src : '${pageContext.request.contextPath}/monitor/Monitor/stackTracesDetail.koala?monitorType=method&detailsId='+id},
		    area : ['900px' , '400px'],
		    offset : ['50%' , '']
		});
	}
</script>
</head>
<body>
	<form id="dataForm">
	    <input type="hidden" id="method" name="method" value="<%=method%>"/>
		<input type="hidden" id="timeStart" name="timeStart" value="<%=timeStart%>"/>
		<input type="hidden" id="timeEnd" name="timeEnd" value="<%=timeEnd%>"/>
		<input type="hidden" id="nodeId" name="nodeId" value="<%=nodeId%>"/>
		<input type="hidden" id="traceKey" name="traceKey" value="<%=traceKey%>"/>
	</form>
	<div id="maingrid"></div>
	<script src="${pageContext.request.contextPath}/js/monitor/methodMonitorDetail.js" type="text/javascript"></script>
</body>
</html>