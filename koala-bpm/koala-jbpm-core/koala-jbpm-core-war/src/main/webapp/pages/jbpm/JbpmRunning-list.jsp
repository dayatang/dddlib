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

function openPng(id){
    window.showModalDialog("/processImage?processId="+id,"流程图","dialogHeight: 600; dialogWidth: 480; dialogTop: 458px; dialogLeft: 166px; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
    
}

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
         onToNext:next,//条件查询方法
         columns: [
          { display: '流程ID', name: 'processId',editor: { type: 'text' }},
          { display: '流程名', name: 'processName',editor: { type: 'text' }},
          { display: '流程实例ID', name: 'processInstanceId',editor: { type: 'text' }},
          { display: '流程创建时间', name: 'createDate',editor: { type: 'text' }},
        { display: '操作', isSort: false, width: 120, render: function (rowdata, rowindex, value)
            {
        	 
        	    var details = "<%=contextPath %>/jbpm-JbpmProcess-processDetail.action?processInstanceId="+rowdata.processInstanceId;
                var h = "<a href='javascript:openPng(\""+rowdata.processId+"\")'>流程图</a>&nbsp;&nbsp;<a href='"+details+"'>查看详情</a>";
                return h;
            }
        }
        ], 
        enabledEdit: false, clickToEdit: false, isScroll: false,  
        url:"<%=contextPath %>/jbpm-JbpmProcess-queryRunningProcess.action",
        parms:[{name:'processId',value:'<s:property value="#parameters.processName"/>'},
               {name:'versionNum',value:'<s:property value="#parameters.versionNum" />'}
              ],
        width: '100%'
    });
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
	   {name:"processId",value: $("#keyprocessIdID").val()},
	   {name:"koalaJbpmVariableVO.key",value: $("#keyversionNumID").val()},
       {}
        ]
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
  <td width="12%">流程ID:</td>
  <td width="22%"><input name="processId" class="input-common" type="text" id="keyprocessIdID"  value="<s:property value="#parameters.processName" />"/></td>
</tr>

 <tr height="25px">
  <td width="12%">流程版本:</td>
  <td width="22%"><input name="versionNum" class="input-common" type="text" id="keyversionNumID"  value="<s:property value="#parameters.versionNum" />"/></td>
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
