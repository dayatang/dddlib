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
          { display: '流程ID', name: 'processName',editor: { type: 'text' }},
          { display: '流程所属版本', name: 'versionNum',editor: { type: 'text' }},
          { display: '流程创建时间', name: 'createDate',editor: { type: 'text' }},
          { display: '是否激活',render: function (rowdata, rowindex, value)
              {
                  if(rowdata.active)return "激活";      	  
              }
        },
        { display: '操作', isSort: false, width: 250, render: function (rowdata, rowindex, value)
            {
        	    var details = "<%=contextPath %>/jbpm-JbpmProcess-queryProcessByProcessId.action?processId="+rowdata.processInstanceId;
                var h = "<a href='javascript:openPng(\""+rowdata.processId+"\")'>流程图</a>&nbsp;&nbsp;<a href='javascript:openRunning(\""+rowdata.processName+"\",\""+rowdata.versionNum+"\")'>流程(运行中)</a>&nbsp;&nbsp;<a href='javascript:openCompleted(\""+rowdata.processName+"\",\""+rowdata.versionNum+"\")'>流程(已完成)</a>";
                return h;
            }
        }
        ], 
        enabledEdit: false, clickToEdit: false, isScroll: false,  
        url:"<%=contextPath %>/jbpm-JbpmProcess-queryProcessVersion.action?processId=<s:property value="#parameters.processId" />",
        width: '100%'
    });
	 return g;
 }
}

function openRunning(processName,versionNum){
	parent.f_addTab("Running"+processName.replace(new RegExp("\\.","gm"),"")+versionNum, "流程实例(运行中):", "/pages/jbpm/JbpmRunning-list.jsp?processName="+processName+"&versionNum="+versionNum);	
}

function openCompleted(processName,versionNum){
	parent.f_addTab("Complete"+processName.replace(new RegExp("\\.","gm"),"")+versionNum, "流程实例(已完结)", "/pages/jbpm/JbpmComplete-list.jsp?processName="+processName+"&versionNum="+versionNum);	
}

/*分页条件查询参数*/
function next(){
	gridManager.setOptions({
        parms: [
	   {name:"koalaJbpmVariableVO.key",value: $("#keyID").val()},
       {}
        ]
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