var publishUrlList = {

    dialog: null,
    item: null,
    grid: null,
    name: null,
    url: null,

    add: function(grid){
        var self = this;
        self.grid = grid;
        self.item = null;
        $.get(contextPath + '/pages/jbpm/publishUrlTemplate.html').done(function(data){
            self.dialog = $(data);
            self.initDialog();
        });
    },
    modify: function(item, grid){
        var self = this;
        self.item = item;
        self.grid = grid;
        $.get(contextPath + '/pages/jbpm/publishUrlTemplate.html').done(function(data){
            self.dialog = $(data);
            self.initDialog();
        });
    },
    initDialog: function(){
        var self = this;
        self.name = self.dialog.find('#name');
        self.url = self.dialog.find('#url');
        self.dialog.find('.modal-title').html(self.item ? '修改': '新增');
        if(self.item ){
            self.name.val(self.item.name);
            self.url.val(self.item.url);
        }
        self.dialog.modal({
            keyboard: true
        }).on({
             'hidden.bs.modal': function(){
                 $(this).remove();
             }
        }).find('#save').on('click', function(){
                self.save();
        });
    },
    save: function(){
        var self = this;
        if(!Validation.notNull(self.dialog, self.name, self.name.val(), '请输入名称')){
            return
        }
        if(!Validation.notNull(self.dialog, self.url, self.url.val(), '请输入URL')){
            return
        }
        var params = {};
        params['publishURLVO.name'] = self.name.val();
        params['publishURLVO.url'] = self.url.val();
        var url = contextPath + '/core-PublishURL-add.action';
        if(self.item){
            params['publishURLVO.id'] = self.item.id;
            url = contextPath + '/core-PublishURL-update.action';
        }
        $.post(url, params).done(function(result){
            if(result.result == 'success'){
                $('body').message({
                    type: 'success',
                    content: '保存成功'
                });
                self.dialog.modal('hide');
                self.grid.grid('refresh');
            }else{
                self.dialog.find('.modal-content').message({
                    type: 'error',
                    content: '保存失败'
                });
            }
        });
    },
    delete: function(ids, grid){
        var params = {};
        $.each(ids, function(index){
            params['urlIds['+index+']'] = this;
        });
        $.post(contextPath + '/core-PublishURL-delete.action', params).done(function(result){
            if(result.result == 'success'){
                $('body').message({
                    type: 'success',
                    content: '删除成功'
                });
                grid.grid('refresh');
            }else{
                $('body').message({
                    type: 'error',
                    content: '删除失败'
                });
            }
        });
    },
    view: function(name, url){
        $.get(contextPath + '/pages/jbpm/publishUrlTemplate.html').done(function(data){
            var dialog = $(data);
            dialog.find('#name').val(name).attr('disabled', true);
            dialog.find('#url').val(url).attr('disabled', true);
            dialog.find('.modal-title').html('查看');
            dialog.find('#cancel').text('返回');
            dialog.find('#save').hide();
            dialog.modal({
                keyboard: true
            }).on({
                    'hidden.bs.modal': function(){
                        $(this).remove();
                    }
                });
        });
    },
    browse: function(id){
        $.get(contextPath + '/pages/jbpm/urlPreviewTemplate.html').done(function(data){
            var dialog = $(data);
            dialog.modal({
                keyboard: true
            }).on({
                    'hidden.bs.modal': function(){
                        $(this).remove();
                    },
                   'shown.bs.modal': function(){
                        $.get(contextPath + '/jbpm-Jbpm-processes.action?id='+id).done(function(data){
                            if(data.actionError){
                                dialog.find('.modal-body').html('<h3>'+data.actionError+'</h3>');
                                return;
                            }
                            var process = data.processes;
                            var columns = [
                                {
                                    title : '流程ID',
                                    name : 'id',
                                    width : 200
                                },
                                {
                                    title : '流程名称',
                                    name : 'name',
                                    width : 200
                                }
                            ];
                            dialog.find('#processGrid').grid({
                                identity: 'id',
                                isShowIndexCol: false,
                                isUserLocalData: true,
                                isShowPages: false,
                                localData: process,
                                columns: columns
                            });
                        });
                    }
                });
        });
    }
}
