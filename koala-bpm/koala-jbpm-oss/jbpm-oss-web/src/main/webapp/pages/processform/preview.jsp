<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>预览表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/main.css"   rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/koala.css"   rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/datetimepicker.css"   rel="stylesheet">
<link href="${pageContext.request.contextPath}/lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/respond.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/koala-ui.plugin.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/datetimepicker.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/processform/form.render.js"></script>
<style type="text/css">
.scroll{overflow-y: auto; overflow-x:hidden;width:835px; height:355px;}
</style>
</head>
<body>
<div class="scroll">
<form class="form-horizontal processDetail" role="form">
${htmlContent}
</form>
</div>
</body>
</html>