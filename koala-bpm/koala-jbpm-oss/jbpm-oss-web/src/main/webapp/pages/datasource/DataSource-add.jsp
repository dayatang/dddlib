<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
$(function (){
	//autoResize();
	$("#dataSourceTypeID").change(function(){
		var value = $("#dataSourceTypeID").val();
		if(value == "SYSTEM_DATA_SOURCE"){
			$("#showTable").html('<table id="form_table" border="0" cellpadding="0" cellspacing="0" class="form2column"><tr><td class="label">数据源ID:</td><td><input name="dataSourceId" size="40" class="input-common" type="text"  id="dataSourceIdID" />&nbsp;<font color="red">*</font><label style="color: #ADADAD;">系统中配置的数据源ID</label></td></tr></table>');
		}else{
			var html = "";
			html = html + '<table id="form_table" border="0" cellpadding="0" cellspacing="0" class="form2column">';
			html = html + '<tr><td class="label">数据源ID:</td><td><input name="dataSourceId" size="40" class="input-common" type="text"  id="dataSourceIdID" />&nbsp;<font color="red">*</font></td></tr>';
			/* html = html + '<tr><td class="label">数据库类型:</td><td><input name="databaseType" size="40" class="input-common" type="text"  id="databaseTypeID" />&nbsp;<font color="red">*</font></td></tr>'; */
			html = html + '<tr><td class="label">描述:</td><td><input name="dataSourceDescription" size="40" class="input-common" type="text"  id="dataSourceDescriptionID" /></td></tr>';
			html = html + '<tr><td class="label">jdbcDriver:</td><td><input name="jdbcDriver" size="40" class="input-common" type="text"  id="jdbcDriverID" />&nbsp;<font color="red">*</font></td></tr>';
			html = html + '<tr><td class="label">Url:</td><td><input name="connectUrl" size="40" class="input-common" type="text"  id="connectUrlID" />&nbsp;<font color="red">*</font></td></tr>';
			html = html + '<tr><td class="label">username:</td><td><input name="username" size="40" class="input-common" type="text"  id="usernameID" />&nbsp;<font color="red">*</font></td></tr>';
			html = html + '<tr><td class="label">password:</td><td><input name="password" size="40" class="input-common" type="text"  id="passwordID" /></td></tr>';
			html = html + '</table>';
			$("#showTable").html(html);
		}
	});
});


function saveDataAction(){
	var url = '<%=contextPath %>/dataSource/add.koala';
   //form validate
   //if(!Validator.Validate(document.getElementById("dataForm"),3))return;
   if( !validate() ){
	   alert("缺少必填项");
	   return;
   }
   Koala.ajax({
		type : 'POST',
		url: url,
		dataType:'json',
		data:$("#dataForm").serialize(),
		success: function(json){
			if(json && json.result){
			 alert(json.result);
			 parent.gridManager.loadData();
			 if(json.result == "success"){
				 parent._dialog.close();
			 }
			}
		}
	});
}

function validate(){
	var dataSourceId = $("#dataSourceIdID").val();
	//不能为空和空格
	if( !checkNotNull(dataSourceId) ){
		return false;
	}
	
	var dataSourceType = $("#dataSourceTypeID").val();
	if(dataSourceType == "SYSTEM_DATA_SOURCE"){
		return true;
	}
	
	//var databaseType = $("#databaseTypeID").val();
	var connectUrl = $("#connectUrlID").val();
	var jdbcDriver = $("#jdbcDriverID").val();
	var username = $("#usernameID").val();
	//var password = $("#passwordID").val();
	if(checkNotNull(connectUrl) && checkNotNull(jdbcDriver)
			&& checkNotNull(username)){
		return true;
	}else{
		return false;
	}
}

/**
 * 检查变量是否不为空  true:不空   false:空
 */
function checkNotNull(item){
	 //不能为空和空格
	if(item==null || item=="" || item.replace(/(^\s*)|(\s*$)/g, "")=="" ){
		return false;
	}else{
		return true;
	}
}

function autoResize(){
	var _form = $("#form_table");
	var width = 550;
	var height = _form.height() + 100;
	if(_form.hasClass('form4column')){
		width = 760;
	}else if(_form.hasClass('form2column')){
		width = 550;
	}
	if(height<240)height = 240;
	try {
		$(parent.document).find("div.l-dialog-body:last").height(height).width(width);
		$(parent.document).find('div.l-dialog-content:last').height(height-5);
	} catch (e) {}
}

function checkConnection(){
	var url = "<%=contextPath %>/dataSource/checkDataSource.koala";
	Koala.ajax({
		//async:true,
		//type : "POST",
		url:url,
		//data:data,
		data:$("#dataForm").serialize(),
		dataType:'json',
		success: function(json){
			if(json && json.result){
			 alert(json.result);
			}
		}
	});
} 
</script>
</head>
<body>
<div class="form_body">
  	<form id="dataForm" name="dataForm">
  		<div style="margin-left: 47px;height: 25px;">
  			选择数据源:&nbsp;&nbsp;&nbsp;
  			<select id="dataSourceTypeID" name="dataSourceType">
  			<option value="SYSTEM_DATA_SOURCE">系统数据源</option>
  			<option value="CUSTOM_DATA_SOURCE">自定义数据源</option>
  		</select>
  		</div>
  		<div id="showTable">
  			<table id="form_table" border="0" cellpadding="0" cellspacing="0" class="form2column">
	           <tr>
		  			<td class="label">数据源ID:</td>
		  			<td>
	               		<input name="dataSourceId" size="40" class="input-common" type="text"  id="dataSourceIdID" />
	               		&nbsp;<font color="red">*</font><label style="color: #ADADAD;">系统中配置的数据源ID</label>
		  			</td>
	  			</tr>
	  		</table>
  		</div>
	  	
	  <div class="form_button">
        <input id="searchButton" type="button" class="btn-normal" onclick="saveDataAction()" value="保存" />
	  	 &nbsp;&nbsp;
	  	<input type="reset" class="btn-normal" value="重置" />
	  	<input type='button' class='btn-normal' style='height:25px;' onclick='checkConnection()' value='测试连接'/>
     </div>
  	</form>
</div>
</body>
</html>
