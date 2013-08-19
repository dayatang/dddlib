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
 $("input[id^='beginTimeID_']").ligerDateEditor();//加载日期选择器
 $("input[id^='endTimeID_']").ligerDateEditor();//加载日期选择器
 },	
 initGridPanel:function(){
	 var g = $("#maingrid").ligerGrid({
         checkbox:true,
         pageSize:10,
         height:"400px",
         rowHeight: 28,//行默认的高度
         headerRowHeight: 30,//表头行的高度
         onToNext:next,//条件查询方法
         columns: [
          { display: '委托名', name: 'name',editor: { type: 'text' }},   
          { display: '委托者', name: 'assigner',editor: { type: 'text' }},   
          { display: '被委托者', name: 'assignerTo',editor: { type: 'text' }},   
          { display: '委托开时时间', name: 'beginTime',editor: { type: 'text' }},   
          { display: '委托结束时间', name: 'endTime',editor: { type: 'text' }},   
          { display: '委托创建者', name: 'creater',editor: { type: 'text' }},   
        { display: '操作', isSort: false, width: 120, render: function (rowdata, rowindex, value)
            {
                var h = "<a href='javascript:openDetailsPage(" + rowdata.id + ")'>查看</a> ";
                return h;
            }
        }
        ], 
        enabledEdit: false, clickToEdit: false, isScroll: false,  
        url:"<%=contextPath %>/core-KoalaAssignInfo-pageJson.action",
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
     //form validate
   if(!Validator.Validate(document.getElementById("searchForm"),3))return;
	var param=$("#searchForm").serialize();
	gridManager.loadServerData(param);/*执行服务器查询*/
}

/*分页条件查询参数*/
function next(){
	gridManager.setOptions({
        parms: [
	   {name:"koalaAssignInfoVO.name",value: $("#nameID").val()},
	   {name:"koalaAssignInfoVO.assigner",value: $("#assignerID").val()},
	   {name:"koalaAssignInfoVO.beginTime",value: $("#beginTimeID").val()},
	   {name:"koalaAssignInfoVO.assignerTo",value: $("#assignerToID").val()},
	   {name:"koalaAssignInfoVO.endTime",value: $("#endTimeID").val()},
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
	var url = "KoalaAssignInfo-view.jsp?id="+id;
	_dialog = jQuery.ligerDialog.open({
  	    title: '查看',
  	    url:url,
  	    isResize: true,
  	    width: 850, 
  	    height: 500, 
  	    isHidden: false
      });
}

function openFormDialog(edit){
	var url = "KoalaAssignInfo-add.jsp";
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
	     url = "KoalaAssignInfo-update.jsp?id=" + newRow.id;
	}
	_dialog = jQuery.ligerDialog.open({
  	    title: edit ? '编辑' : '新增',
  	    url:url,
  	    isResize: true, width: 550, height: 550, isHidden: false
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
    var data = [{ name: 'ids', value: removeData }];
	 Koala.ajax({
 		async:true,
 		type : "POST",
 		url: "<%=contextPath %>/core-KoalaAssignInfo-delete.action",
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
  <td width="12%">委托名:</td>
  <td width="22%"><input name="koalaAssignInfoVO.name" class="input-common" type="text" id="nameID"  /></td>
  <td width="12%">委托者:</td>
  <td width="22%"><input name="koalaAssignInfoVO.assigner" class="input-common" type="text" id="assignerID"  /></td>
  <td width="12%">开始时间:</td>
  <td colspan="3">
  	 <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><input type="text" name="koalaAssignInfoVO.beginTime" id="beginTimeID_start" ></td>
        <td width="15px">&nbsp;-&nbsp;</td>
        <td><input type="text" name="koalaAssignInfoVO.beginTimeEnd" id="beginTimeID_end" ></td>
      </tr>
    </table>
  	</td>
 </tr>
 <tr height="25px">
  <td width="12%">被委托者:</td>
  <td width="22%"><input name="koalaAssignInfoVO.assignerTo" class="input-common" type="text" id="assignerToID"  /></td>
  <td width="12%">结束时间:</td>
  <td colspan="3">
  	 <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><input type="text" name="koalaAssignInfoVO.endTime" id="endTimeID_start" ></td>
        <td width="15px">&nbsp;-&nbsp;</td>
        <td><input type="text" name="koalaAssignInfoVO.endTimeEnd" id="endTimeID_end" ></td>
      </tr>
    </table>
  	</td>
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
