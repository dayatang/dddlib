$(function(){
    var poolMonitor = $('#poolMonitor');
    var databaseChartArea = $('#databaseChartArea');
    var databaseChartSearch = $('#databaseChartSearch');
    var monitorNode = $('#dataBaseMonitorNode');
    $('#monitorCategory').select({
        title: '选择监控项',
        contents: [
            {title: '连接耗时分布图', value: 'connectionProcessedChart', selected: true},
            {title: '连接池状态图', value: 'poolStausChart'}
        ]
    }).on('change', function(){
            var value = $(this).getValue();
            if(value == 'poolStausChart'){
                databaseChartSearch.css('visibility', 'hidden');
                poolMonitor.show();
                databaseChartArea.parent().hide();
                loadPoolStatusData();
            }else{
                databaseChartSearch.css('visibility', 'visible');
                poolMonitor.hide();
                databaseChartArea.parent().show();
                loadConnectionProcessedData();
            }
        });
    $.get('monitor/Monitor/queryAllNodes.koala').done(function(data){
        var contents = new Array();
        $.each(data.Rows, function(index){
            contents.push({title: this.nodeName, value: this.nodeId, selected: index==0});
        });
        monitorNode.select({
            title: '选择监控节点',
            contents: contents
        }).on('change', function(){
                $('#monitorCategory').trigger('change');
            });
        if(data.Rows.length > 0){
            monitorNode.trigger('change');
        }
    });
    $('#timeOut').on('blur', function(){
        $('#monitorCategory').trigger('change');
    });
    var loadConnectionProcessedData = function(){
        var nodeId = monitorNode.getValue();
        var timeOut = $('#timeOut').val();
        $.get('monitor/Monitor/jdbcTimeStat.koala?nodeId='+nodeId+'&limit='+timeOut).done(function(data){
            var data = data.data;
            var xarray = eval('['+data[1]+']');
            var yarray = eval('['+data[0]+']');
            $.jqplot.config.enablePlugins = true;
            $('#databaseChartArea').empty();
            var plot = $.jqplot('databaseChartArea', [xarray], {
                title: "<span style='color:#707070; font-size:18px;'>24小时内数据库连接超过阀值统计</span>",
                animate: !$.jqplot.use_excanvas,
                seriesDefaults:{
                    renderer:$.jqplot.BarRenderer,
                    pointLabels: { show: true }
                },
                axes: {
                    xaxis: {
                        label:"<span style='color:#707070; font-size:18px;font-weight:bold;'>时段</span>",
                        renderer: $.jqplot.CategoryAxisRenderer,
                        ticks: yarray
                    },
                    yaxis: {
                        label: "超时次数",
                        min:0
                    }
                }
            });
            $(window).trigger('resize');
        });
    }
    var loadPoolStatusData = function(){
        $.get('monitor/Monitor/poolMonitorDetail.koala?nodeId='+monitorNode.getValue()).done(function(data){
            var navTabs = poolMonitor.find('.nav-tabs').empty();
            var tabContent = poolMonitor.find('.tab-content').empty();
            var pools = data.pools;
            $.get('pages/monitor/jdbc-monitor-detail.html').done(function(template){
                var index = 0;
                var template = $(template)
                for(var prop in pools){
                   // poolMonitor.find('.panel-heading').html(prop);
                    var content = template.clone();
                    navTabs.append($('<li><a href="#tab'+index+'" data-toggle="tab">'+prop+'</a></li>'));
                    tabContent.append($('<div id="tab'+index+'" class="tab-pane"></div>').append(content));
                    navTabs.find('li').eq(index).find('a').tab('show');
                    var data = pools[prop];
                    content.find('#startTime').html(data.startTime);
                    content.find('#snapshotTime').html(data.snapshotTime);
                    content.find('#initConnectionCount').html(data.initConnectionCount);
                    content.find('#activeConnectionCount').html(data.activeConnectionCount);
                    content.find('#idleConnectionCount').html(data.idleConnectionCount);
                    content.find('#maxConnectionCount').html(data.maxConnectionCount);
                    content.find('#maxActiveTime').html(data.maxActiveTime);
                    var maxConnectionCount = data.maxConnectionCount;
                    var activeConnectionCount = data.activeConnectionCount;
                    var idleConnectionCount = data.idleConnectionCount;
                    content.find('#activeConnectionCountRate').css('width', (activeConnectionCount/maxConnectionCount)*100+'%');
                    content.find('#idleConnectionCountRate').css('width', (idleConnectionCount/maxConnectionCount)*100+'%');
                    var columns = [
                        {
                            title : "创建时间",
                            name : "formatCreateTime",
                            width : 180
                        },
                        {
                            title : "上一次激活时间",
                            name : "formatLastActiveTime",
                            width : 180
                        },
                        {
                            title : "存活时间（毫秒）",
                            name : "activeTime",
                            width : 180
                        },
                        {
                            title : "源",
                            name : "source",
                            width : 120
                        }
                    ];
                    content.find('#poolDetailGrid').grid({
                        identity: 'id',
                        columns: columns,
                        isUserLocalData: true,
                        localData: data.connDetails,
                        isShowPages: false,
                        isShowIndexCol: false
                    });
                    index++;
                }
                navTabs.find('li:first').find('a').tab('show');
                $(window).trigger('resize');
            });
        });
    }
})