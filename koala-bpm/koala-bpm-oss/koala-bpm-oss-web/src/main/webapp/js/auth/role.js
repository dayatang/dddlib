var roleManager = function(){
	var baseUrl = contextPath + '/auth/Role/';
	var dialog = null;    //对话框
	var roleName = null;   //角色名称
	var roleDescript = null;    //角色描述
	var dataGrid = null; //Grid对象
	/*
	 *新增
	 */
	var add = function(grid){
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/role-template.html').done(function(data){
			init(data);
		});
	};
	/*
	 * 修改
	 */
	var modify = function(item, grid){
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/role-template.html').done(function(data){
			init(data,item);
			setData(item);
		});
	};
	/*
	 删除方法
	 */
	var deleteUser = function(roles, grid){
		var data = {};
		for(var i=0,j=roles.length; i<j; i++){
			var role = roles[i];
			data['roles['+i+'].id'] = role.id;
		}
		dataGrid = grid;
		$.post(baseUrl + 'del.koala', data).done(function(data){
			if(data.result == 'success'){
				dataGrid.message({
					type: 'success',
					content: '删除成功'
				});
				dataGrid.grid('refresh');
			}else{
				$('body').message({
					type: 'error',
					content: data.actionError
				});
			}
		}).fail(function(data){
				dataGrid.message({
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
		dialog.find('.modal-header').find('.modal-title').html(item ? '修改角色':'添加角色');
		roleName = dialog.find('#roleName');
		roleDescript = dialog.find('#roleDescript');
		dialog.find('#save').on('click',function(){
			save(item);
		}).end().modal({
			keyboard: false
		}).on({
				'hidden.bs.modal': function(){
					$(this).remove();
				},
				'complete': function(){
					dataGrid.message({
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
		roleName.val(item.name);
		roleDescript.val(item.roleDesc);
	};
		
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
				dialog.find('.modal-content').message({
					type: 'error',
					content: data.actionError
				});
			}
		});
	};
	/**
	 * 数据验证
	 */
	var validate = function(item){
		if(!Validation.notNull(dialog, roleName, roleName.val(), '请输入角色名称')){
			return false;
		}
		return true;
	};
	/*
	*获取表单数据
	 */
	var getAllData = function(item){
		var data = {};
		data['roleVO.name'] = roleName.val();
		data['roleVO.roleDesc'] = roleDescript.val();
		if(item){
			data['roleVO.id'] = item.id;	
		}
		return data;
	};
	/**
	 * 分配角色
	 */
	var assignRole = function(userId, userAccount, grid){
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/select-role.html').done(function(data){
			var dialog = $(data);
			dialog.find('#save').on('click',function(){
				var indexs = dialog.find('#selectRoleGrid').data('koala.grid').selectedRowsIndex();
				if(indexs.length == 0){
					dialog.find('.modal-content').message({
						type: 'warning',
						content: '请选择要分配的角色'
					});
					return;
				}
				var data = {};
				data['userVO.id'] = userId;
				for(var i=0,j=indexs.length; i<j; i++){
					data['roles['+i+'].id'] = indexs[i];
				}
				$.post(contextPath + '/auth/User/assignRoles.koala', data).done(function(data){
					if(data.result == 'success'){
						dataGrid.message({
							type: 'success',
							content: '保存成功'
						});
						dialog.modal('hide');
						dataGrid.grid('refresh');
					}else{
						dataGrid.message({
							type: 'error',
							content: data.actionError
						});
					}
				}).fail(function(data){
					dataGrid.message({
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
						initSelectRoleGrid(userId, userAccount, dialog);
					},
					'complete': function(){
						dataGrid.message({
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
	var initSelectRoleGrid = function(userId, userAccount, dialog){
		var columns = [ 
					{
						title : "角色名称",
						name : "name",
						width : 150
					}, 
					{
						title : "角色描述",
						name : "roleDesc",
						width : 150
					}
				];
		dialog.find('#selectRoleGrid').grid({
			 identity: 'id',
             columns: columns,
             querys: [{title: '角色名称', value: 'roleNameForSearch'}],
             url: baseUrl+'queryRolesForAssign.koala?userId='+userId+'&userAccount='+userAccount
        });
	};
	/**
	 * 解除角色关联
	 */
	var removeRoleForUser = function(userId, roles, grid){
		var data = {};
		for(var i=0,j=roles.length; i<j; i++){
			data['roles['+i+'].id'] = roles[i].id;
		}
		data.userId = userId;
		dataGrid = grid;
		$.post(baseUrl + 'removeRoleForUser.koala', data).done(function(data){
			if(data.result == 'success'){
				dataGrid.message({
					type: 'success',
					content: '删除成功'
				});
				dataGrid.grid('refresh');
			}else{
				dataGrid.message({
					type: 'error',
					content: data.actionError
				});
			}
		}).fail(function(data){
				dataGrid.message({
					type: 'error',
					content: '删除失败'
				});
			});
	};
	var assignUser = function(roleId, name){
		openTab('/pages/auth/user-list.html',
			name+'的用户管理', 'userManager_'+roleId, roleId, {roleId: roleId});
	};
	/**
	 * 资源授权
	 */
	var assignResource = function(grid, roleId){
		$.get(contextPath + '/pages/auth/assign-resource.html').done(function(data){
			var dialog = $(data);
            initResourceTree(dialog, roleId);
            dialog.find('#save').on('click',function(){
				var treeObj = $("#resourceTree").getTree();
				var nodes = treeObj.selectedItems();
				var data = {};
				data['roleVO.id'] = roleId;
				for(var i=0,j=nodes.length; i<j; i++){
					data['menus['+i+'].id'] = nodes[i].id;
				}
				$.post(baseUrl + 'assignMenuResources.koala', data).done(function(data){
					if(data.result == 'success'){
						grid.message({
							type: 'success',
							content: '保存成功'
						});
						dialog.modal('hide');
					}else{
						dialog.find('.modal-content').message({
							type: 'error',
							content: data.result
						});
					}
				}).fail(function(data){
						dialog.find('.modal-content').message({
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
					'complete': function(){
						dataGrid.message({
							type: 'success',
							content: '保存成功'
						});
						$(this).modal('hide');
						dataGrid.grid('refresh');
					}
			});
		});
	};
	/*
	* 加载资源树
	 */
	var initResourceTree = function(dialog, roleId){
		$.get(contextPath + '/auth/Menu/findMenuTreeSelectItemByRole.koala?time='+new Date().getTime()+'&roleId='+roleId).done(function(result){
			var zNodes = new Array();
			var items = result.data;
			for(var i=0, j=items.length; i<j; i++){
				var item = items[i];
				var zNode = {};
                var menu = {};
                menu.id = item.id;
                menu.title = item.name;
                menu.open = true;
                menu.checked = item.ischecked;
                zNode.menu = menu;
				if(item.children && item.children.length > 0){
					zNode.children = getChildrenData(new Array(), item.children);
				}
                zNode.type = item.menuType == '1' ?  'children' : 'parent';
				zNodes.push(zNode);
			}
            var dataSourceTree = {
                data: zNodes,
                delay: 400
            };
            dialog.find('#resourceTree').tree({
                dataSource: dataSourceTree,
                loadingHTML: '<div class="static-loader">Loading...</div>',
                multiSelect: true,
                useChkBox: true,
                cacheItems: true
            });
		});
	};
	/**
	 * 
	 */
	var getChildrenData = function(nodes, items){
		for(var i=0,j=items.length; i<j; i++){
			var item = items[i];
            var zNode = {};
            var menu = {};
            menu.id = item.id;
            menu.title = item.name;
            menu.open = true;
            menu.checked = item.ischecked;
            zNode.menu = menu;
            if(item.children && item.children.length > 0){
                zNode.children = getChildrenData(new Array(), item.children);
            }
            zNode.type = item.menuType == '1' ?  'children' : 'parent';
            nodes.push(zNode);
		}
		return nodes;
	};
	return {
		add: add,
		modify: modify,
		deleteUser: deleteUser,
		assignRole: assignRole,
		removeRoleForUser: removeRoleForUser,
		assignUser: assignUser,
		assignResource: assignResource
	};
};