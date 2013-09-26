<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
$(function (){
autoResize();

$('#scopeID').hide();

$('#variableScope').change(function(){
	if($(this).val()=="&KOALA_GLOBAL")$('#scopeID').hide();
	else $('#scopeID').show();
});
});


function saveDataAction(){
	var url = '<%=contextPath %>/core-KoalaJbpmVariable-add.action';
   //form validate
   if(!Validator.Validate(document.getElementById("dataForm"),3))return;
   
  
   $.ajax({
		type : 'POST',
		url: url,
		dataType:'json',
		data:$("#dataForm").serialize(),
		success: function(json){
			if(json && json.result){
			 alert(json.result);
			 parent.gridManager.loadData();
			 parent._dialog.close();
			}
		}
	});
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
</script>
</head>
<body>
<div class="form_body">
  	<form id="dataForm" name="dataForm">
	  	<table id="form_table" border="0" cellpadding="0" cellspacing="0" class="form2column">
	           <tr>
	  			<td class="label">变量名:</td>
	  			<td>
               <input name="koalaJbpmVariableVO.key" class="input-common" type="text"  dataType="English"  id="keyID" value="<s:property value="koalaJbpmVariableVO.key"/>"/>
<span class="red12">*</span>	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">变量值:</td>
	  			<td>
               <input name="koalaJbpmVariableVO.value" class="input-common" type="text" dataType="Require" id="valueID" value="<s:property value="koalaJbpmVariableVO.value"/>"/>
<span class="red12">*</span>	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">变量范围:</td>
	  			<td>
	  			<select name="koalaJbpmVariableVO.scopeType" id="variableScope" class="select-common">
	           	   <option value="&KOALA_GLOBAL">全局变量</option>
	               <option value="&KOALA_PACKAGE_">包级变量</option>
	               <option value="&KOALA_PROCESS_">流程定义级变量</option>
	           	</select> 
               <input name="koalaJbpmVariableVO.scope" class="input-common" type="text" id="scopeID" value="<s:property value="koalaJbpmVariableVO.scope"/>"/>
<span class="red12">*</span>	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">变量类型:</td>
	  			<td>
                <select name="koalaJbpmVariableVO.type"  id="typeID" class="select-common">
	           	   <option value="String">String</option>
	               <option value="long">long</option>
	               <option value="Boolean">Boolean</option>
	               <option value="double">double</option>
	               <option value="int">int</option>
	           	</select> 
	  			</td>
	  			</tr>
	  </table>
	  <div class="form_button">
        <input id="searchButton" type="button" class="btn-normal" onclick="saveDataAction()" value="保存" />
	  	 &nbsp;&nbsp;
	  	<input type="reset" class="btn-normal" value="重置" />
     </div>
  	</form>
</div>
</body>
</html>
