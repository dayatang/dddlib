<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/pages/common/header.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>堆栈信息</title>
</head>
<body bgcolor="#FAF4FF">
	<table >
		<tbody>
			<c:choose>
				<c:when test="${fn:length(stackTraces) == 0 }">
					<tr>
						<td><font color="red">没有堆栈信息!</font></td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="stackTrace" items="${stackTraces }">
						<tr>
							<td>${stackTrace }</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</body>
</html>