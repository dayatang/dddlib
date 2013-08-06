<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
$(function (){
$("#endTimeID").ligerDateEditor();//加载日期选择器
$("#beginTimeID").ligerDateEditor();

   
$("#relativeProcessID").ligerComboBox({ isShowCheckBox: true, isMultiSelect: true,
	url: '<%=contextPath %>/core-KoalaAssignInfo-queryProcess.action',
	valueField : 'id',
	textField: 'name',
	valueFieldID: 'koalaAssignInfoVO.relativeProcess'
}); 
   
});

function saveDataAction(){
	var url = '<%=contextPath %>/core-KoalaAssignInfo-add.action';
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
	  			<td class="label">委托名称:</td>
	  			<td>
               <input name="koalaAssignInfoVO.name" class="input-common" type="text" dataType="Require" id="nameID" value="<s:property value="koalaAssignInfoVO.name"/>"/>
<span class="red12">*</span>	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">委托者:</td>
	  			<td>
               <input name="koalaAssignInfoVO.assigner" class="input-common" type="text" dataType="Require" id="assignerID" value="<s:property value="koalaAssignInfoVO.assigner"/>"/>
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">被委托人:</td>
	  			<td>
               <input name="koalaAssignInfoVO.assignerTo" class="input-common" type="text"  dataType="Require" id="assignerToID" value="<s:property value="koalaAssignInfoVO.assignerTo"/>"/>
	  			</td>
	  			</tr>

	           <tr>
	  			<td class="label">开始时间:</td>
	  			<td><input name="koalaAssignInfoVO.beginTime" class="input-common" type="text" id="beginTimeID" />
	  			 <span class="red12">*</span></td>
	  			</tr>
	  			<tr>
	  			<td class="label">结束时间:</td>
	  			<td>
                   <input name="koalaAssignInfoVO.endTime" class="input-common" type="text" id="endTimeID" />
	  			</td>
	  			</tr>
	  			
	  			<tr>
	  			<td class="label">关联流程:</td>
	  			<td>
                   <input name="" class="input-common" type="text" id="relativeProcessID" />
                   (空表示所有流程)
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
