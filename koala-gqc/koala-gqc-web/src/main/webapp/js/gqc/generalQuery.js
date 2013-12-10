var generalQuery = function(){
	var baseUrl = '/generalquery/';
	var generalQueryObject = null;
	var dataSourceSelect = null;
	var tableSelect = null
	var dialog = null;
	var staticQueryLeftTable = null;
	var staticQueryRightTable = null;
	var dynamicQueryLeftTable = null;
	var dynamicQueryRightTable = null;
	var showColumnLeftTable = null;
	var showColumnRightTable = null;
	var dataGrid = null;
	/*
	 新增方法
	 */
	var add = function(grid){
		dataGrid = grid;
		$.get('/pages/gqc/generalQueryTemplate.html').done(function(data){
			init(data);
		})
	};
	/*
	 修改方法
	 */
	var modify = function(id, grid){
		dataGrid = grid;
		$.get('/pages/gqc/generalQueryTemplate.html').done(function(data){
			init(data, id);
			setData(id);
		})
	};
	/*
	 删除方法
	 */
	var delDataResource = function(ids, grid){
		dataGrid = grid;
		$.post(baseUrl + 'delete.koala', {ids:ids}).done(function(data){
			 if(data.result == 'success'){
				 $('body').message({
					 type: 'success',
					 content: '删除成功'
				 }) ;
				 dataGrid.grid('refresh');
			 }else{
				 $('body').message({
					 type: 'error',
					 content: '删除失败'
				 })
			 }
		}).fail(function(data){
			$('body').message({
				type: 'error',
				content: '删除失败'
			})
		})
	};
	/**
	 * 初始化
	 */
	var init = function(data, id){
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html(id ? '修改通用查询配置':'添加通用查询配置');
		staticQueryLeftTable = dialog.find('#staticQueryLeftTable');
		staticQueryRightTable = dialog.find('#staticQueryRightTable');
		dynamicQueryLeftTable = dialog.find('#dynamicQueryLeftTable');
		dynamicQueryRightTable = dialog.find('#dynamicQueryRightTable');
		showColumnLeftTable = dialog.find('#showColumnLeftTable');
		showColumnRightTable = dialog.find('#showColumnRightTable');
		dataSourceSelect = dialog.find('#dataSourceSelect');
		tableSelect = dialog.find('#tableSelect');
		initLayout();
		fillSelectData(id);
		fillWidgetTypeSelect(dialog.find('.select[data-role="queryOperation"]'));
		fillQueryOperationSelect(dialog.find('.select[data-role="queryOperation"]'));
		dialog.find('#generalQuerySave').on('click',function(){
			save(id);
		}).end().modal({
				keyboard: false
		}).on({
				'hidden.bs.modal': function(){
					$(this).remove();
				},
				'complete': function(){
					$('body').message({
						type: 'success',
						content: '保存成功'
					})
					$(this).modal('hide');
					dataGrid.grid('refresh');
				}
		});
		dialog.find('.grid').find('.grid-table-body').on('scroll', function(){
			$(this).closest('.grid-body').find('.grid-table-head').css('left', -$(this).scrollLeft());
		})
	};
	var initLayout = function(){
		var width = $(window).width() * 0.73;
		dialog.find('.query-condition').each(function(){
			var leftWidth =  width * 0.27;
			var $this = $(this);
			var padding = width>1150 ? 8 : 14*1150/width;
			$this.closest('td').css('width', leftWidth).next('td').css('width', width * 0.73);
			$this.find('.grid-table-body').css('width', leftWidth-padding).find('table').css('width',leftWidth);
			$this.find('.grid-table-head').find('table').css('width',leftWidth);
		})

		dialog.find('.staticQuerySelected, .dynamicQuerySelected, .showColumnSelected').each(function(){
			var rightWidth = width * 0.73;
			var $this = $(this);
			var padding = width>1150 ? 0 : 8*1150/width;
			$this.find('.grid-table-body').css('width', rightWidth-padding).find('table').css('width', rightWidth);
			$this.find('.grid-table-head').find('table').css('width', rightWidth);
		})
	};
	/**
	 * 填充选择框
	 */
	var fillSelectData = function(){
		$.get(baseUrl + 'findAllDataSource.koala').done(function(data){
			var dataSourceList = data.dataSourceList;
			var contents = new Array();
			for(var i=0, j=dataSourceList.length; i<j; i++){
				var dataSource = dataSourceList[i];
				contents.push({value: dataSource.id, title: dataSource.dataSourceId});
			}
			dataSourceSelect.select({
				title: '选择数据源',
				contents: contents
			}).on('change', function(){
					tableSelect.setSelectItems([]);
					var dataSourceId = $(this).getValue();
					var url = baseUrl + 'findAllTable.koala?id='+ dataSourceId;
					$.get(url).done(function(data){
						var tableList = data.tableList;
						var contents = new Array();
						for(var i=0, j=tableList.length; i<j; i++){
							contents.push({value: tableList[i], title: tableList[i]});
						}
						tableSelect.setSelectItems(contents);
						dialog.trigger('dataSourceSelectComplete.koala')
					})
			})
		});
		tableSelect.select({
			title: '选择表'
		}).on('change', function(){
				var id = dataSourceSelect.getValue();
				var tableName = $(this).getValue();
				var url = baseUrl + 'findAllColumn.koala?id='+id+'&tableName='+tableName;
				$.get(url).done(function(data){
					clearSelectedItem();
					fillLeftTable(data.tableMap, data.tableMap)
					dialog.trigger('tableSelectComplete.koala')
				})
		})
	};
	/*
	 *修改填充数据
	 */
	var setData = function(id){
		$.get(baseUrl + 'getById.koala?id='+id)
			.done(function(data){
				generalQueryObject = data.generalQuery;
				dataSourceSelect.setValue(generalQueryObject.dataSource.id).find('button').addClass('disabled');
				dialog.on('dataSourceSelectComplete.koala',  function(){
					tableSelect.setValue(generalQueryObject.tableName).find('button').addClass('disabled');
					var queryConditionColumns = data.queryConditionColumns;
					var showColumns = data.showColumns;
				})
				dialog.on('tableSelectComplete.koala', $.proxy(fillRightTable, this));
				dialog.find('#generalQuery_queryName').val(generalQueryObject.queryName);
				dialog.find('#generalQuery_description').val(generalQueryObject.description);
			}).fail(function(){
				$('body').message({
					type: 'error',
					content: '获取信息失败, 可能数据源连接不通'
				})
			})
	};
	//清楚右边表格填充内容
	var clearSelectedItem = function(){
		staticQueryRightTable.children().remove();
		dynamicQueryRightTable.children().remove();
		showColumnRightTable.children().remove();
	};

	//填充左边表格列
	var fillLeftTable = function(queryConditionColumns, showColumns) {
		var queryConditionRows = new Array();
		for(var column in queryConditionColumns){
			queryConditionRows.push('<tr><td class="column-name">'+column+'</td><td class="operation"><a data-value="'+column+'" data-type="'+queryConditionColumns[column]+'" data-role="add"><span class="glyphicon glyphicon-plus">添加</span></a></td></tr>');
		}
		var queryConditionRowsHtml = queryConditionRows.join();
		staticQueryLeftTable.html(queryConditionRowsHtml)
			.find('a[data-role="add"]')
			.on('click', function(){
				var $this = $(this);
				var fieldName = $this.data('value');
				var row = $(' <tr><td class="column-name">'+fieldName+'<input data-role="fieldName" type="hidden" value="'+fieldName+'"/></td>' +
					'<td class="query-operation"><div class="btn-group select" data-role="queryOperation"></div></td>' +
					'<td class="value"><input data-role="value" class="form-control" required="true"/><span class="required" >*</span></td>' +
					'<td class="visibility"><div class="checker"><span><input type="checkbox" style="opacity: 0;" data-role="visibility"></span></div></td><td class="delete-btn"><a data-role="delete" data-value="'+fieldName+'"><span class="glyphicon glyphicon-remove">删除</span></a></td></tr>');
				var queryOperation =  row.find('[data-role="queryOperation"]');
				fillQueryOperationSelect(queryOperation);
				queryOperation.on('change', function(){
					var valueTd = row.find('.value');
					if($(this).getValue() == 'BETWEEN'){
						valueTd.html('<input data-role="startValue" class="form-control" required="true" style="width:41%!important;"/>&nbsp;AND&nbsp;<input data-role="endValue" class="form-control" required="true" style="width:41%!important;"/><span class="required" >*</span>');
					}else{
						valueTd.html('<input data-role="value" class="form-control" required="true"/><span class="required" >*</span>');
					}
				})
				$this.closest('tr').hide();
				dynamicQueryLeftTable.find('a[data-value="'+fieldName+'"]')
					.closest('tr')
					.hide();
				row.appendTo(staticQueryRightTable).find('[data-role="delete"]').on('click', function(){
					removeQueryRightTableRow($(this));
				});
				row.find('[data-role="visibility"]').on('click', function(){
					if(this.checked){
						$(this).parent().addClass('checked');
					}else{
						$(this).parent().removeClass('checked');
					}
				})
			});
		dynamicQueryLeftTable.html(queryConditionRowsHtml)
			.find('a[data-role="add"]')
			.on('click', function(){
				var $this = $(this);
				var column = $this.data('value');
				var fieldType = $this.data('type')
				var row = $(' <tr><td class="column-name">'+column+'<input data-role="fieldName" type="hidden" value="'+column+'"/><input data-role="fieldType" type="hidden" value="'+fieldType+'"/</td>' +
					'<td class="show-label"><input data-role="label" class="form-control" required="true" value="'+column+'"/><span class="required">*</span></td>' +
					'<td class="widgetType"><div class="btn-group select" data-role="widgetType"></div></td>'+
					'<td class="query-operation"><div class="btn-group select" data-role="queryOperation"></div></td><td class="delete-btn"><a data-role="delete" data-value="'+column+'"><span class="glyphicon glyphicon-remove">删除</span></a></td></tr>');
				fillQueryOperationSelect(row.find('[data-role="queryOperation"]'));
				fillWidgetTypeSelect(row.find('[data-role="widgetType"]'));
				$this.closest('tr').hide();
				staticQueryLeftTable.find('a[data-value="'+column+'"]')
					.closest('tr')
					.hide();
				row.appendTo(dynamicQueryRightTable).find('[data-role="delete"]')
					.on('click', function(){
						removeQueryRightTableRow($(this));
					});
			});
		var showColumnsRows = new Array();
		for(var column in showColumns){
			showColumnsRows.push('<tr><td class="column-name">'+column+'</td><td class="operation"><a data-value="'+column+'" data-type="'+showColumns[column]+'" data-role="add"><span class="glyphicon glyphicon-plus">添加</span></a></td></tr>');
		}
		var showColumnsRowsHtml = showColumnsRows.join();
		showColumnLeftTable.html(showColumnsRowsHtml)
			.find('a[data-role="add"]')
			.on('click', function(){
				var $this = $(this);
				var column = $this.data('value');
				var row = $(' <tr><td class="column-name">'+column+'<input data-role="fieldName" type="hidden" value="'+column+'"/></td>' +
					'<td class="show-label"><input data-role="label" class="form-control"  required="true" value="'+column+'"/><span class="required">*</span></td>' +
					'<td class="delete-btn"><a data-role="delete" data-value="'+column+'"><span class="glyphicon glyphicon-remove">删除</span></a></td></tr>');
				$this.closest('tr').hide();
				row.appendTo(showColumnRightTable).find('[data-role="delete"]')
					.on('click', function(){
						var $this = $(this);
						$this.closest('tr').hide();
						showColumnLeftTable.find('a[data-value="'+$this.data('value')+'"]')
							.closest('tr')
							.show();
					});
			});
	};

	//填充右边表格
	var fillRightTable = function(){
		var preQueryConditions = generalQueryObject.preQueryConditions;
		var dynamicQueryConditions = generalQueryObject.dynamicQueryConditions;
		var fieldDetails = generalQueryObject.fieldDetails;
		for(var i=0, j=preQueryConditions.length; i<j; i++){
			var  preQueryCondition = preQueryConditions[i];
			var  fieldName =  preQueryCondition.fieldName;
			var row = $(' <tr><td class="column-name">'+fieldName+'<input data-role="fieldName" type="hidden" value="'+fieldName+'"/></td>' +
				'<td class="query-operation"><div class="btn-group select" data-role="queryOperation"></div></td>' +
				'<td class="value"><input data-role="value" class="form-control" required="true" value="'+preQueryCondition.value+'"/><span class="required" >*</span></td>' +
				'<td class="visibility"><div class="checker"><span><input type="checkbox" style="opacity: 0;" data-role="visibility"></span></div></td><td class="delete-btn"><a data-role="delete" data-value="'+fieldName+'"><span class="glyphicon glyphicon-remove">删除</span></a></td></tr>');
			var queryOperation =  row.find('[data-role="queryOperation"]');
			fillQueryOperationSelect(queryOperation);
			var valueTd = row.find('.value');
			queryOperation.on('change', function(){
				if($(this).getValue() == 'BETWEEN'){
					valueTd.html('<input data-role="startValue" class="form-control" required="true" style="width:41%!important;"/>&nbsp;AND&nbsp;<input data-role="endValue" class="form-control" required="true" style="width:41%!important;"/><span class="required" >*</span>');
				}else{
					valueTd.html('<input data-role="value" class="form-control" required="true"/><span class="required" >*</span>');
				}
			});
			queryOperation.setValue(preQueryCondition.queryOperation);
			if(preQueryCondition.queryOperation == 'BETWEEN'){
				valueTd.find('[data-role="startValue"]').val(preQueryCondition.startValue);
				valueTd.find('[data-role="endValue"]').val(preQueryCondition.endValue);
			}else{
				valueTd.find('[data-role="value"]').val(preQueryCondition.value);
			}
			row.find('[data-role="delete"]').on('click', function(){
				removeQueryRightTableRow($(this));
			});
			row.find('[data-role="visibility"]').on('click', function(){
				if(this.checked){
					$(this).parent().addClass('checked');
				}else{
					$(this).parent().removeClass('checked');
				}
			});
			preQueryCondition.visible && row.find('[data-role="visibility"]').attr('checked','checked').parent().addClass('checked');;
			staticQueryRightTable.append(row);
			hideQueryLeftTableRow(fieldName);
		}
		for(var i=0, j=dynamicQueryConditions.length; i<j; i++){
			var  dynamicQueryCondition = dynamicQueryConditions[i];
			var  fieldName =  dynamicQueryCondition.fieldName;
			var row = $(' <tr><td class="column-name">'+fieldName+'<input data-role="fieldName" type="hidden" value="'+fieldName+'"/><input data-role="fieldType" type="hidden" value="'+dynamicQueryCondition.fieldType+'"/</td>' +
				'<td class="show-label"><input data-role="label" class="form-control" required="true" value="'+dynamicQueryCondition.label+'"/><span class="required">*</span></td>' +
				'<td class="widgetType"><div class="btn-group select" data-role="widgetType"></div></td>'+
				'<td class="query-operation"><div class="btn-group select" data-role="queryOperation"></div></td><td class="delete-btn"><a data-role="delete" data-value="'+fieldName+'"><span class="glyphicon glyphicon-remove">删除</span></a></td></tr>');
			var widgetType = row.find('[data-role="widgetType"]');
			fillWidgetTypeSelect(widgetType);
			widgetType.setValue(dynamicQueryCondition.widgetType);
			var queryOperation = row.find('[data-role="queryOperation"]');
			fillQueryOperationSelect(queryOperation);
			queryOperation.setValue(dynamicQueryCondition.queryOperation);
			row.find('[data-role="delete"]').on('click', function(){
				removeQueryRightTableRow($(this));
			});
			dynamicQueryRightTable.append(row);
			hideQueryLeftTableRow(fieldName);
		}
		for(var i=0, j=fieldDetails.length; i<j; i++){
			var  fieldDetail = fieldDetails[i];
			var  fieldName =  fieldDetail.fieldName;
			var row = $(' <tr><td class="column-name">'+fieldName+'<input data-role="fieldName" type="hidden" value="'+fieldName+'"/></td>' +
				'<td class="show-label"><input data-role="label" class="form-control"  required="true" value="'+fieldDetail.label+'"/><span class="required">*</span></td>' +
				'<td class="delete-btn"><a data-role="delete" data-value="'+fieldName+'"><span class="glyphicon glyphicon-remove">删除</span></a></td></tr>');
			row.find('[data-role="delete"]').on('click', function(){
				var $this = $(this);
				$this.closest('tr').remove();
				showColumnLeftTable.find('a[data-value="'+$this.data('value')+'"]')
					.closest('tr')
					.show();
			});
			showColumnRightTable.append(row);
			showColumnLeftTable.find('a[data-value="'+fieldName+'"]')
				.closest('tr')
				.hide();
		}
	};
	 //隐藏查询条件左边的行
	var hideQueryLeftTableRow = function(column){
		staticQueryLeftTable.find('a[data-value="'+column+'"]')
			.closest('tr')
			.hide();
		dynamicQueryLeftTable.find('a[data-value="'+column+'"]')
			.closest('tr')
			.hide();
	};
	//删除右边的行
	var removeQueryRightTableRow = function($this){
		var column = $this.data('value');
		$this.closest('tr').remove();
		staticQueryLeftTable.find('a[data-value="'+column+'"]')
			.closest('tr')
			.show();
		dynamicQueryLeftTable.find('a[data-value="'+column+'"]')
			.closest('tr')
			.show();
	};

	//填充条件
	var fillWidgetTypeSelect = function(select){
		select.select({
			contents: [
				{value: 'TEXT', title: '文本框',selected: true},
				{value: 'DATE', title: '日期框'}
			]
		});
	};
	//填充条件
	var fillQueryOperationSelect = function(select){
		select.select({
			contents: [
				{value: 'EQ', title: '等于',selected: true},
				{value: 'GT', title: '大于'},
				{value: 'GE', title: '大于等于'},
				{value: 'LT', title: '小于'},
				{value: 'LE', title: '小于等于'},
				{value: 'NE', title: '不等于'},
				{value: 'LIKE', title: 'LIKE'},
				{value: 'BETWEEN', title: 'BETWEEN'}
			]
		});
	};

	/*
	 *   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(id){
		if(!validate()){
			return false;
		}
		var url = baseUrl + 'add.koala';
		var params = getAllData();
		if(id){
			url =  baseUrl + 'update.koala';
			params.id = id;
			params['dataSource.dataSourceId'] =  generalQueryObject.dataSource.dataSourceId;
			params.version = generalQueryObject.version;
		}
		$.post(url, params).done(function(data){
			if(data.result == 'success'){
				dialog.trigger('complete');
			}else{
				dialog.message({
					type: 'error',
					content: data.result
				});
			}
		});
	};
	/*
	* 数据验证  成功返回true  失败返回false
	 */
	var validate = function(){
		if(!dataSourceSelect.getValue()){
			showErrorMessage(dataSourceSelect, '请选择数据源');
			return false;
		}
		if(!tableSelect.getValue()){
			showErrorMessage(tableSelect, '请选择数据表');
			return false;
		}
		var flag = true;
		dialog.find('.generalQuery').find('input[required=true],input[rgExp]').each(function(){
			var $this = $(this);
			var value = $this.val();
			if($this.attr('required') && !checkNotNull(value)){
				showErrorMessage($this, '请填入该选项内容');
				flag = false;
				return false;
			}
			var rgExp = $this.attr('rgExp');
			if(rgExp && !value.match(eval(rgExp))){
				showErrorMessage($this, $this.data('content'));
				flag = false;
				return false;
			}
		});
		return flag;
	};
	/**
	 * 显示提示信息
	 */
	var showErrorMessage = function($element, content){
		$element.popover({
			content: content,
			trigger: 'manual',
			container: dialog
		}).popover('show').on({
				'blur': function(){
					$element.popover('destroy');
					$element.parent().removeClass('has-error');
				},
				'keydown': function(){
					$element.popover('destroy');
					$element.parent().removeClass('has-error');
				}
			}).focus().parent().addClass('has-error');
	};
	var getAllData = function(){
		var data = {};
		data['dataSource.id'] = dataSourceSelect.getValue();
		data.tableName =  tableSelect.getValue();
		data.queryName = dialog.find('#generalQuery_queryName').val();
		data.description = dialog.find('#generalQuery_description').val();
		staticQueryRightTable.find('tr').each(function(index,tr){
			var $tr = $(tr);
			data['preQueryConditions['+index+'].fieldName'] = $tr.find('input[data-role="fieldName"]').val();
			data['dynamicQueryConditions['+index+'].fieldType'] = $tr.find('input[data-role="fieldType"]').val();
			var queryOperation = $tr.find('[data-role="queryOperation"]').getValue();
			data['preQueryConditions['+index+'].queryOperation'] = queryOperation;
			if(queryOperation == 'BETWEEN'){
				data['preQueryConditions['+index+'].startValue'] = $tr.find('input[data-role="startValue"]').val();
				data['preQueryConditions['+index+'].endValue'] = $tr.find('input[data-role="endValue"]').val();
			}else{
				data['preQueryConditions['+index+'].value'] = $tr.find('input[data-role="value"]').val();
			}
			var visibility = $tr.find('input[data-role="visibility"]').is(':checked');
			if(visibility){
				data['preQueryConditions['+index+'].visible'] = visibility;
			}
		});
		dynamicQueryRightTable.find('tr').each(function(index,tr){
			var $tr = $(tr);
			data['dynamicQueryConditions['+index+'].fieldName'] = $tr.find('input[data-role="fieldName"]').val();
			data['dynamicQueryConditions['+index+'].fieldType'] = $tr.find('input[data-role="fieldType"]').val();
			data['dynamicQueryConditions['+index+'].label'] = $tr.find('input[data-role="label"]').val();
			data['dynamicQueryConditions['+index+'].queryOperation'] = $tr.find('[data-role="queryOperation"]').getValue();
			data['dynamicQueryConditions['+index+'].widgetType'] = $tr.find('[data-role="widgetType"]').getValue();
		});
		showColumnRightTable.find('tr').each(function(index,tr){
			var $tr = $(tr);
			data['fieldDetails['+index+'].fieldName'] = $tr.find('input[data-role="fieldName"]').val();
			data['fieldDetails['+index+'].label'] = $tr.find('input[data-role="label"]').val();
		});
		return data;
	};
	var preview = function(id){
		window.open('/pages/gqc/previewTemplate.html?id='+id,'预览');
	};
	/**
	 * 检查变量是否不为空  true:不空   false:空
	 */
	var checkNotNull = function(item){
		//不能为空和空格
		if(item==null || item=="" || item.replace(/(^\s*)|(\s*$)/g, "")=="" ){
			return false;
		}else{
			return true;
		}
	};
	return {
		add: add,
		modify: modify,
		del: delDataResource,
		preview: preview
	};
};

