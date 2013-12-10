var scheduleManager = {

    showTimeExample: function(){
        var dialog = $('<div class="modal fade"><div class="modal-dialog">' +
            '<div class="modal-content"><div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
            '<h4 class="modal-title">时间表达式实例</h4></div><div class="modal-body">' +
            '<table class="table table-hover table-striped">' +
                '<tbody><tr><td>0 0/10 * * * ?</td><td>每间隔10分钟执行一次</td></tr>' +
                '<tr><td>0 0 12 * * ? </td><td>每天中午12点触发</td></tr>' +
                '<tr><td>0 15 10 * * ?</td><td>每天上午10:15触发</td></tr>' +
                '<tr><td>0 * 14 * * ?</td><td>每天下午2点到下午2:59期间的间隔每1分钟执行一次</td></tr>'+
                '<tr><td>0 0/5 14 * * ?</td><td>每天下午2点到下午2:55期间的每5分钟触发></td></tr>'+
                '<tr><td>0 0/5 14,18 * * ?</td><td>每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发</span></td></tr>'+
                '<tr><td>0 0-5 14 * * ?</td><td>每天下午2点到下午2:05期间的每1分钟触发</td></tr>'+
                '<tr><td>0 15 10 15 * ?</td><td>每月15日上午10:15触发</td></tr>'+
                '<tr><td>0 0/5 8-17 * * ?</td><td>上午8点到下午5点每间隔5分钟执行一次</td></tr>'+
                '</tbody>' +
            '</table></div>' +
            '<div class="modal-footer">' +
                 '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>' +
            '</div></div>' +
            '</div></div>');
        dialog.modal({
            keyboard: true
        }).on('hidden', function(){
             $(this).remove();
        });
    },
    changeStatus: function(triggerName, currentStatus){
        var params = {triggerName:triggerName,currentStat:currentStatus};
        $.post( "monitor/ServiceMonitor/updateScheduleConf.koala",params)
        .done(function(result){
            if(result.success){
               $('body').message({
                   type: 'success',
                   content: '修改成功'
               })
                $('#scheduleGrid').grid('refresh');
            }else{
                $('body').message({
                    type: 'error',
                    content: '修改失败'
                })
            }
        }).fail(function(result){
                $('body').message({
                    type: 'error',
                    content: '修改失败'
                })
         });
    },
    updateCronExpr: function(triggerName, obj){
        var cronExprInput = $(obj).prev('input');
        if(!cronValidate(cronExprInput.val())){
            showErrorMessage($('body'), cronExprInput, '时间表达式不合法，请参考相关实例');
        }
        var params = {triggerName:triggerName,confExpr:cronExprInput.val()};
        $.post('monitor/ServiceMonitor/updateScheduleConf.koala', params)
         .done(function(result){
            if(result.success){
                $('body').message({
                    type: 'success',
                    content: '修改成功'
                })
                $('#scheduleGrid').grid('refresh');
            }else{
                $('body').message({
                    type: 'error',
                    content: '修改失败'
                })
            }
        }).fail(function(result){
              $('body').message({
                  type: 'error',
                  content: '修改失败'
              })
        })
    }
}