$(function(){
	/*
	 重置菜单栏容器默认高度
	 */
	var self = $(this);
	var $window = $(window);
	$window.on('resize', function(){
		var windowWidth = $window.width();
		var sidebar = $('.g-sidec');
		if(windowWidth < 768){
			$('#sidebar-collapse').hide();
			sidebar.css('height', 'auto');
			return;
		}
	});
	/**
	 * 根据内容改变高度
	 */
	var changeHeight = function(){
		var sidebar = $('.g-sidec');
		var sidebarHeight = sidebar.outerHeight();
		var headerHeight = $('.g-head').outerHeight();
		var windowHeight = $window.height();
		var bodyHeight = $(document).height();
		if(bodyHeight >  windowHeight){
			windowHeight =  bodyHeight;
		}
		var footHeight = $('#footer').outerHeight();
		var height =  windowHeight - headerHeight - footHeight;
		sidebarHeight < height && sidebar.css('height', height);
	};
	/*
	 加载DIV内容
	 */
	var loadContent = function(obj, target){
		$.get(target).done(function(data){
				obj.html(data);
			}).fail(function(){
				throw new Error('加载失败');
			}).always(function(){
				changeHeight();
			});
	};
	loadContent($('#home'), 'pages/welcome.html');

	var firstLevelMenu = $('.first-level-menu');
	/*
	* 菜单收缩样式变化
	 */
	firstLevelMenu.find('[data-toggle="collapse"]').on('click', function(){
		var $this = $(this);
		if($this.hasClass('collapsed')){
			$this.find('i:last').removeClass('glyphicon-chevron-right').addClass('glyphicon-chevron-left');
		}else{
			$this.find('i:last').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-right');
		}
	})
	$.get('/auth/Menu/findTopMenuByUser.koala').done(function(data){
        if(data.data.length == 0){
            var dialog = $('<div class="modal fade"><div class="modal-dialog" style="padding-top:10%;width:400px;"><div class="modal-content">' +
                ' <div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">' +
                '&times;</button><h4 class="modal-title">菜单初始化</h4></div><div class="modal-body">' +
                '<p>是否进行菜单初始化？</p></div><div class="modal-footer"><button type="button" class="btn btn-default"' +
                ' data-dismiss="modal">取消</button><button id="confirm" type="button" class="btn btn-primary">确定</button></div>' +
                '</div></div></div>');
            dialog.modal({
                keyboard: true
            }).on('hidden.bs.modal', function(){
                 $(this).remove();
            }).find('#confirm').on('click', function(){
                    $.ajax({
                        type: 'GET',
                        url:'dbinit',
                        dataType: 'text'
                    }).done(function(data){
                        if(data == 'success'){
                            dialog.modal('hide');
                            $('body').message({
                                type: 'success',
                                content: '权限初始化成功'
                            });
                            setTimeout(function(){
                                 window.location.href = 'index.koala';
                            },2000);
                        }
                    });
            });
        }
		$.each(data.data, function(){
			var $li = $('<li><a data-toggle="collapse" href="#menuMark'+this.id+'"><img class="menu-icon" src="'+this.icon+'"></img>&nbsp;'+this.name+'&nbsp;'+
				'<i class="glyphicon glyphicon-chevron-left"></i></a><ul id="menuMark'+this.id+'" class="second-level-menu"></ul></li>');
			firstLevelMenu.append($li);
			renderSubMenu(this.id, $li);
		});
	});
	var renderSubMenu = function(id, $menu){
		$.get('/auth/Menu/findAllSubMenuByParent.koala?resVO.id='+id).done(function(data){
				var subMenus = new Array();
				$.each(data.data, function(){
					if(this.menuType == "2"){
                        var $li = $('<li><a data-toggle="collapse" href="#menuMark'+this.id+'"><img class="menu-icon" src="'+this.icon+'"></img>&nbsp;'+this.name+'&nbsp;'+
                            '<i class="glyphicon glyphicon-chevron-right pull-right" style="position: relative; right: 12px;font-size: 12px;"></i></a><ul id="menuMark'+this.id+'" class="second-level-menu collapse"></ul></li>');
                        $li.appendTo($menu.find('.second-level-menu:first')).find('a').css('padding-left', parseInt(this.level)*18+'px');
                        renderSubMenu(this.id, $li);
                    }else{
                        var $li = $(' <li class="submenu" data-role="openTab" data-target="'+this.identifier+'" data-title="'+this.name+'" ' +
                            'data-mark="menuMark'+this.id+'"><a ><img class="menu-icon" src="'+this.icon+'"></img>&nbsp;'+this.name+'</a></li>');
                        $li.appendTo($menu.find('.second-level-menu:first')).find('a').css('padding-left', parseInt(this.level)*18+'px');
                    }
				});
				$menu.find('li.submenu').on('click', function(){
						var $this = $(this);
						clearMenuEffect();
						$this.addClass('active').parent().closest('li').addClass('active').parent().closest('li').addClass('active');
						var target = $this.data('target');
						var title = $this.data('title');
						var mark = $this.data('mark');
						if(target && title && mark ){
							$this.openTab(target, title, mark);
						}
					});
		});
	};
	/*
	 * 清除菜单效果
	 */
	var clearMenuEffect = function(){
		$('.first-level-menu').find('li').each(function(){
			var $menuLi = $(this);
			$menuLi.hasClass('active') && $menuLi.removeClass('active').parent().parent().removeClass('active');
		});
	};
	/*
	 点击主页tab事件
	 */
	$('a[href="#home"]').on('click', function(){
		clearMenuEffect();
		$('.g-sidec').find('li[data-mark="home"]').click()
	});
	/*
	 *打开一个Tab
	 */
	$.fn.openTab = function(target, title, mark, id, param){
		var mainc =   $('.g-mainc');
		var tabs = mainc.find('ul.nav');
		var contents =  mainc.find('div.tab-content');
		var content = contents.find('#'+mark);
		if(content.length > 0){
			content.attr('data-value', id);
			loadContent(content, target);
			tabs.find('a[href="#'+mark+'"]').tab('show');
			tabs.find('a[href="#'+mark+'"]').find('span').text(title);
			return;
		}
		var tab = $('<li><a href="#'+mark+'" data-toggle="tab"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button><span>'+title+'<span></a></li>');
		content = $('<div id="'+mark+'" class="tab-pane" data-value="'+id+'"></div>');
		content.data(param);
		loadContent(content, target);
		contents.append(content);
		var closeBtn = tab.appendTo(tabs).on('click',function(){
			var $this = $(this);
			if($this.hasClass('active')){
				return;
			}
			var $li = $('.g-sidec').find('li[data-mark="'+mark+'"]');
			$li.click();
			if($li.parent().hasClass('collapse')){
				$li.parent().prev('a').click();
			}
		}).find('a:first')
			.tab('show')
			.find('.close');
		closeBtn.css({position: 'absolute', right: (closeBtn.width()-10) + 'px', top: -1 + 'px'})
			.on('click',function(){
				var prev =  tab.prev('li').find('a:first');
				content.remove() && tab.remove();
				var herf = prev.tab('show').attr('href').replace("#", '');
				var $menuLi =  $('.g-sidec').find('li[data-mark="'+herf+'"]');
				if($menuLi.length){
					$menuLi.click();
				}else{
					clearMenuEffect();
				}
			});
	};
	/*
	 修改密码
	 */
	self.on('modifyPwd',function(){
		$('body').modifyPassword({
			service: 'auth/User/updatePassword.koala'
		});
	});
	/*
	 切换用户
	 */
	self.on('switchUser',function(){
		window.location.href = "j_spring_security_logout";
	});
	/*
	 注销
	 */
	self.on('loginOut',function(){
		window.location.href = "j_spring_security_logout";
	});
	$('#userManager').find('li').on('click', function(){
		self.trigger($(this).data('target'));
	});
});
