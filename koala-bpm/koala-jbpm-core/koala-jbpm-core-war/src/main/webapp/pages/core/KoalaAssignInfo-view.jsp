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
    loadKoalaAssignInfoData();
	$("#navtab").ligerTab(); 
    loadjbpmNames();  
});

function loadKoalaAssignInfoData(){
 if(id == '')return;
 $.ajax({
		type : 'POST',
		url: '<%=contextPath %>/core-KoalaAssignInfo-get.action?koalaAssignInfoVO.id='+id,
		dataType:'json',
		success: function(json){
			if(json && json.data){
			 json = json.data;
			 var _td;
			 var innerHtml;
			 for(var index in json){
			   _td = $("#"+index + "ID");
			   if(_td.attr('format') == 'boolean'){
			     innerHtml = json[index] == "1" ? "是" : "否";
			   }else{
			     innerHtml = json[index];
			   }
			   _td.html(innerHtml);
			 }
			}
		}
	});		
}

function loadjbpmNames(){
  $("#jbpmNamesGrid").ligerGrid({
        pagesizeParmName:'pageSize',//每页记录数
        pageSize:10,
        height:'300px',
        columns: [
                { display: '流程ID', name: 'processId',width : '50%',editor: { type: 'text' }}
       ],
       enabledEdit: false, clickToEdit: false, isScroll: true,  
       url:'<%=contextPath %>/core-KoalaAssignInfo-findJbpmNamesByKoalaAssignInfo.action?koalaAssignInfoVO.id='+id,
       width: '100%'
   });	
}

function autoResize(){
	var $dialog = $(parent.document).find("div.l-dialog:last");
	var totalWidth = $(parent.window).width();
	var totalHeight = $(parent.window).height();
    var width = 2 * 100;
	var height = totalHeight - 10;
    height = height < 250 ? 250 :height;
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
         <td class="label">委托名:</td>  
         <td id='nameID' class="content">&nbsp;</td>
         <td class="label">委托者:</td>  
         <td id='assignerID' class="content">&nbsp;</td>
		</tr>
		<tr>
         <td class="label">被委托者:</td>  
         <td id='assignerToID' class="content">&nbsp;</td>
         <td class="label">开始时间:</td>  
         <td id='beginTimeID' class="content">&nbsp;</td>
		</tr>
		<tr>
         <td class="label">结束时间:</td>  
         <td id='endTimeID' class="content">&nbsp;</td>
         <td class="label">委托创建者:</td>  
         <td id='createrID' class="content">&nbsp;</td>
		</tr>
  </table>
<br/>
  <div id="navtab" >
	<div title="委托包含流程">
	<div id="jbpmNamesGrid"></div>	
	</div>
  </div>
</div>
</body>
</html>
