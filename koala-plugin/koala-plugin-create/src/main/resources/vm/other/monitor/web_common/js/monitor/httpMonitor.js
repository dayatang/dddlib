$(function(){
    var monitorNode = $('#httpMonitorNode');
    var time = $('#time');
    var timeVal = time.find('input');
    $('#timeUnit').select({
        title: '请选择',
        contents: [
            {title: '天', value: 'hour', selected: true},
            {title: '月', value: 'day'}
        ]
    }).on('change', function(){
            var value = $(this).getValue();
            if(value == 'hour'){
                time.datetimepicker('remove')
                    .datetimepicker({
                    language: 'zh-CN',
                    format: "yyyy-mm-dd",
                    autoclose: true,
                    todayBtn: true,
                    minView: 2,
                    pickerPosition: 'bottom-left'
                }).datetimepicker('setDate', new Date());
            }else{
                time.datetimepicker('remove')
                    .datetimepicker({
                    language: 'zh-CN',
                    format: "yyyy-mm",
                    autoclose: true,
                    todayBtn: true,
                    minView: 4,
                    startView: 3,
                    pickerPosition: 'bottom-left'
                }).datetimepicker('setDate', new Date());
            }
            loadData();
        });
       
    $.get(contextPath + '/monitor/Monitor/queryAllNodes.koala').done(function(data){
        var contents = new Array();
        $.each(data.Rows, function(index){
            contents.push({title: this.nodeName, value: this.nodeId, selected: index==0});
        });
        monitorNode.select({
            title: '选择监控节点',
            contents: contents
        }).on('change', function(){
                $('#timeUnit').trigger('change');
            });
        monitorNode.trigger('change');
    });
    var loadData = function(){
        var params = {};
        params.system = monitorNode.getValue();
        /*if(!Validation.notNull($('body'), monitorNode, params.system , '请选择监控节点!')){
			return;
		}*/
        params.unit = $('#timeUnit').getValue();
        params.queryTime = timeVal.val();
        $.get(contextPath + '/monitor/Monitor/httpMonitorCount.koala', params).done(function(data){
            var columns = [
                {
                    title : '请求时段',
                    name : 'dateStr',
                    width : 250
                },
                {
                    title : '请求次数',
                    name : 'httpCount',
                    width : 150
                },
                {
                    title : '操作',
                    name : 'dateStr',
                    width : 250,
                    render: function(item, name, index){
                        return '<a href="#" onclick="httpMonitorDetail(\''+item.dateStr+'\')">查看明细</a>'
                    }
                }
            ];
            $('#httpMonitorGrid').empty().data('koala.grid', null);
            $('#httpMonitorGrid').grid({
                columns: columns,
                isShowIndexCol: false,
                isShowPages: false,
                isUserLocalData: true,
                localData: data.Rows
            });
            //横轴值列表
            var dataJson = new Array();
            $.each(data.Rows, function(){
                dataJson.push([this.dateStr, this.httpCount]);
            });
            dataJson.sort();
            var formatDate = null;
            if($('#timeUnit').getValue() == 'hour'){
                formatDate = '%m-%d %H';
            }else{
                formatDate = '%m-%d';
            }
            $('#httpMonitorChart').empty();
            $.jqplot('httpMonitorChart', [dataJson], {
                title: 'HTTP请求情况展现',
                axes:{
                    xaxis:{
                        renderer:$.jqplot.DateAxisRenderer,
                        tickOptions:{formatString: formatDate},
                        label:'时段'
                    },
                    yaxis: {
                        min: 0,
                        label:'请求次数'
                    }
                },
                //悬浮展现控制
                highlighter: {
                    show: true,
                    yvalues: 1,
                    tooltipAxes: "xy",
                    formatString: '<table class="jqplot-highlighter"> \
                        <tr><td>时段:</td><td>%s</td></tr> \
                        <tr><td>请求次数:</td><td>%s</td></tr></table>'
                },
                cursor: {
                    show: true
                }
            });
            $(window).trigger('resize');
        });
    }
    var searchBtn = $('#httpMonitor').find('#search');
    searchBtn.off('click').on('click', function(){
        loadData();
    });
})
function httpMonitorDetail(requestDate){
   //monitor/Monitor/httpMonitorDetail.koala?unit=hour&requestDate=2013-11-25%2021&system=test
    $.get(contextPath + '/pages/monitor/http-monitor-detail.html').done(function(data){
    	var dialog = $(data);
    	dialog.modal({
            keyboard: true
        }).on({
                'hidden.bs.modal': function(){
                    $(this).remove();
                },
                'shown.bs.modal': function(){
                    var columns = [
                        {
                            title : 'URI',
                            name : 'uri',
                            width : 250
                        },
                        {
                            title : '耗时（毫秒）',
                            name : 'timeConsume',
                            width : 120
                        },
                        {
                            title : 'IP',
                            name : 'ip',
                            width : 120
                        },
                        {
                            title : '用户',
                            name : 'principal',
                            width : 120
                        },
                        {
                            title : '来路',
                            name : 'referer',
                            width : 150
                        },
                        {
                            title : '参数',
                            name : 'parameters',
                            width : 350
                        },
                        {
                            title : '开始时间',
                            name : 'beginTime',
                            width : 150,
                            render: function(item, name, index){
                                var date = new Date(item[name]);
                                return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()
                                    +' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
                            }
                        }
                    ];
                   var params = {
                        unit: $('#timeUnit').getValue(),
                        requestDate: requestDate,
                        system: $('#httpMonitorNode').getValue()
                    };
                    $(this).find('#detailGrid').grid({
                        identity: 'threadKey',
                        columns: columns,
                        isShowIndexCol: false,
                        searchCondition: params,
                        sortName: 'timeConsume',
                        sortOrder : 'DESC',
                        url : contextPath + '/monitor/Monitor/httpMonitorDetail.koala'
                    });
                }
        });
      //兼容IE8 IE9
        if(window.ActiveXObject){
           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
        	   dialog.trigger('shown.bs.modal');
           }
        }
    });
}