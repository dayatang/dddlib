<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>组织系统</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
    <link href="lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"   rel="stylesheet">
    <link href="css/main.css?time=<%=time%>"    rel="stylesheet">
     <link href="css/organisation.css?time=<%=time%>"    rel="stylesheet">
    <link href="css/koala.css?time=<%=time%>"    rel="stylesheet">
</head>
<body>
<div class="g-head">
    <nav class="navbar navbar-default">
        <a class="navbar-brand" href="#"><img src="images/global.logo.png"/>组织系统</a>
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <div class="btn-group navbar-right">
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
    <div class="col-lg-2 g-sidec">
        <ul class="nav nav-stacked first-level-menu">
            <li>
                <a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-user"></i>&nbsp;主菜单&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
                <ul id="userRight" class="second-level-menu">
                   <li class="submenu" data-role="openTab" data-target="pages/organisation/departmentList.html" openTree=true data-title="部门管理" data-mark="departmentList" ><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;部门管理</a></li>
                   <li class="submenu" data-role="openTab" data-target="pages/organisation/jobList.html" data-title="职务管理" data-mark="jobList" ><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;职务管理</a></li>
                   <li class="submenu" data-role="openTab" data-target="pages/organisation/positionList.html" data-title="岗位管理" data-mark="positionList" ><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;岗位管理</a></li>
                   <li class="submenu" data-role="openTab" data-target="pages/organisation/employeeList.html" data-title="人员管理" data-mark="employeeList"><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;人员管理</a></li>
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
    <span>Copyright Â© 2011-2013 Koala</span>
</div>
<script type="text/javascript" src="lib/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<script type="text/javascript" src="lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="lib/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="lib/koala-ui.plugin.js?time=<%=time%>"></script>
<script type="text/javascript" src="js/tree.js"></script>
<script type="text/javascript" src="js/validation.js"></script>
<script type="text/javascript" src="js/main.js?time=<%=time%>" ></script>
</body>
</html>