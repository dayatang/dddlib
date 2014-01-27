<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ss3" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Koala权限系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="${pageContext.request.contextPath}/lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/main.css?time=<%=time%>" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/security.css"   rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/koala.css?time=<%=time%>" rel="stylesheet">
    <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
</head>
<body>
	<input type="hidden" id="roleId" value="${roleId}" />
	<div class="g-head">
	    <nav class="navbar navbar-default">
	        <a class="navbar-brand" href="#"><img src="<c:url value='/images/global.logo.png'/>"/>Koala权限系统</a>
	        <div class="collapse navbar-collapse navbar-ex1-collapse">
	            <div class="btn-group navbar-right">
	                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                    <i class="glyphicon glyphicon-user"></i>
	                    <span>&nbsp;<ss3:authentication property="principal.username" /></span>
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
	    <div class="col-lg-2 g-sidec">
	        <ul class="nav nav-stacked first-level-menu">
	       		 <li>
                <a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-home"></i>&nbsp;主菜单&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
                <ul id="userRight" class="second-level-menu in">
                   <li class="submenu" data-role="openTab" data-target="${pageContext.request.contextPath}/pages/cas/user-list.html" openTree=true data-title="用户管理" data-mark="userList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;用户管理</a></li>
                   <li class="submenu" data-role="openTab" data-target="${pageContext.request.contextPath}/pages/cas/role-list.html" data-title="角色管理" data-mark="roleList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;角色管理</a></li>
                </ul>
            </li>
	        </ul>
	    </div>
	    <div class="col-lg-10 g-mainc container">
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/respond.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/koala-ui.plugin.js?time=<%=time%>" ></script>	
	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/koala-tree.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/validation.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/main.js?time=<%=time%>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/cas/role.js?time=<%=time%>"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/cas/user.js?time=<%=time%>"></script>
</body>
</html>