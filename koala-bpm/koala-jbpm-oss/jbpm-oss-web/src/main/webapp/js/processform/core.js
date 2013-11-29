$(function(){
        $('#fieldOptions').find('.checker span').on('click', function(){
            $(this).toggleClass('checked');
        });
	$("#formTemplate").select({
		contents : templateSelectOpts
	});
    //流程通过AJAX加载选项
	$("#associationProcess").select({
        title: '请选择流程'
    });

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
                $('#formManagement').modal({
                    keyboard: false
                }).on('shown.bs.modal', {data: null}, function(event){
                      loadFieldGrid(event.data.data)
                });
            	initProcessForm();
            },
            'modify': function(event, data){
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
        				/*for(var index in data){
        					insertFieldRows(index+1,data[index]);
        				}*/
        				$('#formManagement').modal({
                            keyboard: false
                        }).on('shown.bs.modal', {data: data}, function(event){
                              loadFieldGrid(event.data.data)
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
    var currentKeyId = null;
    var loadFieldGrid = function(data){
        var columns = [
            {
                title:'编号',
                name:'keyId' ,
                width: '80px',
                render: function(item, name, index){
                    return index+1;
                }
            },
            {
                title:'字段名称',
                name:'keyId' ,
                width: '120px'
            },
            {
                title:'字段描述',
                name:'keyName' ,
                width: '120px'
            },
            {
                title:'字段类型',
                name:'fieldTypeText' ,
                width: '90px'
            },
            {
                title:'输出类型',
                name:'valOutputTypeText' ,
                width: '90px'
            },
            {
                title:'显示顺序',
                name:'showOrder' ,
                width: '90px'
            },
            {
                title:'选项',
                name:'required' ,
                width: '340px',
                render: function(item, name, index){
                    return '必填:'+item.required+' 是否流程变量:'+item.innerVariable
                        +'   是否显示在流程 :'+item.outputVar;
                }
            },
            {
                title:'验证规则',
                name:'validateRuleText' ,
                width: '120px'
            }
        ];
        var buttons = [
            {content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</button>', action: 'add'},
            {content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button>', action: 'modify'},
            {content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</button>', action: 'delete'}
        ];
        var fieldGrid = $('#fieldGrid');
        fieldGrid.data('koala.grid', null);
        fieldGrid.empty().grid({
            identity: 'keyId',
            columns: columns,
            buttons: buttons,
            isShowPages: false,
            isUserLocalData: true,
            localData: data
        }).off().on({
             'add': function(){
                 currentKeyId = null;
                 $('#fieldManagement').show().modal({
                     keyboard: false
                 });
                 initFormFied();
             },
             'modify': function(event, data){
                 var indexs = data.data;
                 var $this = $(this);
                 if(indexs.length == 0){
                     $('body').message({
                         type: 'warning',
                         content: '请选择一条记录进行修改'
                     })
                     return;
                 }
                 if(indexs.length > 1){
                     $('body').message({
                         type: 'warning',
                         content: '只能选择一条记录进行修改'
                     })
                     return;
                 }
                 currentKeyId = indexs[0];
                 $('#fieldManagement').show().modal({
                     keyboard: false,
                     backdrop: false
                 });
                 initFormFied(data.item[0]);
             },
             'delete': function(event, data){
                 var indexs = data.data;
                 var $this = $(this)
                 if(indexs.length == 0){
                     $('body').message({
                         type: 'warning',
                         content: '请选择要删除的记录'
                     })
                     return;
                 }
                 $this.confirm({
                     content: '确定要删除所选记录吗?',
                     callBack: function(){
                         $.each(indexs, function(){
                             $this.getGrid().removeRows(indexs);
                         })
                     }
                 });
             }
        });
    };
    $(function(){
    	$("button[data-action='save-field']").on('click',function(){
    		if(saveDefineFormFiled()){
    		  $("#keyOptionsPanel").hide();
    		  $("#fieldManagement").modal('hide');
              $('#fieldGrid').find('[data-role="selectAll"]').removeClass('checked');
    		}
    		
        });
    	$("button[data-action='save-processForm']").on('click',function(){
            console.info($('#fieldGrid').getGrid().getAllItems())

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
            $.each($('#fieldGrid').getGrid().getAllItems(), function(){
                fields.push(this);
            });
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
    		 }).on('hidden.bs.modal', function(){
                     $(this).remove();
             });
    	}).fail(function(){
    		
    	});
    }
    
    function initProcessForm(form){
    	$("#formId").val(form ? form.id : "" );
        $("#formName").val(form ? form.bizName : "" );
    	$("#formDesc").val(form ? form.bizDescription : "" );
    	$("#formTemplate").setValue(form?form.templateId:"NULL");
    	loadJbpmProcessOpts(form);
    }
    
    function initFormFied(field){
    	$("#fieldName").val(field ? field.keyId : "" );
    	$("#fieldDesc").val(field ? field.keyName : "" );
    	
    	var checked = field ? field.required == 'true' : false;
    	if(checked){
    	  $("#requiredChk").addClass("checked");
    	}else{
    	  $("#requiredChk").removeClass("checked");
    	}
    	checked = field ? field.innerVariable == 'true' : false;
        if(checked){
            $("#innerVariableChk").addClass("checked");
        }else{
            $("#innerVariableChk").removeClass("checked");
        }
    	checked = field ? field.outputVar == 'true' : false;
        if(checked){
            $("#outputVarChk").addClass("checked");
        }else{
            $("#outputVarChk").removeClass("checked");
        }
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
        var field = {};
    	field.keyId = $("#fieldName").val();
    	field.keyName = $("#fieldDesc").val();		
    	field.required = $("#requiredChk").hasClass("checked") ? "true" : "false";
    	field.innerVariable = $("#innerVariableChk").hasClass("checked") ? "true" : "false";
    	field.outputVar = $("#outputVarChk").hasClass("checked") ? "true" : "false";
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
    			$('body').message({
					type: 'warning',
					content: '请控件设置默认值'
				});
    			showFieldDropdownOpts();
            	return false;
            }
    	}
        if(!currentKeyId){
            $('#fieldGrid').getGrid().insertRows(field);
        }else{
            $('#fieldGrid').getGrid().updateRows(currentKeyId, field);
        }
    	//insertFieldRows(rowId,field);
    	return true;
    }
    
    function loadJbpmProcessOpts(form){
    	var params = form ? {usedId:form.processId} : {};
    	$.get(rootPath + '/processform/getActiveProcesses.koala',params,function(data){
    		var opts = [];
            var data = data.processes;
    		if(data){
                $.each(data, function(){
                    opts.push({value:this.processId,title:this.processName});
                });
    		}
            console.info(opts)
    		$("#associationProcess").data('koala.select', null).empty().select({
                title: '选择关联流程',
                contents: opts
            });
            if(form && form.processId){
                $("#associationProcess").setValue(form.processId);
            }
    	},'json');
    }