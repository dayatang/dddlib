<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>发起流程页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/datetimepicker.css'/>" rel="stylesheet">
    <script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.8.3.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/koala-ui.plugin.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/datetimepicker.js' />" ></script>
	<script type="text/javascript" src="<c:url value='/js/processform/form.render.js' />" ></script>
	<script type="text/javascript" src="<c:url value='/js/koala/Validate.js' />" ></script>
    
	<style>
		body {
			position:relative; 
			top: 20px;
		}
		.processDetail {
			width: 80%;
			margin-left: auto;
			margin-right: auto;
		}
		.processInitiate-buttons {
			text-align: center;
		}
		.processInitiate-buttons button {
			margin-right: 15px;
		}
	</style>
	<script type="text/javascript">
		function cancel(){
			window.close();
		}
		function submit(){
			$("#form1").submit();
			window.close();
		}
	</script>
</head>
<body>
	<c:if test="${not empty success }">
		<script type="text/javascript">
			cancel();
		</script>
	</c:if>
	<form class="form-horizontal processDetail" role="form" id="form1" action="/businessSupport/startProcess.koala" method="post">
		<div class="panel panel-primary">
		   <div class="panel-heading">发起流程</div>
		  <div class="panel-body">
		    	${templatehtmlCode }
		  </div>
		  <div class="panel-footer">
		  		<div class="processInitiate-buttons">
				   <button data-dismiss="modal" class="btn btn-default" type="button" role="cancel" onclick="cancel();">取消</button>
				   <button class="btn btn-success" type="button" onclick="submit();">提交</button>
				</div>
		  </div>
		</div>
		<%-- <input type="hidden" name="dynaProcessKeysForShow" value="${dynaProcessKeysForShow }" /> --%>
		<input type="hidden" name="processId" value="${processId }" />
	</form>
</body>
</html>