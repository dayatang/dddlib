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
});


function saveDataAction(){
	var url = '<%=contextPath %>/core-Employee-add.action';
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
	  			<td class="label">name:</td>
	  			<td>
               <input name="employeeVO.name" class="input-common" type="text"  id="nameID" value="<s:property value="employeeVO.name"/>"/>
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
