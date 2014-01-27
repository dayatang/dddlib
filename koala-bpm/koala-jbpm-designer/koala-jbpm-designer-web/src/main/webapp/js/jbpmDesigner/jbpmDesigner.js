var gunvorServerUrl = null;
$(function(){
   // var data = [{"parentId":"root","menu":{"id":"001","title":"权限管理","href":"#"},"children":{"001001":{"parentId":"001","menu":{"id":"001001","title":"用户管理","href":"#"},"children":{"001001001":{"parentId":"001001","menu":{"id":"001001001","title":"添加新用户","href":"#"},"children":{}},"001001002":{"parentId":"001001","menu":{"id":"001001002","title":"系统用户查询","href":"#"},"children":{}}}},"001002":{"parentId":"001","menu":{"id":"001002","title":"角色管理","href":"#"},"children":{"001002001":{"parentId":"001002","menu":{"id":"001002001","title":"添加新角色","href":"#"},"children":{}},"001002002":{"parentId":"001002","menu":{"id":"001002002","title":"用户角色配置","href":"#"},"children":{}},"001002003":{"parentId":"001002","menu":{"id":"001002003","title":"角色权限配置","href":"#"},"children":{}}}},"001003":{"parentId":"001","menu":{"id":"001003","title":"菜单管理","href":"#"},"children":{}}}},{"parentId":"root","menu":{"id":"002","title":"系统管理","href":"#"},"children":{"002001":{"parentId":"002","menu":{"id":"002001","title":"功能配置","href":"#"},"children":{}},"002002":{"parentId":"002","menu":{"id":"002002","title":"系统访问记录","href":"#"},"children":{}}}},{"parentId":"root","menu":{"id":"003","title":"日志管理","href":"#"},"children":{"003001":{"parentId":"003","menu":{"id":"003001","title":"日志查询","href":"#"},"children":{}}}}];
    var menuTree = $('#menuTree');
    $.get(contextPath + '/jbpm-Jbpm-findPackages.action').done(function(data){
        gunvorServerUrl = data.gunvorServerUrl;
        var children = data.packages || [];
        var rootNode = {menu:{id:'root', title:'流程', level: 0}, parentId:"root", type: 'parent'};
        //rootNode.children = children;
        var packages = new Array();
        $.each(children, function(){
            packages.push({menu:{id:this.text, title:this.text, level: 1}, parentId:"root", type: 'parent', children: []});
        });
        rootNode.children = packages;
        var dataSourceTree = {
            data: [rootNode],
            delay: 400
        };
        menuTree.tree({
            dataSource: dataSourceTree,
            loadingHTML: '<div class="static-loader">Loading...</div>',
            multiSelect: false,
            cacheItems: true
        }).on({
                'contextmenu': function(e){
                    return false;
                },
                'rightClick': function(e, originalEvent){
                     createRightMenu(originalEvent);
                },
                'addPackage': function(e, $element){
                    addPackage($element);
                },
                'addProcess': function(e, $element){
                    addProcess($element);
                },
                'deletePackage': function(e, $element){
                    deletePackage($element);
                },
                'editProcess': function(e, $element){
                    editProcess($($element).data());
                },
                'deleteProcess': function(e, $element){
                    deleteProcess($element);
                },
                'publishProcess': function(e, $element){
                    publishProcess($element);
                },
                'opened': function(e, data){
                    openPackage(data);
                },
                'selected': function(e, data){
                    editProcess(data.info[0]);
                }
         });
    });

    var createRightMenu = function(ev){
        var $element = $(ev.currentTarget);
        var level = $element.data('level');
        var menuData = new Array();
        if(level == 0){
            menuData.push({action: 'addPackage', title:'增加新包'});
        }else if(level == 1){
            menuData.push({action: 'addProcess', title:'新增流程'});
            menuData.push({action: 'deletePackage', title:'删除此包'});
        }else{
            menuData.push({action: 'editProcess', title:'编辑流程'});
            menuData.push({action: 'deleteProcess', title:'删除流程'});
            menuData.push({action: 'publishProcess', title:'发布流程'});
        }
        menuTree.tree('createRightMenu', {event:ev, element: $element, data:menuData});
    };

    var  addPackage = function($element){
        $.get(contextPath + '/pages/jbpm/jbpmPackageTemplate.html').done(function(data){
            var dialog = $(data);
            dialog.modal({
                keyboard: true
            }).on('hidden.bs.modal', function(){
                $(this).remove();
            }).find('#save').on('click', {dialog: dialog}, function(e){
                var dialog = e.data.dialog;
                var name = dialog.find('#name');
                var descript = dialog.find('#descript');
                if(!Validation.notNull(dialog, name, name.val(), '请输入包名')){
                    return
                }
                var params = {
                    packageName: name.val(),
                    description: descript.val()
                }
                $.post(contextPath + '/jbpm-Jbpm-createPackage.action',params).done(function(data){
                    if(!data.errors){
                        $('body').message({
                            type: 'success',
                            content: '添加成功'
                        });
                        dialog.modal('hide');
                        var menu = {
                            title: name.val(),
                            level: $($element).data('level')+1
                        };
                        menuTree.tree('addChildren', {node: $element, type: 'parent',menu: menu });
                    }else{
                        dialog.find('.modal-content').message({
                            type: 'error',
                            content: '添加失败'
                        });
                    }
                });
            });
        });
    };

    var addProcess = function($element){
        $.get(contextPath + '/pages/jbpm/jbpmProcessTemplate.html').done(function(data){
            var dialog = $(data);
            dialog.modal({
                keyboard: true
            }).on('hidden.bs.modal', function GunvorApplicationImpl(){
                    $(this).remove();
                }).find('#save').on('click', {dialog: dialog}, function(e){
                    var dialog = e.data.dialog;
                    var name = dialog.find('#name');
                    var descript = dialog.find('#descript');
                    if(!Validation.notNull(dialog, name, name.val(), '请输入名称')){
                        return
                    }
                    var menu = {
                        title: name.val(),
                        level: $($element).data('level')+1,
                        parent: $($element).data('title')
                    };
                    menuTree.tree('addChildren', {node: $element, type: 'children',menu: menu });
                    menu.url = gunvorServerUrl+'/org.drools.guvnor.Guvnor/standaloneEditorServlet' +
                        '?locale=zh_CN&packageName='+$($element).data('title')+
                        '&createNewAsset=true&assetName='+menu.title+'&description='+descript.val()+
                        '&assetFormat=bpmn2&client=oryx';
                    openTab('/pages/jbpm/edit-process.html', name.val(), name.val(),  name.val(), menu);
                    $('body').message({
                          type: 'success',
                          content: '添加成功'
                    });
                    dialog.modal('hide');
                });
        });
    };

    var deletePackage = function($element){
        var data = $($element).data();
        //http://localhost:9090/jbpm-Jbpm-deletePackage.action?packageName=test
        $('body').confirm({
            content: '确定要删除所选的包吗?',
            callBack: function(){
                $.post(contextPath + '/jbpm-Jbpm-deletePackage.action?packageName='+data.id).done(function(result){
                    if(!data.errors){
                        $('body').message({
                            type: 'success',
                            content: '删除成功'
                        });
                        menuTree.tree('removeChildren', $element);
                    }else{
                        $('body').message({
                            type: 'error',
                            content: '删除失败'
                        });
                    }
                });
            }
        });
    };

    var editProcess = function(data){
        data.url = gunvorServerUrl+"/org.drools.guvnor.Guvnor/standaloneEditorServlet?locale=zh_CN&assetsUUIDs="+ data.id + "&client=oryx";
        openTab('/pages/jbpm/edit-process.html', data.title, data.title,  data.title, data);
    };

    var deleteProcess = function($element){
        $('body').confirm({
            content: '确定要删除所选的流程吗?',
            callBack: function(){
                var data = $($element).data();
                var bpmnName = data.title;
                $.post(contextPath + '/jbpm-Jbpm-deleteBpmn.action?packageName='+data.parent+'&bpmnName='+bpmnName).done(function(result){
                    alert(result.errors);
                    if(!result.errors){
                        $('body').message({
                            type: 'success',
                            content: '删除成功'
                        });
                        menuTree.tree('removeChildren', $element);
                        $('#navTabs').find('[href="#home"]').tab('show');
                        $('#navTabs').find('[href="#'+bpmnName+'"]').parent().remove();
                        $('#tabContent').find('#'+bpmnName).remove();
                    }else{
                        $('body').message({
                            type: 'error',
                            content: '删除失败'
                        });
                    }
                });
            }
        });
    };

    var publishProcess = function($element){
        $.get(contextPath + '/pages/jbpm/publishProcessTemplate.html').done(function(data){
            var dialog = $(data);
            $.get(contextPath + '/jbpm-Jbpm-getPublish.action').done(function(result){
                var urls = new Array();
                $.each(result.urls, function(){
                    urls.push({title: this.name, value: this.url});
                });
                dialog.find('#urlList').select({
                    title: '选择发布地址',
                    contents: urls
                });
            });
            dialog.modal({
                keyboard: true
            }).on('hidden.bs.modal', function(){
                    $(this).remove();
                }).find('#save').on('click', {dialog: dialog}, function(e){
                    var dialog = e.data.dialog;
                    var urlList = dialog.find('#urlList');
                    if(!Validation.notNull(dialog, urlList, urlList.getValue(), '请选择发布地址')){
                        return
                    }
                    var data = $($element).data();
                    var params = {
                        bpmnName: data.title,
                        packageName: data.parent,
                        wsdl: urlList.getValue()
                    };
                    $.post(contextPath + '/jbpm-Jbpm-publish.action', params).done(function(data){
                        if(!data.errors){
                            $('body').message({
                                type: 'success',
                                content: '发布成功'
                            });
                            dialog.modal('hide');
                        }else{
                            dialog.message({
                                type: 'error',
                                content: '发布失败:'+data.errors
                            })
                        }
                    });
                });
        });
    };

    var openPackage = function(params){
        var $element = $(params.element);
        var data = params.data;
        var treeFolderContent = $element.parent().find('.tree-folder-content');
        if(data.level == 1 && treeFolderContent.children().length == 0){
            $.get(contextPath + '/jbpm-Jbpm-findBpmns.action?packageName='+data.id).done(function(result){
                $.each(result.bpmns, function(){
                    menuTree.tree('addChildren', {node: $element, type: 'children',menu: {id:this.uuid, parent:data.id, title: this.text, level: 2} });
                });
            });
        }
    };
});
