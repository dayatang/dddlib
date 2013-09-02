<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String id = request.getParameter("id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
var preQueryConditionsIndex = 0;
var dynamicQueryConditionsIndex = 0;
var fieldDetailsIndex = 0;
//当前要修改的记录
var CurrentRecord = null;
$(function (){
	$("select").live("change",function(){
		//alert($(this).val());
		var name = $(this).attr("name");
		if(name!=null && name!="" && "pre"==name.substr(0,3)){
			//获取索引值
			var index = name.split("[")[1].split("]")[0];
			var html = "";
			if($(this).val()=="BETWEEN"){
				html = html + "<input type='text' name='preQueryConditions["+index+"].startValue' size='30'/>";
				html = html + "&nbsp;AND&nbsp;";
				html = html + "<input type='text' name='preQueryConditions["+index+"].endValue' size='30'/>";
			}else{
				html = html + "<input type='text' name='preQueryConditions["+index+"].value' size='30'/>";
			}
			//label元素中注入内容
			$(this).next().html(html);
		}
	});
	
	//新增N行静态查询条件
	$("#staticButton").click(function (){
		//获取静态查询条件行数索引
		var index = 0;
		
		//获取选中的项
		$("input[name='checkbox']:checkbox:checked").each(function(){
			for(var i=0;i<1000;i++){
				var staticColumn = $("#preId"+i).val();
				if( checkNotNull(staticColumn) ){
					continue;
				}else{
					index = i;
					break;
				}
			}
			
			var column = $(this).val().split("==")[0];
			var columnType = $(this).val().split("==")[1];
			//把选中的复选框从列表中删除
			$(this).parent().empty();
			//创建三个元素
	      	var $txt = $("<span name='preQueryConditions"+index+"'><input type='text' id='preId"+index+"' name='preQueryConditions["+index+"].fieldName' size='40' style='background-color: #E0E0E0;' value='"+column+"' readonly='readonly'/>&nbsp;<select name='preQueryConditions["+index+"].queryOperation'><option value='EQ'>等于</option><option value='GT'>大于</option><option value='GE'>大于等于</option><option value='LT'>小于</option><option value='LE'>小于等于</option><option value='NE'>不等于</option><option value='LIKE'>LIKE</option><option value='BETWEEN'>BETWEEN</option></select>&nbsp;<span><input type='text' name='preQueryConditions["+index+"].value' size='30'/></span><font color='red'>*</font><input type='hidden' name='preQueryConditions["+index+"].fieldType' value='"+columnType+"'/>&nbsp;&nbsp;<input type='checkbox' title='是否可见' name='preQueryConditions["+index+"].visible' value='true'/>&nbsp;&nbsp;<a onclick='removeLine(this);' name='pre' value='"+$(this).val()+"'  style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;&nbsp;─</a><br/></span>");
	      	//var $visible = $("&nbsp;&nbsp;<input type='checkbox' title='是否可见' name='preQueryConditions["+index+"].visible' value='true'/>&nbsp;&nbsp;");
	        //var $btn = $("<a onclick='removePre(this);' name='pre' value='"+$(this).val()+"=="+index+"'  style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;&nbsp;─</a>");  
	        //var $br = $("<br/></span>");
			//$("#div1").append($txt).append($visible).append($btn).append($br); 
			$("#div1").append($txt); 
		});
	});
	 
	//新增N行动态查询条件
	$("#dynamicButton").click(function (){
		//获取动态查询条件行数索引
		var index = 0;
		
		//获取选中的项
		$("input[name='checkbox']:checkbox:checked").each(function(){
			for(var i=0;i<1000;i++){
				var dynamicColumn = $("#dynamicId"+i).val();
				if( checkNotNull(dynamicColumn) ){
					continue;
				}else{
					index = i;
					break;
				}
			}

			//alert($(this).val().split("==")[0]+"--"+$(this).val().split("==")[1]);
			var column = $(this).val().split("==")[0];
			var columnType = $(this).val().split("==")[1];
			//把选中的复选框从列表中删除
			$(this).parent().empty();
			//创建三个元素
	      	var $txt = $("<span><input type='text' id='dynamicId"+index+"' name='dynamicQueryConditions["+index+"].fieldName' size='40' style='background-color: #E0E0E0;' value='"+column+"' readonly='readonly'/>&nbsp;<input type='text' name='dynamicQueryConditions["+index+"].label' size='40' value='"+column+"'/>&nbsp;<select name='dynamicQueryConditions["+index+"].widgetType' style='width: 100px;'><option value='TEXT'>文本框</option><option value='DATE'>日期框</option></select><select name='dynamicQueryConditions["+index+"].queryOperation'><option value='EQ'>等于</option><option value='GT'>大于</option><option value='GE'>大于等于</option><option value='LT'>小于</option><option value='LE'>小于等于</option><option value='NE'>不等于</option><option value='LIKE'>LIKE</option><option value='BETWEEN'>BETWEEN</option></select><font color='red'>*</font><input type='hidden' name='dynamicQueryConditions["+index+"].fieldType' value='"+columnType+"'/><a onclick='removeLine(this);' name='dynamic' value='"+$(this).val()+"' style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;&nbsp;─</a><br/></span>");
	        //var $btn = $("<a onclick='removeDynamicLine(this);' value='"+$(this).val()+"' style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;&nbsp;─</a>");  
	        //var $br = $("<br/></span>");
			//$("#div2").append($txt).append($btn).append($br); 
			$("#div2").append($txt);
		});
	});
	
	//新增显示列
	$("#columnShowButton").click(function (){
		//获取显示列行数索引
		var index = 0;
		
		//获取选中的项
		$("input[name='checkbox2']:checkbox:checked").each(function(){
			for(var i=0;i<1000;i++){
				var fieldColumn = $("#fieldId"+i).val();
				if( checkNotNull(fieldColumn) ){
					continue;
				}else{
					index = i;
					break;
				}
			}
			
			//把选中的复选框从列表中删除
			$(this).parent().empty();
			//创建三个元素
	      	var $txt = $("<span><input type='text' id='fieldId"+index+"' name='fieldDetails["+index+"].fieldName' size='40' style='background-color: #E0E0E0;' value='"+$(this).val()+"' readonly='readonly'/>&nbsp;<input type='text' name='fieldDetails["+index+"].label' size='40' value='"+$(this).val()+"'/>&nbsp;<font color='red'>*</font>");
	        var $btn = $("<a onclick='removeShowColumn(this);' value='"+$(this).val()+"' style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;&nbsp;─</a>");  
	        var $br = $("<br/></span>");
	        /* 
	      	//设置删除按钮的onclick事件
			$btn.click(function (){
			  fieldDetailsIndex = fieldDetailsIndex - 1;
			  $txt.remove();
			  $btn.remove();
			  $br.remove();
			  var data = "<label><input type='checkbox' name='checkbox2' value='"+$txt.val()+"'/>"+$txt.val()+"&nbsp;&nbsp;&nbsp;&nbsp;</br></label>";
	          $("#columnsDiv2").append(data);
			});
	      	 */
			$("#div3").append($txt).append($btn).append($br); 
			//fieldDetailsIndex = fieldDetailsIndex + 1;
		});
	});
});

//删除一行条件
/* function remove1(a){
	//alert($(a).attr("value"));
	var nameAttr = $(a).attr("name");
	$(a).parent().remove();
	var data = "<label><input type='checkbox' name='checkbox' value='"+$(a).attr("value")+"'/>"+$(a).attr("value").split("==")[0]+"<br/></label>";
    $("#columnsDiv").append(data);
    
    if(nameAttr == "pre"){
    	//把索引调整成从0顺序开始
    	reIndexForPre();
    }else{
    	//把索引调整成从0顺序开始
    	reIndexForDynamic();
    }
} */
//删除一行条件
function removeLine(a){
	//var value = $(a).attr("value");
	//列==列类型==索引值
	//var valueArr = value.split("==");
	//删除整行
	//$("span[name='preQueryConditions"+valueArr[2]+"']").remove();
	
	//用来区分是删除静态条件，还是动态条件
	var nameAttr = $(a).attr("name");
	//列==列类型
	var value = $(a).attr("value");
	//删除整行
	$(a).parent().remove();
	//添加回列池中
	var data = "<label><input type='checkbox' name='checkbox' value='"+value+"'/>"+value.split("==")[0]+"<br/></label>";
    $("#columnsDiv").append(data);
   
   //把索引调整成从0顺序开始
    if(nameAttr == "pre"){
    	reIndexForPre();
    }else{
    	reIndexForDynamic();
    }
}

/**
 * 把静态查询条件索引调整成从0顺序开始
 */
function reIndexForPre(){
	var index = 0;//name$='fieldName']
	//找到所有name以fieldDetails开头的元素
	$("input[name^='preQueryConditions']").each(function(){
		var nameAttr = $(this).attr("name");
		//获取的当前索引
		//var elemIndex = nameAttr.split("[")[1].split("]")[0];
		if(nameAttr.split(".")[1] == "fieldName"){
			$(this).attr("id","preId"+index);
			$(this).attr("name","preQueryConditions["+index+"].fieldName");
			$(this).next().attr("name","preQueryConditions["+index+"].queryOperation");
		}else if(nameAttr.split(".")[1] == "value"){
			$(this).attr("name","preQueryConditions["+index+"].value");
		}else if(nameAttr.split(".")[1] == "startValue"){
			$(this).attr("name","preQueryConditions["+index+"].startValue");
		}else if(nameAttr.split(".")[1] == "endValue"){
			$(this).attr("name","preQueryConditions["+index+"].endValue");
		}else if(nameAttr.split(".")[1] == "fieldType"){
			$(this).attr("name","preQueryConditions["+index+"].fieldType");
		}else{
			$(this).attr("name","preQueryConditions["+index+"].visible");
			index++;
		}
	});
}

/**
 * 把动态查询条件索引调整成从0顺序开始
 */
function reIndexForDynamic(){
	var index = 0;//name$='fieldName']
	//找到所有name以fieldDetails开头的元素
	$("input[name^='dynamicQueryConditions']").each(function(){
		var nameAttr = $(this).attr("name");
		//获取的当前索引
		//var elemIndex = nameAttr.split("[")[1].split("]")[0];
		//alert(nameAttr.split(".")[1]);
		if(nameAttr.split(".")[1] == "fieldName"){
			$(this).attr("id","dynamicId"+index);
			$(this).attr("name","dynamicQueryConditions["+index+"].fieldName");
		}else if(nameAttr.split(".")[1] == "label"){
			$(this).attr("name","dynamicQueryConditions["+index+"].label");
			//$(this).next().attr("name","dynamicQueryConditions["+index+"].widgetType");
		}else if(nameAttr.split(".")[1] == "widgetType"){
			$(this).attr("name","dynamicQueryConditions["+index+"].widgetType");
		}else if(nameAttr.split(".")[1] == "queryOperation"){
			$(this).attr("name","dynamicQueryConditions["+index+"].queryOperation");
		}else{
			$(this).attr("name","dynamicQueryConditions["+index+"].fieldType");
			index++;
		}
	});
}

//删除一行条件
function removeShowColumn1(a){
	$(a).parent().remove();
	var data = "<label><input type='checkbox' name='checkbox2' value='"+$(a).attr("value")+"'/>"+$(a).attr("value")+"<br/></label>";
    $("#columnsDiv2").append(data); 
    //把索引调整成从0顺序开始
    reIndexForField();
}
//删除一行条件
function removeShowColumn(a){
	$(a).prev().remove();
	$(a).next().remove();
	$(a).remove();
	var data = "<label><input type='checkbox' name='checkbox2' value='"+$(a).attr("value")+"'/>"+$(a).attr("value")+"<br/></label>";
    $("#columnsDiv2").append(data);
    //把索引调整成从0顺序开始
    reIndexForField();
}

/**
 * 把显示列索引调整成从0顺序开始
 */
function reIndexForField(){
    var index = 0;//name$='fieldName']
	//找到所有name以fieldDetails开头的元素
	$("input[name^='fieldDetails']").each(function(){
		var nameAttr = $(this).attr("name");
		//获取的当前索引
		//var elemIndex = nameAttr.split("[")[1].split("]")[0];
		//alert(nameAttr.split(".")[1]);
		if(nameAttr.split(".")[1] != "label"){
			$(this).attr("id","fieldId"+index);
			$(this).attr("name","fieldDetails["+index+"].fieldName");
		}else{
			$(this).attr("name","fieldDetails["+index+"].label");
			index++;
		}
	});
}

function saveDataAction(){
	var url = '<%=contextPath %>/generalquery/update.koala';
   if( !validate() ){
	   alert("缺少必填项");
	   return;
   }
   Koala.ajax({
		type : 'POST',
		url: url,
		dataType:'json',
		data:$("#dataForm").serialize(),
		success: function(json){
			if(json && json.result){
				 alert(json.result);
				 if(json.result == "success"){
					 parent.gridManager.loadData();
					 parent._dialog.close();
				 }
			}
		}
	});
}

function validate(){
	//数据源
	var dataSourceId = $("#dataSourceIdID").val();
	//不能为空和空格
	if( !checkNotNull(dataSourceId) ){
		return false;
	}
	
	//表
	var tableName = $("#tableNameID").val();
	//不能为空和空格
	if( !checkNotNull(tableName) ){
		return false;
	}
	
	var flag = true;
	$("input:text").each(function(){
		if(this.id != "descriptionID"){
			//不能为空和空格
			if( !checkNotNull(this.value) ){
				//alert(this.name + "=" + this.value);
				flag = false;
			}
		}
	}); 
	
	return flag;
}

/**
 * 检查变量是否不为空  true:不空   false:空
 */
function checkNotNull(item){
	 //不能为空和空格
	if(item==null || item=="" || item.replace(/(^\s*)|(\s*$)/g, "")=="" ){
		return false;
	}else{
		return true;
	}
}

var loadCurrentRecord = function(){
	//alert($("#generalQueryId").val());
	var url = "${pageContext.request.contextPath}/generalquery/getById.koala?id=" + $("#generalQueryId").val();
	$.get(url, function(data){
		CurrentRecord = data['data'];
		//alert(CurrentRecord.dataSourceId);
	});
};

/* 
var loadDataSourceList = function(){
	var url = "${pageContext.request.contextPath}/generalquery/findAllDataSource.koala";
	$.get(url, function(data){
		//先清空
		 $("#dataSourceIdID").empty();
		 $("#dataSourceIdID").append("<option value=''>--选择数据源--</option>");
		 if(data && !data.result){
			//再赋值
			  var datas = data['dataSourceList'];
			  for(var index in datas){
	        	 var node = datas[index];
	        	 var data = "<option value='"+node['id']+"'>"+node['dataSourceId']+"</option>";
	        	 $("#dataSourceIdID").append(data);
			  }
		 }
	});
};
 */
 /* 
var loadTableList = function(){
	//先清空
	$("#tableNameID").empty();
	$("#tableNameID").append("<option value=''>--选择表--</option>");
	 
	var id = $("#dataSourceIdID").val();
	if(id != "0"){
		var url = "${pageContext.request.contextPath}/generalquery/findAllTable.koala?id=" + id;
		$.get(url, function(data){
			var datas = data['tableList'];
			//再赋值
	         for(var index in datas){
	        	 var node = datas[index];
	        	 var data = "<option value='"+node+"'>"+node+"</option>";
	        	 $("#tableNameID").append(data);
			  }
		});
	}
};
 */
 
var loadColumnList = function(){
	//$("#showColumns").hide();
	//$("#showColumns2").hide();
	//先清空
	//$("#columnsDiv").empty();
	//$("#columnsDiv2").empty();
	
	var id = $("#dataSourceIdID").val();
	var tableName = $("#tableNameID").val();
	var url = "${pageContext.request.contextPath}/generalquery/findAllColumn.koala?id=" + id + "&tableName=" + tableName;
	$.get(url, function(data){
		//var index = 0;
		//再赋值
         for(var pro in data['tableMap']){
        	 //alert(data['tableMap'][pro]);//获取列类型
        	 var data = "<label><input type='checkbox' name='checkbox' value='"+pro+"'/>"+pro+"<br/></label>";
        	 $("#columnsDiv").append(data);
        	 var data2 = "<label><input type='checkbox' name='checkbox2' value='"+pro+"'/>"+pro+"</br></label>";
        	 $("#columnsDiv2").append(data2);
		  }
	});
	//$("#showColumns").show();
	//$("#showColumns2").show();
};

</script>
<style type="text/css">
	.form2column{
		border:#D2D2D2 1px solid;
		width:95%;
	}
	.form2column td.label{
		width:55px;
		height:22px;
		line-height:22px;
		text-align:center;
	}
}
</style>
</head>
<body>
<div class="form_body">
  	<form id="dataForm" name="dataForm">
		<input type="hidden" id="generalQueryId" name="id" value="<%=id %>" />
		<input type="hidden" id="version" name="version" value="${data.version}" />
  		<div style="margin-left: 20px;height: 25px;">
  			<!-- <select id="dataSourceIdID" name="dataSource.id">
	  			<option value="0">--选择数据源--</option>
	  			<option value="">dataSource</option>
	  			<option value="">test</option>
	  		</select> -->
	  		<label style="font-weight:bold;">数据源：</label>
	  		<input type='text' id="dataSourceIdID" name='dataSource.dataSourceId' value="${data.dataSource.dataSourceId }" size='25' style='background-color: #E0E0E0;' readonly='readonly'/>
	  		<input type='hidden' name='dataSource.id' value="${data.dataSource.id }" />
	  		<!-- <select id="tableNameID" name="tableName">
	  			<option value="">--选择表--</option>
	  			
	  			<option value="">t_user</option>
	  			<option value="">t_log</option>
	  		</select> -->
	  		&nbsp;&nbsp;
	  		<label style="font-weight:bold;">表：</label>
	  		<input type='text' id="tableNameID" name='tableName' value="${data.tableName }" size='30' style='background-color: #E0E0E0;' readonly='readonly'/>
	  		&nbsp;&nbsp;
	  		<label style="font-weight:bold;">查询器名称：</label>
	  		<input type="text" id="queryNameID" name="queryName" value="${data.queryName }" style='background-color: #E0E0E0;' readonly='readonly'/>
	  		&nbsp;&nbsp;
	  		<label style="font-weight:bold;">描述：</label>
	  		<input type="text" id="descriptionID" name="description" value="${data.description }"/>
  		</div>
  		<div id="showColumns" style="margin-left: 47px;float:left;width:300px;" >
  			<div>
  				<label style="font-weight:bold;">所有列：</label>
  			</div>
  			<div id="columnsDiv" style="border-style:inset;">
  				<c:forEach items="${tableMapLeftDiv }" var="entry">
  					<label><input type='checkbox' name='checkbox' value='${entry.key }==${entry.value }'/>${entry.key }<br/></label>
  				</c:forEach>
  			</div>
  			<div>
  				<input type="button" id="staticButton" class="btn-normal" value="添加到静态查询条件" />
  				<input type="button" id="dynamicButton" class="btn-normal" value="添加到动态查询条件" />
  			</div>
  		</div>
  		<div id="showColumns2" style="margin-left: 47px;float:left;width:300px;" >
  			<div>
  				<label style="font-weight:bold;">所有列：</label>
  			</div>
  			<div id="columnsDiv2" style="border-style:inset;">
  				<c:forEach items="${tableMapRightDiv }" var="entry">
  					<label><input type='checkbox' name='checkbox2' value='${entry.key }'/>${entry.key }<br/></label>
  				</c:forEach>
  			</div>
  			<div>
  				<input type="button" id="columnShowButton" class="btn-normal" value="添加到显示列" />
  			</div>
  		</div>
  		<!-- 该div防止 静态查询条件div错位-->
  		<div style="margin-left: 47px;color: white"><label>--------------------------------------------------------------------------------------------------------------------------------------------------------</label></div>
  		<div id="staticQuery" style='margin-left: 47px;width:850px;'>
  			<div><label style="font-weight:bold;">静态查询条件</label></div>
	  		<!-- <table border="1" cellpadding="0" cellspacing="0" class="form2column">
	           <tr>
		  			<td class="label">列</td>
		  			<td class="label">条件</td>
		  			<td class="label">是否可见</td>
	  			</tr>
	  		</table> -->
	  		<div id="div1">
	  			<c:forEach items="${data.preQueryConditions }" var="preBean" varStatus="idxStatus">
  					<span>
	  					<input type='text' id="preId${idxStatus.index}" name="preQueryConditions[${idxStatus.index}].fieldName" 
	  						value='${preBean.fieldName }' size='40' style='background-color: #E0E0E0;' readonly='readonly'/>
	  					<%-- <select name="preQueryConditions[${idxStatus.index}].queryOperation">
		  					<option value='EQ' <c:if test="${preBean.queryOperation=='EQ' }">selected="selected"</c:if>>等于<option>
		  					<option value='GT' <c:if test="${preBean.queryOperation=='GT' }">selected="selected"</c:if>>大于<option>
		  					<option value='GE' <c:if test="${preBean.queryOperation=='GE' }">selected="selected"</c:if>>大于等于<option>
		  					<option value='LT' <c:if test="${preBean.queryOperation=='LT' }">selected="selected"</c:if>>小于<option>
		  					<option value='LE' <c:if test="${preBean.queryOperation=='LE' }">selected="selected"</c:if>>小于等于<option>
		  					<option value='NE' <c:if test="${preBean.queryOperation=='NE' }">selected="selected"</c:if>>不等于<option>
		  					<option value='LIKE' <c:if test="${preBean.queryOperation=='LIKE' }">selected="selected"</c:if>>like<option>
	  					</select> --%>
	  					<select name="preQueryConditions[${idxStatus.index}].queryOperation">
	  						<option value='EQ' <c:if test="${preBean.queryOperation=='EQ' }">selected="selected"</c:if>>等于</option>
	  						<option value='GT' <c:if test="${preBean.queryOperation=='GT' }">selected="selected"</c:if>>大于</option>
	  						<option value='GE' <c:if test="${preBean.queryOperation=='GE' }">selected="selected"</c:if>>大于等于</option>
	  						<option value='LT' <c:if test="${preBean.queryOperation=='LT' }">selected="selected"</c:if>>小于</option>
	  						<option value='LE' <c:if test="${preBean.queryOperation=='LE' }">selected="selected"</c:if>>小于等于</option>
	  						<option value='NE' <c:if test="${preBean.queryOperation=='NE' }">selected="selected"</c:if>>不等于</option>
	  						<option value='LIKE' <c:if test="${preBean.queryOperation=='LIKE' }">selected="selected"</c:if>>like</option>
	  						<option value='BETWEEN' <c:if test="${preBean.queryOperation=='BETWEEN' }">selected="selected"</c:if>>BETWEEN</option>
	  					</select>
	  					<span>
		  					<c:choose>
		  						<c:when test="${preBean.queryOperation=='BETWEEN' }">
		  							<input type='text' name="preQueryConditions[${idxStatus.index}].startValue" value="${preBean.startValue }" size='30'/>
		  							&nbsp;AND&nbsp;
		  							<input type='text' name='preQueryConditions[${idxStatus.index}].endValue' value="${preBean.endValue }" size='30'/>
		  						</c:when>
		  						<c:otherwise>
		  							<input type='text' name="preQueryConditions[${idxStatus.index}].value" value="${preBean.value }" size='30'/>
		  						</c:otherwise>
		  					</c:choose>
	  					</span>
	  					<font color='red'>*</font>
	  					<input type='hidden' name='preQueryConditions[${idxStatus.index}].fieldType' value='${preBean.fieldType }'/>
	  					<input type="checkbox" title="是否可见" name="preQueryConditions[${idxStatus.index}].visible" value="true" <c:if test="${preBean.visible==true }">checked="checked"</c:if> />
	  					<a onclick="removeLine(this);" name="pre" value="${preBean.fieldName }==${preBean.fieldType }" href='#' title='删除' style='font-size:14px;color: blue;text-decoration:none;' >&nbsp;─</a>
	  					<br/>
  					</span>
  				</c:forEach>
	  		</div>
  		</div>
  		<br/>
  		<div id="dynamicQuery" style='margin-left: 47px;width: 800px;'>
  			<div><label style="font-weight:bold;">动态查询条件</label></div>
  			<div>
  			<!-- &nbsp;<a id="dynamicButton" style='font-size:16px;color: blue;text-decoration:none;' href="#" title='新增'><b>+</b></a> -->
  			</div>
  			<table border="1" cellpadding="0" cellspacing="0" class="form2column" style="width:86%">
	           <tr>
		  			<td class="label" style="width:37%">列</td>
		  			<td class="label" style="width:38%">页面显示名称</td>
		  			<td class="label" style="width:15%">输入框类型</td>
		  			<td class="label">操作</td>
	  			</tr>
	  		</table>
  			<div id="div2">
  				<c:forEach items="${data.dynamicQueryConditions }" var="dynamicBean" varStatus="idxStatus">
  					<span>
	  					<input type='text' id="dynamicId${idxStatus.index}" name="dynamicQueryConditions[${idxStatus.index}].fieldName" 
	  						value='${dynamicBean.fieldName }' size='40' style='background-color: #E0E0E0;' readonly='readonly'/>
	  					<input type='text' name="dynamicQueryConditions[${idxStatus.index}].label" 
	  						size='40' value='${dynamicBean.label }'/>
	  					<select name="dynamicQueryConditions[${idxStatus.index}].widgetType" style='width: 100px;'>
	  						<option value='TEXT' <c:if test="${dynamicBean.widgetType=='TEXT' }">selected="selected"</c:if>>文本框</option>
	  						<option value='DATE' <c:if test="${dynamicBean.widgetType=='DATE' }">selected="selected"</c:if>>日期框</option>
	  					</select>
	  					<select name="dynamicQueryConditions[${idxStatus.index}].queryOperation">
	  						<option value='EQ' <c:if test="${dynamicBean.queryOperation=='EQ' }">selected="selected"</c:if>>等于</option>
	  						<option value='GT' <c:if test="${dynamicBean.queryOperation=='GT' }">selected="selected"</c:if>>大于</option>
	  						<option value='GE' <c:if test="${dynamicBean.queryOperation=='GE' }">selected="selected"</c:if>>大于等于</option>
	  						<option value='LT' <c:if test="${dynamicBean.queryOperation=='LT' }">selected="selected"</c:if>>小于</option>
	  						<option value='LE' <c:if test="${dynamicBean.queryOperation=='LE' }">selected="selected"</c:if>>小于等于</option>
	  						<option value='NE' <c:if test="${dynamicBean.queryOperation=='NE' }">selected="selected"</c:if>>不等于</option>
	  						<option value='LIKE' <c:if test="${dynamicBean.queryOperation=='LIKE' }">selected="selected"</c:if>>like</option>
	  						<option value='BETWEEN' <c:if test="${dynamicBean.queryOperation=='BETWEEN' }">selected="selected"</c:if>>BETWEEN</option>
	  					</select>
	  					<font color='red'>*</font><input type='hidden' name='dynamicQueryConditions[${idxStatus.index}].fieldType' value='${dynamicBean.fieldType }'/>
	  					<a onclick="removeLine(this);" name="dynamic" value="${dynamicBean.fieldName }==${dynamicBean.fieldType }" href='#' title='删除' style='font-size:14px;color: blue;text-decoration:none;' >&nbsp;─</a>
	  					<br/>
  					</span>
  				</c:forEach>
  			</div>
  		</div>
  		
  		<br/>
  		<div id="columnShowDiv" style='margin-left: 47px;width: 550px;'>
  			<div><label style="font-weight:bold;">显示列</label></div>
  			<table border="1" cellpadding="0" cellspacing="0" class="form2column">
	           <tr>
		  			<td class="label">列</td>
		  			<td class="label">显示列名称</td>
	  			</tr>
	  		</table>
  			<div id="div3">
  				<c:forEach items="${data.fieldDetails }" var="fieldBean" varStatus="idxStatus">
  					<span>
	  					<input type='text' id="fieldId${idxStatus.index}" name="fieldDetails[${idxStatus.index}].fieldName" value='${fieldBean.fieldName }' 
	  						size='40' style='background-color: #E0E0E0;' readonly='readonly'/>
	  					<input type='text' name="fieldDetails[${idxStatus.index}].label" value='${fieldBean.label }' size='40'/>
	  					<font color='red'>*</font>
	  					<a onclick="removeShowColumn1(this);" value="${fieldBean.fieldName }" href='#' title='删除' style='font-size:14px;color: blue;text-decoration:none;' >&nbsp;─</a>
	  					<br/>
  					</span>
  				</c:forEach>
  			</div>
  		</div>
	  	
	  <div class="form_button">
        <input id="searchButton" type="button" class="btn-normal" onclick="saveDataAction()" value="保存" />
     </div>
  	</form>
</div>
</body>
</html>
