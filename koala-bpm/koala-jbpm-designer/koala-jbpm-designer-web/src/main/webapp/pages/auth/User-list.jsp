<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户管理</title>
<%@ include file="/pages/common/header.jsp" %>
</head>
<body style="padding:10px;height:100%; text-align:center;">
  <input type="hidden" id="roleId" value="${roleId}" />
  
  <div id="search" class="searchtitle">
  	用户名称：<input id="userNameForSearch" type="text" class="input-common" />
  	用户帐号：<input id="userAccountForSearch" type="text" class="input-common" />
    <input id="searchButton" type="button" class="btn-normal" onclick="search()" value="查询" />
  </div>
  
  <div id="maingrid"></div> 
  <div id="addUser" style="display: none;">
  	<form id="addform">
	  	<table class="form2column">
	  		<tr>
	  			<td class="label">用户名称</td>
	  			<td class="content"><input type="text" id="userName" name="userName" class="input-common" validate="{required:true}" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">用户帐号</td>
	  			<td class="content"><input type="text" id="userAccount" name="userAccount" class="input-common" validate="{required:true}" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">密码</td>
	  			<td class="content"><input type="password" id="password" name="password" class="input-common" validate="{required:true}" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">用户描述</td>
	  			<td class="content"><input type="text" id="userDescription" name="userDescription" class="input-common" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">是否有效</td>
	  			<td class="content"><input type="checkbox" checked="true" id="valid" name="valid" /></td>
	  		</tr>
	  	</table>
  	</form>
  </div>

  <div id="editUser" style="display: none;">
  	<form id="editform">
	  	<table>
	  		<tr><td colspan="2">&nbsp;</td></tr>
	  		<tr>
	  			<td>用户名称</td>
	  			<td><input type="text" id="editUserName" name="editUserName" validate="{required:true}" /></td>
	  			<td align="left"></td>
	  		</tr>
	  		<tr><td colspan="2">&nbsp;</td></tr>
	  		<tr>
	  			<td>用户帐号</td>
	  			<td><input type="text" id="editUserAccount" name="editUserAccount" disabled="true" /></td>
	  		</tr>
	  		<tr><td colspan="2">&nbsp;</td></tr>
	  		<tr id="passwordTR">
	  			<td>密码</td>
	  			<td><input type="text" id="editPassword" name="editPassword" validate="{required:true}" /></td>
	  			<td align="left"></td>
	  		</tr>
	  		<tr><td colspan="2">&nbsp;</td></tr>
	  		<tr>
	  			<td>用户描述</td>
	  			<td><input type="text" id="editUserDescription" name="editUserDescription" /></td>
	  		</tr>
	  		<tr><td colspan="2">&nbsp;</td></tr>
	  		<tr>
	  			<td>是否有效</td>
	  			<td><input type="checkbox" checked="true" id="editValid" name="editValid" /></td>
	  		</tr>
	  	</table>
  	</form>
  </div>
  
  <div id="assignUserToRoleDialog" style="display: none;">
	  <div id="dialogSearch">
	  	用户名称：<input id="dialogUserNameForSearch" type="text" />
	  	用户帐号：<input id="dialogUserAccountForSearch" type="text" />
	    <input id="dialogSearchButton" type="button" value="查询" />
	  </div>
	  <div id="assignUsersToRole">
	  </div>
  </div>
  
  <script src="js/auth/user.js" type="text/javascript"></script>
</body>
</html>
