<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>监控节点服务器控制台</title>
<%@ include file="/pages/common/header.jsp" %>
<style type="text/css">
.tab_div{margin:5px;height:440px;overflow-x:hidden;}
.block_panel{ float:left;width:99%;}
.block_table{ float:left; width:250px; text-align:left; border:0;}
.block_icon_disk{text-align:center; background:url(${pageContext.request.contextPath}/images/icons/other/disk.jpg) no-repeat;}
.block_icon_cpuInfos{text-align:center; background:url(${pageContext.request.contextPath}/images/icons/other/disk.jpg) no-repeat;}
.block_stat{ background:#FFFFFF; border:#666666 solid 1px; height:15px; width:150px;}

.cmd_table{width:800px;}
.cmd_table select{ width:700px; height:30px; background:#FFCC99;}
.cmd_table textarea{ width:800px; height:400px; background:#D9FFD9; border:#FFFF00 solid 1px;}
</style>
<%
 String nodeId = request.getParameter("nodeId") == null ? "" : request.getParameter("nodeId");
%>
<script type="text/javascript">
var id = "<%=nodeId%>";

var allLoadFinished=false;
$(function (){
	parent.showLoading(true);
   $("#navtab1").ligerTab({contextmenu:false}); 
});

function loadTabData(){
	parent.showLoading(true);
	Koala.ajax({
		type : 'POST',
		url: "${pageContext.request.contextPath}/monitor/NodeInfo/getServerStatus.koala",
		dataType:'json',
		data:{nodeId:id},
		success: function(json){
			allLoadFinished = true;
			var data = json['data'];
			var _target;
			var html;
			for(var index in data){
				if(index == 'diskInfos'){
					html = createDiskInfosHtml(index,data[index]);
				}else if(index == 'cpuInfos'){
					html = createCpuInfosHtml(data[index]);
				}else{
					html = data[index];
				}
				_target = $("#"+index);
				if(_target)_target.html(html);
			}
			var memPercent = (100 * data['usedMem']/data['totalMem']).toFixed(1) +"%";
			var swapPercent = (100 * data['usedSwap']/data['totalSwap']).toFixed(1) +"%";
			$("#mem_percent").css("width",memPercent);
			$("#swap_percent").css("width",swapPercent);
			$("#memPercent").html(memPercent);
			$("#swapPercent").html(swapPercent);
			parent.showLoading(false);
		}
	});
}

function createCpuInfosHtml(datas){
	var html = "";
	for(var index in datas){
		var info = datas[index];
		var title = info['vendor'] + " " + info['model'];
		var usedPercent = info['used'];
		html = html + "<div class=\"form_tltle\">第 "+(parseInt(index) + 1)+" 块CPU</div>";
		html = html + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"form2column\">";
		html = html + "<tr><td class=\"label\">CPU信息:</td>";
		html = html + "<td>"+title+"</td></tr>";
		html = html + "<tr><td class=\"label\">使用情况:</td>";
		html = html + "<td><div class=\"block_stat\"><div style=\"background:#46AF6D;height:15px; width:"+usedPercent+";\">&nbsp;</div></div> &nbsp;使用率："+usedPercent+"</td>";
		html = html + "</table>";
	}
	return html;
}

function createDiskInfosHtml(type,datas){
	var html = "";
	var name,title,availSize,freeSize,percentSize;
	for(var index in datas){
		var info = datas[index];
		name = "磁盘 "+info['devName'];
		totalSize = info['totalSize'];
		availSize = info['availSize'];
		percentSize = 150 * (info['usedSize']/info['totalSize']);
		html = html + "<table class=\"block_table\">";
		html = html + "<tr><th width=\"20%\">&nbsp;</th><th width=\"80%\">"+name+"</th></tr>";
		html = html + "<tr>";
		html = html + "<td rowspan=\"2\" class=\"block_icon_disk\"></td>";
		html = html + "<td><div class=\"block_stat\"><div style=\"background:#46AF6D;height:15px; width:"+percentSize+"px;\">&nbsp;</div></div></td>";
		html = html + "</tr>";
		html = html + "<tr><td>"+availSize+"G 可用 共"+totalSize+"G </td></tr>";
		html = html + "</table>";
	}
	return html;
}
</script>
</head>
<body>
<div id="navtab1" style="width: 828px;overflow:hidden; border:1px solid #A3C0E8; ">
<div tabid="home" title="预警信息" lselected="true" class="tab_div" >
<iframe id="tab1_iframe"  name="tab1_iframe"  frameborder="0" height="600" width="700" scrolling="yes" src="nodeWarnInfo.jsp?nodeId=<%=nodeId%>" ></iframe>
</div>
<div title="运行环境" class="tab_div" >
<div class="block_panel">
<table border="0" cellpadding="0" cellspacing="0" class="form2column">

 <tr>
	 <td class="label">当前服务器时间:</td>
	 <td><span id="serverTime"></span></td>
</tr>
 <tr>
	 <td class="label">服务器名:</td>
	 <td><span id="serverName"></span></td>
</tr>
<tr>
	 <td class="label">操作系统:</td>
	 <td><span id="serverOs"></span></td>
</tr>
<tr>
	 <td class="label">Java 容器:</td>
	 <td><span id="javaServer"></span></td>
</tr>
<tr>
	 <td class="label">部署目录:</td>
	 <td><span id="deployPath"></span></td>
</tr>
 <tr>
	 <td class="label">Java目录:</td>
	 <td><span id="javaHome"></span></td>
</tr>
 <tr>
	 <td class="label">Java版本:</td>
	 <td><span id="javaVersion"></span></td>
</tr>
<tr>
	 <td class="label">Java临时目录:</td>
	 <td><span id="javaTmpPath"></span></td>
</tr>
<tr>
	 <td class="label">JVM总内存:</td>
	 <td><span id="jvmTotalMem"></span> M</td>
</tr>
<tr>
	 <td class="label">JVM空闲内存:</td>
	 <td><span id="jvmFreeMem"></span> M</td>
</tr>
</table>
</div>
</div>
<div  title="CPU信息" class="tab_div" >
<div class="block_panel" id="cpuInfos">
</div>
</div>
<div  title="内存信息"  class="tab_div" >
<div class="block_panel">
<table border="0" cellpadding="0" cellspacing="0" class="form2column">
 <tr>
	 <td class="label">总内存容量:</td>
	 <td><span id="totalMem"></span> M</td>
</tr>
 <tr>
	 <td class="label">已用内存:</td>
	 <td><span id="usedMem"></span> M</td>
</tr>
 <tr>
	 <td class="label">可用内存:</td>
	 <td><span id="freeMem"></span> M</td>
</tr>
 <tr>
	 <td class="label">使用率:</td>
	 <td><div class="block_stat"><div id="mem_percent" style="background:#46AF6D;height:15px; width:30px;">&nbsp;</div></div><span id="memPercent"></span></td>
</tr>
 <tr>
	 <td class="label">总交换区容量:</td>
	 <td><span id="totalSwap"></span> M</td>
</tr>
 <tr>
	 <td class="label">已用交换区:</td>
	 <td><span id="usedSwap"></span> M</td>
</tr>
 <tr>
	 <td class="label">可用交换区:</td>
	 <td><span id="freeSwap"></span> M</td>
</tr>
 <tr>
	 <td class="label">使用率:</td>
	 <td><div class="block_stat"><div id="swap_percent" style="background:#46AF6D;height:15px; width:30px;">&nbsp;</div></div><span id="swapPercent"></span></td>
</tr>
</table>
</div>
</div>
<div title="磁盘信息" class="tab_div" >
<div class="block_panel" id="diskInfos"></div>
</div>
</div>
</body>
</html>
