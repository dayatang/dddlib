/**
 * koalaDropdown.js
 * <b>jQuery plugin for koalaDropdown</b>
 * <br>
 * HomePage:http://
 * Copyright (c) 2011 vakin jiang (chiang.www@gmail.com)
 * Dual licensed under the MIT and GPL licenses. 
 * @version 1.0
            at 2013-01-23
 */
;(function($){
  $.koalaDropdown = {
    //dropdownStyle:'list',//list,tree,table
    //isMultiSelect:false,//是否支持多选
    //创建普通下拉框
    createListSelect:function($origElement,options){
    	var params = serializeJSON(options);
    	$.ajax({
            url : "/koalaUISupport?_type=dropdown&"+params,
            datatype:"json",
            cache:true,
            complete:function(response){
            	if(response.responseText == '')return;
        		var dropdownData = eval("(" + (response.responseText || '') + ")");
        		buildListSelectDropdown($origElement,options,dropdownData);
            }
      	});
	 },
	//创建树下拉框
 	createTreeSelect:function($origElement,options){
 	  var params = serializeJSON(options);
 	  $origElement.ligerComboBox({
 	     width : options.width || 180 , 
 	     selectBoxWidth: 200,
 	     selectBoxHeight: 200, 
 	     valueField: 'text', 
 	     treeLeafOnly: false,
 	     tree: { url: '/koalaUISupport?_type=dropdown&'+params}
 	   }); 
 	 },
 	//创建表格下拉框
 	createTableSelect:function($origElement,options){
 	   buildTreeSelectDropdown($origElement,option);
 	}
  };

  
  function buildListSelectDropdown($origElement,options,dropdownData){
	//存储真实值的FieldId
	var valueFieldId = options.valueFieldId;
	var $valueField = $('#'+valueFieldId);
	//级联下拉框
  	var isCascadeSelect = options.cascadeFieldIds && options.cascadeFieldIds != '';
  	//初始化第一个下拉框
	 $origElement.ligerComboBox({  
          data:dropdownData, 
          isMultiSelect:options.isMultiSelect,
          isShowCheckBox : options.isMultiSelect,
          valueFieldID: valueFieldId
      });
	 
	 //初始化级联下拉框
	  if(isCascadeSelect){
	  		var cascadeFields;
	  		var tmpParent = $origElement;
	  		cascadeFields = options.cascadeFieldIds.split(',');
	  		for ( var i = 0; i < cascadeFields.length; i++) {
	  			var isLast = i == cascadeFields.length - 1;
	  			var isMultiSelect =  isLast ? options.isMultiSelect : false;
  				tmpParent.attr('cascade',cascadeFields[i]);
  				//级联事件
  				tmpParent.bind('change',function(e){
  		  			var cascadeFieldId = $(this).attr('cascade');
  		  			window.console.log('---cascadeField：' + cascadeFieldId);
  		  			cascadeSelectEvent(options,cascadeFieldId,$valueField.val());
  		  		});
	  			tmpParent = $("#"+cascadeFields[i]);
	  			tmpParent.ligerComboBox({
	  	          data:null, 
	  	          isMultiSelect:isMultiSelect,
	  	          isShowCheckBox:isMultiSelect,
	  	          valueFieldID: options.valueFieldId
	  			});
			}
	  }
  }
  
  //下拉框事件
  function cascadeSelectEvent(options,cascadeFieldId,parentVal){
	  var params = serializeJSON(options,'parentId');
	  var url = "/koalaUISupport?_type=dropdown&"+params + "&parentId="+parentVal;
	  $.ajax({
          url : url,
          datatype:"json",
          cache:true,
          complete:function(response){
          	if(response.responseText == '')return;
      		var dropdownData = eval("(" + (response.responseText || '') + ")");
      		$("#"+cascadeFieldId).ligerGetComboBoxManager().setData(dropdownData);
          }
    	});
  }
  

  $.fn.koalaDropdown = function(options) {
	  var _options = options;//$.extend($.koalaDropdown,options);
	  if(!_options.dropdownStyle)_options.dropdownStyle = 'list';
	  return this.each(function(){
		  //window.console.log('dropdownStyle：' + _options.dropdownStyle);
		  var $this = $(this);
		  if(_options.dropdownStyle == 'list'){
			  $.koalaDropdown.createListSelect($this,_options);
		  }else if(_options.dropdownStyle == 'tree'){
			  $.koalaDropdown.createTreeSelect($this,_options);
		  }else if(_options.dropdownStyle == 'table'){
			  $.koalaDropdown.createTableSelect($this,_options);
		  }
	  });
  };
})(jQuery);

/**
 * koalaTree
 */
;(function($){
    $.koalaTree = {
    	lazy:false,//是否延时加载子节点
    	checkbox:true,//是否带多选框
    	create:function($origElement,options){
    		var _options = $.extend(this,options);
    		window.console.log('===>' + _options.beanName);
    		buildTree($origElement,_options);
    	}
    };
    
    function buildTree($origElement,_options){
    	var params = serializeJSON(_options);
    	$origElement.ligerTree({ 
    		checkbox:_options.checkbox,
            nodeWidth: _options.nodeWidth || 100,
            url: '/koalaUISupport?_type=tree&'+params,
            onSuccess:function(){},
            onClick:function(node){
            	if($origElement.ligerGetTreeManager().hasChildren(node))return;
            	var url = '/koalaUISupport?_type=tree&' + params + '&parentId='+ node.data.id;
            	$origElement.ligerGetTreeManager().loadData(node.target, url);
            }
        });
    }
    
    $.fn.koalaTree = function(options) {
  	  return this.each(function(){
  		$.koalaTree.create($(this),options);
  	  });
    };
})(jQuery);


//*************************************************************
function serializeJSON(json,excludes){
	 excludes = excludes || '';
	 var str = '';
	 for(var index in json){
		 if(typeof json[index] ==='function' )continue;
		 if(excludes.indexOf(index)>=0)continue;
		 str = str + index + '=' + json[index] + '&';
	 } 
	 if(str.length>0)str = str.substring(0, str.length - 1);
	 //window.console.log("serialize:"+str);
	 return str;
}