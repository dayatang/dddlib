<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<link href="css/koala-common.css" rel="stylesheet" />
<style>
.main{margin-top:30px; height:85px; width:400px; background:#EFEFEF; border:#CCCCCC solid 2px; float:left;}
.main .l{ width:68%; float:left;}
.main .r{width:30%; float:right; text-align:center; height:85px; line-height:85px; margin-top:25px;}
.item{ float:left; margin-left:25px; margin-top:10px;  list-style:none; width:100%;}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		var id = "";
		$.ajax({
			type: "json",
			url: "<%=contextPath %>/domain-SecuritySettings-findAll.action",
			success: function(result) {
				if (result.data.length > 0) {
					$("#ip").attr("checked", result.data[0].ipFilterEnabled);
					$("#usernamepassword").attr("checked", result.data[0].usernamePasswordEnabled);
					id = result.data[0].id;
				}
			}
		});
		
		$("#submit").click(function() {
			$.ajax({
				type: "post",
				dataType: "json",
				url: "<%=contextPath %>/domain-SecuritySettings-save.action",
				data: [{
					name: "securitySettingsVO.ipFilterEnabled", 
					value: $("#ip").attr("checked") == "checked" ? true : false
				}, {
					name: "securitySettingsVO.usernamePasswordEnabled",
					value: $("#usernamepassword").attr("checked") == "checked" ? true : false
				}, {
					name: "securitySettingsVO.id",
					value: id
				}],
				success: function(result) {
					alert(result.result);
				}
			});
		});
	});
</script>
</head>
<body>

<div class="form_tltle" style="width:450px;">设置</div>
<div class="form_body">
<table class="form2column" style="width:450px;">
   <tr>
	 <td class="label" style="width:150px;">启用黑白名单:</td>
	 <td><input type="checkbox" id="ip" /></td>
</tr>
 <tr>
	 <td class="label">启用用户名密码验证:</td>
	 <td><input type="checkbox" id="usernamepassword" /></td>
</tr>
</table>
</div>
<div class="form_button" style="width:449px; margin-bottom:2px;">
<input type="button" class="btn-normal" id="submit"  value="确定"/>
</div>
</body>
</html>
