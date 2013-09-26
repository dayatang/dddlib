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
          { display: '数据源ID', name: 'dataSourceId',editor: { type: 'text' }},   
          /* { display: '数据库类型', name: 'databaseType',width: 100,editor: { type: 'text' }},    */
          { display: '类型', name: 'dataSourceTypeDesc',editor: { type: 'text' }},   
          { display: '描述', name: 'dataSourceDescription',editor: { type: 'text' }},
          { display: 'jdbcDriver', name: 'jdbcDriver',width: 150,editor: { type: 'text' }}, 
          { display: 'Url', name: 'connectUrl',width: 235,editor: { type: 'text' }},     
          { display: 'username', name: 'username',editor: { type: 'text' }},   
          { display: 'password', name: 'password',editor: { type: 'text' }},   
          { display: '操作', isSort: false, width: 100, render: function (rowdata, rowindex, value)
            {
                //var h = "<a href='javascript:openDetailsPage(" + rowdata.id + ")'><font color='blue'>测试连接</font></a> ";
                //return h;
                var button = "<input type='button' class='btn-normal' style='height:25px;' onclick='openDetailsPage(" + rowdata.id + ")' value='测试连接'/>";
                return button;
            } 
        }
        ], 
        enabledEdit: false, clickToEdit: false, isScroll: false,  
        url:"<%=contextPath %>/dataSource/pageJson.koala",
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


//查询方法
function searchAction(){
     //form validate
   if(!Validator.Validate(document.getElementById("searchForm"),3))return;
	var param=$("#searchForm").serialize();
	gridManager.loadServerData(param);/*执行服务器查询*/
}

/*分页条件查询参数*/
/* function next(){
	gridManager.setOptions({
        parms: [
       {name:"dataSourceId",value: $("#dataSourceIdID").val()},
	   {name:"databaseType",value: $("#databaseTypeID").val()},
	   {name:"dataSourceType",value: $("#dataSourceTypeID").val()},
	   {name:"dataSourceDescription",value: $("#dataSourceDescriptionID").val()},
	   {name:"connectUrl",value: $("#connectUrlID").val()},
	   {name:"jdbcDriver",value: $("#jdbcDriverID").val()},
	   {name:"driverUri",value: $("#driverUriID").val()},
	   {name:"username",value: $("#usernameID").val()},
	   {name:"password",value: $("#passwordID").val()},
       {}
        ]
    });
} */

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
	var url = "<%=contextPath %>/dataSource/checkDataSourceById.koala?id="+id;
	Koala.ajax({
		//async:true,
		//type : "POST",
		url:url,
		//data:data,
		dataType:'json',
		success: function(json){
			if(json && json.result){
			 alert(json.result);
			}
		}
	});

	/* _dialog = jQuery.ligerDialog.open({
  	    title: '查看',
  	    url:url,
  	    isResize: true,
  	    width: 850, 
  	    height: 500, 
  	    isHidden: false,
  	    success: function(json){
			if(json && json.result){
			 alert(json.result);
			 parent._dialog.close();
			}
		}
      }); */
} 

function openFormDialog(edit){
	var url = "DataSource-add.jsp";
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
	     if(newRow.dataSourceType == "SYSTEM_DATA_SOURCE"){
	    	 alert('系统数据源不能修改'); return;
	     }
	     id = newRow.id;
	     url = "DataSource-update.jsp?id=" + newRow.id;
	}
	_dialog = jQuery.ligerDialog.open({
  	    title: edit ? '编辑' : '新增',
  	    url:url,
  	    isResize: true, width: 550, height: 380, isHidden: false
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
 		url: "<%=contextPath %>/dataSource/delete.koala",
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
<!-- grid -->
<div id="maingrid"></div> 
</div>
</body>
</html>
