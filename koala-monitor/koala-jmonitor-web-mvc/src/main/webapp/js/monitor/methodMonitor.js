$(function(){
    var startTime = $('#startTime');
    var startTimeVal = startTime.find('input');
    var endTime = $('#endTime');
    var endTimeVal = endTime.find('input');
    startTime.datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd hh:ii:ss",
            autoclose: true,
            todayBtn: true,
            pickerPosition: 'bottom-left'
    }).on('changeDate', function(){
          endTime.datetimepicker('setStartDate', startTimeVal.val());
    });
    var yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    startTime.datetimepicker('setDate', yesterday)
    endTime.datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd hh:ii:ss",
        autoclose: true,
        todayBtn: true,
        pickerPosition: 'bottom-left'
    }).on('changeDate', function(ev){
         startTime.datetimepicker('setEndDate', endTimeVal.val());
    }).datetimepicker('setDate', new Date()).trigger('changeDate');
    startTime.trigger('changeDate');
    var methodCountType = $('#methodCountType');
    methodCountType.select({
        title: '选择监控项',
        contents: [
            {title: '调用次数', value: 'methodCount', selected: true},
            {title: '平均耗时（毫秒）', value: 'avgTimeConsume'},
            {title: '出错次数', value: 'methodExceptionCount'}
        ]
    }).on('change', function(){
         loadData($(this).getValue());
    });
    $('#methodSearch').on('click', function(){
        loadData($(this).getValue());
    });
    var monitorNode = $('#methodMonitorNode');
    $.get(contextPath + '/monitor/Monitor/queryAllNodes.koala').done(function(data){
        var contents = new Array();
        $.each(data.Rows, function(index){
            contents.push({title: this.nodeName, value: this.nodeId, selected: index==0});
        });
        monitorNode.select({
            title: '选择监控节点',
            contents: contents
        }).on('change', function(){
                methodCountType.trigger('change');
            });
        monitorNode.trigger('change');
    });
    var loadData = function(type){
    	$('#methodDetailGrid').empty();
        $('#methodDetailChart').empty();
        var title = '调用次数';
        if(type == 'methodCount'){
        	title = '调用次数';
        }else if(type == 'avgTimeConsume'){
        	title = '平均耗时（毫秒）';
        }else{
        	title = '出错次数';
        }
        var columns = [
            {
                title : '方法',
                name : 'method',
                width : 450
            },
            {
                title : title,
                name : methodCountType.getValue(),
                width : 150
            },
            {
                title : '操作',
                name : 'method',
                width : 150,
                render: function(item, name, index){
                    return '<a onclick="showMethodMonitorDetail(\''+item[name]+'\')">查看明细</a>'
                }
            }
        ];
        var searchCondition = {
            timeStart: startTimeVal.val(),
            timeEnd: endTimeVal.val(),
            system: monitorNode.getValue(),
            methodCountType: methodCountType.getValue()
        };
        $('#methodDetailGrid').data('koala.grid', null);
        $('#methodDetailGrid').grid({
            identity: 'id',
            columns: columns,
            isShowIndexCol: false,
            isShowPages: false,
            searchCondition: searchCondition,
            url: contextPath + '/monitor/Monitor/methodMonitorCount.koala'
        }).on('complateRenderData', function(event, data){
                loadChart(data.Rows);
                $(window).trigger('resize');
            });
    }
    var loadChart = function(data){
        if(!data || data.length == 0){
        	$('#methodDetailChart').css('height', 0);
            return;
        }
        $.jqplot.config.enablePlugins = true;
        //var xArray = ['aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..','aaaaaaaaaaa..'];
       // var yArray = [10,9,8,7,6,5,4,3,2,1];
        var xArray = [];
        var yArray = [];

        //只显示前10个方法
        var showNums = 0;
        if(data.length <= 10){
            showNums = data.length;
        }else{
            showNums = 10;
        }

        //只显示前10个方法（即横轴只显示10条数据）
        var showNums = 10;
        //统计类型
        for (var i = 0; i < showNums; i++) {
            //如果数据未到10条，则剩下的补上空值
            if( i >= data.length ){
                xArray[i] = '';
                yArray[i] = '';
            }else{
                var chObj = data[i];
                //横坐标长度超过11时,截取显示
                var xInfos = chObj.method.split(".");
                var xInfo = null;
                if(xInfos.length > 1){
                    xInfo = xInfos[1].split("(")[0];
                }else{
                    xInfo = xInfos[0];
                }
                if(xInfo.length > 11){
                    xInfo = xInfo.substring(0,11) + "..";
                }
                //只显示方法名称
                xArray[i] = xInfo;
                //xArray[i] = chObj.method;
                var methodCountTypeVal = methodCountType.getValue();
                if(methodCountTypeVal == "methodCount"){
                    yArray[i] = chObj.methodCount;
                }else if(methodCountTypeVal == "avgTimeConsume"){
                    yArray[i] = chObj.avgTimeConsume;
                }else{
                    yArray[i] = chObj.methodExceptionCount;
                }
            }

        }
        plot1 = $.jqplot('methodDetailChart', [yArray], {
            // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
            animate: !$.jqplot.use_excanvas,
            seriesDefaults:{
                renderer:$.jqplot.BarRenderer,
                pointLabels: { show: true }
            },
            axes: {
                xaxis: {
                    label:"方法",
                    renderer: $.jqplot.CategoryAxisRenderer,
                    ticks: xArray
                }
            },
            /* axesDefaults: {
             min: 1,
             //max: null,
             pad: 1
             }, */
            highlighter: { show: false }
        });
    }
});
var showMethodMonitorDetail = function(method){
    $.get(contextPath + '/pages/monitor/method-monitor-detail.html').done(function(data){
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
                            title : '方法',
                            name : 'method',
                            width : 450
                        },
                        {
                            title : '耗时（毫秒）',
                            name : 'timeConsume',
                            width : 120
                        },
                        {
                            title : '开始时间',
                            name : 'beginTime',
                            width : 180,
                            render: function(item, name, index){
                                var date = new Date(item[name]);
                                return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()
                                    +' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
                            }
                        },
                        {
                            title : '是否执行成功',
                            name : 'successed',
                            width : 120,
                            align: 'center',
                            render: function(item, name, index){
                                return item[name] ? '<span class="glyphicon glyphicon-ok" style="color:#5CB85C;"></span>': '<span class="glyphicon glyphicon-remove" style="color:#b94a48;"></span>';
                            }
                        },
                        {
                            title : '操作',
                            name : 'stackTracesDetails',
                            width : 150,
                            render: function(item, name, index){
                                return '<a onclick="showSqlsMonitorDetail(\''+item.id+'\')">查看SQLS </a>&nbsp;&nbsp;&nbsp;<a onclick="showStackTracesDetail(this)" stackTracesDetails="'+item.stackTracesDetails+'">查看堆栈信息</a>';
                            }
                        }
                    ];
                    var params = {
                        timeStart: $('#startTime').find('input').val(),
                        timeEnd: $('#endTime').find('input').val(),
                        nodeId: $('#methodMonitorNode').getValue(),
                        method: method
                     };
                    $(this).find('#detailGrid').grid({
                        identity: 'id',
                        columns: columns,
                        isShowIndexCol: false,
                        searchCondition: params,
                        sortName: 'timeConsume',
                        sortOrder : 'DESC',
                        url : contextPath + '/monitor/Monitor/methodMonitorDetail.koala'
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
var showStackTracesDetail = function(obj){
    $.get('/pages/monitor/stack-trace-detail.html').done(function(data){
        $(data).modal({
            keyboard: true,
            backdrop: false
        }).on('hidden.bs.modal', function(){
             $(this).remove();
         }).find('.modal-body').html($(obj).attr('stackTracesDetails'));
    });
}
var showSqlsMonitorDetail = function(methodId){
    $.get('/pages/monitor/sql-monitor-detail.html').done(function(data){
    	var dialog = $(data);
    	dialog.modal({
        	keyboard: true,
        	backdrop: false
        }).on({
                'hidden.bs.modal': function(){
                    $(this).remove();
                },
                'shown.bs.modal': function(){
                    var columns = [
                        {
                            title : 'SQL',
                            name : 'sql',
                            width : 450
                        },
                        {
                            title : '耗时（毫秒）',
                            name : 'timeConsume',
                            width : 150
                        },
                        {
                            title : '开始时间',
                            name : 'beginTime',
                            width : 180,
                            render: function(item, name, index){
                                var date = new Date(item[name]);
                                return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()
                                    +' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
                            }
                        }
                    ];
                    $(this).find('#dataGrid').grid({
                        identity: 'id',
                        isShowIndexCol: false,
                        columns: columns,
	                    sortName: 'timeConsume',
	                    sortOrder : 'DESC',
                        url : '/monitor/Monitor/sqlsMonitorDetail.koala?methodId='+methodId
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