<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <title>Koala通用查询</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
        <link href="lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"   rel="stylesheet">
        <link href="css/main.css?time=<%=time%>"   rel="stylesheet">
        <link href="css/koala.css?time=<%=time%>"   rel="stylesheet">
    </head>
  	<body>
            <div class="g-head">
                <nav class="navbar navbar-default">
                     <a class="navbar-brand" href="http://openkoala.org/display/koala/Home" target="_blank"><img src="images/global.logo.png"/>Koala通用查询</a>
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
                    <ul class="nav nav-stacked">
                        <li class="active">
                            <a href="#generalQueryConfig" data-toggle="collapse"><span class="glyphicon glyphicon-home"></span><span>菜单栏</span></a>
                            <ul class="submenu" id="generalQueryConfig">
                                <li data-target="pages/generalQueryList.html" data-title="通用查询配置" data-mark="generalQueryList"><a><span class="glyphicon glyphicon-edit"></span>&nbsp;&nbsp;通用查询配置</a></li>
                                <li data-target="pages/dataSourceList.html" data-title="数据源配置"  data-mark="dataSourceList"><a><span class="glyphicon glyphicon-wrench"></span>&nbsp;&nbsp;数据源配置</a></li>
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
	    <script type="text/javascript" src="lib/jquery.js"></script>
        <script type="text/javascript" src="lib/respond.min.js"></script>
        <script type="text/javascript" src="lib/bootstrap/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="js/datetimepicker.js"></script>
        <script type="text/javascript" src="js/koala-ui.plugin.js?time=<%=time%>"></script>
	    <script type="text/javascript" src="js/main.js?time=<%=time%>"></script>
    </body>
</html>