/**
 * Tree
 */
+function ($) {

	"use strict";

	// TREE CONSTRUCTOR AND PROTOTYPE

	var Tree = function (element, options) {
		this.$element = $(element);
		this.options = $.extend({}, $.fn.tree.defaults, options);
		//渲染，改成build建造方法
		this.build(options);
    };

	Tree.prototype = {
		constructor: Tree,

		//建造方法，包括对参数ajaxUrl的判断，如果包含有该参数，则ajax方式获取，而不是纯this.options.dataSource.data
		build: function(options){
			var self = this;
			if(options.ajaxUrl==""||options.ajaxUrl==null){
				self.render();
			}else{
				$.ajax({
					type:"POST",
					url:options.ajaxUrl,
					dataType:"text",
					data:options.reqParam,
					success:function(data){
						options.dataSource.data = eval(data);
						self.render();
					},error:function(){
						
					}
				});
			}
		},
		
		//渲染方法
		render: function () {
			this.populate(this.options.dataSource.data, this.$element);
            //叶子节点点击的时候
            this.$element.on('click', '.tree-item', $.proxy( function(ev) { this.selectChildren(ev.currentTarget); } ,this));
            //父节点点击的时候
            this.$element.on('click', '.tree-folder-header', $.proxy( function(ev) { this.selectParent(ev.currentTarget); }, this));
            //复选框点击的时候
            this.$element.on('click',':checkbox',$.proxy(function(ev){this.checkParent(ev.currentTarget);},this));
            //右键点击树节点的时候，目前先暂定任意节点
            this.$element.on('mousedown','.tree-folder-name,.tree-item-name',$.proxy(function(ev){this.popMenu(ev.currentTarget,ev);},this));
            //添加鼠标右键事件监听
            this.$element.on('contextmenu','.tree-folder-header,.tree-item-name',$.proxy(function(ev){this.initContextmenu(ev);},this));

            this.$element.find('.tree-folder').show();
			this.$element.find('.tree-item').show();
		},
		populate: function (data, $el) {
			var self = this;
			$.each(data, function(index, value) {
				var $entity ;
				//如果还有子节点
				if(!self.isEmpty(value.children) || value.type == 'parent'){
					$entity = self.createParent();
					$entity.find('.tree-folder-name').html(value.menu.title);
					$entity.find('.tree-folder-header').data(value.menu);
					$entity.attr("id",value.menu.id);
					self.populate(value.children, $entity.find(".tree-folder-content:eq(0)"));
					$entity.find(".tree-folder-content:eq(0)").hide();
					//这里加上检测是否有自定义图标
					if(value.menu.icon){
						$entity.children('.tree-folder-header').children("i").addClass(value.menu.icon)
						.prop("icon",value.menu.icon);
					}
				}else{
					$entity = self.createChildren();
					$entity.find('.tree-item-name').html(value.menu.title);
					$entity.attr("href",value.menu.href);
					$entity.attr("id",value.menu.id);
					$entity.data(value.menu);
					//这里加上检测是否有自定义图标，对于子节点，还有个bug
					if(value.menu.icon){
						$entity.children("i").addClass(value.menu.icon).prop("icon",value.menu.icon);
					}
				}
				$el.append($entity);
			});
		},
		isEmpty: function(obj){
			if(!obj){
				return true; 
			}
			for (var i in obj ) { 
				if(obj.hasOwnProperty(i)){
			          return false;
			     }
			} 
			return true; 
		},
		createParent: function(){
			var $node;
			//如果采用复选框
			if(this.options.useChkBox){
				$node = $('<div class="tree-folder checkbox" style="display:none;">' + 
						'<input type="checkbox">'+
						'<div class="tree-folder-header">' +
						'<i class="glyphicon glyphicon-folder-close"></i>'+
						'<div class="tree-folder-name"></div></div><div class="tree-folder-content"></div>'+
						'<div class="tree-loader" style="display:none"></div></div>');
			}else{
				$node = $('<div class="tree-folder" style="display:none;">'+
						'<div class="tree-folder-header"><i class="glyphicon glyphicon-folder-close"></i>'+
						'<div class="tree-folder-name"></div></div><div class="tree-folder-content"></div>'+
						'<div class="tree-loader" style="display:none"></div></div>');
			}
			
			return $node;
		},
		createChildren:function(){
			var $node;
			//如果采用复选框
			if(this.options.useChkBox){
				$node = $('<div class="tree-item checkbox" style="display:none;">'+
					'<input type="checkbox">' + 
					'<div class="tree-item-name"></div></div>');
			}else{
				$node = $('<div class="tree-item" style="display:none;">'+
					'<i class="glyphicon glyphicon-list-alt"></i><div class="tree-item-name"></div></div>');
			}
			
			return $node;
		},
		selectChildren: function (el) {
			var $el = $(el);
            var $all = this.$element.find('.tree-selected');
			var data = [];
			if (this.options.multiSelect) {
				$.each($all, function(index, value) {
					var $val = $(value);
					if($val[0] !== $el[0]) {
						data.push( $(value).data() );
					}
				});
			} else if ($all[0] !== $el[0]) {
				$all.removeClass('tree-selected')
					.find('i').removeClass('glyphicon-ok').addClass('glyphicon-list-alt');
				data.push($el.data());
			}

			if($el.hasClass('tree-selected')) {
				$el.removeClass('tree-selected');
				$el.find('i').removeClass('glyphicon-ok').addClass('glyphicon-list-alt');
				//这里检测是否有icon属性
				if($el.find("i").prop("icon")){
					$el.find("i").addClass($el.find("i").prop("icon"));
				}
			} else {
				$el.addClass ('tree-selected');
				$el.find('i').removeClass('glyphicon-list-alt').addClass('glyphicon-ok');
				if (this.options.multiSelect) {
					data.push( $el.data() );
				}
			}
			//console.info("icon = " + $el.find("i").prop("icon"));

			if(data.length) {
				this.$element.trigger('selected', {info: data});
			}

		},

		selectParent: function (el) {
			var $el = $(el);
			var $par = $el.parent();

			if($el.find('.glyphicon-folder-close').length) {
				if ($par.find('.tree-folder-content').children().length) {
					$par.find('.tree-folder-content:eq(0)').show();
				} 

				$par.find('.glyphicon-folder-close:eq(0)')
					.removeClass('glyphicon-folder-close')
					.addClass('glyphicon-folder-open');

				this.$element.trigger('opened', {element:$el, data: $el.data()});
			} else {
				if(this.options.cacheItems) {
					$par.find('.tree-folder-content:eq(0)').hide();
				} else {
					$par.find('.tree-folder-content:eq(0)').empty();
				}

				$par.find('.glyphicon-folder-open:eq(0)')
					.removeClass('glyphicon-folder-open')
					.addClass('glyphicon-folder-close');
                this.$element.trigger('closed', {element:$el, data: $el.data()});
			}
		},

		selectedItems: function () {
			var $sel = this.$element.find('.tree-selected');
			var data = [];

			$.each($sel, function (index, value) {
				data.push($(value).data());
			});
			return data;
		}
		
		//新加函数，勾选父节点的复选框时，下面所有的子节点的复选框都要同样的勾选状态
		,checkParent:function(el,ev){
			var $el = $(el);
			var isChecked = $el.prop("checked");
			$el.parent().find(":checkbox").prop("checked",isChecked);
		}
		
		//新加函数，弹出菜单
		,popMenu:function(el,ev){
			var mouseMenu = this.options.mouseMenu;
			var $el = $(el);
			if(mouseMenu==null){
				return false;
			}
			if(ev.which==3){
				mouseMenu.hide();
				mouseMenu.css({
					"position":"absolute",
					"top":ev.clientY-70,			//这里需要再调试
					"left":ev.clientX-190
				});
				mouseMenu.show("fast");
				//加上一个标记
				var $p = null;						//临时节点，存储parent
				if($el.hasClass("tree-folder-name")){
					$p = $el.parent().parent();
				}else if($el.hasClass("tree-item-name")){
					$p = $el.parent();
				}
				$el.prop("node",$p.attr("id"));
				console.info($el + ".node = " + $el.prop("node"));
			}
		},

        //鼠标右键事件监听
        initContextmenu: function(ev){
            this.$element.trigger('rightClick', ev);
        },
        //根据节点创建右键菜单
        createRightMenu: function(data){
            var self = this;
            $('.tree-right-menu').remove();
            var $element = $(data.element);
            var ev = data.event;
            var menuData = data.data;
            var menuHtml = new Array();
            menuHtml.push('<ul class="dropdown-menu tree-right-menu">')
            $.each(menuData, function(){
                menuHtml.push('<li action="'+this.action+'"><a>'+this.title+'</a></li>');
            });
            menuHtml.push('</ul>');
            $(menuHtml.join('')).appendTo($('body')).show()
                .css({position:'fixed', left: ev.pageX, top: ev.pageY})
                .on('mouseleave', function(){
                    $(this).remove();
                }).find('li').on('click', function(){
                    var $this = $(this);
                    if($element.hasClass('tree-item-name')){
                        self.$element.trigger($this.attr('action'), $element.parent());
                    }else{
                        self.$element.trigger($this.attr('action'), $element);
                    }
                    $this.parent().remove();
            });
        },
        //添加一个节点
        addChildren: function(data){
            var self = this;
            var node = $(data.node);
            var $entity = null;
            var menu = data.menu;
            if(data.type && data.type == 'parent'){
                $entity = self.createParent();
                $entity.find('.tree-folder-name').html(menu.title);
                $entity.find('.tree-folder-header').data(menu);
            }else{
                $entity = self.createChildren();
                $entity.find('.tree-item-name').html(menu.title);
                $entity.data(menu);
            }
            $entity.show().on('contextmenu', function(ev){
                self.initContextmenu(ev);
            });
            node.parent().find('>.tree-folder-content').append($entity).show();
            if(node.find('.glyphicon-folder-close').length > 0){
                node.trigger('click');
            }
        },
        //删除一个节点
        removeChildren: function(data){
            $(data).remove();
        }
		//提供一个显示文本框用于重新设置标题的接口
		
	};


	// TREE PLUGIN DEFINITION

	$.fn.tree = function (option, value) {
		var methodReturn;

		var $set = this.each(function () {
			var $this = $(this);
			var data = $this.data('tree');
			var options = typeof option === 'object' && option;

			if (!data) $this.data('tree', (data = new Tree(this, options)));
			if (typeof option === 'string') methodReturn = data[option](value);
		});
		
		//如果有右键菜单，就把原来的右键除掉
		if(option.mouseMenu!=null){
			$(document).bind("contextmenu",function(){return false;});
		}
		
		//添加判断是否可拖放
		if(option.draggable){
//			var $node = $(this).find(".tree-folder,.tree-item");
			var $node = $(".tree");
			$node.sortable({
				items:".tree-folder,.tree-item"
			});
			$node.disableSelection();
		}
		
		return (methodReturn === undefined) ? $set : methodReturn;
		
	};

	//默认内置属性列表
	$.fn.tree.defaults = {
		multiSelect: false,
		loadingHTML: '<div>Loading...</div>',
		cacheItems: true
		//增加参数useChkBox，表示是否增加对应div旁边的复选框，
		,useChkBox:false			//是否带复选框
		,draggable:false			//节点可否拖放
		,editable:false				//节点可否编辑
		,mouseMenu:null				//弹出菜单，为下拉框的jQuery，
		,ajaxUrl:""					//异步加载的url，一旦设置这个，取代原生的json data
		,reqParam:null				//与ajaxUrl对应的请求参数，为一个{}对象
	};

	$.fn.tree.Constructor = Tree;
	
}(window.jQuery);
