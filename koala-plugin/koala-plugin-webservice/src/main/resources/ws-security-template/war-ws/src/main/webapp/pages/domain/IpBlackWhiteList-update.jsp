<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
var id = ${param.id};
$(function (){
autoResize();
loadData();
});

function loadData(){
 if(id == '')return;
 $.ajax({
		type : 'POST',
		url: '<%=contextPath %>/domain-IpBlackWhiteList-get.action?ipBlackWhiteListVO.id='+id,
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
		}
	});		
}

function saveDataAction(){
	var url = '<%=contextPath %>/domain-IpBlackWhiteList-update.action?ipBlackWhiteListVO.id='+id;	
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
	  			<td class="label">IP开始地址:</td>
	  			<td>
               <input name="ipBlackWhiteListVO.start" class="input-common" type="text"  id="startID" dataType="IP"/>
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">IP结束地址:</td>
	  			<td>
               <input name="ipBlackWhiteListVO.end" class="input-common" type="text"  id="endID" dataType="IP" require="false" />
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">IP类型:</td>
	  			<td>
               	<select name="ipBlackWhiteListVO.type" id="typeID">
               		<option>--请选择--</option>
               		<option value="0">黑名单</option>
               		<option value="1">白名单</option>
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
