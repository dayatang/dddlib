<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
var id = "<s:property value="#parameters['id']"/>";
var manager;
$(function (){
    loadPublishURLData();
    //
    autoResize();
});

function loadPublishURLData(){
 if(id == '')return;
 $.ajax({
		type : 'POST',
		url: '<%=contextPath %>/core-PublishURL-get.action?publishURLVO.id='+id,
		dataType:'json',
		success: function(json){
			if(json && json.data){
			 json = json.data;
			 for(var index in json){
			   $("#"+index + "ID").html(json[index]);
			 }
			}
		}
	});		
}


function autoResize(){
	var $dialog = $(parent.document).find("div.l-dialog:last");
	var totalWidth = $(parent.window).width();
	var totalHeight = $(parent.window).height();
    var width = 550;
	var height = $("#top_table").height();

	var leftoff = (totalWidth - width)/2;
	var topoff = (totalHeight - height)/2;
	
	try {
		$dialog.css('left',leftoff).css('top',topoff);
		$(parent.document).find("div.l-dialog-body:last").height(height).width(width);
		$(parent.document).find('div.l-dialog-content:last').height(height-5);
	} catch (e) {}
}
</script>
</head>
<body>
<div id="mainpanel">
  <div class="form_tltle"></div>
  <table id="top_table" border="0" cellpadding="0" cellspacing="0" class="form4column">
		<tr>
         <td class="label">id:</td>       
         <td id='idID' class="content">&nbsp;</td>
         <td class="label">name:</td>       
         <td id='nameID' class="content">&nbsp;</td>
		</tr>
		<tr>
         <td class="label">url:</td>       
         <td id='urlID' class="content">&nbsp;</td>
  </table>
<br/>
  <div id="navtab" >
  </div>
</div>
</body>
</html>
