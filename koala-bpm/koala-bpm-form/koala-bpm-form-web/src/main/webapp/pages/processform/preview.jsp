<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/koala.css"   rel="stylesheet">
	<style>
		body {
			position:relative; 
			top: 20px;
		}
		.col-lg-8 {
			position:relative; 
			top: 8px;
		}
	</style>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	$.ajax({
		method: "post",
		url: "${pageContext.request.contextPath}/processform/templatePreview.koala?formId=${param.formId}",
		success: function(result) {
			$(result.data).appendTo("body");
		}
	});
</script>
</head>
<body>

</body>
</html>