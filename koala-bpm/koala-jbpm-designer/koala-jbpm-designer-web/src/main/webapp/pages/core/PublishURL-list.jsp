<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
var gridManager;
var _dialog;
$(function (){
	PageLoader.initSearchPanel();
	gridManager = PageLoader.initGridPanel();
});

PageLoader = {
 //
 initSearchPanel:function(){
 },	
 initGridPanel:function(){
	 var g = $("#maingrid").ligerGrid({
         checkbox:true,
         pageSize:10,
         height:"400px",
         onToNext:next,//条件查询方法
         columns: [
          { display: 'id', name: 'id',editor: { type: 'text' }},   
          { display: 'name', name: 'name',editor: { type: 'text' }},   
          { display: 'url', name: 'url',editor: { type: 'text' }},   
        { display: '操作', isSort: false, width: 120, render: function (rowdata, rowindex, value)
            {
                var h = "<a href='javascript:openDetailsPage(" + rowdata.id + ")'>查看</a> ";
                return h;
            }
        },
        { display: '浏览', render: function (rowdata, rowindex, value)
        	{
        	  var h = "<a href='javascript:openProcessesPage(" + encodeURIComponent(rowdata.id) + ")'>浏览</a> ";
              return h;
        	}
        }
        ], 
        enabledEdit: false, clickToEdit: false, isScroll: false,  
        url:"<%=contextPath %>/core-PublishURL-pageJson.action",
        width: '100%',
        toolbar: { items: [
            { id: "add", text: '增加', click: itemclick, icon: 'add' },
	        { line: true },
	        { id: "modify", text: '修改', click: itemclick, icon: 'modify' },
            { line: true },
	        { id: "remove", text: '删除', click: itemclick, icon: 'delete' }
        ]
        }
    });
	 g.toggleCol("id",false);//隐藏id列
	 return g;
 }
}


//查询方法
function searchAction(){
	var param=$("#searchForm").serialize();
	gridManager.loadServerData(param);/*执行服务器查询*/
}

/*分页条件查询参数*/
function next(){
	gridManager.setOptions({
        parms: [
	   {name:"publishURLVO.name",value: $("#nameID").val()},
	   {name:"publishURLVO.url",value: $("#urlID").val()},
       {}
        ]
    });
}

function itemclick(item)
{
	switch (item.id) {
		case "add":
			openFormDialog(false);
	        break;
		case "modify":
			openFormDialog(true);
	        break;
		case "remove":
			deleteAction();
	        break;
	}
}


function openDetailsPage(id){
	var url = "PublishURL-view.jsp?id="+id;
	if(_dialog)_dialog.close();
	_dialog = jQuery.ligerDialog.open({
  	    title: '查看',
  	    url:url,
  	    isResize: true,
  	    width: 850, 
  	    height: 600, 
  	    isHidden: false, 
  	    rownumbers: true
      });
}

function openFormDialog(edit){
	var id;
	if(edit){
		var newRow = gridManager.getSelected();
	     if (!newRow) { alert('请选择行'); return; }
	     var i = 0;
	     $.each(gridManager.getCheckedRows(), function(index, element) {
			  i++;
		  });
	     if(i>1){
	    	 alert('请只选择一行'); return;
	     }
	     id = newRow.id;
	}
	var url = "PublishURL-form.jsp" + (edit ? ("?id="+id) : "");
	if(_dialog)_dialog.close();
	_dialog = jQuery.ligerDialog.open({
  	    title: edit ? '编辑' : '新增',
  	    url:url,
  	    isResize: true, width: 550, height: 550, isHidden: true, rownumbers: true
      });
}

function openProcessesPage(id){
	var url = "<%=contextPath %>/jbpm-Jbpm-processes.action?id="+id;
	_dialog = jQuery.ligerDialog.open({
  	    title: '流程',
  	    url:url,
  	    isResize: true,
  	    width: 750, 
  	    height: 450, 
  	    isHidden: false, 
  	    rownumbers: true
      });
}

/*删除行*/
function deleteAction()
{ 
    var newRow = gridManager.getSelected();
    if (!newRow) { alert('请选择行'); return; }
    
    var removeData = '';
	  $.each(gridManager.getCheckedRows(), function(index, element) {
		  removeData = removeData + element.id + ',';
	  });
	  removeData = removeData.substr(0, removeData.length - 1) ;
    var data = [{ name: 'publishURLVO.id', value: removeData }];
	 $.ajax({
 		async:true,
 		type : "POST",
 		url: "<%=contextPath %>/core-PublishURL-delete.action",
 		data:data,
 		dataType:'json',
 		success: function(data, textStatus){
  			 gridManager.deleteSelectedRow();
  			 gridManager.loadData();
 		}
 		});
}

</script>
</head>
<body>
<div style="width:98%;height:100%;padding-left: 5px;">
<!-- search form -->
<form name="searchForm" id="searchForm" target="_self">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
<table width="800" border="0" style="margin:5px">
 <tr height="25px">
  <td width="12%">name:</td>
  <td width="22%"><input name="publishURLVO.name" class="input-common" type="text" id="nameID" /></td>
  <td width="12%">url:</td>
  <td width="22%"><input name="publishURLVO.url" class="input-common" type="text" id="urlID" /></td>
</tr>
</table>	
</td>
    <td><input id="searchButton" type="button" class="btn-normal" onclick="searchAction()" value="查询" /></td>
  </tr>
</table>	
</form>
<!-- grid -->
<div id="maingrid"></div> 
</div>
</body>
</html>
