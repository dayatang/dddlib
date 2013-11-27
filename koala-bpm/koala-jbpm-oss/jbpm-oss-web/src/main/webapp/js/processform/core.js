$(function(){
    	var  isActivateds = $('[name="isActivated"]');
        isActivateds.on('click', function(){
               isActivateds.each(function(){
                   $(this).parent().removeClass('checked');
               })
               $(this).parent().addClass('checked');
        });
        
        $(':checkbox').live('click', function(){
            $(this).parent().toggleClass('checked');
        });
      
        
        $('#formManagement').find('input[data-role="selectAll"]').on('click',function(){
        	var checked = $(this).parent().hasClass('checked');
        	$('#formManagement').find(':checkbox').each(function(){
        		if(checked == false){
        			if($(this).parent().hasClass('checked') == false){
        				$(this).parent().addClass('checked');
        			}
        		}else{
        			$(this).parent().removeClass('checked');
        		}
        	});
        });
    });


  $(function() {
	$("#formTemplate").select({
		title: '请选择流程',
		contents : templateSelectOpts
	});
    //流程通过AJAX加载选项
	$("#associationProcess").select();

	$("#valOutputTypes").select({
		contents : valOutputTypeOpts
	});

	$("#formFieldTypes").select({
		contents : fieldSelectOpts
	}).on(
			'change',
			function(e) {
				var selectedVal = $(this).getValue();
				if (selectedVal == 'DropDown' || selectedVal == 'Checkbox'
						|| selectedVal == 'Radio') {
					showFieldDropdownOpts();
				} else {
					$("#keyOptionsPanel").hide();
				}
			});

	$("#validateRules").select({
		contents : validateSelectOpts
	}).on('change', function() {
		if ($(this).getValue() == 'Regex') {
			$("#validateExpr").show().next('span').show();
		} else {
			$("#validateExpr").hide().next('span').hide();
		}
	});
});
    
    
    $(function(){
        var cols = [
            {title:'表单名称', name:'bizName', width: '180'},
            { title:'表单描述', name:'bizDescription' , width: '120'},
            { title:'模板名称', name:'templateName' , width: '200'},
            { title:'关联流程', name:'processId' , width: '250'},
            { title:'是否激活', name:'active', width: '120'},
            { title:'操作', name:'operate' , width: '220', render: function(item, name, index){//
                return "<a href=\"javascript:previewForm('"+item.bizName+"','"+item.id+"');\"><i class=\"glyphicon glyphicon-eye-open\"></i>预览</a>";
            }}
        ];
        var buttons = [
            {content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;新增</button>', action: 'add'},
            {content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button>', action: 'modify'},
            {content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;刪除</button>', action: 'delete'}
        ];
        
        $('#processformList').grid({
            identity: 'id',
            columns: cols,
            buttons: buttons,
            url: '/processform/getDataList.koala'
        }).on({
            'add': function(){
            	currentRowId = 0;
            	$('#formManagement').modal({
                    keyboard: false
                }).on('hidden.bs.modal', function(){
                	$(this).remove();
                });
            	initProcessForm();
            },
            'modify': function(event, data){
            	currentRowId = 0;
               var indexs = data.data;
                var $this = $(this);
                if(indexs.length == 0){
                    $('body').message({
                        type: 'warning',
                        content: '请选择一条记录进行修改'
                    });
                    return;
                }
                if(indexs.length > 1){
                    $('body').message({
                        type: 'warning',
                        content: '只能选择一条记录进行修改'
                    });
                    return;
                }
                $.ajax({
        			type: "GET",
        			url: "/processform/get.koala",
        			data: {id:indexs[0]},
        			success: function(data){
        				if(!data.result){
        					$('body').message({type: 'warning',content: '获取数据错误'});
        					return;
        				}
        				data = data.result;
        				initProcessForm(data);
        				data = data.processKeys;
        				for(var index in data){
        					insertFieldRows(index+1,data[index]);
        				}
        				$('#formManagement').clone().modal({
                            keyboard: false
                        }).on('hidden.bs.modal', function(){
                        	$('#formTemplate').empty().data('koala.select', null);
                        	$('#formTemplate').empty().data('koala.select', null);
                        	$('#formTemplate').empty().data('koala.select', null);
                        });
        			}
        		});
             },
            'delete': function(event, data){
                var indexs = data.data;
                var $this = $(this);
                if(indexs.length == 0){
                	$('body').message({
                        type: 'warning',
                        content: '请选择要删除的记录'
                    });
                    return;
                }
                $this.confirm({
                    content: '确定要删除所选记录吗?',
                    callBack: function(){ 
                    	$.ajax({
                			type: "POST",
                			url: "/processform/delete.koala",
                			data: {id:indexs.join(',')},
                			success: function(msg){
                				$('body').message({
                                    type: 'success',
                                    content: '删除成功'
                                });
                				$this.grid('refresh');
                			}
                		});
                    }
                });
            }
        });
    });
    
    var filedMap = {};
    var currentRowId = 0;
    $(function(){
    	$("button[data-action='open-field-adddialog']").on('click', function(){
    		currentRowId = 0;
            $('#fieldManagement').show().modal({
                   keyboard: false
            });
    		initFormFied();
         });
    	$("button[data-action='open-filed-moddialog']").on('click', function(){
    		var ids = getSelectFieldIds();
            if(ids.length ==0){
            	$('body').message({
                    type: 'warning',
                    content: '请选择需要编辑记录'
                })
    			return;
    		}
    		if(ids.length >1){
    			$('body').message({
                    type: 'warning',
                    content: '只能选择一条记录'
                })
    			return;
    		}
    		currentRowId = ids[0];
    		
            $('#fieldManagement').show().modal({
                   keyboard: false
            });
    		initFormFied(filedMap['field_'+currentRowId]);
         });
    	$("button[data-action='delete-filed']").on('click', function(){
    		var ids  = getSelectFieldIds();
    		for(var index in ids){
    			$("#field_rowId_"+ids[index]).remove();
    			filedMap['field_'+ids[index]] = null;
    		}
         });
    	$("button[data-action='save-field']").on('click',function(){
    		if(saveDefineFormFiled()){
    		  $("#keyOptionsPanel").hide();
    		  $("#fieldManagement").modal('hide');
    		}
    		
        });
    	$("button[data-action='save-processForm']").on('click',function(){
    		var form = {};
    		form.id = $("#formId").val();
    		form.bizName = $("#formName").val();
    		form.bizDescription = $("#formDesc").val();
    		form.templateId = $("#formTemplate").getValue();
    		form.processId = $("#associationProcess").getValue();
    		form.active = $("input[name='isActivated']").val();
    		var dialog = $('#formManagement');
    		if(!Validation.notNull(dialog, $("#formName"), form.bizName, '请填写表单名称')){
        		return;
        	}
    		if(!Validation.notNull(dialog, $("#formTemplate"), form.templateId , '请选择表单模板')){
        		return;
        	}
    		if(form.id == "" && !Validation.notNull(dialog, $("#associationProcess"), form.processId , '请选择关联流程')){
        		return;
        	}
    		var fields = [];
    		for(var index in filedMap){
    			if(filedMap[index])fields.push(filedMap[index]);
    		}
    		
    		if(fields.length == 0){
    			$('body').message({
	                type: 'warning',
	                content: '表单字段不能为空'
    			})
        		return;
        	}
    		
    		form.processKeys = fields;
    		$.ajax({
    			type: "POST",
    			url: rootPath+"/processform/create.koala",
    			data: {jsondata:JSON.stringify(form)},
    			success: function(data){
    				if(!data.result){
    					$('body').message({type: 'warning',content: '服务繁忙'});
    					return;
    				}
    			   if(data.result!="OK"){
					  $('body').message({type: 'warning',content: data.result});
					  return;
				    }
    			   	$('body').message({type: 'success', content: '保存成功'});
    				$('#formManagement').modal('hide');
    				$('#processformList').grid('refresh');
    			}
    		});
        });
    	
    });
    
    
    function previewForm(title,formId){
    	$.get('pages/processform/preview.html')
    	 .done(function(data){
    		 var dialog = $(data);
    		 dialog.find('.modal-title').html(title+" - 预览");
    		 $.ajax({
    			 url: '/processform/templatePreview.koala?formId='+formId,
    			 type: 'GET',
    			 dataType: 'html'
    		 }).done(function(data){
    			 dialog.find('.modal-body').html(data);
    		 });
    		 dialog.modal({
    			keyboard: false
    		 }).on({
    			'hidden.bs.modal': function(){
    				$(this).remove();
    			}
    		});
    	}).fail(function(){
    		
    	});
    }
    
    function initProcessForm(form){
    	loadJbpmProcessOpts(form);
    	$("#formId").val(form ? form.id : "" );
    	$("#formName").val(form ? form.bizName : "" );
		$("#formDesc").val(form ? form.bizDescription : "" );
		$("#formTemplate").setValue(form?form.templateId:"NULL");
    	var active = form ? form.active : 'true';
    	$("input[name='isActivated'][value='"+active+"']").click(); 
    	if(currentRowId == 0){
    		$("#fieldListBody").empty();
    	}
    }
    
    function initFormFied(field){
    	$("#fieldName").val(field ? field.keyId : "" );
    	$("#fieldDesc").val(field ? field.keyName : "" );
    	
    	var checked = field ? field.required == 'true' : false;
    	if(field && field.required == 'true'){
    	  $("#requiredChk").parent().addClass("checked");
    	}else{
    	  $("#requiredChk").parent().removeClass("checked");
    	}
    	checked = field ? field.innerVariable == 'true' : false;
    	$("#innerVariableChk").attr("checked",checked);
    	checked = field ? field.outputVar == 'true' : false;
    	$("#outputVarChk").attr("checked",checked);
    	
    	$("#formFieldTypes").setValue(field?field.keyType:"NULL");
    	$("#validateRules").setValue(field?field.validationType:"NULL");
    	$("#valOutputTypes").setValue(field?field.valOutputType:"NULL");
    	$("#showOrder").val(field ? field.showOrder : "");
    	$("#validateExpr").val(field ? field.validateExpr : "" );
    	//
    	$("#keyOptionsForm >div").remove();
    	initFieldDropdownOpts(field ? $.parseJSON(field.keyOptions) : null);
        if(field && (field.keyType == 'DropDown' || field.keyType == 'Checkbox' || field.keyType == 'Radio')){
        	showFieldDropdownOpts();
    	}
    }
    
    function initFieldDropdownOpts(opts){
    	var isblank = opts ? opts.length>0 : true;
    	var optsHtml = "";
    	if(isblank == true){
    		optsHtml = "<div><input class='input-mini left' style='width:60px;'> - <input class='input-mini right' style='width:40px;'></div>";
    	}else{
    		for(var index in opts){
    			optsHtml = optsHtml + "<div><input class='input-mini left' style='width:60px;' value='"+index+"'> - <input class='input-mini right' style='width:40px;' value='"+opts[index]+"'>";
    			optsHtml = optsHtml + "&nbsp;<a href='javascript:;' onclick='javascript:$(this).parent().remove();'>-</a></div>";
    		}
    	}
    	$("#keyOptionsForm").append(optsHtml);
    }
    
    function showFieldDropdownOpts(){
    	var innerOffset = $('#formFieldTypes').offset();
    	$("#keyOptionsPanel").css("z-index","9999")
		   .css("top",innerOffset.top - 80)
		   .css("left",innerOffset.left - 230)
		   .show();
    }
    
    function saveDefineFormFiled(){
    	var $target = $("#fieldListBody");
    	var rowId = currentRowId > 0 ? currentRowId : ($target.find("tr").length + 1);
    	var field = {};
    	field.keyId = $("#fieldName").val();
    	field.keyName = $("#fieldDesc").val();		
    	field.required = $("#requiredChk").parent().hasClass("checked") ? "true" : "false";
    	field.innerVariable = $("#innerVariableChk").parent().hasClass("checked") ? "true" : "false";
    	field.outputVar = $("#outputVarChk").parent().hasClass("checked") ? "true" : "false";
    	field.keyType =  $("#formFieldTypes").getValue();
    	field.validationType =  $("#validateRules").getValue();
    	field.fieldTypeText =  $("#formFieldTypes").getItem();
    	field.validateRuleText =  $("#validateRules").getItem();
    	field.validateExpr =  $("#validateExpr").getItem();
    	field.showOrder =  $("#showOrder").val();
    	field.valOutputType =  $("#valOutputTypes").getValue();
    	field.valOutputTypeText =  $("#valOutputTypes").getItem();
    	var dialog = $('#fieldManagement');
    	if(!Validation.checkByRegExp(dialog, $("#fieldName"),'^[A-Za-z]{1}[_A-Za-z0-9]+$', field.keyId , '字段id必填且只能是字母开头的数字或字母组成')){
    		return false;
    	}
    	
    	if(field.validationType == 'NULL'){
    		field.validationType = '';
    		field.validateRuleText = '无';
    	}
    	if(!Validation.notNull(dialog, $("#formFieldTypes"), field.keyType , '请选择字段控件类型')){
        	return false;
        }
    	if(field.validationType == 'Regex' && !Validation.notNull(dialog, $("#validateExpr"), $("#validateExpr").val() , '请填写自定义验证规则')){
        	return false;
        }
    	
    	//拼装下拉框选项
    	if(field.keyType == 'DropDown' || field.keyType == 'Checkbox' || field.keyType == 'Radio'){
    		var textArr = [];
    		var valArr = [];
    		$("#keyOptionsForm input").each(function(index){
    			var v = $(this).val();
    			if(index % 2 == 0){textArr.push(v);}else{valArr.push(v);}
    		});
    		var optString = "{";
    	    for(var index in textArr){
    	    	if(textArr[index] !=""  && valArr[index] !=""){
    	    		optString = optString + '"' + textArr[index] + '":"' + valArr[index] + '",';
    	    	}
    	    }
    	    if(optString.length>1)optString = optString.substr(0,optString.length - 1);
    		optString = optString + "}";
    		field.keyOptions = optString;
    		if(field.keyOptions == "{}"){
    			alert("请控件设置默认值");
    			showFieldDropdownOpts();
            	return false;
            }
    	}
    	
    	insertFieldRows(rowId,field);
    	return true;
    }
    function insertFieldRows(rowId,field){
    	var validateTD = field.validationType == 'Regex' ? field.validateExpr : field.validateRuleText;
    	var tdContents = new Array();
    	tdContents.push("<td><div class='checker'>");
    	tdContents.push("<span><input type='checkbox' data-role='indexCheckbox' style='opacity: 0;' value='"+rowId+"'></span></div></td>");
    	tdContents.push("<td index='0'>"+rowId+"</td>");
    	tdContents.push("<td index='1'>"+field.keyId+"</td>");
    	tdContents.push("<td index='2'>"+field.keyName+"</td>");
    	tdContents.push("<td index='3'>"+field.fieldTypeText+"</td>");
    	tdContents.push("<td index='4'>"+field.valOutputTypeText+"</td>");
    	tdContents.push("<td index='5'>"+field.showOrder+"</td>");
    	tdContents.push("<td index='6'>必填:"+field.required+",变量:"+field.innerVariable+"显示:"+field.outputVar+"</td>");
    	tdContents.push("<td index='7'>"+validateTD+"</td>");
    	if(currentRowId == 0){
    		$("#fieldListBody").append("<tr id='field_rowId_"+rowId+"'>"+tdContents.join('')+"</tr>");
    	}else{
    		$("#field_rowId_"+rowId).html(tdContents.join(''));
    	}
    	
    	filedMap['field_'+rowId] = field;
    }
    
    function getSelectFieldIds(){
    	var result = [];
    	$('#formManagement :checkbox:checked').each(function(){
    		result.push($(this).val());
    	});
    	return result;
    }
    
    function loadJbpmProcessOpts(form){
    	var params = form ? {usedId:form.processId} : {};
    	$.get(rootPath + '/processform/getActiveProcesses.koala',params,function(data){
    		var opts = [];
    		if(!form)opts.push({value:'NULL',title:'请选择绑定流程',selected:true});
    		if(data.processes){
    			data = data.processes;
    			for(var index in data){
    				if(form && form.processId != data[index].processId )continue;
    				opts.push({value:data[index].processId,title:data[index].processName});
    			}
    		}
    		$("#associationProcess").resetItems(opts).setValue(form ? form.processId : 'NULL');
    	},'json');
    }