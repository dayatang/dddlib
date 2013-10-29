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
         rowHeight: 28,//行默认的高度
         headerRowHeight: 30,//表头行的高度
         //onToNext:next,//条件查询方法
         columns: [
          { display: '查询器名称', name: 'queryName'},   
          { display: '描述', name: 'description'},
//          { display: '数据源', render: function (rowdata, rowindex, value){return rowdata.dataSource.dataSourceId}, width: 150,editor: { type: 'text' }}, 
          { display: '数据源', name: 'dataSourceId'}, 
          { display: '查询表', name: 'tableName'},     
          { display: '预览', isSort: false, width: 100, render: function (rowdata, rowindex, value)
            {
                var button = "<input type='button' class='btn-normal' style='height:25px;' onclick='preview(" + rowdata.id + ")' value='预览'/>";
                return button;
            } 
          }
        ], 
        enabledEdit: false, clickToEdit: false, isScroll: false,  
        url:"<%=contextPath %>/generalquery/pageJson.koala",
        width: '100%',
        pagesize:2,
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


function preview(id){
	var url = "<%=contextPath %>/query/"+id+'.koala';
	window.open(url,'',"_blank" ,"resizable:true")
} 

function openFormDialog(edit){
	var url = "Generalquery-add.jsp";
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
	     url = "<%=contextPath %>/generalquery/getById.koala?id=" + newRow.id;
	}
	_dialog = jQuery.ligerDialog.open({
  	    title: edit ? '编辑' : '新增',
  	    url:url,
  	    isResize: true, width: 1024, height: 550, isHidden: false
      });
}

/*删除行*/
function deleteAction()
{ 
    var newRow = gridManager.getSelected();
    if (!newRow) { alert('请选择行'); return; }
    
    //确认删除
    if(!confirm("确认要删除？")){return;}
    
    var removeData = '';
	  $.each(gridManager.getCheckedRows(), function(index, element) {
		  removeData = removeData + element.id + ',';
	  });
	  removeData = removeData.substr(0, removeData.length - 1) ;
    var data = [{ name: 'ids', value: removeData }];
	 Koala.ajax({
 		async:true,
 		type : "POST",
 		url: "<%=contextPath %>/generalquery/delete.koala",
 		data:data,
 		dataType:'json',
 		success: function(data, textStatus){
  			 gridManager.deleteSelectedRow();
  			 gridManager.loadData();
 		}
 		});
}

function findByQueryName() {
	var queryName = $.trim($("#queryName").val());
	if(queryName.length > 0) {
		gridManager.set('url', "<%=contextPath %>/generalquery/pageJson.koala?queryName=" + queryName);
		//gridManager.loadData();
	}
}

</script>
</head>
<body>
<div>
	<div>
		<form id="searchForm">
			查询器名称：<input id="queryName" type="text" name="queryName"/>
			<input id="searchButton" class="btn-normal" type="button" onclick="findByQueryName()" value="查询"/>
		</form>
	</div>
<!-- grid -->
<div id="maingrid" ></div> 
</div>
</body>
</html>
