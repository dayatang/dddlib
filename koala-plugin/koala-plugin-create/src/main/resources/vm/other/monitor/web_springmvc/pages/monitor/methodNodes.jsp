<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>method监控节点列表</title><!--[if IE 6]> 
<script type="text/javascript" src="http://sentsin.com/lily/lib/png.js"></script>
<script type="text/javascript">DD_belatedPNG.fix('.ie6PNG');</script>
<![endif]-->
<%@ include file="/pages/common/header.jsp" %>
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="<c:url value='/js/jqplot/excanvas.js'/>"></script><![endif]-->
<link type="text/css" rel="stylesheet" href="<c:url value='/css/jqplot/shCoreDefault.min.css'/>" />
<link type="text/css" rel="stylesheet" href="<c:url value='/css/jqplot/shThemejqPlot.min.css'/>" />
<LINK TYPE="text/css" REL="stylesheet" HREF="${pageContext.request.contextPath}/css/pageCommon.css" />
<script type="text/javascript" defer="defer"
	src="${pageContext.servletContext.contextPath }/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var urlPart = "${pageContext.request.contextPath}/monitor/Monitor";
        $(document).ready(function () {
        	loadMonitorNodes();
            $("#btnLoadData").bind("click", checkTimes);
            $("#showInfo").bind("change", loadData);
            
        });
        
        var loadMonitorNodes = function(){
        	var url = urlPart + "/queryAllNodes.koala";
        	$.get(url, function(data){
        		//先清空
        		 $("#system").empty();
        		//再赋值
                 for(var index in data['data']){
                	 var node = data['data'][index];
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
        
        //检查天数是否超过30（天数不能超过30）
        var checkTimes = function(){
        	var timeStart = $("#timeStart").val();
        	var timeEnd = $("#timeEnd").val();
        	if(timeStart == "" || timeEnd == ""){
        		alert("时间不能为空！");
        		return;
        	}
        	if(timeStart >= timeEnd){
        		alert("开始时间必须小于结束时间！");
        		return;
        	}
        	var days = daysBetween(timeStart.split(" ")[0],timeEnd.split(" ")[0]);
        	if(days > 30){
        		alert("查询范围不能超过30天！");
        		return;
        	}
        	
        	//导入图形报表和数据报表
        	loadData();
        };
        
      //+--------------------------------------------------- 
      //| 求两个时间的天数差 日期格式为 YYYY-MM-dd ，返回double类型
      //+--------------------------------------------------- 
      function daysBetween(DateOne,DateTwo) { 
	      var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-')); 
	      var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1); 
	      var OneYear = DateOne.substring(0,DateOne.indexOf ('-')); 
	
	      var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-')); 
	      var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1); 
	      var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-')); 
	
	      var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);
	      return Math.abs(cha); 
      } 
      
      var loadData = function () {
    		// 数据报表
    		$("#maingrid").ligerGrid(
    			{
    				columns : [ {
    					display : "方法",
    					//name : "method",
    					width : 450,
    					type : "text",
    					align : "left",
    					//isSort: false,
    					render : function(row){
    						if(row.method != null && row.method != ""){
    							var html = "<label title='"+row.method+"'>"+row.method+"</label>";
    							return html;
    						}
    					}
    				}, {
    					display : $("#showInfo").find("option:selected").text(),
    					name : $("#showInfo").val(),
    					width : 100,
    					type : "text",
    					align : "center",
    					isSort: false
    				}, {
    					display : "操作",
    					width : 70,
    					align : "center",
    					render : function(row){
    						//var test='List MonitorNodeManageApplicationImpl.getSqlsMonitorDetails(String)';
    						var html = "";
    						html = html + "<a href='javascript:void(0);' style='color:blue;' onclick=\"showMethodDetail('"+row.method+"');\">查看明细</a>";
    						return html;
    					}
    				} ],
    				dataAction : 'server',
    				rownumbers : true,
    				url : urlPart + '/methodMonitorCount.koala?timeStart=' + $("#timeStart").val() + '&timeEnd='+$("#timeEnd").val() + '&system=' + $("#system").val() + '&methodCountType=' + $("#showInfo").val(),
    				onSuccess : function(data){
    					try{
    						//图形报表
        					showImg(data);
    					}catch(Exception){
    						
    					}
    					//没有数据时
    					if(data['Rows'].length == 0){
    						var methodCountType = $("#showInfo").val();
    						if(methodCountType == "methodExceptionCount"){
    							$("#showImg").html("<font color='blue'>没有方法出错过。</font>");
    						}else{
    							$("#showImg").html("<font color='blue'>没有方法被调用过。</font>");
    						}
    					}
    					
    				},
    				sortName : 'method',
    				width : '98%',
    				height : '100%',
    				rowHeight: 28,//行默认的高度
    		        headerRowHeight: 30,//表头行的高度
    				heightDiff : -10,
    				checkbox : false
    			});
      };
        
        /**
    	 *弹出层，显示方法统计信息
    	*/
    	function showMethodDetail(method){
    		//alert($("#timeStart").val());
    		$.layer({
    			type : 2,
    		    title : ['<h3>明细</h3>',true],
    		    iframe : {src : '${pageContext.request.contextPath}/pages/monitor/Monitor_methodMonitorDetail.jsp?method='+method+'&timeStart='+$("#timeStart").val()+'&timeEnd='+$("#timeEnd").val()+'&system='+$("#system").val()},
    		    area : ['1024px' , '500px'],
    		    offset : ['20%' , '']
    		});
    	}
        
        function showImg(data){
        	
        	if($("#timeStart").val() == null || $("#timeStart").val() == ""){
        		$("#timeStart").val(data['timeStart']);
        	}
        	
        	if($("#timeEnd").val() == null || $("#timeEnd").val() == ""){
        		$("#timeEnd").val(data['timeEnd']);
        	}
        	
        	$.jqplot.config.enablePlugins = true;
            //var xArray = ['aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..'];
            //var yArray = [10,9,8,7,6,5,4,3,2,1];
            var xArray = [];
            var yArray = [];
            
          	//只显示前10个方法
        	var showNums = 0;
        	if(data['Rows'].length <= 10){
        		showNums = data['Rows'].length;
        	}else{
        		showNums = 10;
        	}

        	//只显示前10个方法（即横轴只显示10条数据）
        	var showNums = 10;
        	 //统计类型
            var methodCountType = $("#showInfo").val();
        	for (var i = 0; i < showNums; i++) {
        		//如果数据未到10条，则剩下的补上空值
        		if( i>=data['Rows'].length ){
        			xArray[i] = '';
        			yArray[i] = '';
        		}else{
        			var chObj = data['Rows'][i];
                    //横坐标长度超过11时,截取显示
                    var xInfo = chObj.method.split(".")[1].split("(")[0];
                    if(xInfo.length > 11){
                    	xInfo = xInfo.substring(0,11) + "..";
                    }
                    //只显示方法名称
                    xArray[i] = xInfo;
                    //xArray[i] = chObj.method;
                    
                    if(methodCountType == "methodCount"){
                    	yArray[i] = chObj.methodCount;
                    }else if(methodCountType == "avgTimeConsume"){
                    	yArray[i] = chObj.avgTimeConsume;
                    }else{
                    	yArray[i] = chObj.methodExceptionCount;
                    }
        		}
                
            }
            
            $("#showImg").text("");
            plot1 = $.jqplot('showImg', [yArray], {
                // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
                animate: !$.jqplot.use_excanvas,
                seriesDefaults:{
                    renderer:$.jqplot.BarRenderer,
                    pointLabels: { show: true }
                },
                axes: {
                    xaxis: {
                    	label:"方法",
                        renderer: $.jqplot.CategoryAxisRenderer,
                        ticks: xArray
                    }
                },
                /* axesDefaults: {
                	min: 1,
                	//max: null,
                	pad: 1
                }, */
                highlighter: { show: false }
            });
        }
        
    </script>
</head>

<BODY>

<div class="list-search">
<ul>
<li>
<label>查询条件</label>
<select id="system"></select>
</li>
<li>
<label>时间：</label>
<input type="text" id="timeStart" name="timeStart" class="Wdate" size="18" value="${timeStart}"
				onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:00',alwaysUseStartDate:true})"/>
        	至
        	<input type="text" id="timeEnd" name="timeEnd" class="Wdate" size="18" value="${timeEnd}"
				onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:00',alwaysUseStartDate:true})"/>
</li>
<li>
<div class="search-btn-area"><input class="btn-normal" type="button" id="btnLoadData" value="查询"/></div>
</li>
</ul>
</div>

<!--显示表单内容-->
<DIV ID="MainArea" style="margin-top:20px;">
		<!-- <DIV CLASS="ItemBlock_Title1"><h3>图形报表</h3></DIV> --> 
		<DIV STYLE="margin-left: 15px;">
			统计内容：<select id="showInfo">
				<option value="methodCount">调用次数</option>
				<option value="avgTimeConsume">平均耗时（毫秒）</option>
				<option value="methodExceptionCount">出错次数</option>
			</select>
		</DIV>
        <DIV CLASS="ItemBlockBorder" STYLE="margin-left: 15px;width:98%;">
            <DIV id="showImg" CLASS="ItemBlock" STYLE="text-align: center; font-size: 16px;"></DIV>
        </DIV>
        <!-- <DIV CLASS="ItemBlock_Title1"><h3>数据报表</h3></DIV>  -->
        <!-- <DIV id="showTable" CLASS="ItemBlockBorder" STYLE="margin-left: 15px;width:98%;height:180px; overflow:scroll;"></DIV> -->
        <DIV id="maingrid" CLASS="ItemBlockBorder" STYLE="margin-left: 15px;"></DIV>
        <!-- 操作 -->
</DIV>
<script type="text/javascript" src="<c:url value='/js/jqplot/jquery.jqplot.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqplot/shCore.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqplot/shBrushJScript.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqplot/shBrushXml.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqplot/jqplot.barRenderer.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqplot/jqplot.pieRenderer.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqplot/jqplot.categoryAxisRenderer.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqplot/jqplot.pointLabels.min.js'/>"></script>
</BODY>
</html>