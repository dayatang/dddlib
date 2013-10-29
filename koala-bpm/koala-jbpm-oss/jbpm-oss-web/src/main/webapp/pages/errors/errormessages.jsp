<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/datetimepicker.css'/>" rel="stylesheet">
    <script type="text/javascript">
		function cancel(){
			window.close();
		}
	</script>
</head>
<body>
	<font size="" color="red">${error }</font>
	<div class="row processInitiate-buttons">
	   <button data-dismiss="modal" class="btn btn-default" type="button" role="cancel" onclick="cancel();">返回</button>
	</div>
</body>
</html>