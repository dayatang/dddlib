<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/pages/common/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>欢迎使用Koala</title>
<link href="/lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
<script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.8.3.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/koala-ui.plugin.js' />"></script>	
<style type="text/css">
@charset "UTF-8";
/* CSS Document */
*   .* {
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
	width: 376px;
	height: 332px;
	border: 1px solid #dce7f4;
}

.login_con_R h4 {
	background: #F2F2F2;
	line-height: 36px;
	width: 376px;
	padding: 0px 6px;
	border: 1px solid #fff;
	border-bottom: 1px solid #d4d4d4;
	margin-top: 0px;
}

.login_con_R ul {
	margin-top: 50px;
	margin-left: 20px;
}

.login_con_R li {
	list-style-type: none;
	line-height: 30px;
	padding-left: 20px;
	margin-bottom: 30px;
}
.login_con_R li label {
  	position: relative;
    top: 6px;
	padding-right: 15px;
}
.login_con_R li input {
	width: 192px;
	display: inline;
}

.login_con_R button {
	margin-top: 25px;
	margin-left: 28%;
	width: 210px;
	text-align: center;
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
	function login() {
		$('#loginFormId').submit();
	}
	
	function refreshCode(){
		
		$('#checkCode').attr('src',"jcaptcha.jpg");
	}
	
</script>
</head>
<body>
	<div class="head"></div>
	<div class="logo">
		<img src="images/background/logo.gif" />
		<div>Koala系统</div>
	</div>
	<div class="login_con">
		<div class="login_con_L">
			<img src="images/background/login_img.gif" />
		</div>
		<div class="login_con_R">
			<FORM id=loginFormId method=post action="j_spring_security_check">
				<h4>登录</h4>
				<ul>
				    <c:if test="${param.login_error == '1' }">
				     	<script>
				     		$('body').message({
								type: 'error',
								content: '用户名或密码错误!'
							});
				     	</script>
				    </c:if>
					<li><label class="col-lg-3">用户名:</label><input type="text" name="j_username" id="j_username" class="form-control"/></li>
					<li><label class="col-lg-3">密&nbsp;&nbsp;&nbsp;码:</label><input type="password" name="j_password" id="j_password" class="form-control"/></li>
				</ul>
				<button class="btn btn-primary" onclick="javascript:login()">登陆</button>
			</FORM>
		</div>
	</div>
	<div class="login_footer">Koala 版权信息 2013</div>
</body>
</html>
</html>