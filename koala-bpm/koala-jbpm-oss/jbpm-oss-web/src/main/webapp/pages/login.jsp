<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/pages/common/header.jsp" %>
<title>欢迎使用Koala</title>
<link href="/lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
<style type="text/css">
@charset "UTF-8";
/* CSS Document */
* .* {
	margin: 0;
	padding: 0;
}

body {
	margin: 0;
	padding: 0;
}

.head {
	height: 30px;
	width: 100%;
	background: #428BCA;
	padding: 0;
	margin: 0;
}

.logo {
	height: 90px;
	width: 960px;
	margin: 0 auto;
	overflow: hidden;
	clear: both;
}

.logo img {
	height: 59px;
	width: 94px;
	overflow: hidden;
	margin-top:20px;
	float: left;
}

.logo div {
	font-size: 24px;
	color: #666;
	height: 40px;
	float: left;
	line-height: 60px;
	margin: 20px 10px;
	padding: 10px;
	border-left: 1px solid #d2d2d2;
}

.login_con {
	width: 960px;
	height: 332px;
	margin: 10px auto;
	clear: both;
}

.login_con_L {
	float: left;
	width: 568px;
	height: 332px;
	overflow: hidden;
}

.login_con_R {
	float: left;
	width: 360px;
	height: 332px;
}

.login_con_R .panel-body .input-group {
	width: 75%;
	margin-left: auto;
	margin-right: auto;
	margin-top: 25px;
} 
.login_con_R .panel-body .input-group:first-child {
	margin-top: 35px;
}
.login_con_R .panel-body .input-group:last-child {
	margin-top: 55px;
}
.login_con_R .panel-body .input-group span {
	color: #357EBD;
}
.login_con_R .panel-body .input-group  input {
	height: 38px;
}
.login_con_R .panel {
	margin-bottom: 0;
	padding-bottom: 0;
	height: 332px;
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
}
.login_con_R .panel .panel-heading {
	border-top-left-radius: 0;
}
.login_con_R .panel .panel-title {
	text-align: center;
	font-size: 18px;
}
.login_con_R .login-btn {
	width: 75%;
	margin-left: auto;
	margin-right: auto;
	margin-top: 18%;
	text-align: center
}
.login_con_R .login-btn button {
	width: 90%;
}
.login_footer {
	clear: both;
	margin-top: 8%;
	width: 100%;
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}
</style>
<script type="text/javascript">

function login(){
	$('#loginFormId').submit();
}

/**
 * 按回车键时，触发登录按钮
 */
function keyDown(e){
	//这样写是为了兼容FireFox和IE，因为IE的onkeydown在FF中不起作用
	var ev =window.event||e; 
 	//按回车键
  	if (ev.keyCode==13) {
   		ev.returnValue=false;
   		ev.cancel = true;
   		var sub =  document.getElementById("btnSubmit");
  		sub.click();
  	} 
}

</script>
</head>
<body>
	<div class="head"></div>
	<div class="logo">
		<img src="images/background/koala.png" />
		<div>Koala系统</div>
	</div>
	<div class="login_con">
		<div class="login_con_L">
			<img src="images/background/login_img.gif" />
		</div>
		<div class="login_con_R">
			<FORM id=loginFormId method=post action="j_spring_security_check">
				<div class="panel panel-primary">
			        <div class="panel-heading">
			          <h3 class="panel-title">登陆</h3>
			        </div>
			        <div class="panel-body">
				          <div class="input-group">
			                   <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
			                   <input type="text" name="j_username" id="j_username" class="form-control" placeholder="用户名">
			              </div>
			              <div class="input-group">
			                    <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
			                    <input type="password" name="j_password" id="j_password"   onkeydown="keyDown(event)" class="form-control" placeholder="密码">
			              </div>
			              <div class="input-group login-btn">
						  	<button class="btn btn-primary" id="btnSubmit" onclick="javascript:login()">登陆</button>
						  </div>
			        </div>
		      	</div>
		   </FORM>
		</div>
	</div>
	<div class="login_footer"><h3>Koala 版权信息 2013</h3></div>
</body>
</html>
</html>