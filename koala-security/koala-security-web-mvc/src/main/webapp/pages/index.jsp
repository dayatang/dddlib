<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ss3" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Koala权限系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
    <link href="/lib/z-tree/css/zTreeStyle.css"   rel="stylesheet">
    <link href="/css/main.css"   rel="stylesheet">
    <link href="/css/koala.css"   rel="stylesheet">
</head>
<body>
	<input type="hidden" id="roleId" value="${roleId}" />
	<div class="g-head">
	    <nav class="navbar navbar-default">
	        <a class="navbar-brand" href="#"><img src="images/global.logo.png"/>Koala权限系统</a>
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
                	<a data-toggle="collapse" href="#resourceManagement"><i class="glyphicon glyphicon-th-list"></i>&nbsp;资源&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
	                <ul id="resourceManagement" class="second-level-menu">
	                    <li class="submenu" data-role="openTab" data-target="pages/auth/resource-list.html" data-title="资源管理" data-mark="resourceList"><a ><i class="glyphicon glyphicon-hand-right"></i>&nbsp;资源管理</a></li>
	                    <li class="submenu" data-role="openTab" data-target="pages/auth/menu-list.html" data-title="菜单管理" data-mark="menuList"><a ><i class="glyphicon glyphicon-hand-right"></i>&nbsp;菜单管理</a></li>
	                    <li class="submenu" data-role="openTab" data-target="pages/auth/resource-type-list.html" data-title="资源类型管理" data-mark="resourceTypeList"><a ><i class="glyphicon glyphicon-hand-right"></i>&nbsp;资源类型管理</a></li>
	                </ul>
	            </li>
	            <li>
                	<a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-user"></i>&nbsp;用户角色管理&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
	                <ul id="userRight" class="second-level-menu">
	                    <li class="submenu" data-role="openTab" data-target="pages/auth/user-list.html" data-title="用户管理" data-mark="userList"><a ><i class="glyphicon glyphicon-hand-right"></i>&nbsp;用户管理</a></li>
	                    <li class="submenu" data-role="openTab" data-target="pages/auth/role-list.html" data-title="角色管理" data-mark="roleList"><a ><i class="glyphicon glyphicon-hand-right"></i>&nbsp;角色管理</a></li>
	                </ul>
	            </li>
	        </ul>
	    </div>
	    <div class="col-lg-10 g-mainc container">
	        <ul class="nav nav-tabs">
	            <li class="active"><a href="#home" data-toggle="tab">主页</a></li>
	        </ul>
	        <div class="tab-content">
	            <div id="home" class="tab-pane active"></div>
	        </div>
	    </div>
	</div>
	<div id="footer" class="g-foot">
	    <span>Copyright © 2011-2013 Koala</span>
	</div>
	<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.8.3.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/koala-ui.plugin.js' />"></script>	
	<script type="text/javascript" src="<c:url value='/lib/z-tree/js/jquery.ztree.all-3.5.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/validation.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/main.js' />"></script>
</body>
</html>