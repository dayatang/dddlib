<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
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
								<div id="formTemplate" class="btn-group select"></div>
								<span class="required">*</span>
							</div>
						</div>
					</div>
					<div class="form-group row">
						<!--<div class="col-lg-6 form-group">
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
						</div>  -->
						<div class="col-lg-6 form-group">
							<label class="col-lg-4 control-label">关联流程:</label>
							<div class="col-lg-8">
								<div id="associationProcess" class="btn-group select"></div>
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
						<table class="table table-responsive table-bordered table-hover table-striped fieldList">
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
									<th width="100px" index="4">输出类型
										<div class="colResize"></div>
									</th>
									<th width="80px" index="5">显示顺序
										<div class="colResize"></div>
									</th>
									<th style="" index="6">选项
										<div class="colResize"></div>
									</th>
									<th width="110px" index="7" >验证规则
										<div class="colResize"></div>
									</th>
								</tr>
							</thead>

							<tbody id="fieldListBody"></tbody>
						</table>
						<input type="hidden"  id="formId"/>
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
                            <label for="formName" class="col-lg-4 control-label">字段ID:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" id="fieldName" placeholder="字段ID"><span class="required">*</span>
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">字段名称:</label>
                            <div class="col-lg-8">
                                 <input type="text" class="form-control" id="fieldDesc" placeholder="字段名称"><span class="required">*</span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label for="formName" class="col-lg-4 control-label">字段控件类型:</label>
                            <div class="col-lg-8">
                                <div id="formFieldTypes" class="btn-group select"></div> <span class="required">*</span>
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label" >值输出类型:</label>
                            <div class="col-lg-8">
                                <div id="valOutputTypes" class="btn-group select"></div><span class="required">*</span>
                            </div>
                        </div>
                    </div>
                    
                    
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">显示顺序:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" id="showOrder" placeholder="显示顺序">
                            </div>
                        </div>
                    </div>
                     <div class="form-group row">
                        <div class="col-lg-12 form-group">
                            <label for="formName" class="col-lg-2 control-label">验证规则:</label>
                            <div class="col-lg-10 rule">
                                <div id="validateRules" class="btn-group select"></div>
                                <input class="validateExpr form-control" type="text" style="display:none;"  id="validateExpr" placeholder="正则表达式"><span style="display:none;" class="required">*</span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-lg-2 control-label">选项:&nbsp;&nbsp;</label>
                        <div class="col-lg-10" style="position: relative; top: 6px;" id="fieldOptions">
                            <div class="checker"><span><input type="checkbox"  style="opacity: 0;"  id="requiredChk"></span></div>必填
                            &nbsp;&nbsp;&nbsp;<div class="checker"><span><input type="checkbox"  style="opacity: 0;" id="innerVariableChk"></span></div>是否流程变量
                            &nbsp;&nbsp;&nbsp;<div class="checker"><span><input type="checkbox" style="opacity: 0;" id="outputVarChk"></span></div>是否显示在流程
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
<div id="keyOptionsPanel" class="popover fade right in" style="display:none;">
<div class="arrow"></div>
<div class="popover-content">
<form class="form-inline" id="keyOptionsForm">
 <label style="width:60px;">显示文字</label><label style="width:40px;text-align:center;">值</label>
</form>
<div><a href="javascript:initFieldDropdownOpts();">add Item</a>
&nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="javascript:$('#keyOptionsPanel').hide();">Close</a></div>
</div>
</div>
<script>
   var rootPath = "${pageContext.request.contextPath}";
   var templateSelectOpts = [
            		          {value:'NULL',title:'请选择绑定模板',selected:true},
                              <c:forEach items="${templates}" var="t" varStatus="status">  
                              {value:'${t.id}',title:'${t.templateName}'},
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
    var valOutputTypeOpts = [
        		       <c:forEach items="${valOutputTypes}" var="t" varStatus="stat">  
                         {value:'${t.value}',title:'${t.text}'<c:if test="${stat.index==0}">,selected:true</c:if>},
                       </c:forEach>
        		      ];
    
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/processform/core.js"></script>

