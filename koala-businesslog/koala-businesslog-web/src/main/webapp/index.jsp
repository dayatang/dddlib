<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>日志系统</title>
    <meta charset="utf-8">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/main.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />" rel="stylesheet">
    <script>
        var contextPath = '<%=request.getContextPath()%>';
    </script>
</head>
<body>
<div class="g-head">
    <nav class="navbar navbar-default">
        <a class="navbar-brand" href="#"><img src="<c:url value='/images/global.logo.png'/>"/>Koala 日志系统</a>
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <div class="btn-group navbar-right" style="display:none;">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <i class="glyphicon glyphicon-user"></i>
                    <span>&nbsp;Admin</span>
                    <span class="caret"></span>
                </button>
               <ul class="dropdown-menu" id="userManager">
	                    <li data-target="modifyPwd"><a href="#">修改密码</a></li>
	                    <li data-target="switchUser"><a href="#">切换用户</a></li>
	                    <li data-target="loginOut"><a href="#">注销</a></li>
	           </ul>
            </div>
        </div>
    </nav>
</div>
<div class="g-body">
    <div class="col-xs-2 g-sidec">
        <ul class="nav nav-stacked first-level-menu">
            <li>
                <a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-home"></i>&nbsp;主菜单&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
                <ul id="userRight" class="second-level-menu in">
                     <li class="submenu" data-role="openTab" data-target="/pages/log/log-list.html" data-title="日志管理" data-mark="logList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;日志管理</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <div class="col-xs-10 g-mainc">
        <ul class="nav nav-tabs" id="navTabs">
            <li class="active"><a href="#home" data-toggle="tab">主页</a></li>
        </ul>
        <div class="tab-content" id="tabContent">
            <div id="home" class="tab-pane active"></div>
        </div>
    </div>
</div>
<div id="footer" class="g-foot">
    <span>Copyright © 2011-2013 Koala</span>
</div>
<script type="text/javascript" src="<c:url value='/lib/jquery-1.8.3.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />"></script>
<script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>"></script>
    <script type="text/javascript" src="<c:url value='/lib/koala-tree.js' />?time=<%=time%>"></script>
<script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>"></script>
<script type="text/javascript" src="<c:url value='/lib/validate.js' />?time=<%=time%>"></script>
</body>
</html>