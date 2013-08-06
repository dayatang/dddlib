<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/pages/common/header.jsp" %>
<title>Koala流程设计平台</title>
<link href="css.css" rel="stylesheet" type="text/css" />
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
	background: #f2f2f2;
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
	height: 90px;
	width: 200px;
	overflow: hidden;
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
	width: 390px;
	height: 332px;
	border: 1px solid #dce7f4;
}

.login_con_R h3 {
	background: #f0f3f6;
	line-height: 36px;
	height: 36px;
	width: 376px;
	font: 18px;
	color: #666;
	font-weight: 100;
	padding: 0px 6px;
	border: 1px solid #fff;
	border-bottom: 1px solid #d4d4d4;
	margin-top: 0px;
}

.login_con_R ul {
	margin-top: 20px;
	margin-left: 20px;
}

.login_con_R li {
	list-style-type: none;
	line-height: 30px;
}

.login_con_R li input {
	height: 30px;
	line-height: 30px;
	border: 1px solid #d2d2d2;
	margin-top: 12px;
	width: 192px;
}

.login_bnt {
	width: 211px;
	height: 42px;
	background: url(images/background/loginbnt.gif) no-repeat;
	margin: 30px auto;
	cursor: pointer;
}

.login_footer {
	clear: both;
	line-height: 40px;
	color: #999;
	margin: 20px auto;
	font-size: 12px;
	width: 300px;
}
</style>
<script type="text/javascript">

function login(){
	$('#loginFormId').submit();
}

</script>
</head>
<body>
	<div class="head"></div>
	<div class="logo">
		<img src="images/background/logo.gif" />
		<div>Koala-流程设计平台</div>
	</div>
	<div class="login_con">
		<div class="login_con_L">
			<img src="images/background/login_img.gif" />
		</div>
		<div class="login_con_R">
			<FORM id=loginFormId method=post action="j_spring_security_check">
				<h3>登录</h3>
				<ul>
					<li>用户名：<input type="text" name="j_username" id="j_username"/></li>
					<li>密&nbsp;&nbsp;码：<input type="password" name="j_password" id="j_password" /></li>
				</ul>
				<div class="login_bnt" onclick="javascript:login()"></div>
			</FORM>
		</div>
	</div>
	<div class="login_footer">Koala 版权信息 2013</div>
</body>
</html>
</html>