<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>监控节点列表</title>
<%@ include file="/pages/common/header.jsp" %>
<style type="text/css">
.list{float:left;}
.list li{text-align:left; padding-left:5px;height:25px;border:#FFFFCC dashed 1px; text-decoration:underline;}
.config_text{width:400px; height:35px; border:#CCCCCC solid 1px;background:#FFCC99;}
</style>
<script type="text/javascript">
var _plugins = {};
var _dialog;
var _dialog2;
$(function(){
	var url = "${pageContext.request.contextPath}/monitor/NodeInfo/queryAllNodes.koala?_"+new Date().getTime();
	$.get(url, function(data){
		 var html = "";
         for(var index in data['data']){
        	 if(data['data'][index]['active'] == false || data['data'][index]['active'] == 'false')continue;
        	 html = html + "<tr>";
        	 var node = data['data'][index];
        	 var nodeId = node['nodeId'];
        	 html = html + "<td>"+nodeId+"</td>";
        	 html = html + "<td>"+node['nodeName']+"</td>";
        	 html = html + "<td>"+node['nodeUri']+"</td>";
        	 //
        	 html = html + "<td><ul class='list'>";
        	 var conponents = node['conponents'];
        	 for(var ii in conponents){
        		 var comp = conponents[ii];
        		 var key = nodeId + "->" + comp['type'];
        		 _plugins[key] = comp;
        		 html = html + "<li class='grayLink' bind='"+key+"'>"+comp['name']+"<img src='${pageContext.request.contextPath}/images/icons/16x16/"+(comp['active']==true ? 'true':'false')+".gif' height='16px' width='16px' /></li>";
        	 }
        	 html = html + "</ul></td>";
        	 html = html + "<td>"+node['latestSessionTime'].toString().replace(/T/g,' ')+"</td>";
        	 html = html + "<td><a class='grayLink' href=\"javascript:pageServerSummryInfo('"+nodeId+"')\">进入控制台</a></td>";
        	 html = html + "</tr>";
		  }
         $("#tbody").html(html);
         
         $("li[bind]").bind('click',function(){
        	 var pluginKey = $(this).attr('bind');
        	 var aa = pluginKey.split("->");
        	 $("#nodeId").val(aa[0]);
        	 $("#compType").val(aa[1]);
        	 var conponent = _plugins[pluginKey];
        	 $("#name").html(conponent.name);
        	 $("#comp_active_"+conponent.active).attr('checked','checked');
        	 var text = conponent.properties['trace-filter-active-time'] || '-1';
        	 $("#tracetime").val(text);
        	 
        	 text = conponent.properties['trace-timeout'] || '60';
        	 $("#tracetimeout").val(text);
        	 
        	 text = conponent.properties['trace-stack'] || 'false';
        	 $("#stack_"+text).attr('checked','checked');
        	 
        	 var propContent = "";
        	 if(conponent.type == 'HTTP'){
        		 $("input[id^='stack_']").parent().hide();
        		 propContent = propContent + "<div class=\"prop_item\">仅监控以下URL(<span class='red12'>多个用分号\";\"隔开</span>)</div>";
        		 text = conponent.properties['includeUrls'] || '';
        		 propContent = propContent + "<textarea propName=\"includeUrls\" class='config_text'>"+text+"</textarea>";
        		 propContent = propContent + "<div class=\"prop_item\">排除以下URL(<span class='red12'>多个用分号\";\"隔开</span>)</div>";
        		 text = conponent.properties['excludeUrls'] || '';
        		 propContent = propContent + "<textarea propName=\"excludeUrls\" class='config_text'>"+text+"</textarea>";
        	 }else if(conponent.type == 'JDBC'){
        		 $("input[id^='stack_']").parent().hide();
        	 }else if(conponent.type == 'METHOD'){
        		 $("input[id^='stack_']").parent().show();
        		 propContent = propContent + "<div class=\"prop_item\">监控以下类或者方法(<span class='red12'>多个用分号\";\"隔开</span>)</div>";
        		 var text = conponent.properties['detect-clazzs'] || '';
        		 propContent = propContent + "<textarea propName=\"detect-clazzs\" class='config_text'>"+text+"</textarea>";
        		 propContent = propContent + "<div class=\"prop_item\">监控以下包(<span class='red12'>多个用分号\";\"隔开</span>)</div>";
        		 text = conponent.properties['detect-packages'] || '';
        		 propContent = propContent + "<textarea propName=\"detect-packages\" class='config_text'>"+text+"</textarea>";
        	 }
        	 $("#specProps").html(propContent);
        	 _dialog.show();
         });
	});
	
	_dialog = $.ligerDialog.open({ title:"修改监控参数",target: $("#compConfDialog"),width:550 });
	_dialog.hidden();
	
	_dialog2 = $.ligerDialog.open({ title:"数据同步配置",target: $("#SyncDataConfDialog"),width:400 });
	_dialog2.hidden();
});


function saveDataAction(){
	var props = "&props=";
	$("*[propName]").each(function(){
		var _this = $(this);
		props = props + _this.attr('propName') + "->"+_this.val()+"@@@";
	});
	Koala.ajax({
		type : 'POST',
		url: "${pageContext.request.contextPath}/monitor/NodeInfo/updateMonitorConfig.koala",
		dataType:'json',
		data:$("#dataform").serialize() + props,
		success: function(json){
			_dialog.hidden();
			window.location.reload();
		}
	});
}


function openSyncDataConfDig(){
	var url = "${pageContext.request.contextPath}/monitor/NodeInfo/getScheduleConf.koala";
	$.get(url, function(data){
		if(data.actionError){
			alert(data.actionError);
			return;
		}
		data = data['data'];
		$("#schedule_lastTime").html(data.lastBeginRunTime);
		$("#schedule_interval").val(data.interval);
		$("#schedule_active_"+data.active).attr('checked','checked');
		_dialog2.show();
	});
}

function updateSynadataConf(){
	var url = "${pageContext.request.contextPath}/monitor/NodeInfo/updateScheduleConf.koala";
	Koala.ajax({
		type : 'POST',
		url: url,
		dataType:'json',
		data:$("#dataform2").serialize(),
		success: function(json){
			if(json.actionError){
				alert(json.actionError);
				return;
			}
			_dialog2.hidden();
		}
	});
}

function pageServerSummryInfo(nodeId){
	var url = "${pageContext.request.contextPath}/pages/monitor/serverSummryInfo.jsp?nodeId="+nodeId;
	parent.topDialog = parent.$.ligerDialog.open( {
		height : window.screen.availHeight - 150,
		url : url,
		width : window.screen.availWidth - 50,
		isHidden : false,
		title : '实时监控信息'
	});
}

</script>
</head>
<body>
<div class="toolbar">
<ul>
  <li><a href="javascript:openSyncDataConfDig();"><span>同步监控数据配置</span></a></li>
</ul>
</div>
<table class="common-table">
  <thead>
  <tr>
    <th width="100px">监控节点标识</th>
    <th>监控节点名称</th>
    <th>监控节点URL</th>
    <th>监控内容</th>
    <th>最后响应时间</th>
    <th>操作</th>
  </tr>
  </thead>
<tbody id="tbody">

</tbody>
</table>
</body>
<div style="display:none;" id="compConfDialog" >
<form id="dataform">
<input type="hidden" id="nodeId" name="nodeId"/>
<input type="hidden" id="compType" name="compType" />
<table border="0" cellpadding="0" cellspacing="0" class="form2column">
 <tr>
	 <td class="label">监控内容:</td>
	 <td><span id="name"></span></td>
</tr>
<tr>
	 <td class="label">当前状态:</td>
	 <td>
	    <label><input type="radio" id="comp_active_true"  name="active" value="true" />激活</label>
	    <label><input type="radio" id="comp_active_false"  name="active" value="false" />关闭</label>
	 </td>
</tr>
<tr>
	 <td class="label">监控参数:</td>
	 <td id="td_config">
	 <div id="commonProps">
	 <div class="prop_item">仅监控执行时间超过<input id="tracetime" propName="trace-filter-active-time" style="width:75px; height:20px;background:#FFCC99;" />毫秒的过程</div>
	 <div class="prop_item">执行超过<input id="tracetimeout" propName="trace-timeout" style="width:75px; height:20px;background:#FFCC99;" />毫秒的过程标记为超时</div>
      <div class="prop_item">
       开启堆栈信息跟踪：<input id="stack_true" name="stack" type="radio" propName="trace-stack"  value="true" />激活
	    <input type="radio" id="stack_false" name="stack" propName="trace-stack" value="false" />关闭
      </div>
	 </div>
	 <div id="specProps"></div>
	 </td>
</tr>
</table>
<div class="form_button">
        <input id="searchButton" type="button" class="btn-normal" onclick="saveDataAction()" value="更新" />
	  	 &nbsp;&nbsp;
	  	<input type="button" class="btn-normal" onclick="_dialog.hidden();" value="关闭" />
</div>
</form>
<br>
</div>

<div style="display:none;" id="SyncDataConfDialog" >
<form id="dataform2">
<table border="0" cellpadding="0" cellspacing="0" class="form2column">

<tr>
	 <td class="label">同步数据服务:</td>
	 <td>
	    <label><input type="radio" id="schedule_active_true" name="active" value="1" />激活</label>
	    <label><input type="radio" id="schedule_active_false"  name="active" value="0" />关闭</label>
	 </td>
</tr>
 <tr>
	 <td class="label">同步数据间隔:</td>
	 <td><input id="schedule_interval"  name="interval" style="width:80px; height:20px;background:#FFCC99;" />秒</td>
</tr>

<tr>
	 <td class="label">上一次同步数据:</td>
	 <td><span id="schedule_lastTime" ></span></td>
</tr>

</table>
<div class="form_button">
        <input type="button" class="btn-normal" onclick="updateSynadataConf()" value="更新" />
	  	 &nbsp;&nbsp;
	  	<input type="button" class="btn-normal" onclick="_dialog2.hidden();" value="关闭" />
</div>
</form>
<br>
</div>
</html>