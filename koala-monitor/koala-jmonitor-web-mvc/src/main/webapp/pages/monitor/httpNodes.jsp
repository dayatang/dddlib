<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/pages/common/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>http监控节点列表</title><!--[if IE 6]> 
<script type="text/javascript" src="http://sentsin.com/lily/lib/png.js"></script>
<script type="text/javascript">DD_belatedPNG.fix('.ie6PNG');</script>
<![endif]-->
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="<c:url value='/js/jqplot/excanvas.js'/>"></script><![endif]-->
<LINK TYPE="text/css" REL="stylesheet" HREF="${pageContext.request.contextPath}/css/pageCommon.css" />
<link class="include" rel="stylesheet" type="text/css" href="<c:url value='/css/jqplot/jquery.jqplot.css'/>" />
<script type="text/javascript" src="<c:url value='/js/jqplot/jquery.jqplot.min.js'/>"></script>
<script type="text/javascript" defer="defer"
	src="${pageContext.servletContext.contextPath }/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var urlPart = "${pageContext.request.contextPath}/monitor/Monitor";
        $(document).ready(function () {
        	loadMonitorNodes();
            $("#btnLoadData").bind("click", checkTimes);
            $("#showInfo").bind("change", loadData);
            
            $("#unit").change(function(){
            	var html = "";
            	if("hour" == $("#unit").val()){
            		html = html + '<input type="text" id="queryTime" name="queryTime" class="Wdate" size="18" value="${queryTime}" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:&quot;yyyy-MM-dd&quot;,alwaysUseStartDate:true})"/>';
            	}else{
            		html = html + '<input type="text" id="queryTime" name="queryTime" class="Wdate" size="18" value="${queryTime}" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:&quot;yyyy-MM&quot;,alwaysUseStartDate:true})"/>';
            	}
            	
            	$("#condition").html(html);
            });
        });
        
        var loadMonitorNodes = function(){
        	var url = "${pageContext.request.contextPath}/monitor/Monitor/queryAllNodes.koala?_"+new Date().getTime();
        	$.get(url, function(data){
        		//先清空
        		 $("#system").empty();
        		//再赋值
        		 data = data['data'];
        		 if(!data)return;
                 for(var index in data){
                	 var node = data[index];
                	 var html = "<option value='"+node['nodeId']+"'>"+node['nodeName']+"</option>";
                	 $("#system").append(html);
        		  }
        		/*loadMonitorNodes方法是异步的，loadData方法要用到loadMonitorNodes异步返回的数据，
        		      因此要放在loadMonitorNodes返回数据之后，否则，loadData有可能会在loadMonitorNodes返回数据之前来获取该方法的变量，
        		      则将会获取到空值。
        		*/
                 loadData();
        	});
        };
        
        //
        var checkTimes = function(){
        	var queryTime = $("#queryTime").val();
        	if(queryTime == ""){
        		//alert("查询时间不能为空！");
        		return;
        	}
        	//导入图形报表和数据报表
        	loadData();
        };
        
        //导入图形报表和数据报表
        var loadData = function () {
        	//横轴值列表
        	var xArray = [];
        	
        	var ajaxDataRenderer = function (url, plot, options) {
                var ret = null;
                var dataJson = [[]];
                $.ajax({
                    async: false,
                    url: url,
                    dataType: "json",
                    success: function (data) {
                    	if(data.actionError){
                			alert(data.actionError);
                			return;
                		}
                    	if($("#queryTime").val() == null || $("#queryTime").val() == ""){
                    		$("#queryTime").val(data['queryTime']);
                    	}
                    	
                        for (var i = 0; i < data['Rows'].length; i++) {
                            var chObj = data['Rows'][i];
                            dataJson[0].push([chObj.dateStr, chObj.httpCount]);
                            xArray[i] = chObj.dateStr;
                        }
                        
                        dataJson.sort();
                        xArray.sort();
                        ret = dataJson;
                        
                        //把数据显示在table中
                         var html = "<table style='width:100%;' border='1px solid #aaa' ><tr><td style='width:120px;' align='center'>时段</td><td style='width:80px;' align='center'>请求次数</td><td></td></tr>";
                        for(var index in data['Rows']){
                       	 html = html + "<tr>";
                       	 var node = data['Rows'][index];
                       	 html = html + "<td align='center'><a onclick=showDetails(this);return false;>"+node['dateStr']+"</a></td>";
                       	 html = html + "<td align='center'>"+node['httpCount']+"</td>";
                       	 html = html + "<td></td>";
                       	 html = html + "</tr>";
               		  	}
                        html = html + "</table>";
                        $("#showTable").text("");
                        $("#showTable").html(html); 
                    }
                });
                return ret;
            };
            var jsonurl = urlPart + "/httpMonitorCount.koala?timestamp=" + new Date().getTime() + 
            		"&system=" + $("#system").val() + "&unit=" + $("#unit").val() + "&queryTime=" + $("#queryTime").val();
            var formatDate = null;
            if("hour" == $("#unit").val()){
            	formatDate = "%H";
            }else{
            	formatDate = "%d";
            }
            var options = {
                title: "HTTP请求情况展现",
                axes: {
                    xaxis: { 
                    	label:"时段",
                    	renderer: $.jqplot.DateAxisRenderer,
                    	ticks: xArray,
                    	tickOptions: { formatString: formatDate}
                    },
                    yaxis: {
                    	label: "请求次数",
                    	min:0,
                    	renderer: $.jqplot.LogAxisRenderer 
                    }
                },
                //悬浮展现控制
                highlighter: { show: true, yvalues: 1, tooltipAxes: "xy",
                    formatString: '<table class="jqplot-highlighter"> \
                <tr><td>时段:</td><td>%s</td></tr> \
                <tr><td>请求次数:</td><td>%s</td></tr></table>'
                },
                dataRenderer: ajaxDataRenderer,
                cursor: {
                    show: true
                },
                dataRendererOptions: { unusedOptionalUrl: jsonurl }
            };
            $("#showImg").text("");
            plot = $.jqplot("showImg", jsonurl, options);
        };
        
        /**
         *弹出层，显示详细信息
        */
        function showDetails(a){
        	$.layer({
        		type : 2,
        	    //title : ['<h3>'+$(a).html()+' 明细</h3>',true],
        	    title : ['<h3>明细</h3>',true],
        	   iframe : {src : '${pageContext.request.contextPath}/pages/monitor/Monitor_httpMonitorDetail.jsp?unit='+$('#unit').val()+'&requestDate='+$(a).html()+'&system='+$('#system').val()},
        	    area : ['1024px' , '500px'],
        	    offset : ['20%' , '']
        	});
        }
    </script>
</head>

<BODY>
<div class="list-search">
<ul>
<li>
<label>查询条件</label>
<select id="system"></select>&nbsp;
        	<select id="unit">
        		<!-- 某一天每小时 -->
        		<option value="hour">查看一天</option>
        		<!-- 某一月每天 -->
        		<option value="day">查看一月</option>
        	</select>
</li>
<li>
<label>选择时间：</label>
<span id="condition">
<input type="text" id="queryTime" name="queryTime" class="Wdate" size="18" value="${queryTime}"
					onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
</span>

</li>
<li>
<div class="search-btn-area"><input class="btn-normal" type="button" id="btnLoadData" value="查询"/></div>
</li>
</ul>
</div>

<!--显示表单内容-->
<DIV ID="MainArea" style="margin-top:20px;">
    <DIV CLASS="ItemBlockBorder" STYLE="margin-left: 15px;width:98%;">
        <DIV id="showImg" CLASS="ItemBlock" STYLE="text-align: center; font-size: 16px;"></DIV>
    </DIV>
    <DIV id="showTable" CLASS="ItemBlockBorder" STYLE="margin-left: 15px;width:98%;height:180px; overflow:scroll;"></DIV>
</DIV>

<script language="javascript" type="text/javascript" src="<c:url value='/js/jqplot/jqplot.highlighter.js'/>"></script>
<script language="javascript" type="text/javascript" src="<c:url value='/js/jqplot/jqplot.json2.min.js'/>"></script>
<script language="javascript" type="text/javascript" src="<c:url value='/js/jqplot/jqplot.dateAxisRenderer.min.js'/>"></script>
<script language="javascript" type="text/javascript" src="<c:url value='/js/jqplot/jqplot.cursor.min.js'/>"></script>
</BODY>
</html>