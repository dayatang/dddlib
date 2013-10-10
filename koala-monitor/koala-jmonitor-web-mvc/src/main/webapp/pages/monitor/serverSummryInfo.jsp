<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务器运行状态信息</title>
<%@ include file="/pages/common/header.jsp" %>
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.dateAxisRenderer.min.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.highlighter.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.cursor.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jqplot/jquery.jqplot.min.css" />
<%
 String nodeId = request.getParameter("nodeId") == null ? "" : request.getParameter("nodeId");
%>
<script>
var id = "<%=nodeId%>";
$(document).ready(function(){
		var url = "${pageContext.request.contextPath}/monitor/NodeInfo/serverSummryInfo.koala?nodeId=<%=nodeId%>&_t"+new Date().getTime();
		$.get(url, function(data){
			$("#span1").html(data.activeCount || 0);
			$("#span2").html(data.pageAvgResponseTime || 0);
			$("#span3").html(data.activeCount || '');
			$("#span4").html(data.mostCallMethod || '');
			$("#span5").html(data.maxAvgTimeMethod || '');
			$("#exception_percent").css("width",data.exceptionRate).html(data.exceptionRate);
			
			var serverInfo = eval("("+data['serverInfo']+")");
			if(serverInfo && serverInfo.error){
				$("div.error").html(serverInfo.error);
				return;
			}
			$("#serverinfo").show();
			var memData=serverInfo.MEMORY;
			if(memData){
				var dataArray = [[]];
				for(var index in memData){
					dataArray[0].push([index, memData[index]]);
				}
				drawMemStatusChart(dataArray);
			}
			var cpuData=serverInfo.CPU;
			if(cpuData){
				drawCpuStatusChart(cpuData);
			}
			
			var diskData=serverInfo.DISK;
			if(diskData){
				drawDiskStatusChart(diskData);
			}
		});
	});


	function drawMemStatusChart(datas){  
		   //var datas=[['2013-08-27 09',67],['2013-08-27 10',45],['2013-08-27 11',76],['2013-08-27 12',55],['2013-08-27 13',90],['2013-08-27 14',11],['2013-08-27 15',55],['2013-08-27 16',37],['2013-08-27 17',88],['2013-08-27 18',99],['2013-08-27 19',55],['2013-08-27 20',34],['2013-08-27 21',0],['2013-08-27 22',0],['2013-08-27 23',0],['2013-08-27 9',0],['2013-08-28 0',0],['2013-08-28 1',0],['2013-08-28 2',0],['2013-08-28 3',0],['2013-08-28 4',0],['2013-08-28 5',0],['2013-08-28 6',0],['2013-08-28 7',0],['2013-08-28 8',44.87]];
		   $.jqplot('memchart', datas, {
			   axes:{
			        xaxis:{
			          renderer:$.jqplot.DateAxisRenderer,
			          tickOptions:{formatString:'%y-%m-%d %H'},
			          //tickInterval:'2 hour'
			        },
			        yaxis: {
			          min:0,
			          max:100
			        }
			      },
			    //悬浮展现控制
	                highlighter: { show: true, yvalues: 1, tooltipAxes: "xy",
	                    formatString: '<table class="jqplot-highlighter"> \
	                <tr><td>时段:%s</td></tr> \
	                <tr><td>内存使用率:%s%</td></tr></table>'
	                },
	                cursor: {
	                    show: true
	                }
		  });
	}
	
	function drawCpuStatusChart(datas){
		var i = 0;
		for(var index in datas){
			var title = index;
			var dataArray = [[]];
			for(var index2 in datas[index]){
				dataArray[0].push([index2, datas[index][index2]]);
			}
			var cpuchartId = "cpuchart"+i;
			$("<div id='"+cpuchartId+"'></div>").appendTo("div.cpu_info");
			$.jqplot(cpuchartId, dataArray, {
				  title:title,
			      axes:{
			        xaxis:{
			          renderer:$.jqplot.DateAxisRenderer,
			          tickOptions:{formatString:'%y-%m-%d'},
			          //tickInterval:'1 hour'
			        },
			        yaxis: {
				       min:0,
				       max:100
				    }
			      },
			    //悬浮展现控制
	                highlighter: { show: true, yvalues: 1, tooltipAxes: "xy",
	                    formatString: '<table class="jqplot-highlighter"> \
	                <tr><td>时段:%s</td></tr> \
	                <tr><td>CPU使用率:%s%</td></tr></table>'
	                },
	                cursor: {
	                    show: true
	                }
			    
			  });
		   i++;
		}
	}
	
	function drawDiskStatusChart(datas){
		var diskInfo;
		for(var disk in datas){
			diskInfo = datas[disk];
			var diskpieId = "diskpie"+disk.replace(/[^a-zA-Z0-9]/g,"");
			$("<li><div class='diskname'>磁盘名称："+disk.replace(/[^a-zA-Z0-9]/g,"")+"</div><div id='"+diskpieId+"'></div></li>").appendTo("#disklist");
			
			var total = diskInfo['total'];
			var used = diskInfo['used'];
			$.jqplot(diskpieId, [[['total',total],['used',used]]], {
		        gridPadding: {top:0, bottom:38, left:0, right:0},
		        seriesDefaults:{
		            renderer:$.jqplot.PieRenderer,
		            trendline:{ show:true },
		            rendererOptions: { padding: 8, showDataLabels: true }
		        },
		        legend:{
		            show:true
		        }      
		    });
		}
	}

</script>

<style type="text/css">
.block_panel{ float:left;width:99%;}
.block_table{ float:left; width:250px; text-align:left; border:0;}
.block_icon_disk{text-align:center; background:url(${pageContext.request.contextPath}/images/icons/other/disk.jpg) no-repeat;}
.block_icon_cpuInfos{text-align:center; background:url(${pageContext.request.contextPath}/images/icons/other/disk.jpg) no-repeat;}
.block_stat{ background:#FFFFFF; border:#666666 solid 1px; height:15px; width:150px;}
.title{height:20px;font-size:16px; color:#999999; text-align:center;font-weight:bold;margin:10px;}
.disk_info{ width:900px; float:left;}
.disk_info ul{ list-style:none;}
.disk_info li{ width:22%; float:left; padding-left:10px;}
</style>
</head>

<BODY>
<div class="toolbar">
<ul>
  <li></li>
</ul>
</div>
<div class="app_info">
<table border="0" cellpadding="0" cellspacing="0" class="form2column">
 <tr>
	 <td class="label" style="width:130px;">在线人数:</td>
	 <td><span id="span1"></span></td>
</tr>
 <tr>
	 <td class="label">页面平均响应速度:</td>
	 <td><span id="span2"></span> 秒</td>
</tr>
 <tr>
	 <td class="label">耗时最长页面:</td>
	 <td><span id="span3"></span></td>
</tr>
 <tr>
	 <td class="label">调用最多方法:</td>
	 <td><span id="span4"></span></td>
</tr>
 <tr>
	 <td class="label">耗时最长方法:</td>
	 <td><span id="span5"></span></td>
</tr>
 <tr>
	 <td class="label">异常率:</td>
	 <td><div class="block_stat"><div id="exception_percent" style="background:#46AF6D;height:15px; width:30px;">&nbsp;</div></div><span id="swapPercent"></span></td>
</tr>
</table>
</div>
<div class="title error"></div>
<!--  -->
<div id="serverinfo" style="display:none;">
<div class="memory_info">
<div class="title">24小时内存使用情况</div>
<div id="memchart"></div>
</div>
<!--  -->
<div class="cpu_info">
<div class="title">24小时CPU使用情况</div>
<div id="cpuchart"></div>
</div>
<!--  -->
<div class="disk_info">
<div class="title">磁盘使用情况</div>
<ul id="disklist">
</ul>
</div>
</div>
</BODY>
</html>