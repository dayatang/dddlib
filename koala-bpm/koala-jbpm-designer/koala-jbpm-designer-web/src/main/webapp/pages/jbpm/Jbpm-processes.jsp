<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<link href="<%=contextPath %>/css/list.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
  
</script>
</head>
<body>
<div style="width:100%;height:100%;padding-left: 2px;">
	<div>
	 <table border="0" cellspacing="0" cellpadding="0" class="info-box">
	   <tr>
	     <td>流程名</td>
	     <td>流程ID</td>
	   </tr>
	   <s:iterator value="processes">
	   <tr>
	     <td><s:property value="name"/></td>
	     <td><s:property value="id"/></td>
	   </tr>
	   </s:iterator>
	</table>
	</div>
<br/>
<div id="navtab" >
</div>
</div>
</body>
</html>