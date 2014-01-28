<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix ="s" uri="/struts-tags"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/pages/common/header.jsp"%>
	<link rel="stylesheet" href="/css/reset.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="/css/style.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="/css/invalid.css" type="text/css" media="screen" />
	
	<script src="http://code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
	
  <title>流程详情</title>
  <script type="text/javascript">
  
	   function openPng(id){
		    window.showModalDialog("/processImage?processInstanceId="+id,"流程图","dialogHeight: 600; dialogWidth: 480; dialogTop: 458px; dialogLeft: 166px; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
		}
		
   $(function(){
		
		$('#openPng').click(function(){
		  openPng("<s:property value="processInstanceId"/>");
		});
   });
   
   
  </script>
</head>
  
<body>
<div id="main-content">	
	
	<p id="page-png">流程图片</p>
	<input type="button" value="查看流程图" class="button" id="openPng"/>
	
	<p id="page-intro">审批历史记录：</p>
	
	<table>
			<thead>
				<tr>
	                <td>步骤名称</td>
	                <td>审批时间</td>
	                <td>审批人员</td>
	                <td>审批人账号</td>
	                <td>审批结果</td>
	                <td>备注</td>
				</tr>
			</thead>
			<tbody>
			<s:iterator value="logs">
			   <tr>
					<td><s:property value="nodeName"/></td>
					<td><s:property value="createDate"/></td>
					<td><s:property value="user"/></td>
					<td><s:property value="user"/></td>
					<td><s:property value="result"/></td>
					<td><s:property value="comment"/></td>
				</tr>
			</s:iterator>
			</tbody>
		</table>
		
</div>
</body>
</html>
