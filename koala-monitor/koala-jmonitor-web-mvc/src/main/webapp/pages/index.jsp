<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ss3" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Koala监控系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <link href="/lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
    <link href="/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"   rel="stylesheet">
    <link href="/css/main.css?time=<%=time%>"   rel="stylesheet">
    <link href="/css/koala.css?time=<%=time%>"  rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/lib/jqplot/css/jquery.jqplot.css" />
    <link type="text/css" rel="stylesheet" href="/lib/jqplot/css/shCoreDefault.min.css" />
    <link type="text/css" rel="stylesheet" href="/lib/jqplot/css/shThemejqPlot.min.css" />
    <link href="/css/monitor.css"   rel="stylesheet">
</head>
<body>
	<div class="g-head">
	    <nav class="navbar navbar-default">
	        <a class="navbar-brand" href="#"><img src="../images/global.logo.png"/>Koala监控系统</a>
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
                	<a data-toggle="collapse" href="#businessSupport"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;监控数据&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
	                <ul id="businessSupport" class="second-level-menu">
	                    <li class="submenu" data-role="openTab" data-target="pages/monitor/http-monitor.html" data-title="HTTP监控" data-mark="httpMonitor"><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;HTTP监控</a></li>
                        <li class="submenu" data-role="openTab" data-target="pages/monitor/method-monitor.html" data-title="方法监控" data-mark="methodMonitor"><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;方法监控</a></li>
                        <li class="submenu" data-role="openTab" data-target="pages/monitor/jdbc-monitor.html" data-title="数据库监控" data-mark="databaseMonitor"><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;数据库监控</a></li>
	                </ul>
	            </li>
	            <li>
                <a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-list"></i>&nbsp;监控节点&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
	                <ul id="userRight" class="second-level-menu">
                         <li class="submenu" data-role="openTab" data-target="pages/monitor/monitor-node-list.html" data-title="监控节点列表" data-mark="monitorNodeList"><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;监控节点列表</a></li>
	                </ul>
	            </li>
	             <li>
                    <a data-toggle="collapse" href="#organisation"><i class="glyphicon glyphicon-tasks"></i>&nbsp;监控服务&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
	                <ul id="organisation" class="second-level-menu">
	                    <li class="submenu" data-role="openTab" data-target="pages/monitor/schedule-list.html" data-title="定时任务" data-mark="scheduleList" ><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;定时任务</a></li>
	                </ul>
	            </li>
	        </ul>
	    </div>
	    <div class="col-lg-10 g-mainc container">
	        <ul class="nav nav-tabs" id="navTabs">
	            <li class="active"><a href="#home" data-toggle="tab">主页面</a></li>
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
	<script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>"></script>
	<script type="text/javascript" src="<c:url value='/js/validation.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/excanvas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jquery.jqplot.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/shCore.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/shBrushXml.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.barRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.pieRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.categoryAxisRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.pointLabels.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.dateAxisRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.highlighter.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.cursor.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>"></script>
</body>
</html>