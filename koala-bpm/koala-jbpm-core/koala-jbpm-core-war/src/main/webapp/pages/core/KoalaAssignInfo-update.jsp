<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
var id = "<s:property value="#parameters['id']"/>";
$(function (){

$("#beginTimeID").ligerDateEditor();//加载日期选择器
$("#endTimeID").ligerDateEditor();//加载日期选择器

var relativeProcess = $("#relativeProcess").ligerComboBox({ isShowCheckBox: true, isMultiSelect: true,
	url: '<%=contextPath %>/core-KoalaAssignInfo-queryProcess.action',
	valueField : 'id',
	textField: 'name',
	valueFieldID: 'koalaAssignInfoVO.relativeProcess'
});

	   $.ajax({
			type : 'POST',
			url: '<%=contextPath %>/core-KoalaAssignInfo-get.action?koalaAssignInfoVO.id='+id,
			dataType:'json',
			success: function(json){
				if(json && json.data){
				 json = json.data;
				 var elm;
				 for(var index in json){
					 elm = document.getElementById(index + "ID");
					 if(elm){
						 if("SELECT" == elm.nodeName){
							 $(elm).find("option[value='"+json[index]+"']").attr("selected","selected");
						 }else{
							 $(elm).val(json[index]);
						 }
					 }
				 }
				}
				relativeProcess.setValue($('#relativeProcessID').val());
			}
		});

});



function saveDataAction(){
	var url = '<%=contextPath %>/core-KoalaAssignInfo-update.action?koalaAssignInfoVO.id='+id;	
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
	  			 <input name="koalaAssignInfoVO.name" class="input-common" type="text"  readonly  id="nameID" value="<s:property value="koalaAssignInfoVO.name"/>"/>
	  			</td>
	  			</tr>
	  			
	           <tr>
	  			<td class="label">委托者:</td>
	  			<td>
	  			
               <input name="koalaAssignInfoVO.assigner" class="input-common" type="text"  id="assignerID" value="<s:property value="koalaAssignInfoVO.assigner"/>"/>
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">被委托者:</td>
	  			<td>
               <input name="koalaAssignInfoVO.assignerTo" class="input-common" type="text"  id="assignerToID" value="<s:property value="koalaAssignInfoVO.assignerTo"/>"/>
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">开始时间:</td>
	  			<td>
                <input name="koalaAssignInfoVO.beginTime" class="input-common" type="text" id="beginTimeID" />
	  			</td>
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
                   <input name="" class="input-common" type="text" id="relativeProcess" />
                   (空表示所有流程)
                   <input type="hidden" value="" id="relativeProcessID"/>
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
