<%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <title>Koala通用查询</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
        <link href="<c:url value='/css/gqc.css' />?time=<%=time%>"   rel="stylesheet">
        <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
        <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />"   rel="stylesheet">
         <script>
             var contextPath = '${pageContext.request.contextPath}';
        </script>
    </head>
    <body>
        <div class="query">
            <div class="query-body">
                <table class="table table-responsive previewQuery" id="previewQuery">

                </table>
            </div>
            <div class="buttons">
                <button class="btn btn-primary" id="searchBtn"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button>
            </div>
        </div>
        <div class="previewGrid" id="previewGrid"></div>
        <script type="text/javascript" src="<c:url value='/lib/jquery-1.8.3.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />"></script>
        <script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>"></script>
        <script type="text/javascript" src="<c:url value='/js/gqc/previewTemplate.js' />?time=<%=time%>"></script>
    </body>
</html>
