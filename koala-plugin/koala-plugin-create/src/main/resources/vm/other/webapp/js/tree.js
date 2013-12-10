+function(require) {

	"use strict";

	// TREE CONSTRUCTOR AND PROTOTYPE

	var Tree = function (element, options) {
		this.$element = $(element);
		this.options = $.extend({}, $.fn.tree.defaults, options);
		this.render();
	};

	Tree.prototype = {
		constructor: Tree,

		render: function () {
			var self = this;
			$.get(this.options.url).done(function(data){
				self.populate(data, self.$element);
				self.$element.find('.tree-folder').show();
				self.$element.find('.tree-item').show();
				$(this).off('click');
				self.$element.find('.tree-item').on('click', function(ev){
					self.selectChildren(ev.currentTarget);
				})
				self.$element.find('.tree-folder-header').on('click', function(ev){
					self.selectParent(ev.currentTarget)
				})
				self.$element.find('.tree-folder-header:first').click();
				if(self.options.isShowAddIcon){
					self.$element.find('.add-parent-icon').on('click', function(e){
						e.stopPropagation();
						var parent = $(this).closest('.tree-folder');
						self.$element.trigger('addIconClick', {id: parent.attr('id'), name:parent.attr('name')})
					})
					self.$element.find('.add-icon').on('click', function(e){
						var parent = $(this).parent();
						self.$element.trigger('addIconClick', {id: parent.attr('id'), name:parent.attr('name')})
					})
				} else {
					self.$element.find('.add-icon,.add-parent-icon').hide();
				}
				if(self.options.isShowRightMenu && self.options.rightMenus && self.options.rightMenus.length > 0){
					  self.initRightMenu();
				}
			})
		},
		initRightMenu: function(){
			var self = this;
			var rightMenus = self.options.rightMenus;
			self.$element.find('.tree-folder-header,.tree-item').on('contextmenu', function(e){
				self.$element.find('[data-id="rightMenu"]').remove();
				var rightMenuContainer = $('<ul class="dropdown-menu" data-id="rightMenu"></ul>');
				var $this = $(this).click();
			    var data = null;
				if($this.hasClass('tree-item')){
					data = $this.data();
				}else{
					data = $this.parent().data();
				}
				var id = $(this).attr('id') || $(this).parent().attr('id');
				for(var i= 0, j=rightMenus.length; i<j; i++){
					var rightMenu = rightMenus[i];
					if(data.organizationType != 'company' && rightMenu.action == 'addCompany'){
						continue;
					}
					var $li = $('<li><a>'+rightMenu.title+'</a></li>').off('click');
					$li.appendTo(rightMenuContainer)
						.on('click', {action:rightMenu.action}, function(event){
							 self.$element.trigger(event.data.action, data);
							 $(this).parent().remove();
						})
				}
				rightMenuContainer.show().css({left: e.pageX, top: e.pageY})
					.on('mouseleave', function(){
						$(this).remove();
					})
					.appendTo($('body'));
			})
		},
		populate: function (data, $el) {
			var self = this;
			$.each(data, function(index, value) {
				var $entity ;
				if(!self.isEmpty(value.children)){
					$entity = self.createParent();
					$entity.data(value);
					$entity.find('.tree-folder-name').html(value.name+'&nbsp;<i class="glyphicon glyphicon-plus add-parent-icon" ></i>');
					$entity.attr("id",value.id);
					$entity.attr("name",value.name);
					self.populate(value.children, $entity.find(".tree-folder-content:eq(0)"));
					$entity.find(".tree-folder-content:eq(0)").hide();
				}else{
					$entity = self.createChildren();
					$entity.data(value);
					$entity.find('.tree-item-name').html(value.name);
					$entity.attr("id",value.id);
					$entity.attr("name",value.name);
					$entity.data(value.menu);
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
			return $('<div class="tree-folder" style="display:none;">'+
				'<div class="tree-folder-header"><i class="glyphicon glyphicon-folder-close"></i>'+
				'<div class="tree-folder-name"></div></div>' +
				'<div class="tree-folder-content"></div>'+
			'<div class="tree-loader" style="display:none"></div></div>');
		},
		createChildren:function(){
			return $('<div class="tree-item" style="display:none;">'+
			'<i class="tree-dot glyphicon"></i><span class="tree-item-name">' +
				'</span><i class="glyphicon glyphicon-plus add-icon" ></i></div>');
		},
		selectChildren: function (el) {
			var $el = $(el);
			this.$element.trigger('selected', $el.data());
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
				data.push($el.data());
			}

			if($el.hasClass('tree-selected')) {
				$el.removeClass('tree-selected');
			} else {
				$el.addClass ('tree-selected');
				if (this.options.multiSelect) {
					data.push( $el.data() );
				}
			}
		},

		selectParent: function (el) {
			var $el = $(el);
			var $par = $el.parent();
			this.$element.trigger('selected', $par.data());
			this.$element.find('.tree-selected').removeClass('tree-selected');
			$el.addClass ('tree-selected');
			if($el.find('.glyphicon-folder-close').length) {
				if ($par.find('.tree-folder-content').children().length) {
					$par.find('.tree-folder-content:eq(0)').show();
				} 

				$par.find('.glyphicon-folder-close:eq(0)')
					.removeClass('glyphicon-folder-close')
					.addClass('glyphicon-folder-open');

				this.$element.trigger('opened', $el.data());
			} else {
				if(this.options.cacheItems) {
					$par.find('.tree-folder-content:eq(0)').hide();
				} else {
					$par.find('.tree-folder-content:eq(0)').empty();
				}

				$par.find('.glyphicon-folder-open:eq(0)')
					.removeClass('glyphicon-folder-open')
					.addClass('glyphicon-folder-close');

				this.$element.trigger('closed', $el.data());
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
	};


	// TREE PLUGIN DEFINITION

	$.fn.tree = function (option) {
		return this.each(function () {
			var $this = $(this);
			var data = $this.data('tree');
			var options = typeof option === 'object' && option;
			if(!data){
				$this.data('tree', (data = new Tree(this, options)));
			}else{
				$this.html('');
				data.render();
			}
		});
	};

	$.fn.tree.defaults = {
		multiSelect: false,
		isShowAddIcon: false,
		loadingHTML: '<div>Loading...</div>',
		cacheItems: true
	};

	$.fn.tree.Constructor = Tree;

}(window.jquery)