<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.openkoala.com/permission" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试权限标签</title>
</head>
<body>
	<security:hasPermission identify="TEST001">
		Test001
	</security:hasPermission>	
	<security:hasPermission identify="TEST002">
		Test002
	</security:hasPermission>	
</body>
</html>