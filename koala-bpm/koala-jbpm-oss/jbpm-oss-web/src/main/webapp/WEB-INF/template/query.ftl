<#--
	通用查询模板
-->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- base jquery -->
<script type="text/javascript" src="${request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<!-- ligerUI -->
<script type="text/javascript" src="${request.contextPath}/js/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/ligerUI/js/plugins/ligerTree.js"></script>
<link href="${request.contextPath}/js/ligerUI/skins/koala/css/style-all.css" rel="stylesheet" type="text/css" />  
<!-- common js --> 
<script type="text/javascript" src="${request.contextPath}/js/koala/koala-ui.plugin.js"></script>
<script src="${request.contextPath}/js/koala/Koala.js" type="text/javascript"></script>
<script src="${request.contextPath}/js/koala/Validate.js" type="text/javascript"></script>
<!-- common css -->
<link href="${request.contextPath}/css/koala-common.css" rel="stylesheet" type="text/css" /> 
<!-- auth -->
<script src="${request.contextPath}/js/common/common.js" type="text/javascript"></script>
<link href="${request.contextPath}/css/auth-common.css" rel="stylesheet" type="text/css" /> 
<script type="text/javascript">
var gridManager;
var _dialog;
$(function (){
	PageLoader.initSearchPanel();
	gridManager = PageLoader.initGridPanel();
<#list gq.dynamicQueryConditions as dqc>
<#if dqc.queryOperation.operator == "between">
	<#if dqc.widgetType == "DATE">
		$("#${dqc.fieldName}SvID").ligerDateEditor();
		$("#${dqc.fieldName}EvID").ligerDateEditor();
	</#if>
</#if>
</#list>
});

PageLoader = {
	initSearchPanel:function() {
	},	
initGridPanel:function(){
	 var g = $("#maingrid").ligerGrid({
         checkbox: false,
         pageSize: 10,
         height: "400px",
         rowHeight: 28,//行默认的高度
         headerRowHeight: 30,//表头行的高度
         onToNext: next,//条件查询方法
         columns: [
         <#-- ligerGrid列定义 -->
         <#list gq.fieldDetails as fd>
         {
         	display: "${fd.label}",
         	name: "${fd.fieldName}"
         }
         <#if fd_index lt gq.fieldDetails?size - 1>
         ,
         </#if>
         </#list>
         ], 
        enabledEdit: false, clickToEdit: false, isScroll: false,  
        newPage: 1,
        url: "${request.contextPath}/search/${gqId}.koala",
        width: '100%'
    });
	return g;
 }
}


//查询方法
function searchAction(){
    //form validate
   	if(!Validator.Validate(document.getElementById("searchForm"), 3)) {
   		return;
   	}
	var param = $("#searchForm").serialize();
	gridManager.loadServerData(param);/*执行服务器查询*/
}

/*分页条件查询参数*/
function next(){
	gridManager.setOptions({
		parms: [
		<#-- 查询条件 -->
		<#list gq.dynamicQueryConditions as dqc>
		<#if dqc.queryOperation.operator == "between">
		{
			name:"${dqc.fieldName}Start@",
			value: $("#${dqc.fieldName}SvID").val()
		}, 
		{
			name:"${dqc.fieldName}End@",
			value: $("#${dqc.fieldName}EvID").val()
		}, 
		<#else>
		{
			name:"${dqc.fieldName}",
			value: $("#${dqc.fieldName}ID").val()
		}, 
		</#if>
		</#list>
		{}]
	});
}

</script>
</head>
<body>
<div style="width:98%;height:100%;padding-left: 5px;">
<!-- search form -->
<#if gq.visiblePreQueryConditions?size gt 0>
静态条件：<br>
</#if>
<#list gq.visiblePreQueryConditions as preQueryCondition>
	<#if preQueryCondition.visible == true>
		${preQueryCondition.fieldName}  ${preQueryCondition.queryOperation.operator}  ${preQueryCondition.value}<br>
	</#if>
</#list>
<form name="searchForm" id="searchForm" target="_self">
<input type="hidden" name="page" value="1">
<input type="hidden" name="pagesize" value="10">
<table border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td>
<table width="800" border="0" style="margin:5px">
 <tr>
<#-- 动态查询条件 -->
<#list gq.dynamicQueryConditions as dqc>
	<#if dqc.queryOperation.operator == "between">
		<td>${dqc.label}</td>
		<td>从</td>
		<td><input class="input-common" name="${dqc.fieldName}Start@" type="text" id="${dqc.fieldName}SvID" /></td>
		<td>到</td>
		<td><input class="input-common" name="${dqc.fieldName}End@" type="text" id="${dqc.fieldName}EvID"  /></td>
	<#elseif dqc.queryOperation.operator == "like" || dqc.queryOperation.operator == "=">
		<td>${dqc.label}</td>
		<td><input name="${dqc.fieldName}" class="input-common" type="text" id="${dqc.fieldName}ID"  /></td>
	<#else>
		<td>${dqc.label}</td>
		<td>${dqc.queryOperation.operator}</td>
		<td><input name="${dqc.fieldName}" class="input-common" type="text" id="${dqc.fieldName}ID"  /></td>
	</#if>
</#list>
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
