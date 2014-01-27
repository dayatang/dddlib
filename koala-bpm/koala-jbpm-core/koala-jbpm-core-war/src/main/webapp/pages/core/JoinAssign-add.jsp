<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
$(function (){
});


function saveDataAction(){
	var url = '<%=contextPath %>/core-JoinAssign-add.action';
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

</script>
</head>
<body>
<div class="form_body">
  	<form id="dataForm" name="dataForm">
	  	<table id="form_table" border="0" cellpadding="0" cellspacing="0" class="form2column">
	           <tr>
	  			<td class="label">会签名称:</td>
	  			<td>
               <input name="joinAssignVO.name" class="input-common" type="text"  dataType="English"  id="nameID" />
<span class="red12">*</span>	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">会签描述:</td>
	  			<td>
               <input name="joinAssignVO.description" class="input-common" type="text"  id="descriptionID" />
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">关键属性:</td>
	  			<td>
               <input name="joinAssignVO.keyChoice" class="input-common" type="text"  dataType="English"  id="keyChoiceID" />
<span class="red12">*</span>	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">通过条件:</td>
	  			<td>
               <input name="joinAssignVO.successResult" class="input-common" type="text" dataType="Require" id="successResultID" />
<span class="red12">*</span>	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">监控值:</td>
	  			<td>
               <input name="joinAssignVO.monitorVal" class="input-common" type="text"  id="monitorValID" />
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">会签类型:</td>
	  			<td>
                <select name="joinAssignVO.type"  id="typeID" class="select-common">
	           	   <option value="">Select...</option>
	               <option value="Number">按人数</option>
	               <option value="Percent">按比例</option>
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
