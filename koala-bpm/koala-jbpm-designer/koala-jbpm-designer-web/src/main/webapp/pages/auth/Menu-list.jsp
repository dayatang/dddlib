<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>菜单资源管理</title>
<%@ include file="/pages/common/header.jsp" %>
</head>
<body style="padding:10px;height:100%; text-align:center;">
  <div id="maingrid"></div> 
  
  <div id="editMenu" style="display: none;">
	  <form id="form">
	  	<table class="form2column" >
	  		<tr id="parentTR">
	  			<td class="label">父菜单名称</td>
	  			<td class="content"><input type="text" id="parent" disabled="disabled" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单类型</td>
	  			<td class="content"><input type="text" id="menuType" name="menuType" class="input-common" validate="{required:true}" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单名称</td>
	  			<td class="content"><input type="text" id="name" name="name" class="input-common" validate="{required:true}" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单标识</td>
	  			<td class="content"><input type="text" id="identifier" class="input-common" name="identifier" validate="{required:true}" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单图片</td>
	  			<td><img id="menuIcon" name="menuIcon" width="20" height="20" />
	  			<input id="iconBtn" type="button" value="浏览图片" />
	  			</td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单描述</td>
	  			<td class="content"><input type="text" id="resDesc" name="desc" class="input-common"/></td>
	  		</tr>
	  	</table>
	  </form>
  </div>
  
  <div id="icons"></div>
  
  <script src="js/auth/menu.js" type="text/javascript"></script>
</body>
</html>
