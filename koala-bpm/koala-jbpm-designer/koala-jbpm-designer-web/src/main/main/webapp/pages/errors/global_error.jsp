<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
 String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<style>
.errorPanel{width:425px; height:195px; clear:both; margin-top:100px;background-color:#fff;border:2px;border-style:groove; padding:0px;}
.errorPanel .errorPic{ width:110px; float:left;margin-top:30px;}
.errorPanel .errorTxt{width:305px; float:right; color:#FF0000; font-size:14px; font-weight:bolder; margin-top:30px;}
.errorPanel .errorBtn{width:100%; clear:both;}
.errorTip {width:100%; float:left; color:#FF0000; font-size:14px; font-weight:bolder;}
.errorContent {width:90%; float:left; color:#666666; font-size:14px; font-weight:bolder; margin-left:5px;}
.errorMessage {list-style:none;margin:0px;} 
</style>
</head>
<body>
<div id="errorContent" align="center">
   <div class="errorPanel">
      <div class="errorPic"> <img src="${pageContext.request.contextPath}/images/base/warn.jpg" /></div>
	  <div class="errorTxt" align="left">
	     <span class="errorTip">警告信息</span><br /><br />
		 <span class="errorContent"> ${errorMsg}</span>
	  </div>
	  <div class="errorBtn"></div>
   </div>
</div>
</body>
</html>