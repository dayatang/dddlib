<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>角色管理</title>
<%@ include file="/pages/common/header.jsp" %>
</head>
<body style="padding:10px;height:100%; text-align:center;">
  <input type="hidden" id="userId" value="${userId}" />
  <input type="hidden" id="userAccount" value="${userAccount}" />
  
  <div id="search" class="searchtitle">
  	角色名称：<input id="roleNameForSearch" type="text" class="input-common" />
    <input id="searchButton" type="button" class="btn-normal" value="查询" />
  </div>
  
  <div id="maingrid"></div> 
  
  <div id="editRole" style="display: none;">
	  <form id="form">
	  	<table class="form2column">
	  		<tr>
	  			<td class="label">角色名称</td>
	  			<td class="content"><input type="text" id="roleName" name="roleName" class="input-common" dataType="Require"  /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">角色描述</td>
	  			<td class="content"><input type="text" id="roleDescription" class="input-common" name="roleDescription" /></td>
	  		</tr>
	  	</table>
	  </form>
  </div>
  
  <div id="treeDiv">
  	<ul id="menuTree">
  	</ul>
  </div>

	<div id="assignRolesToUserDialog" style="display: none;">
	  <div id="dialogSearch">
	  	角色名称：<input id="dialogRoleNameForSearch" type="text" />
	   	<input id="dialogSearchButton" type="button" value="查询" />
	  </div>
	  <div id="assignRolesToUser">
	  </div>
	</div>
  <div id="urlDiv">
	 <div id="urlResource"></div>
  </div>
  
  <script src="js/auth/role.js" type="text/javascript"></script>
</body>
</html>
