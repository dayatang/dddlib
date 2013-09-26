<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据库监控</title> 
<%@ include file="/pages/common/header.jsp" %>
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="<c:url value='/js/jqplot/excanvas.js'/>"></script><![endif]-->
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/jqplot/shCoreDefault.min.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/jqplot/shThemejqPlot.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/shCore.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/shBrushJScript.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/shBrushXml.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqplot/jqplot.pointLabels.min.js"></script>
<script type="text/javascript">
$(function(){
	loadMonitorNodes();
    $("#nodes").bind("change", loadData);
    
    $("#page_redirct").bind("change", function(){
    	var url = $(this).val();
    	if(location.href.indexOf(url)>0)return;
    	location.href = url + "?nodeId="+$("#nodes").val();
    });
});

function loadData(){
	var limit = $("#limit").val();
	if(limit == null && parseInt(limit)<0){
		alert("时间限制条件仅支持整数");
		return;
	}
	var params = "nodeId=" + $("#nodes").val() + "&limit=" + limit;
	var url = "${pageContext.request.contextPath}/monitor/Monitor/jdbcTimeStat.koala?"+ params;
	$.get(url, function(data){
		data = data.data;
		var xarray = eval('['+data[1]+']');
		var yarray = eval('['+data[0]+']');
		drawBar(xarray,yarray);
	});
}

function loadMonitorNodes(){
	var url = "${pageContext.request.contextPath}/monitor/Monitor/queryAllNodes.koala?_"+new Date().getTime();
	$.get(url, function(data){
		 $("#nodes").empty();
		 var activeNodeCount = 0;
         for(var index in data['data']){
        	 var node = data['data'][index];
        	 if(node['active'] == false || node['active'] == 'false')continue;
        	 var html = "<option value='"+node['nodeId']+"'>"+node['nodeName']+"</option>";
        	 $("#nodes").append(html);
        	 activeNodeCount++;
		  }
         if(activeNodeCount>0){
    		 <%
    			String nodeId = request.getParameter("nodeId");
    		    if(nodeId != null){
    		 %>   
    		 $("#nodes option[value='<%=nodeId%>']").attr("selected", true); 
    		<%}%>
    		  loadData();
    		}
	});
};


function drawBar(xarray,yarray) {
	$('#chartArea').empty();
	$.jqplot.config.enablePlugins = true;
    var plot = $.jqplot('chartArea', [xarray], {
    	title: "<span style='color:#707070; font-size:18px;'>24小时内数据库连接超过阀值统计</span>",
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
            	label:"<span style='color:#707070; font-size:18px;font-weight:bold;'>时段</span>",
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: yarray
            },
            yaxis: {
            	label: "超时次数",
            	min:0
            }
        }
    });
    $('#chartArea').bind('jqplotDataClick', 
        function (ev, seriesIndex, pointIndex, data) {
            
        }
    );
}
</script>
</head>

<BODY>
<div class="toolbar">
	<ul style="float:left; padding-left:20px;">
	  <li style="float:left"><span style="color:#707070; font-size:13px;font-weight:bold;">选择监控节点系统:</span><select id="nodes"></select>&nbsp;&nbsp;</li>
	  <li style="float:left"><span style="color:#707070; font-size:13px;font-weight:bold;">选择监控项:</span>
	  <select id="page_redirct">
	    <option value="dbNodes.jsp">连接耗时分布图</option>
		<option value="poolNodes.jsp">连接池状态图</option>
	  </select>&nbsp;&nbsp;</li>
	</ul>
</div>

<div class="list-search" style="height:25px;">
	<ul>
		<li>
			<label>仅查看时间超过：</label>
			<input class="input-common" type="text" id="limit" value="60" onblur="loadData();">秒数据库连接
		</li>
	</ul>
</div>

<div class="container">
	<div id="chartArea"></div>
</div>

</BODY>
</html>