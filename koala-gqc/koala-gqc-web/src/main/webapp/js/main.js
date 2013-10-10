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
	})
	/**
	 * 根据内容改变高度
	 */
	var changeHeight = function(){
		var windowWidth = $window.width();
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
	}
	/*
	 加载DIV内容
	 */
	var loadContent = function(obj, target){
		$.ajax({
			url: target,
			dataType: 'html'
		}).done(function(data){
				obj.html(data);
			}).fail(function(){
				throw new Error('加载失败')
			}).always(function(){
				changeHeight();
			})
	}
	loadContent($('#home'), 'pages/welcome.html');
	/*
	 左边菜单栏收缩
	 */
	$('#sidebar-collapse').on('click', function(){
		var $this = $(this);
		var $menu = $this.parent();
		if($menu.hasClass('menu-mini')){
			$this.find('span:first')
				.removeClass('glyphicon-forward')
				.addClass('glyphicon-backward');
			$menu.animate({'width':'16.666666666666664%'}, 400, 'swing', function(){
				$(this).removeClass('menu-mini');
			});
		}else{
			$this.find('span:first')
				.removeClass('glyphicon-backward')
				.addClass('glyphicon-forward');
			$menu.addClass('menu-mini')
				.animate({'width':'4%'}, 400, 'swing', function(){

				});
		}
	});
	/*
	 *菜单点击事件
	 */
	var $submenu = $('#generalQueryConfig').find('li');
	$submenu.on('click', function(){
		var $this = $(this);
		if($this.hasClass('active')) {
			return;
		}
		clearMenuEffect();
		$this.addClass('active').parent().parent().addClass('active');
		var target = $this.data('target');
		var title = $this.data('title');
		var mark = $this.data('mark');
		if(target && title && mark ){
			var $a = $('a[href="#'+mark+'"]');
			if($a.length){
				$a.tab('show')
			}else{
				$this.openTab(target, title, mark);
			}
		}
	})
	/*
	 * 清除菜单效果
	 */
	var clearMenuEffect = function(){
		$submenu.each(function(){
			var $menuLi = $(this);
			$menuLi.hasClass('active') && $menuLi.removeClass('active').parent().parent().removeClass('active');
		})
	}
	/*
	 点击主页tab事件
	 */
	$('a[href="#home"]').on('click', function(){
		clearMenuEffect();
	})
	/*
	 *打开一个Tab
	 */
	$.fn.openTab = function(target, title, mark){
		var mainc =   $('.g-mainc');
		var tabs = mainc.find('ul.nav');
		var contents =  mainc.find('div.tab-content');
		var tab = $('<li><a href="#'+mark+'" data-toggle="tab"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'+title+'</a></li>');
		var content = $('<div id="'+mark+'" class="tab-pane"></div>');
		loadContent(content, target);
		contents.append(content);
		var closeBtn = tab.appendTo(tabs).on('click',function(){
			var $this = $(this);
			if($this.hasClass('active')){
				return;
			}
			$('#generalQueryConfig').find('li[data-mark="'+mark+'"]').click();
		}).find('a:first')
			.tab('show')
			.find('.close')
		closeBtn.css({position: 'absolute', right: (closeBtn.width()-10) + 'px', top: -1 + 'px'})
			.on('click',function(){
				var prev =  tab.prev('li').find('a:first');
				content.remove() && tab.remove();
				var herf = prev.tab('show').attr('href').replace("#", '');
				var $menuLi =  $('#generalQueryConfig').find('li[data-mark="'+herf+'"]');
				if($menuLi.length){
					$menuLi.click();
				}else{
					clearMenuEffect();
				}
			});
	}
	/*
	 修改密码
	 */
	self.on('modifyPwd',function(){
		$('body').modifyPassword({
			service: 'auth-User-updatePassword.action'
		});
	});
	/*
	 切换用户
	 */
	self.on('switchUser',function(){
		window.location.href = "login.jsp";
	});
	/*
	 注销
	 */
	self.on('loginOut',function(){
		window.location.href = "login.jsp";
	});
	$('#userManager').find('li').on('click', function(){
		self.trigger($(this).data('target'));
	})
})
