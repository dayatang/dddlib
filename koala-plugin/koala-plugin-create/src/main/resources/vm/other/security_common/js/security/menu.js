var menuManager = function(){
	var baseUrl = contextPath + '/auth/Menu/';
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
		$.get(contextPath + '/pages/auth/menu-template.html').done(function(data){
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
		$.get(contextPath + '/pages/auth/menu-template.html').done(function(data){
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
		menuImg.addClass('glyphicon glyphicon-list-alt').attr('src', 'glyphicon glyphicon-list-alt');
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
            var icons = ['glyphicon  glyphicon-adjust','glyphicon  glyphicon-align-center','glyphicon  glyphicon-align-justify','glyphicon  glyphicon-align-left','glyphicon  glyphicon-align-right','glyphicon  glyphicon-arrow-down','glyphicon  glyphicon-arrow-left','glyphicon  glyphicon-arrow-right','glyphicon  glyphicon-arrow-up','glyphicon  glyphicon-asterisk','glyphicon  glyphicon-backward','glyphicon  glyphicon-ban-circle','glyphicon  glyphicon-barcode','glyphicon  glyphicon-bell','glyphicon  glyphicon-bold','glyphicon  glyphicon-book','glyphicon  glyphicon-bookmark','glyphicon  glyphicon-briefcase','glyphicon  glyphicon-bullhorn','glyphicon  glyphicon-calendar','glyphicon  glyphicon-camera','glyphicon  glyphicon-certificate','glyphicon  glyphicon-check','glyphicon  glyphicon-chevron-down','glyphicon  glyphicon-chevron-left','glyphicon  glyphicon-chevron-right','glyphicon  glyphicon-chevron-up','glyphicon  glyphicon-circle-arrow-down','glyphicon  glyphicon-circle-arrow-left','glyphicon  glyphicon-circle-arrow-right','glyphicon  glyphicon-circle-arrow-up','glyphicon  glyphicon-cloud','glyphicon  glyphicon-cloud-download','glyphicon  glyphicon-cloud-upload','glyphicon  glyphicon-cog','glyphicon  glyphicon-collapse-down','glyphicon  glyphicon-collapse-up','glyphicon  glyphicon-comment','glyphicon  glyphicon-compressed','glyphicon  glyphicon-copyright-mark','glyphicon  glyphicon-credit-card','glyphicon  glyphicon-cutlery','glyphicon  glyphicon-dashboard','glyphicon  glyphicon-download','glyphicon  glyphicon-download-alt','glyphicon  glyphicon-earphone','glyphicon  glyphicon-edit','glyphicon  glyphicon-eject','glyphicon  glyphicon-envelope','glyphicon  glyphicon-euro','glyphicon  glyphicon-exclamation-sign','glyphicon  glyphicon-expand','glyphicon  glyphicon-export','glyphicon  glyphicon-eye-close','glyphicon  glyphicon-eye-open','glyphicon  glyphicon-facetime-video','glyphicon  glyphicon-fast-backward','glyphicon  glyphicon-fast-forward','glyphicon  glyphicon-file','glyphicon  glyphicon-film','glyphicon  glyphicon-filter','glyphicon  glyphicon-fire','glyphicon  glyphicon-flag','glyphicon  glyphicon-flash','glyphicon  glyphicon-floppy-disk','glyphicon  glyphicon-floppy-open','glyphicon  glyphicon-floppy-remove','glyphicon  glyphicon-floppy-save','glyphicon  glyphicon-floppy-saved','glyphicon  glyphicon-folder-close','glyphicon  glyphicon-folder-open','glyphicon  glyphicon-font','glyphicon  glyphicon-forward','glyphicon  glyphicon-fullscreen','glyphicon  glyphicon-gbp','glyphicon  glyphicon-gift','glyphicon  glyphicon-glass','glyphicon  glyphicon-globe','glyphicon  glyphicon-hand-down','glyphicon  glyphicon-hand-left','glyphicon  glyphicon-hand-right','glyphicon  glyphicon-hand-up','glyphicon  glyphicon-hd-video','glyphicon  glyphicon-hdd','glyphicon  glyphicon-header','glyphicon  glyphicon-headphones','glyphicon  glyphicon-heart','glyphicon  glyphicon-heart-empty','glyphicon  glyphicon-home','glyphicon  glyphicon-import','glyphicon  glyphicon-inbox','glyphicon  glyphicon-indent-left','glyphicon  glyphicon-indent-right','glyphicon  glyphicon-info-sign','glyphicon  glyphicon-italic','glyphicon  glyphicon-leaf','glyphicon  glyphicon-link','glyphicon  glyphicon-list','glyphicon  glyphicon-list-alt','glyphicon  glyphicon-lock','glyphicon  glyphicon-log-in','glyphicon  glyphicon-log-out','glyphicon  glyphicon-magnet','glyphicon  glyphicon-map-marker','glyphicon  glyphicon-minus','glyphicon  glyphicon-minus-sign','glyphicon  glyphicon-move','glyphicon  glyphicon-music','glyphicon  glyphicon-new-window','glyphicon  glyphicon-off','glyphicon  glyphicon-ok','glyphicon  glyphicon-ok-circle','glyphicon  glyphicon-ok-sign','glyphicon  glyphicon-open','glyphicon  glyphicon-paperclip','glyphicon  glyphicon-pause','glyphicon  glyphicon-pencil','glyphicon  glyphicon-phone','glyphicon  glyphicon-phone-alt','glyphicon  glyphicon-picture','glyphicon  glyphicon-plane','glyphicon  glyphicon-play','glyphicon  glyphicon-play-circle','glyphicon  glyphicon-plus','glyphicon  glyphicon-plus-sign','glyphicon  glyphicon-print','glyphicon  glyphicon-pushpin','glyphicon  glyphicon-qrcode','glyphicon  glyphicon-question-sign','glyphicon  glyphicon-random','glyphicon  glyphicon-record','glyphicon  glyphicon-refresh','glyphicon  glyphicon-registration-mark','glyphicon  glyphicon-remove','glyphicon  glyphicon-remove-circle','glyphicon  glyphicon-remove-sign','glyphicon  glyphicon-repeat','glyphicon  glyphicon-resize-full','glyphicon  glyphicon-resize-horizontal','glyphicon  glyphicon-resize-small','glyphicon  glyphicon-resize-vertical','glyphicon  glyphicon-retweet','glyphicon  glyphicon-road','glyphicon  glyphicon-save','glyphicon  glyphicon-saved','glyphicon  glyphicon-screenshot','glyphicon  glyphicon-sd-video','glyphicon  glyphicon-search','glyphicon  glyphicon-send','glyphicon  glyphicon-share','glyphicon  glyphicon-share-alt','glyphicon  glyphicon-shopping-cart','glyphicon  glyphicon-signal','glyphicon  glyphicon-sort','glyphicon  glyphicon-sort-by-alphabet','glyphicon  glyphicon-sort-by-alphabet-alt','glyphicon  glyphicon-sort-by-attributes','glyphicon  glyphicon-sort-by-attributes-alt','glyphicon  glyphicon-sort-by-order','glyphicon  glyphicon-sort-by-order-alt','glyphicon  glyphicon-sound-5-1','glyphicon  glyphicon-sound-6-1','glyphicon  glyphicon-sound-7-1','glyphicon  glyphicon-sound-dolby','glyphicon  glyphicon-sound-stereo','glyphicon  glyphicon-star','glyphicon  glyphicon-star-empty','glyphicon  glyphicon-stats','glyphicon  glyphicon-step-backward','glyphicon  glyphicon-step-forward','glyphicon  glyphicon-stop','glyphicon  glyphicon-subtitles','glyphicon  glyphicon-tag','glyphicon  glyphicon-tags','glyphicon  glyphicon-tasks','glyphicon  glyphicon-text-height','glyphicon  glyphicon-text-width','glyphicon  glyphicon-th','glyphicon  glyphicon-th-large','glyphicon  glyphicon-th-list','glyphicon  glyphicon-thumbs-down','glyphicon  glyphicon-thumbs-up','glyphicon  glyphicon-time','glyphicon  glyphicon-tint','glyphicon  glyphicon-tower','glyphicon  glyphicon-transfer','glyphicon  glyphicon-trash','glyphicon  glyphicon-tree-conifer','glyphicon  glyphicon-tree-deciduous','glyphicon  glyphicon-unchecked','glyphicon  glyphicon-upload','glyphicon  glyphicon-usd','glyphicon  glyphicon-user','glyphicon  glyphicon-volume-down','glyphicon  glyphicon-volume-off','glyphicon  glyphicon-volume-up','glyphicon  glyphicon-warning-sign','glyphicon  glyphicon-wrench','glyphicon  glyphicon-zoom-in','glyphicon  glyphicon-zoom-out'];
			$.get(contextPath + '/pages/auth/imgsDialog.html').done(function(result){
				var imgsDialog = $(result);
				var contents = new Array();
                $.each(icons, function(){
                    contents.push('<span class="menu-icon '+this+'" src="'+this+'"></span>')
                });
				imgsDialog.find('#images').html(contents.join(''))
				.find('span')
				.on('click', function(){
					 menuImg.removeClass().addClass('menu-icon')
                         .addClass($(this).attr('src')).attr('src',$(this).attr('src'));
					 imgsDialog.modal('hide');
				});
				imgsDialog.modal({
					keyboard: false,
                    backdrop: false
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
		menuImg.removeClass().addClass('menu-icon').addClass(item.icon).attr('src', item.icon);
		menuType.setValue(item.menuType);
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
        if(dataGrid.up(indexs[0])){
		    changePosition(dataGrid.getAllItems());
        }
	};

	/**
	 * 下移
	 */
	var moveDown = function(grid){
		var dataGrid = grid.getGrid();
		var indexs = dataGrid.selectedRowsNo();
		if(indexs.length == 0){
	        $('body').message({
	            type: 'warning',
	             content: '请选择要操作的记录'
	        });
	        return;
	    }
		if(indexs.length > 1){
            $('body').message({
	            type: 'warning',
	             content: '只能选择一条记录进行操作'
	        })
	        return;
	    }
		var dataGrid = grid.getGrid();
		if(dataGrid.down(indexs[0])){
		    changePosition(dataGrid.getAllItems());
        }
	};
	var changePosition = function(items){
		var data = {};
		for(var i=0,j=items.length; i<j; i++){
			var item = items[i];
			data['resourceVOs['+i+'].id'] = item.id;
			data['resourceVOs['+i+'].sortOrder'] = i+1;
		}
		$.post(contextPath + '/auth/Menu/updateMenuOrder.koala', data).done(function(result){
			$('body').message({
                type: 'success',
                content: '移动成功'
            })
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