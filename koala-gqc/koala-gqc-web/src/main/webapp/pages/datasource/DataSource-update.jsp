<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String id = request.getParameter("id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
//var id = "<s:property value="#parameters['id']"/>";
//var id = $("#idTab").val();
//alert(id);
$(function (){
//autoResize();
loadData();
});

function loadData(){
 if($("#id").val() == '')return;
 Koala.ajax({
		type : 'POST',
		url: '<%=contextPath %>/dataSource/get/' + $("#id").val() + '.koala',
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
			}else{
				alert(json.error);
			}
		}
	});		
}

function saveDataAction(){
	var url = '<%=contextPath %>/dataSource/update.koala?id='+$("#id").val();	
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
				 if(json.result == "success"){
					 parent.gridManager.loadData();
					 parent._dialog.close();
				 }
			}
		}
	});
}

function validate(){
	//var databaseType = $("#databaseTypeID").val();
	var connectUrl = $("#connectUrlID").val();
	var jdbcDriver = $("#jdbcDriverID").val();
	var username = $("#usernameID").val();
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

/* function autoResize(){
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
} */
</script>
</head>
<body>
<div class="form_body">
	<input type="hidden" id="id" value="<%=id %>" />
  	<form id="dataForm" name="dataForm">
	  	<table id="form_table" border="0" cellpadding="0" cellspacing="0" class="form2column">
	           <tr>
	  			<td class="label">数据源ID:</td>
	  			<td>
               	<input name="dataSourceId" size="40" class="input-common" type="text" id="dataSourceIdID" style="background-color: #D0D0D0;" readonly="readonly"/>
	  			</td>
	  			</tr>
	           <!-- <tr>
	  			<td class="label">数据库类型:</td>
	  			<td>
               <input name="databaseType" size="40" class="input-common" type="text"  id="databaseTypeID" />
               &nbsp;<font color="red">*</font>
	  			</td>
	  			</tr> -->
	           <tr>
	  			<td class="label">类型:</td>
	  			<td>
                <input name="dataSourceType" class="input-common" type="hidden"  id="dataSourceTypeID" />
	  			<input name="dataSourceTypeDesc" size="40" class="input-common" type="text"  id="dataSourceTypeDescID" style="background-color: #D0D0D0;" readonly="readonly" />
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">描述:</td>
	  			<td>
               <input name="dataSourceDescription" size="40" class="input-common" type="text"  id="dataSourceDescriptionID" />
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">jdbcDriver:</td>
	  			<td>
               <input name="jdbcDriver" size="40" class="input-common" type="text"  id="jdbcDriverID" />
               &nbsp;<font color="red">*</font>
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">Url:</td>
	  			<td>
               <input name="connectUrl" size="40" class="input-common" type="text"  id="connectUrlID" />
               &nbsp;<font color="red">*</font>
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">username:</td>
	  			<td>
               <input name="username" size="40" class="input-common" type="text"  id="usernameID" />
               &nbsp;<font color="red">*</font>
	  			</td>
	  			</tr>
	           <tr>
	  			<td class="label">password:</td>
	  			<td>
               <input name="password" size="40" class="input-common" type="text"  id="passwordID" />
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
