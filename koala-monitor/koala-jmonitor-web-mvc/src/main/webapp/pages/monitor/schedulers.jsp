<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>定时任务监控管理</title>
<%@ include file="/pages/common/header.jsp" %>
<script src="${pageContext.request.contextPath}/js/common/validateCronExprUtil.js" type="text/javascript"></script>
<style type="text/css">
.list{float:left;}
.list li{text-align:left; padding-left:5px;height:25px;border:#FFFFCC dashed 1px; text-decoration:underline;}
.config_text{width:400px; height:35px; border:#CCCCCC solid 1px;background:#FFCC99;}
</style>
<script type="text/javascript">
$(function(){
	var url = "${pageContext.request.contextPath}/monitor/ServiceMonitor/queryAllSchedulers.koala";
	$.get(url, function(data){
		 var tbodyContent="";
		 var row = 1;
		 data=data['data'];
		 for(var index in data){
			 tbodyContent = tbodyContent + "<tr class='"+(row%2==0?'grid-row-even':'grid-row-odd')+"'>";
			 tbodyContent = tbodyContent + "<td>"+data[index]['schedulerName']+"</td>";
			 tbodyContent = tbodyContent + "<td>"+data[index]['triggerName']+"</td>";
			 tbodyContent = tbodyContent + "<td><input class='input-big' style='width:180px;background:#FFFF9D;' value='"+data[index]['cronExpr']+"' id='input_"+data[index]['triggerName']+"'><input type='button' class='btn-normal' value='更新' onclick='updateCronExpr(\""+data[index]['triggerName']+"\")'></td>";
			 var lasttime = data[index]['lastBeginRunTime']? data[index]['lastBeginRunTime'].toString().replace(/T/g,' ') : "";
			 tbodyContent = tbodyContent + "<td>"+lasttime+"</td>";
			 tbodyContent = tbodyContent + "<td><img src='${pageContext.request.contextPath}/images/icons/16x16/"+(data[index]['active']==true ? 'true':'false')+".gif' height='16px' width='16px' /></td>";
			 tbodyContent = tbodyContent + "<td><span>"+(data[index]['running']==true ? '运行中':'空闲中')+"</span></td>";
			 tbodyContent = tbodyContent + "<td><input class='btn-normal' type='button' value='"+(data[index]['active']==true ? '停用':'启用')+"' onclick='changeStatus(\""+data[index]['triggerName']+"\","+data[index]['active']+");'/></td>";
			 tbodyContent = tbodyContent + "</tr>";
			 row++;
		 }
		 $('#tbody').html(tbodyContent);
	});
});

function updateCronExpr(triggerName){

	  var confExpr = $("#input_"+triggerName).val();
	  if(cronValidate(confExpr) == false){
	      alert("时间表达式不合法，请参考相关实例");
	      return;
	  }
	    $.ajax({
	         url : "${pageContext.request.contextPath}/monitor/ServiceMonitor/updateScheduleConf.koala",
	         datatype:"json",
	         data:{triggerName:triggerName,confExpr:confExpr},
	         complete:function(data){
	        	 if(data.success){
			    	$.ligerDialog.success('更新成功');
			     }else{
			    	alert('更新失败');
			     }
			 }
		});
	}


function changeStatus(triggerName,currentStatus){
    $.ajax({
         url : "${pageContext.request.contextPath}/monitor/ServiceMonitor/updateScheduleConf.koala",
         datatype:"json",
         data:{triggerName:triggerName,currentStat:currentStatus+""},
         complete:function(json){
        	 if(json.success != true){
	 			alert("修改失败");
	 			return;
	 		  }
		      window.location.reload();
		 }
	});
}

function showExample(){
    $.ligerDialog.open( {
		height : 500,
		url : '${pageContext.request.contextPath}/pages/static/cronExpr.html',
		width : 539,
		title : '时间表达式实例'
	});
}


</script>
</head>
<body>
<div class="toolbar">
<ul>
 <li><a href="javascript:void(0)" onclick="showExample()" onfocus="this.blur()"><img src="${contextPath}/images/icons/16x16/ico-helps.gif" alt="时间表达式实例" title="点击查看时间表达式实例" /><span>时间表达式实例&nbsp;</span></a></li>
</ul>
</div>
<table class="common-table">
  <thead>
  <tr>
    <td width="180" >任务名称</td>
	<td width="150" >定时器标识</td>
	<td width="275">执行时间表达式</td>
	<td>上一次执行时间</td>
	<td width="100" >是否启用</td>
	<td width="100" >当前状态</td>
	<td width="100" >操作</td>
  </tr>
  </thead>
<tbody id="tbody">

</tbody>
</table>
</body>
</html>
