<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>连接池监控节点列表</title><!--[if IE 6]> 
<script type="text/javascript" src="http://sentsin.com/lily/lib/png.js"></script>
<script type="text/javascript">DD_belatedPNG.fix('.ie6PNG');</script>
<![endif]-->
<%@ include file="/pages/common/header.jsp" %>
<script type="text/javascript">
	var urlPart = "${pageContext.request.contextPath}/monitor/Monitor";
        $(document).ready(function () {
        	loadMonitorNodes();
            $("#nodes").bind("change", loadData);
            
            $("#page_redirct").bind("change", function(){
            	var url = $(this).val();
            	if(location.href.indexOf(url)>0)return;
            	location.href = url + "?nodeId="+$("#nodes").val();
            });
        });
        
        var loadMonitorNodes = function(){
        	var url = urlPart + "/queryAllNodes.koala";
        	$.get(url, function(data){
        		//先清空
        		 $("#nodes").empty();
        		//再赋值
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
        		<%    }
        		 %>
        			loadData();
        		}
        	});
        };
        
        //导入数据报表
        var loadData = function () {
        	var nodeId = $("#nodes").val();
        	if(nodeId == '')return;
        	$.ajax({
                async: false,
                url: urlPart + "/poolMonitorDetail.koala?nodeId="+nodeId,
                dataType: "json",
                success: function (data) {
                    
                    var html = "";
                    for(var key in data['pools']){
                    	var node = data['pools'][key];
                    	html = html + "<div title='" + key + "' style='width:80%;'>";
                    	
                    	html = html + "<table style='width:100%;' class='form2column'>";
                    	html = html + "<tr><td class='label' style='width:200px;'>创建时间：</td><td>&nbsp;"+node['startTime']+"</td></tr>";
                    	html = html + "<tr><td class='label'>快照时间：</td><td>&nbsp;"+node['snapshotTime']+"</td></tr>";
                    	html = html + "<tr><td class='label'>初始化连接数：</td><td>&nbsp;"+node['initConnectionCount']+"</td></tr>";
                    	html = html + "<tr><td class='label'>正在使用连接数：</td><td>&nbsp;"+node['activeConnectionCount']+"</td></tr>";
                    	html = html + "<tr><td class='label'>可用连接数：</td><td>&nbsp;"+node['idleConnectionCount']+"</td></tr>";
                    	html = html + "<tr><td class='label'>最大连接数：</td><td>&nbsp;"+node['maxConnectionCount']+"</td></tr>";
                    	html = html + "<tr><td class='label'>最大存活时间：</td><td>&nbsp;"+node['maxActiveTime']+"秒</td></tr>";
                    	//正在使用连接数百分比
                    	//alert(node['activeConnectionCount']);
                    	var connUsing = Math.round(node['activeConnectionCount']/node['maxConnectionCount']*100);
                    	//可用的连接数
                    	var connUseable = Math.round(node['idleConnectionCount']/node['maxConnectionCount']*100);
                    	//未创建连接数
                    	var connUnCreate = 100 - connUsing - connUseable;
                    	
                    	html = html + "<tr><td class='label'>连接数比例：</td><td align='center'><font color='#FF0000'>在用</font>/<font color='#00EC00'>可用</font>/<font color='#D0D0D0'>未创建</font><br><div style='width:100%;margin: 0px;'><div style='float:left;width:"+connUsing+"%;background-color:#FF0000;height:20px;' title='正在使用连接数："+connUsing+"%'><label>&nbsp;<label></div><div style='float:left;width:"+connUseable+"%;background-color:#00EC00;height:20px;' title='可用连接数："+connUseable+"%'><label>&nbsp;<label></div><div style='float:left;width:"+connUnCreate+"%;background-color:#D0D0D0;height:20px;' title='未创建连接数："+connUnCreate+"%'><label>&nbsp;<label></div></div></td></tr>";
                    	html = html + "</table>";
                    	
                    	//表头
                    	html = html + "<div style='margin-right: 17px;' ><table style='width:100%;' border='1px solid #aaa'>";
                    	html = html + "<tr><td width='5%' align='center'>&nbsp;</td><td width='20%' align='center'>创建时间</td><td width='20%' align='center'>上一次激活时间</td><td width='15%' align='center'>存活时间（毫秒）</td><td align='center'>源</td></tr>";
                    	html = html + "</table></div>";
                    	
                    	html = html + "<div STYLE='height:220px; overflow:scroll;'><table style='width:100%;' border='1px solid #aaa'>";
                    	var connDetails = node['connDetails'];
                    	for(var index in connDetails){
                    		html = html + "<tr onmouseover=\"this.bgColor='#ACD6FF'\" onmouseout=\"this.bgColor=''\">";
                    		//可用
                    		if(connDetails[index]['idle'] == true){
                    			html = html + "<td style='background-color:#79FF79;' width='5%' align='center'>"+index+"</td>";
                    		}else{//在用
                    			html = html + "<td style='background-color:#FF0000;' width='5%' align='center'>"+index+"</td>";
                    		}
                    		html = html + "<td width='20%' align='center'>"+connDetails[index]['formatCreateTime']+"</td>";
                    		html = html + "<td width='20%' align='center'>"+connDetails[index]['formatLastActiveTime']+"</td>";
                    		html = html + "<td width='15%' align='center'>"+connDetails[index]['activeTime']+"</td>";
                    		/* if(connDetails[index]['idle'] == true){
                        		html = html + "<td width='15%' align='center'>是</td>";
                    		}else{
                    			html = html + "<td width='15%' align='center'>否</td>";
                    		} */
                    		var source = connDetails[index]['source'] || '';
                    		if(connDetails[index]['idle'] == true){
                    			html = html + "<td align='center'><span class='greenLink' key='"+connDetails[index]['threadKey']+"' >"+source+"</span></td>";
                    		}else{//在用
                    			html = html + "<td align='center'><span>"+source+"</span></td>";
                    		}
                    		html = html + "</tr>";
                    	}
                    	html = html + "</table></div>";
                    	
                    	html = html + "</div>";
                    }

                	$("#showTable").text("");
                    $("#showTable").html(html);
                    
                    $("#showTable").ligerTab();
                }
            });
        };
        
       
        $('.greenLink').live('click',function(){
        	var source = $(this).text();
        	var traceKey = $(this).attr('key');
        	var url = '${pageContext.request.contextPath}/pages/monitor/Monitor_methodMonitorDetail.jsp?method='+source+'&traceKey='+traceKey;
        	$.layer({
    			type : 2,
    		    title : ['<h3>方法明细</h3>',true],
    		    iframe : {src : url},
    		    area : ['1024px' , '500px'],
    		    offset : ['20%' , '']
    		});
        });
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

<!--显示表单内容-->
<DIV ID="MainArea">
	<!-- <DIV  CLASS="ItemBlockBorder" STYLE="margin-left: 15px;width:98%;height:500px; overflow:scroll;">
		
	</DIV> -->
	<div id="showTable" 
		style="margin-left: 15px; width:98%; overflow: hidden; border: 1px solid #A3C0E8;"></div>
</DIV>

</BODY>
</html>