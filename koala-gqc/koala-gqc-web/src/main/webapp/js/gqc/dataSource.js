var dataSource = function(){
	var baseUrl = 'dataSource/';
	var dialog = null;    //对话框
	var dataSourceType = null;   //数据源类型
	var dataSourceId = null;    //数据源ID
	var dataSourceDescription = null;    //描述
	var dataSourceJdbcDriver = null; //数据源JdbcDriver
	var dataSourceUrl = null;     //数据源URL
	var dataSourceUserName = null;   //用户名
	var dataSourcePassword = null; //密码
	var dataGrid = null; //Grid对象
	/*
	 *新增
	 */
	var add = function(grid){
		dataGrid = grid;
		$.get('pages/dataSourceTemplate.html').done(function(data){
			init(data);
		});
	};
	/*
	 * 修改
	 */
	var modify = function(id, grid){
		dataGrid = grid;
		$.get('pages/dataSourceTemplate.html').done(function(data){
			init(data,id);
			setData(id);
		});
	};
	/*
	 删除方法
	 */
	var delGeneralQuery = function(ids, grid){
		dataGrid = grid;
		$.post(baseUrl + 'delete.koala', {ids:ids}).done(function(data){
			if(data.result == 'success'){
				$('body').message({
					type: 'success',
					content: '删除成功'
				});
				dataGrid.grid('refresh');
			}else{
				$('body').message({
					type: 'error',
					content: '删除失败'
				});
			}
		}).fail(function(data){
				$('body').message({
					type: 'error',
					content: '删除失败'
				});
			});
	};
	/**
	 * 初始化
	 */
	var init = function(data, id){
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html(id ? '修改数据源':'添加数据源');
		dataSourceType = dialog.find('#dataSourceType');
		dataSourceId = dialog.find('#dataSourceId');
		dataSourceDescription =  dialog.find('#dataSourceDescription');
		dataSourceJdbcDriver = dialog.find('#dataSourceJdbcDriver');
		dataSourceUrl = dialog.find('#dataSourceUrl');
		dataSourceUserName = dialog.find('#dataSourceUserName');
		dataSourcePassword = dialog.find('#dataSourcePassword');
		fillSelectData();
		dialog.find('#dataSourceSave').on('click',function(){
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
					});
					$(this).modal('hide');
					dataGrid.grid('refresh');
				}
		}).find('#dataSourceConnectionTest').on('click',function(){
			 testConnection();
		});
	};
	/*
	*测试连接性
	 */
	var testConnection = function(){
		$.post(baseUrl+'checkDataSource.koala', getAllData()).done(function(data){
			var result = data.result;
			var type = data.result == '该数据源不可用' ? 'warning' : 'success';
			dialog.message({
				type: type,
				content: result
			});
		});
	};
	/*
	 *测试连接性
	 */
	var testConnectionById = function(id){
		$.post(baseUrl+'checkDataSourceById.koala?id='+id).done(function(data){
			var result = data.result;
			var type = data.result == '该数据源不可用' ? 'warning' : 'success';
			$('body').message({
				type: type,
				content: result
			});
		});
	};
	/*
	 *设置值
	 */
	var setData = function(id){
		$.get(baseUrl+'get/'+id+'.koala')
			.done(function(result){
				var data = result.data;
				dataSourceType.setValue(data.dataSourceType).trigger('change').find('button').addClass('disabled');
				dataSourceId.val(data.dataSourceId).attr('disabled',true);
				if(data.dataSourceType == 'CUSTOM_DATA_SOURCE'){
					dataSourceDescription.val(data.dataSourceDescription);
					dataSourceJdbcDriver.val(data.jdbcDriver);
					dataSourceUrl.val(data.connectUrl);
					dataSourceUserName.val(data.username);
					dataSourcePassword.val(data.password);
				}
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
		if(id){
			url =  baseUrl + 'update.koala?id='+id;
		}
		$.post(url,getAllData()).done(function(data){
			if(data.result == 'success'){
				dialog.trigger('complete');
			}else if(data.result == '该数据源ID已存在'){
				showErrorMessage(dataSourceId, '该数据源ID已存在');
			}else{
				dialog.message({
					type: 'error',
					content: '系统繁忙'
				});
			}
		});
	};
	/*
	*获取表单数据
	 */
	var getAllData = function(){
		var data = {};
		data.dataSourceType = dataSourceType.getValue();
		data.dataSourceId = dataSourceId.val();
		if(data.dataSourceType == 'CUSTOM_DATA_SOURCE'){
			data.dataSourceDescription = dataSourceDescription.val();
			data.jdbcDriver = dataSourceJdbcDriver.val();
			data.connectUrl = dataSourceUrl.val();
			data.username = dataSourceUserName.val();
			data.password = dataSourcePassword.val();
		}
		return data;
	};
	/**
	 * 填充选择框
	 */
	var fillSelectData  = function(){
		dialog.find('#dataSourceType').select({
			title: '选择数据源类型',
			contents: [
				{value: 'SYSTEM_DATA_SOURCE', title: '系统数据源'},
				{value: 'CUSTOM_DATA_SOURCE', title: '自定义数据源', selected: true}
			]
		}).on('change', function(){
				if($(this).getValue() == 'SYSTEM_DATA_SOURCE'){
					dataSourceDescription.closest('.form-group').hide();
					dataSourceJdbcDriver.closest('.form-group').hide();
					dataSourceUrl.closest('.form-group').hide();
					dataSourceUserName.closest('.form-group').hide();
					dataSourcePassword.closest('.form-group').hide();
				}else{
					dataSourceDescription.closest('.form-group').show();
					dataSourceJdbcDriver.closest('.form-group').show();
					dataSourceUrl.closest('.form-group').show();
					dataSourceUserName.closest('.form-group').show();
					dataSourcePassword.closest('.form-group').show();
				}
			});
	};
	/**
	 * 表单验证 通过返回true  失败返回false
	 */
	var validate = function(){
		if(!dataSourceType.getValue()){
			showErrorMessage(dataSourceType, '请选择数据源类型');
			return false;
		}
		if(!checkNotNull(dataSourceId.val())){
			showErrorMessage(dataSourceId, '请填写数据源ID');
			return false;
		}
		if(dataSourceType.getValue() == 'CUSTOM_DATA_SOURCE'){
			if(!checkNotNull(dataSourceJdbcDriver.val())){
				showErrorMessage(dataSourceJdbcDriver, '请填写JdbcDriver');
				return false;
			}
			if(!checkNotNull(dataSourceUrl.val())){
				showErrorMessage(dataSourceUrl, '请填写Url');
				return false;
			}
			if(!checkNotNull(dataSourceUserName.val())){
				showErrorMessage(dataSourceUserName, '请填写UserName');
				return false;
			}
		}
		return true;
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
		del: delGeneralQuery,
		testConnectionById: testConnectionById
	};
};