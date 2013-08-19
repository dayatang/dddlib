<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>监控节点预警信息</title>
<%@ include file="/pages/common/header.jsp" %>
<style type="text/css">
.list{float:left;}
.list li{text-align:left; padding-left:5px;height:25px;border:#FFFFCC dashed 1px; text-decoration:underline;}
.config_text{width:400px; height:35px; border:#CCCCCC solid 1px;background:#FFCC99;}
.common-table ul{margin:0; padding:0; float:left;}
.common-table li{ float:left; margin-left:5px; margin-top:10px;  list-style:none; width:100%; text-align:left;font-size:13px;}
.common-table li span{color:#FF0000;font-weight:normal;text-decoration:underline; color:#707070; }
</style>
<%
 String nodeId = request.getParameter("nodeId") == null ? "" : request.getParameter("nodeId");
%>
<script type="text/javascript">

$(function(){
	var url = "${pageContext.request.contextPath}/monitor/NodeInfo/generalStatus.koala?nodeId=<%=nodeId%>";
	$.get(url, function(data){
		try {
			parent.loadTabData();
			parent.parent.showLoading(false);
		} catch (e) {}
		if(data.actionError){
			alert(data.actionError);
			return;
		}
		
		data = data['monitorStatus'];
		for(var index in data){
			if(index == 'abnormalIps'){
				var html = "";
				for(var index2 in data[index]){
					html = html + data[index][index2] + "<br>";
				}
				$("#abnormalIps").html(html);
			}else if(index == 'abnormalServices'){
				var html = "";
				for(var index2 in data[index]){
					html = html + data[index][index2] + "<br>";
				}
				$("#abnormalServiceList").html(html);
			}else if(index == 'serverInfo'){
				for(var index2 in data[index]){
					$("#serverInfo_"+index2).html(data[index][index2]);
				}
			}else{
				$("#"+index).text(data[index]);
			}
		}
		
	});
});

</script>
</head>
<body>
<div class="toolbar">
<ul>
  <li>刷新时间:<span></span>&nbsp;&nbsp;</li>
</ul>
</div>
<table class="common-table">
  <thead>
  <tr>
    <th style="width:100px;">预警选项</th>
    <th style="width:300px;">预警详情</th>
    <th>备注</th>
  </tr>
  </thead>
<tbody id="tbody">
<tr>
  <td>页面请求</td>
  <td>
     <ul>
	    <LI>当前在线人数:<span id="activeCount"></span></LI>
		<LI>页面平均响应速度:<span id="pageAvgResponseTime"></span>秒</LI>
		<LI>页面响应速度最大值:<span id="maxAvgTimePage"></span></LI>
		<LI>异常请求IP:<br><span id="abnormalIps" style="color:red;"></span></LI>
	 </ul>
  </td>
  <td></td>
</tr>

<tr>
  <td>后台服务</td>
  <td>
     <ul>
	    <LI>方法调用次数:<span id="methodCallCount"></span></LI>
		<LI>异常次数:<span id="methodExceptionCount"></span></LI>
		<LI>异常率:<span id="exceptionRate"></span></LI>
		<LI>调用最多方法:<span id="mostCallMethod"></span></LI>
		<LI>平均耗时最长方法:<span id="maxAvgTimeMethod"></span></LI>
	 </ul>
  </td>
  <td></td>
</tr>

<tr>
  <td>第三方接口/服务</td>
  <td>
     <LI>检查时间:<span id="serviceCheckTime"></span></LI>
     <LI>不正常服务列表:<br><span id="abnormalServiceList" style="color:red;"></span></LI>
  </td>
  <td></td>
</tr>
  
<tr>
  <td>服务器状态</td>
  <td>
  <ul>
        <LI>CPU:<span id="serverInfo_cpu"></span></LI>
		<LI>内存:<span id="serverInfo_mem"></span></LI>
		<LI>JVM:<span id="serverInfo_jvm"></span></LI>
	 </ul>
  </td>
  <td></td>
</tr>

</tbody>
</table>
</body>
</html>