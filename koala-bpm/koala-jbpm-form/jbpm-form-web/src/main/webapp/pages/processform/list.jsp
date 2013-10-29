<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/layer.js"></script>    
 
<div class="table-responsive" id="processformList"></div>

<!-- 表单输入dialog -->
<div class="modal fade form-management" id="formManagement">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">新增表单</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" role="form">
					<div class="form-group row">
						<div class="col-lg-6 form-group">
							<label for="formName" class="col-lg-4 control-label">表单名称:</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="formName"
									placeholder="表单名称"><span class="required">*</span>
							</div>
						</div>
						<div class="col-lg-6 form-group">
							<label class="col-lg-4 control-label">表单模板:</label>
							<div class="col-lg-8">
								<div id="formTemplate"></div>
								<span class="required">*</span>
							</div>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-lg-6 form-group">
							<label for="formName" class="col-lg-4 control-label">是否激活:</label>
							<div class="col-lg-8">
								<div class="radio">
									<span class="checked"><input type="radio"
										style="opacity: 0;" checked name="isActivated" value="true"></span>
								</div>
								<span style="position: relative; top: 8px;">是</span>
								&nbsp;&nbsp;&nbsp;
								<div class="radio">
									<span><input type="radio" style="opacity: 0;"
										name="isActivated" value="false"></span>
								</div>
								<span style="position: relative; top: 8px;">否</span>
							</div>
						</div>
						<div class="col-lg-6 form-group">
							<label class="col-lg-4 control-label">关联流程:</label>
							<div class="col-lg-8">
								<div id="associationProcess"></div>
								<span class="required">*</span>
							</div>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-lg-8 form-group">
							<label class="col-lg-3 control-label">表单描述:</label>
							<div class="col-lg-9">
								<textarea class="form-control" rows="3" id="formDesc"></textarea>
							</div>
						</div>
						<div class="col-lg-4 form-group"></div>
					</div>
					<div class="btn-group buttons" style="margin-bottom:5px; margin-top:5px;">
                      <button data-action="open-field-adddialog" data-role="add" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;新增</button>
                      <button data-action="open-filed-moddialog" data-role="modify" class="btn btn-success" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;修改</button>
                      <button data-action="delete-filed" type="button" class="btn btn-danger" data-role="delete"><span class="glyphicon glyphicon-remove"></span>&nbsp;刪除</button>
                    </div>
					<div class="row table-responsive" style="padding-left: 15px; padding-right: 15px;">
						<table
							class="table table-responsive table-bordered table-hover table-striped">
							<thead>
								<tr>
									<th width="40px;">
										<div class="checker">
											<span><input type="checkbox" style="opacity: 0;"
												data-role="selectAll"></span>
										</div>
									</th>
									<th width="50px" index="0">编号
										<div class="colResize"></div>
									</th>
									<th width="100px" index="1" style="">字段名称
										<div class="colResize"></div>
									</th>
									<th width="100px" index="2">字段描述
										<div class="colResize"></div>
									</th>
									<th width="100px" index="3">字段类型
										<div class="colResize"></div>
									</th>
									<th width="100px" index="4">是否显示列
										<div class="colResize"></div>
									</th>
									<th width="100px" index="5">显示顺序
										<div class="colResize"></div>
									</th>
									<th style="" index="6">选项
										<div class="colResize"></div>
									</th>
									<th width="110px" index="7" class="sort">验证规则
										<div class="colResize"></div>
									</th>
								</tr>
							</thead>

							<tbody id="fieldListBody">
							</tbody>
						</table>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button data-action="save-processForm" type="button" class="btn btn-success">保存</button>
			</div>
		</div>
	</div>
</div>
<!-- 表单输入dialog  end-->

<div class="modal fade field-management" id="fieldManagement">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">新增字段</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="fieldsform">
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label for="formName" class="col-lg-4 control-label">字段名称:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" id="fieldName" placeholder="字段名称"><span class="required">*</span>
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">字段描述:</label>
                            <div class="col-lg-8">
                                 <input type="text" class="form-control" id="fieldDesc" placeholder="字段描述">
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label for="formName" class="col-lg-4 control-label">字段控件类型:</label>
                            <div class="col-lg-8">
                                <div id="formFieldTypes"></div> <span class="required">*</span>
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">长度:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" id="fieldLength" placeholder="长度">
                            </div>
                        </div>
                    </div>
                    
                    
                      <div class="form-group row">
                        <div class="col-lg-6 form-group">
                             <label for="formName" class="col-lg-4 control-label">是否显示在列表:</label>
							<div class="col-lg-8">
								<div class="radio">
									<span class="checked"><input type="radio"
										style="opacity: 0;" checked name="outputVar" value="true"></span>
								</div>
								<span style="position: relative; top: 8px;">是</span>
								&nbsp;&nbsp;&nbsp;
								<div class="radio">
									<span><input type="radio" style="opacity: 0;"
										name="outputVar" value="false"></span>
								</div>
								<span style="position: relative; top: 8px;">否</span>
							</div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">显示顺序:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" id="showOrder" placeholder="显示顺序">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group row">
                        <label class="col-lg-2 control-label">选项:&nbsp;&nbsp;</label>
                        <div class="col-lg-10" style="position: relative; top: 6px;" id="fieldOptions">
                            <div class="checker"><span><input type="checkbox" value="0" style="opacity: 0;"  id="requiredChk"></span></div>必填
                            &nbsp;&nbsp;&nbsp;<div class="checker"><span><input type="checkbox" value="2" style="opacity: 0;" id="innerVariableChk"></span></div>是否流程变量
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label for="formName" class="col-lg-4 control-label">验证规则:</label>
                            <div class="col-lg-8">
                                <div id="validateRules"></div>
                                <input type="text" style="display:none;" class="form-control" id="validateExpr" placeholder="正则表达式">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-success"  data-action="save-field">保存</button>
            </div>
        </div>
    </div>
</div>
<script>
   var templateSelectOpts = [
            		          {value:'NULL',title:'请选择绑定模板',selected:true},
                              <c:forEach items="${templates}" var="t" varStatus="status">  
                              {value:'${t.id}',title:'${t.templateName}'},
                              </c:forEach>
            		      ];
    var processSelectOpts = [
                     		   {value:'NULL',title:'请选择关联流程',selected:true},
                    		   <c:forEach items="${processes}" var="p">  
                               {value:'${p.processId}',title:'${p.processName}'},
                               </c:forEach>
                    		 ];
    
    var fieldSelectOpts = [
            		          {value:'NULL',title:'请选择字段控件类型',selected:true},
            		          <c:forEach items="${dataTypes}" var="t">  
                              {value:'${t.value}',title:'${t.text}'},
                              </c:forEach>
            		      ];
    
    var validateSelectOpts = [
            		          {value:'NULL',title:'请选择验证规则',selected:true},
            		          <c:forEach items="${validateRules}" var="t">  
                           {value:'${t.value}',title:'${t.text}'},
                           </c:forEach>
            		      ];
    
    
    
    $(function(){
    	var  isActivateds = $('[name="isActivated"],[name="outputVar"]');
        isActivateds.on('click', function(){
               isActivateds.each(function(){
                   $(this).parent().removeClass('checked');
               })
               $(this).parent().addClass('checked');
        });
        $('#fieldOptions').find('[type="checkbox"]').on('click', function(){
            $(this).parent().toggleClass('checked');
        });
        
        $('#formManagement').find('[type="checkbox"]').live('click', function(){
            $(this).parent().toggleClass('checked');
        });
        
        $('#formManagement').find('input[data-role="selectAll"]').live('click',function(){
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
    
  $(function(){
      $("#formTemplate").select({
		       contents:templateSelectOpts
      });
      
      $("#associationProcess").select({
		       contents:processSelectOpts
      });
      
      $("#formFieldTypes").select({
		       contents:fieldSelectOpts
      });
      $("#validateRules").select({
		       contents:validateSelectOpts
   }).on('change',function(){
  	  if($(this).getValue() == 'Regex'){$("#validateExpr").show();}else{$("#validateExpr").hide();}
   });    
  });
    
    
    $(function(){
        var cols = [
            {title:'表单名称', name:'bizName', width: '180'},
            { title:'表单描述', name:'bizDescription' , width: '120'},
            { title:'模板名称', name:'templateName' , width: '200'},
            { title:'关联流程', name:'processId' , width: '250'},
            { title:'是否激活', name:'active', width: '120'},
            { title:'操作', name:'operate' , width: '220', render: function(item, name, index){
                return '<a href="javascript:previewForm('+item.id+');"><i class="glyphicon glyphicon-eye-open"></i>预览</a>';
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
            	initProcessForm();
            	$('#formManagement').modal({
                    keyboard: false
                }).hide();
            },
            'modify': function(event, data){
            	currentRowId = 0;
               var indexs = data.data;
                var $this = $(this);
                if(indexs.length == 0){
                    $this.message({
                        type: 'warning',
                        content: '请选择一条记录进行修改'
                    })
                    return;
                }
                if(indexs.length > 1){
                    $this.message({
                        type: 'warning',
                        content: '只能选择一条记录进行修改'
                    })
                    return;
                }
                $.ajax({
        			type: "GET",
        			url: "/processform/get.koala",
        			data: {id:indexs[0]},
        			success: function(data){
        				if(!data.result){
        					$this.message({type: 'warning',content: '获取数据错误'});
        					return;
        				}
        				data = data.result;
        				initProcessForm(data);
        				data = data.processKeys;
        				for(var index in data){
        					insertFieldRows(index+1,data[index]);
        				}
        				$('#formManagement').modal({
                            keyboard: false
                        });
        			}
        		});
             },
            'delete': function(event, data){
                var indexs = data.data;
                var $this = $(this);
                if(indexs.length == 0){
                    $this.message({
                        type: 'warning',
                        content: '请选择要删除的记录'
                    })
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
                				alert(msg.result);
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
    		initFormFied();
            $('#fieldManagement').show().modal({
                   keyboard: false
            });
         });
    	$("button[data-action='open-filed-moddialog']").on('click', function(){
    		var ids = getSelectFieldIds();
            if(ids.length ==0){
            	alert("请选择需要编辑记录");
    			return;
    		}
    		if(ids.length >1){
    			alert("只能选择一条记录");
    			return;
    		}
    		currentRowId = ids[0];
    		initFormFied(filedMap['field_'+currentRowId]);
    		
            $('#fieldManagement').show().modal({
                   keyboard: false
            });
         });
    	$("button[data-action='delete-filed']").on('click', function(){
    		var ids  = getSelectFieldIds();
    		for(var index in ids){
    			$("#field_rowId_"+ids[index]).remove();
    			filedMap['field_'+ids[index]] = null;
    		}
         });
    	$("button[data-action='save-field']").on('click',function(){
    		saveDefineFormFiled();
    		$("#fieldManagement").modal('hide');
    		
        });
    	$("button[data-action='save-processForm']").on('click',function(){
    		var form = {};
    		form.bizName = $("#formName").val();
    		form.bizDescription = $("#formDesc").val();
    		form.templateId = $("#formTemplate").getValue();
    		form.processId = $("#associationProcess").getValue();
    		form.active = $("input[name='isActivated']").val();
    		
    		if(form.bizName == ''){
    			alert('请填写表单名称');
        		return;
        	}
    		
    		if(form.templateId == '' || form.templateId == 'NULL'){
    			alert('请选择表单模板');
        		return;
        	}
    		
    		if(form.processId == '' || form.processId == 'NULL'){
    			alert('请选择关联流程');
        		return;
        	}
    		
    		var fields = [];
    		for(var index in filedMap){
    			if(filedMap[index])fields.push(filedMap[index]);
    		}
    		
    		if(fields.length == 0){
    			alert('表单字段不能为空');
        		return;
        	}
    		
    		form.processKeys = fields;
    		$.ajax({
    			type: "POST",
    			url: "/processform/create.koala",
    			data: {jsondata:JSON.stringify(form)},
    			success: function(data){
    				if(!data.result){
    					$this.message({type: 'warning',content: '获取数据错误'});
    					return;
    				}
    				$('#formManagement').modal('hide');
    				$('#processformList').grid('refresh');
    			}
    		});
        });
    	
    });
    
    
    function previewForm(formId) {
    	$.layer({
    		type: 2,
    		title: ["<h3>明细</h3>", true],
    		iframe: {
    			src: "${pageContext.request.contextPath}/pages/processform/preview.jsp?formId=" + formId,
    		},
    		area: ["900px", "400px"],
    		offset: ["50%", ""]
    	});
    }
    
    function initProcessForm(form){
    	$("#formName").val(form ? form.bizName : "" );
		$("#formDesc").val(form ? form.bizDescription : "" );
		$("#formTemplate").setValue(form?form.templateId:"NULL");
    	$("#associationProcess").setValue(form?form.processId:"NULL");
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
    	$("#requiredChk").attr("checked",checked);
    	checked = field ? field.innerVariable == 'true' : false;
    	$("#innerVariableChk").attr("checked",checked);
    	$("#formFieldTypes").setValue(field?field.keyType:"NULL");
    	$("#validateRules").setValue(field?field.validationType:"NULL");
    	$("#showOrder").val(field ? field.showOrder : "");
    	var outputVar = field ? field.outputVar : 'true';
    	$("input[name='outputVar'][value='"+outputVar+"']").click(); 
    	$("#validateExpr").val(field ? field.validateExpr : "" );
    }
    
    function saveDefineFormFiled(){
    	var $target = $("#fieldListBody");
    	var rowId = currentRowId > 0 ? currentRowId : ($target.find("tr").length + 1);
    	var field = {};
    	field.keyId = $("#fieldName").val();
    	field.keyName = $("#fieldDesc").val();		
    	field.required = $("#requiredChk").attr("checked")==true ? "true" : "false";
    	field.innerVariable = $("#innerVariableChk").attr("checked")==true ? "true" : "false";
    	field.keyType =  $("#formFieldTypes").getValue();
    	field.validationType =  $("#validateRules").getValue();
    	field.fieldTypeText =  $("#formFieldTypes").getItem();
    	field.validateRuleText =  $("#validateRules").getItem();
    	field.validateExpr =  $("#validateExpr").getItem();
    	field.showOrder =  $("#showOrder").val();
    	field.outputVar = $("input[name='outputVar']").val();
    	
    	if(field.validationType == 'NULL'){
    		field.validationType = '';
    		field.validateRuleText = '无';
    	}
    	
    	if(field.keyType == '' || field.keyType == 'NULL'){
    		alert("请选择字段控件类型");
    		return;
    	}
    	
    	if(field.validationType == 'Regex' && field.validateExpr == ''){
    		alert("请填写自定义验证规则");
    		return;
    	}
    	
    	insertFieldRows(rowId,field);
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
    	tdContents.push("<td index='4'>"+field.outputVar+"</td>");
    	tdContents.push("<td index='5'>"+field.showOrder+"</td>");
    	tdContents.push("<td index='6'>必填:"+field.required+",变量:"+field.innerVariable+"</td>");
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
    
</script>
 <script type="text/javascript" src="js/processform/core.js"></script>
