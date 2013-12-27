var userManager = function(){
	var baseUrl = contextPath + '/auth/User/';
	var dialog = null;    //对话框
	var userName = null;   //用户名
	var userAccount = null;    //用户账号
    var email = null;    //用户邮箱
	var userPassword = null;    //用户密码
	var userDescript = null; //描述
	var isEnable = null;     //是否启用
	var dataGrid = null; //Grid对象
	/*
	 *新增
	 */
	var add = function(grid){
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/user-template.html').done(function(data){
			init(data);
		});
	};
	/*
	 * 修改
	 */
	var modify = function(item, grid){
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/user-template.html').done(function(data){
			init(data,item);
			setData(item);
		});
	};
	/*
	 删除方法
	 */
	var deleteUser = function(users, grid){
		var data = {};
		for(var i=0,j=users.length; i<j; i++){
			var user = users[i];
			data['users['+i+'].id'] = user.id;
			data['users['+i+'].userAccount'] = user.userAccount;
		}
		dataGrid = grid;
		$.post(baseUrl + 'del.koala', data).done(function(data){
			if(data.result == 'success'){
				$('body').message({
					type: 'success',
					content: '删除成功'
				});
				dataGrid.grid('refresh');
			}else{
				$('body').message({
					type: 'error',
					content: data.result
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
	var init = function(data, item){
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html(item ? '修改用户信息':'添加用户');
		userName = dialog.find('#userName');
		userAccount = dialog.find('#userAccount');
        email = dialog.find('#email');
		userPassword = dialog.find('#userPassword');
		userDescript = dialog.find('#userDescript');
		isEnable = dialog.find('[name="isEnable"]');
		isEnable.on('click', function(){
			isEnable.each(function(){
				$(this).parent().removeClass('checked');
			})
			$(this).parent().addClass('checked');
		});
		dialog.find('#save').on('click',function(){
			save(item);
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
		});
	};

	/*
	 *设置值
	 */
	var setData = function(item){
		userName.val(item.name);
		userAccount.val(item.userAccount).attr('disabled', 'disabled');
		email.val(item.email).attr('disabled', 'disabled');
		userPassword.closest('.form-group').hide();
		userDescript.val(item.userDesc);
		if(!item.valid){
			dialog.find('[name="isEnable"][value="true"]').removeAttr('checked', 'checked').parent().removeClass('checked');
			dialog.find('[name="isEnable"][value="false"]').attr('checked', 'checked').parent().addClass('checked');
		}
	}
		
	/*
	*   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(item){
		if(!validate(item)){
			return false;
		}
		var url = baseUrl + 'add.koala';
		if(item){
			url =  baseUrl + 'update.koala';
		}
		$.post(url,getAllData(item)).done(function(data){
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
	/**
	 * 数据验证
	 */
	var validate = function(item){
		if(!Validation.notNull(dialog, userName, userName.val(), '请输入用户名称')){
			return false;
		}
		if(!Validation.notNull(dialog, userAccount, userAccount.val(), '请输入用户账号')){
			return false;
		}
        if(!Validation.notNull(dialog, email, email.val(), '请输入用户邮箱')){
            return false;
        }
        if(!Validation.email(dialog, email, email.val(), '邮箱不合法')){
            return false;
        }
		if(!item && !Validation.notNull(dialog, userPassword, userPassword.val(), '请输入用户密码')){
			return false;
		}
		return true;
	}
	/*
	*获取表单数据
	 */
	var getAllData = function(item){
		var data = {};
		data['userVO.name'] = userName.val();
		data['userVO.userAccount'] = userAccount.val();
        data['userVO.email'] = email.val();
		if(item){
			data['userVO.id'] = item.id;	
		}else{
			data['userVO.userPassword'] = userPassword.val();
		}
		data['userVO.userDesc'] = userDescript.val();
		data['userVO.valid'] = dialog.find(':radio:checked').val();
		return data;
	};
	/**
	 * 分配角色
	 */
	var assignRole = function(userId, userAccount){
		openTab('/pages/auth/role-list.html',
			userAccount+'的角色管理', 'roleManager_'+userId, userId, {userId: userId, userAccount:userAccount});
	};
	/**
	 * 分配用户
	 */
	var assignUser = function(roleId, grid){
		$.get(contextPath + '/pages/auth/select-user.html').done(function(data){
			var dialog = $(data);
			dialog.find('#save').on('click',function(){
				var indexs = dialog.find('#selectUserGrid').data('koala.grid').selectedRowsIndex();
				if(indexs.length == 0){
					$('body').message({
						type: 'warning',
						content: '请选择要分配的用户'
					});
					return;
				}
				var data = {};
				data['roleVO.id'] = roleId;
				for(var i=0,j=indexs.length; i<j; i++){
					data['users['+i+'].id'] = indexs[i];
				}
				$.post(contextPath + '/auth/Role/assignUsers.koala', data).done(function(data){
					if(data.result == 'success'){
						$('body').message({
							type: 'success',
							content: '保存成功'
						});
						dialog.modal('hide');
						grid.grid('refresh');
					}else{
						$('body').message({
							type: 'error',
							content: data.result
						});
					}
				}).fail(function(data){
					$('body').message({
						type: 'error',
						content: '保存失败'
					});
				});
			}).end().modal({
				keyboard: false
			}).on({
					'hidden.bs.modal': function(){
						$(this).remove();
					},
					'shown.bs.modal': function(){
						initSelectUserGrid(roleId, dialog);
					},
					'complete': function(){
						$('body').message({
							type: 'success',
							content: '保存成功'
						});
						$(this).modal('hide');
						dataGrid.grid('refresh');
					}
			});
			 //兼容IE8 IE9
	        if(window.ActiveXObject){
	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
	        	   dialog.trigger('shown.bs.modal');
	           }
	        }
		});
	};
	/**
	 * 初始化角色选择grid
	 */
	var initSelectUserGrid = function(roleId, dialog){
		var columns = [ 
					{
						title : "用户名称",
						name : "name",
						width : 150
					}, 
					{
						title : "用户帐号",
						name : "userAccount",
						width : 150
					}, 
					{
						title : "是否有效",
						name : "valid",
						width : 100,
						render : function(item, name, index) {
							return item[name] == true ? "是" : "否";
						}
					}
				];
		dialog.find('#selectUserGrid').grid({
			 identity: 'id',
             columns: columns,
             querys: [{title: '用户名称', value: 'userNameForSearch'},{title: '用户账号', value: 'userAccountForSearch'}],
             url: baseUrl+'queryUsersForAssign.koala?roleId='+roleId
        });
	};
	/**
	 * 从角色下删除用户
	 */
	var removeUserForRole = function(roleId, users, grid){
		var data = {};
		for(var i=0,j=users.length; i<j; i++){
			data['users['+i+'].id'] = users[i].id;
		}
		data.roleId = roleId;
		$.post(baseUrl + 'removeUserForRole.koala', data).done(function(data){
			if(data.result == 'success'){
				$('body').message({
					type: 'success',
					content: '删除成功'
				});
				grid.grid('refresh');
			}else{
				$('body').message({
					type: 'error',
					content: data.result
				});
			}
		}).fail(function(data){
				$('body').message({
					type: 'error',
					content: '删除失败'
				});
			});
	};
	return {
		add: add,
		modify: modify,
		deleteUser: deleteUser,
		assignRole: assignRole,
		assignUser: assignUser,
		removeUserForRole: removeUserForRole
	};
};