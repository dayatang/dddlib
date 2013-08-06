<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
$(function (){
	//加载所有数据源
	loadDataSourceList();
	
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
	
	//加载所有表
	$("#dataSourceIdID").change(function(){
		//先清空
		$("#columnsDiv").empty();
		$("#columnsDiv2").empty();
		$("#div1").empty();
		$("#div2").empty();
		$("#div3").empty();
		//再加载所有表
		loadTableList();
	});
	
	//加载所有列
	$("#tableNameID").change(function(){
		//先清空
		$("#div1").empty();
		$("#div2").empty();
		$("#div3").empty();
		//再加载所有列
		loadColumnList();
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
			//把选中的复选框从列表中删除
			$(this).parent().empty();
			//创建三个元素
	      	//var $txt = $("<input type='text' name='preQueryConditions["+preQueryConditionsIndex+"].fieldName' size='40' style='background-color: #E0E0E0;' value='"+$(this).val()+"' readonly='readonly'/><input type='text' name='preQueryConditions["+preQueryConditionsIndex+"].value' size='40' value='"+$(this).val()+"'/>  <select name='preQueryConditions["+preQueryConditionsIndex+"].value'><option value='>'>大于<option><option value='>='>大于等于<option><option value='<'>小于<option><option value='<='>小于等于<option><option value='='>等于<option><option value='='>不等于<option><option value='like'>like<option></select><input type='text' name='staticConditionPart2' size='30'/><font color='red'>*</font>");
	      	var $txt = $("<input type='text' id='preId"+index+"' name='preQueryConditions["+index+"].fieldName' size='40' style='background-color: #E0E0E0;' value='"+column+"' readonly='readonly'/><select name='preQueryConditions["+index+"].queryOperation'><option value='EQ'>等于<option><option value='GT'>大于<option><option value='GE'>大于等于<option><option value='LT'>小于<option><option value='LE'>小于等于<option><option value='NE'>不等于<option><option value='LIKE'>LIKE<option><option value='BETWEEN'>BETWEEN<option></select><span><input type='text' name='preQueryConditions["+index+"].value' size='30'/></span><font color='red'>*</font>");
	      	var $hide = $("<input type='hidden' value='"+$(this).val().split("==")[1]+"'/>");
	        var $visible = $("<input type='checkbox' title='是否可见' name='preQueryConditions["+index+"].visible' value='true'/>&nbsp;&nbsp;");
	        var $btn = $("<a style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;─</a>");  
	        var $br = $("<br/>");
	      	//设置删除按钮的onclick事件
			$btn.click(function (){
			  //alert($txt.val());
			  $txt.remove();
			  $hide.remove();
			  $visible.remove();
			  $btn.remove();
			  $br.remove();
			  //alert($(this).val());
			  var data = "<label><input type='checkbox' name='checkbox' value='"+$txt.val()+"=="+$hide.val()+"'/>"+$txt.val()+"&nbsp;&nbsp;&nbsp;&nbsp;</br></label>";
	          $("#columnsDiv").append(data);
	          
	         //把索引调整成从0顺序开始
	      	 reIndexForPre();
			});
			$("#div1").append($txt).append($visible).append($btn).append($br); 
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
			
			//alert($(this).val()+"=="+$(this).val().split("==")[0]+"类型=="+$(this).val().split("==")[1]);
			var column = $(this).val().split("==")[0];
			var columnType = $(this).val().split("==")[1];
			var select = null;
			if(columnType==91 || columnType==92 || columnType==93){
				select = "<option value='TEXT'>文本框</option><option value='DATE' selected='selected'>日期框</option>";
			}else{
				select = "<option value='TEXT' selected='selected'>文本框</option><option value='DATE'>日期框</option>";
			}
			//把选中的复选框从列表中删除
			$(this).parent().empty();
			//创建三个元素//<select name='preQueryConditions["+index+"].queryOperation'><option value='EQ'>等于<option><option value='GT'>大于<option><option value='GE'>大于等于<option><option value='LT'>小于<option><option value='LE'>小于等于<option><option value='NE'>不等于<option><option value='LIKE'>LIKE<option></select><label><input type='text' name='preQueryConditions["+index+"].value' size='30'/>
	      	var $txt = $("<input type='text' id='dynamicId"+index+"' name='dynamicQueryConditions["+index+"].fieldName' size='40' style='background-color: #E0E0E0;' value='"+column+"' readonly='readonly'/><input type='text' name='dynamicQueryConditions["+index+"].label' size='40' value='"+column+"'/><select name='dynamicQueryConditions["+index+"].widgetType' style='width: 100px;'>"+select+"</select><select name='dynamicQueryConditions["+index+"].queryOperation'><option value='EQ'>等于<option><option value='GT'>大于<option><option value='GE'>大于等于<option><option value='LT'>小于<option><option value='LE'>小于等于<option><option value='NE'>不等于<option><option value='LIKE'>LIKE<option><option value='BETWEEN'>BETWEEN<option></select><font color='red'>*</font>");
			var $hide = $("<input type='hidden' name='dynamicQueryConditions["+index+"].fieldType' value='"+columnType+"'/>");
	        var $btn = $("<a style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;─</a>");  
	        var $br = $("<br/>");
	      	//设置删除按钮的onclick事件
			$btn.click(function (){
			  //alert($txt.val());
			  $txt.remove();
			  $hide.remove();
			  $btn.remove();
			  $br.remove();
			  var data = "<label><input type='checkbox' name='checkbox' value='"+$txt.val()+"=="+$hide.val()+"'/>"+$txt.val()+"</br></label>";
	          $("#columnsDiv").append(data);
	          
		      //把索引调整成从0顺序开始
		      reIndexForDynamic();
			});
			$("#div2").append($txt).append($hide).append($btn).append($br); 
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
	      	var $txt = $("<input type='text' id='fieldId"+index+"' name='fieldDetails["+index+"].fieldName' size='40' style='background-color: #E0E0E0;' value='"+$(this).val()+"' readonly='readonly'/><input type='text' name='fieldDetails["+index+"].label' size='40' value='"+$(this).val()+"'/><font color='red'>*</font>");
	        var $btn = $("<a style='font-size:14px;color: blue;text-decoration:none;' href='#' title='删除'>&nbsp;─</a>");  
	        var $br = $("<br/>");
	      	//设置删除按钮的onclick事件
			$btn.click(function (){
			  $txt.remove();
			  $btn.remove();
			  $br.remove();
			  var data = "<label><input type='checkbox' name='checkbox2' value='"+$txt.val()+"'/>"+$txt.val()+"&nbsp;&nbsp;&nbsp;&nbsp;</br></label>";
	          $("#columnsDiv2").append(data);
	          
	          //把索引调整成从0顺序开始
	          reIndexForField();
			});
			$("#div3").append($txt).append($btn).append($br); 
		});
	});
});

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
		//alert(nameAttr.split(".")[1]);
		if(nameAttr.split(".")[1] == "fieldName"){
			$(this).attr("id","preId"+index);
			$(this).attr("name","preQueryConditions["+index+"].fieldName");
			//alert($(this).next().attr("name"));
			$(this).next().attr("name","preQueryConditions["+index+"].queryOperation");
		}else if(nameAttr.split(".")[1] == "value"){
			$(this).attr("name","preQueryConditions["+index+"].value");
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
			//alert($(this).next().attr("name"));
			$(this).next().attr("name","dynamicQueryConditions["+index+"].widgetType");
		}else{
			$(this).attr("name","dynamicQueryConditions["+index+"].fieldType");
			index++;
		}
	});
}

/**
 * 把显示列索引调整成从0顺序开始
 */
function reIndexForField(){
    var index = 0;
	//找到所有name以fieldDetails开头的元素
	$("input[name^='fieldDetails']").each(function(){
		var nameAttr = $(this).attr("name");
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
	var url = '<%=contextPath %>/generalquery/add.koala';
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
			 parent.gridManager.loadData();
			 if(json.result == "success"){
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

var loadDataSourceList = function(){
	var url = "${pageContext.request.contextPath}/generalquery/findAllDataSource.koala";
	$.get(url, function(data){
		//先清空
		 $("#dataSourceIdID").empty();
		 $("#dataSourceIdID").append("<option value=''>--选择数据源--</option>");
		 if(data && !data.result){
			//再赋值
	         /* for(var index in data['dataSourceList']){
	        	 var node = data['dataSourceList'][index];
	        	 var data = "<option value='"+node['dataSourceId']+"'>"+node['dataSourceId']+"</option>";
	        	 $("#dataSourceIdID").append(data);
			  } */
			  var datas = data['dataSourceList'];
			  for(var index in datas){
	        	 var node = datas[index];
	        	 var data = "<option value='"+node['id']+"'>"+node['dataSourceId']+"</option>";
	        	 $("#dataSourceIdID").append(data);
			  }
		 }
	});
};

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

var loadColumnList = function(){
	$("#showColumns").hide();
	$("#showColumns2").hide();
	//先清空
	$("#columnsDiv").empty();
	$("#columnsDiv2").empty();
	
	var id = $("#dataSourceIdID").val();
	var tableName = $("#tableNameID").val();
	if(tableName != "0"){
		var url = "${pageContext.request.contextPath}/generalquery/findAllColumn.koala?id=" + id + "&tableName=" + tableName;
		$.get(url, function(data){
			//var index = 0;
			var node = data['tableMap'];
			//再赋值
	         for(var pro in node){
	        	 //alert(data['tableMap'][pro]);//获取列类型
	        	 /* index ++;
	        	 var data = "<label><input type='checkbox' name='checkbox' value='"+pro+"'/>"+pro+"&nbsp;&nbsp;&nbsp;&nbsp;</label>";
	        	 //每行显示4个列
	        	 if(index%4 == 0){
	        		 $("#columnsDiv").append(data).append("</br>");
	        	 }else{
	        		 $("#columnsDiv").append(data);
	        	 } */
	        	 var data = "<label><input type='checkbox' name='checkbox' value='"+pro+"=="+node[pro]+"'/>"+pro+"&nbsp;&nbsp;&nbsp;&nbsp;</br></label>";
	        	 $("#columnsDiv").append(data);
	        	 var data2 = "<label><input type='checkbox' name='checkbox2' value='"+pro+"'/>"+pro+"&nbsp;&nbsp;&nbsp;&nbsp;</br></label>";
	        	 $("#columnsDiv2").append(data2);
			  }
		});
		$("#showColumns").show();
		$("#showColumns2").show();
	}
};

</script>
<style type="text/css">
	.form2column{
		border:#D2D2D2 1px solid;
		width:94%;
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
  		<div style="margin-left: 47px;height: 25px;">
  			<select id="dataSourceIdID" name="dataSource.id">
	  			<!-- <option value="0">--选择数据源--</option>
	  			<option value="">dataSource</option>
	  			<option value="">test</option> -->
	  		</select>
	  		<select id="tableNameID" name="tableName">
	  			<option value="">--选择表--</option>
	  			<!-- 
	  			<option value="">t_user</option>
	  			<option value="">t_log</option> -->
	  		</select>
	  		&nbsp;&nbsp;<label style="font-weight:bold;">查询器名称：</label>
	  		<input type="text" id="queryNameID" name="queryName" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
	  		<font color='red'>*</font><font color="#ADADAD" size="1">( 只能输入数字、字母及下划线 )</font>
	  		&nbsp;&nbsp;<label style="font-weight:bold;">描述：</label>
	  		<input type="text" id="descriptionID" name="description" />
  		</div>
  		<div id="showColumns" style="margin-left: 47px;float:left;width:300px;display:none;" >
  			<div>
  				<label style="font-weight:bold;">所有列：</label>
  			</div>
  			<div id="columnsDiv" style="border-style:inset;"></div>
  			<div>
  				<input type="button" id="staticButton" class="btn-normal" value="添加到静态查询条件" />
  				<input type="button" id="dynamicButton" class="btn-normal" value="添加到动态查询条件" />
  			</div>
  		</div>
  		<div id="showColumns2" style="margin-left: 47px;float:left;width:300px;display:none;" >
  			<div>
  				<label style="font-weight:bold;">所有列：</label>
  			</div>
  			<div id="columnsDiv2" style="border-style:inset;"></div>
  			<div>
  				<input type="button" id="columnShowButton" class="btn-normal" value="添加到显示列" />
  			</div>
  		</div>
  		<!-- 该div防止 静态查询条件div错位-->
  		<div style="margin-left: 47px;color: white"><label>--------------------------------------------------------------------------------------------------------------------------------------------------------</label></div>
  		<div id="staticQuery" style='margin-left: 47px;width: 850px;'>
  			<div><label style="font-weight:bold;">静态查询条件</label></div>
	  		<!-- <table border="1" cellpadding="0" cellspacing="0" class="form2column">
	           <tr>
		  			<td class="label">列</td>
		  			<td class="label">条件</td>
		  			<td class="label">是否可见</td>
	  			</tr>
	  		</table> -->
	  		<div id="div1"></div>
  		</div>
  		<br/>
  		<div id="dynamicQuery" style='margin-left: 47px;width: 800px;'>
  			<div><label style="font-weight:bold;">动态查询条件</label></div>
  			<table border="1" cellpadding="0" cellspacing="0" class="form2column" style="width:86%">
	           <tr>
		  			<td class="label" style="width:37%">列</td>
		  			<td class="label" style="width:37%">页面显示名称</td>
		  			<td class="label" style="width:15%">输入框类型</td>
		  			<td class="label">操作</td>
	  			</tr>
	  		</table>
  			<div id="div2"></div>
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
  			<div id="div3"></div>
  		</div>
	  	
	  <div class="form_button">
        <input id="searchButton" type="button" class="btn-normal" onclick="saveDataAction()" value="保存" />
     </div>
  	</form>
</div>
</body>
</html>
