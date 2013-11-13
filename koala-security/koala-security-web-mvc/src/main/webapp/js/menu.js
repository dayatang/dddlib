var menuManager = function(){
	var baseUrl = 'auth/Menu/';
	var dialog = null;    //对话框
	var parentName = null;   //父资源名称
	var name = null; //资源名称
	var identifier = null; //资源标识
	var menuType = null; //资源类型
	var desc = null; //资源描述
	var menuImg = null; //菜单图片
	var menuImgBtn = null; //菜单图片按钮
	var dataGrid = null; //Grid对象
	var opreate = null;
	var parentId = null;
	var parentLevel = null;
	/*
	 *新增
	 */
	var add = function(grid, item){
		dataGrid = grid;
		opreate = 'add';
		$.get('pages/auth/menu-template.html').done(function(data){
			init(data);
			if(item){
				parentId = item.id;
				parentLevel = item.level;
				parentName.val(item.name);
			}
		});
	};
	/*
	 * 修改
	 */
	var modify = function(item, grid){
		dataGrid = grid;
		opreate = 'modify';
		$.get('pages/auth/menu-template.html').done(function(data){
			init(data,item);
		});
	};
	/*
	 删除方法
	 */
	var deleteItem = function(resource, grid){
		var data = {};
		data['resVO.id'] = resource.id;
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
		dialog.find('.modal-header').find('.modal-title').html(item ? '修改菜单信息':'添加菜单');
		parentName = dialog.find('#parentName');
		name = dialog.find('#name');
		identifier = dialog.find('#identifier');
		menuType = dialog.find('#menuType');
		menuImg = dialog.find('#menuIcon');
		menuImgBtn = dialog.find('#iconBtn');
		menuImg.attr("src", "/images/icons/menu/attibutes.gif");
		menuImg.attr("alt", "图片为空");
		desc = dialog.find('#desc');
		$.get(baseUrl+'findMenuType.koala').done(function(result){
			var content = new Array();
			for(var i=0,j=result.data.length; i<j; i++){
				var obj = result.data[i];
				if (obj.name == "KOALA_MENU") {
					content.push({title: '菜单', value: obj.id});
				}else if (obj.name == "KOALA_DIRETORY") {
					content.push({title: '目录', value: obj.id});
				}else{
					content.push({title: obj.name, value: obj.id});
				}
			}
			menuType.select({
				title: '选择菜单类型',
				contents: content
			});
			if(opreate == 'modify'){
				setData(item);
			}
		});
		menuImgBtn.on('click', function(){
			$.get('pages/auth/imgsDialog.html').done(function(result){
				var imgsDialog = $(result);
				$.get('/auth/Menu/getIconNames.koala').done(function(result){
					var icons = result.data;
					var contents = new Array();
					for(var i=0,j=icons.length; i<j; i++){
						contents.push('<img src="/'+icons[i]+'"></img>');
					}
					imgsDialog.find('#images').html(contents.join(''))
						.find('img')
						.on('click', function(){
							 menuImg.attr('src', $(this).attr('src'));
							 imgsDialog.modal('hide');
						});
				});
				imgsDialog.modal({
					keyboard: false
				}).on({
					'hidden.bs.modal': function(){
						$(this).remove();
					}
				});
			});
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
		parentName.closest('.form-group').hide();
		name.val(item.name);
		identifier.val(item.identifier);
		desc.val(item.desc);
		menuImg.attr('src', item.icon)
		menuType.setValue(item.menuType)
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
		if(parentId && parentLevel){
			url =  baseUrl + 'addAndAssignParent.koala';
		}
		$.post(url,getAllData(item)).done(function(data){
			if(data.result == 'success'){
				dialog.trigger('complete');
			}else{
				dialog.message({
					type: 'error',
					content: '保存失败'
				});
			}
		});
	};
	/**
	 * 数据验证
	 */
	var validate = function(item){
		if(!Validation.notNull(dialog, name, name.val(), '请输入菜单名称')){
			return false;
		}
		if(!Validation.notNull(dialog, identifier, identifier.val(), '请输入菜单标识')){
			return false;
		}
		if(!Validation.notNull(dialog, menuType, menuType.getValue(), '请选择菜单类型')){
			return false;
		}
		return true;
	}
	/*
	*获取表单数据
	 */
	var getAllData = function(item){
		var data = {};
		data['resVO.desc'] = desc.val();
		data['resVO.identifier'] = identifier.val();
		data['resVO.name'] = name.val();
		data['resVO.icon'] = menuImg.attr('src');
		data['resVO.menuType'] = menuType.getValue();
		if(item){
			data['resVO.id'] = item.id;	
		}
		if(parentId && parentLevel){
			data['parent.id'] = parentId;
			data['parent.level'] = parentLevel;
		}
		return data;
	};

	/**
	 * 上移
	 */
	var moveUp = function(grid){
		var dataGrid = grid.getGrid();
		var indexs = dataGrid.selectedRowsNo();
		if(indexs.length == 0){
	        $this.message({
	            type: 'warning',
	             content: '请选择要操作的记录'
	        })
	        return;
	    }
		if(indexs.length > 1){
	        $this.message({
	            type: 'warning',
	             content: '只能选择一条记录进行操作'
	        })
	        return;
	    }
		var dataGrid = grid.getGrid();
		dataGrid.up(indexs[0]);
		changePosition(dataGrid.selectedAllRows());
	};

	/**
	 * 下移
	 */
	var moveDown = function(grid){
		var dataGrid = grid.getGrid();
		var indexs = dataGrid.selectedRowsNo();
		if(indexs.length == 0){
	        $this.message({
	            type: 'warning',
	             content: '请选择要操作的记录'
	        })
	        return;
	    }
		if(indexs.length > 1){
	        $this.message({
	            type: 'warning',
	             content: '只能选择一条记录进行操作'
	        })
	        return;
	    }
		var dataGrid = grid.getGrid();
		dataGrid.down(indexs[0]);
		changePosition(dataGrid.selectedAllRows());
	};
	var changePosition = function(items){
		console.info(items)
		var data = {};
		for(var i=0,j=items.length; i<j; i++){
			var item = items[i];
			data['resourceVOs['+i+'].id'] = item.id;
			data['resourceVOs['+i+'].sortOrder'] = i+1;
		}
		$.post('/auth/Menu/updateMenuOrder.koala', data).done(function(result){
			console.info(result)
		}).fail(function(result){

		});
	};
	return {
		add: add,
		modify: modify,
		deleteItem: deleteItem,
		moveUp: moveUp,
		moveDown: moveDown
	};
};