/**
 * Tree
 */
+function ($) {

    "use strict";

    // TREE CONSTRUCTOR AND PROTOTYPE

    var Tree = function (element, options) {
        this.$element = $(element);
        this.childrenElements = [];//子元素节点
        this.childrenItems = [];//子元素节点数据
        this.selectedDatas = {}; //已选择的数据
        this.options = $.extend({}, $.fn.tree.defaults, options);
        this.init();
    };

    Tree.prototype = {
        constructor: Tree,
        init: function(){
            var self = this;
            //渲染，改成build建造方法
            this.build(this.options);
            $.each(self.$element.find('.tree-folder'), function(){
                var $folder = $(this);
                $folder.on({
                    'selectCheckBox': function(e){
                        e.preventDefault();
                        e.stopPropagation();
                        var $this = $(this);
                        $this.find('>input').prop('checked', true);
                        var data = $this.find('.tree-folder-header').data();
                        self.selectedDatas[data.id] = data;
                        var treeFolderContent = $this.parent();
                        var treeFolder = treeFolderContent.parent();
                        if(!treeFolder.hasClass('tree-folder')){
                            return;
                        }
                        var treeFolderCheckBox = treeFolder.find('>input');
                        if(!treeFolderCheckBox.is(':checked')){
                            treeFolder.trigger('selectCheckBox');
                        }
                    },
                    'disSelectCheckBox': function(e){
                        e.preventDefault();
                        e.stopPropagation();
                        var $this = $(this);
                        $this.find('>input').prop('checked', false);
                        var data = $this.find('.tree-folder-header').data();
                        delete self.selectedDatas[data.id];
                        var treeFolderContent = $this.parent();
                        var treeFolder = treeFolderContent.parent();
                        if(!treeFolder.hasClass('tree-folder')){
                            return;
                        }
                        var treeFolderCheckBox = treeFolder.find('>input');
                        if(treeFolderCheckBox.is(':checked')){
                            if(treeFolderContent.find('>.tree-selected').length == 0 &&
                                treeFolderContent.find('>.tree-folder>input:checked').length == 0){
                                treeFolder.trigger('disSelectCheckBox');
                            }
                        }
                    }
                });
                $folder.find('>input').on('click', function(){
                    if(this.checked){
                        $folder.find('.tree-item').each(function(){
                            var $this = $(this);
                            if(!$this.hasClass('tree-selected')){
                                $this.click();
                            }
                        });
                        $folder.find('.tree-folder').trigger('selectCheckBox');
                    }else{
                        $folder.find('.tree-selected').click();
                        $folder.find('.tree-folder').trigger('disSelectCheckBox');
                    }
                });
            });
            //叶子节点点击的时候
            this.$element.on('click', '.tree-item', $.proxy( function(ev) { this.selectChildren(ev.currentTarget); } ,this));
            //父节点点击的时候
            this.$element.on('click', '.tree-folder-header', $.proxy( function(ev) { this.selectParent(ev.currentTarget); }, this));
            //复选框点击的时候
            //this.$element.on('click',':checkbox',$.proxy(function(ev){this.checkParent(ev.currentTarget);},this));
            //右键点击树节点的时候，目前先暂定任意节点
            this.$element.on('mousedown','.tree-folder-name,.tree-item-name',$.proxy(function(ev){this.popMenu(ev.currentTarget,ev);},this));
            //添加鼠标右键事件监听
            this.$element.on('contextmenu','.tree-folder-header,.tree-item',$.proxy(function(ev){this.initContextmenu(ev);},this));
            if(this.options.useChkBox){
                this.initCheckBox();
            }
        },
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
            var self = this;
            this.populate(self.options.dataSource.data, self.$element);
            this.$element.find('.tree-folder').show();
            this.$element.find('.tree-item').show();
        },
        populate: function (data, $el) {
            var self = this;
            $.each(data, function(index, value) {
                var $entity ;
                //如果还有子节点
                if(!self.isEmpty(value.children) || value.type == 'parent'){
                    $entity = self.createParent(value.menu.id);
                    $entity.find('.tree-folder-name').html(value.menu.title);
                    $entity.find('.tree-folder-header').data(value.menu);
                    $entity.attr("id",value.menu.id);
                    if(value.children && value.children.length > 0){
                        self.populate(value.children, $entity.find(".tree-folder-content:eq(0)"));
                    }
                    if(value.menu.open){
                        $entity.find('.glyphicon-folder-close').removeClass('glyphicon-folder-close').addClass('glyphicon-folder-open');
                        $entity.find(".tree-folder-content:eq(0)").show();
                    }else{
                        $entity.find('.glyphicon-folder-open').removeClass('glyphicon-folder-open').addClass('glyphicon-folder-close');
                        $entity.find(".tree-folder-content:eq(0)").hide();
                    }
                    //这里加上检测是否有自定义图标
                    if(value.menu.icon){
                        $entity.children('.tree-folder-header').children("i").addClass(value.menu.icon)
                            .attr("icon",value.menu.icon);
                    }
                }else{
                    $entity = self.createChildren(value.menu.id);
                    $entity.find('.tree-item-name').html(value.menu.title);
                    $entity.attr("id",value.menu.id);
                    $entity.data(value.menu);
                    //这里加上检测是否有自定义图标，对于子节点，还有个bug
                    if(value.menu.icon){
                        $entity.children("i").removeClass().addClass(value.menu.icon).prop("icon",value.menu.icon);
                    }
                    self.childrenItems.push(value);
                    self.childrenElements.push($entity);
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
        createParent: function(value){
            var $node;
            //如果采用复选框
            if(this.options.useChkBox){
                $node = $('<div class="tree-folder checkbox" style="display:none;">' +
                    '<input type="checkbox" value="'+value+'">'+
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
        createChildren:function(value){
            var $node;
            //如果采用复选框
            if(this.options.useChkBox){
                $node = $('<div class="tree-item checkbox" style="display:none;">'+
                    '<input type="checkbox" value="'+value+'">' +
                    '<div class="tree-item-name"></div></div>');
            }else{
                $node = $('<div class="tree-item" style="display:none;">'+
                    '<i class="glyphicon glyphicon-list-alt"></i><div class="tree-item-name"></div></div>');
            }
            return $node;
        },
        selectChildren: function (el) {
            var self = this;
            var $el = $(el);
            var data = $el.data();
            if (!this.options.multiSelect) {
                this.$element.find('.tree-selected').removeClass('tree-selected')
                    .find('i').removeClass('glyphicon-ok').addClass('glyphicon-list-alt');
            }
            if($el.hasClass('tree-selected')) {
                $el.removeClass('tree-selected').find('i').removeClass('glyphicon-ok').addClass('glyphicon-list-alt');
                //这里检测是否有icon属性
                if($el.find("i").prop("icon")){
                    $el.find("i").addClass($el.find("i").prop("icon"));
                }
                delete self.selectedDatas[data.id];
            } else {
                $el.addClass ('tree-selected').find('i').removeClass('glyphicon-list-alt').addClass('glyphicon-ok');
                self.selectedDatas[data.id] = data;
            }
            if(this.options.useChkBox){
                $el.find('input').prop('checked', $el.hasClass('tree-selected'));
                var treeFolderContent = $el.parent()
                var treeFolder = treeFolderContent.parent();
                if(!treeFolder.hasClass('tree-folder')){
                    return;
                }
                if($el.hasClass('tree-selected')){
                    treeFolder.trigger('selectCheckBox');
                }else{
                    if(treeFolderContent.find('>.tree-selected').length == 0 &&
                        treeFolderContent.find('>.tree-folder>input:checked').length == 0){
                        treeFolder.trigger('disSelectCheckBox');
                    }
                }
            }
            if(data.length) {
                this.$element.trigger('selected', {info: self.selectedItems()});
            }
            this.$element.trigger('selectChildren', $el.data());
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
            this.$element.trigger('selectParent', {element:$el, data: $el.data()});
        },

        selectedItems: function () {
            var items = [];
            for(var prop in this.selectedDatas){
                items.push(this.selectedDatas[prop])
            }
            return items;
        },
        /**
         * 初始化checkbox
         */
        initCheckBox: function(){
            var self = this;
            $.each(self.childrenItems, function(index, value){
                if(value.menu.checked){
                    self.childrenElements[index].click();
                    self.selectedDatas[value.menu.id] = value.menu;
                }
            });
        },
        //新加函数，勾选父节点的复选框时，下面所有的子节点的复选框都要同样的勾选状态
        checkParent:function(el,ev){
            var $el = $(el);
            var isChecked = $el.prop("checked");
            if(isChecked){
                $el.parent().find('.tree-item').addClass('tree-selected').find(":checkbox").prop("checked",'checked');
            }else{
                $el.parent().find('.tree-item').removeClass('tree-selected').find(":checkbox").prop("checked",'');
            }
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
                //console.info($el + ".node = " + $el.prop("node"));
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
                    self.$element.trigger($this.attr('action'), $element);
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
                $entity.on('click', {element: $(this)}, function(e){
                    self.selectParent(e.data.element);
                });
            }else{
                $entity = self.createChildren();
                $entity.find('.tree-item-name').html(menu.title);
                $entity.data(menu);
                $entity.on('click',{element: $(this)}, function(e){
                    self.selectChildren(e.data.element);
                });
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
        },
        //销毁tree
        destory: function(){
            this.$element.data('koala.tree', null);
            this.$element.empty();
        }
        //提供一个显示文本框用于重新设置标题的接口

    };

    //返回tree对象
    $.fn.getTree = function(){
        return $(this).data('koala.tree');
    },
    // TREE PLUGIN DEFINITION

    $.fn.tree = function (option, value) {
        var methodReturn;

        var $set = this.each(function () {
            var $this = $(this);
            var data = $this.data('koala.tree');
            var options = typeof option === 'object' && option;

            if (!data) $this.data('koala.tree', (data = new Tree(this, options)));
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
