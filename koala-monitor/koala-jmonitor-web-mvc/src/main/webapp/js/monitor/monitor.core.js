$(function(){
	loadMonitorNodes();
    $("#nodes").bind("change", loadData);
});

function loadData(){
	var params = "nodeId=" + $("#nodes").val() + "&gt=" + $("#gt").val();
	var url = "/monitor/Monitor/jdbcTimeStat.koala?"+ params;
	$.get(url, function(data){
		
	});
}

function loadMonitorNodes(){
	var url = "/monitor/Monitor/queryAllNodes.koala?_"+new Date().getTime();
	$.get(url, function(data){
		 $("#nodes").empty();
		 var activeNodeCount = 0;
         for(var index in data['data']){
        	 var node = data['data'][index];
        	 if(node['active'] == false || node['active'] == 'false')continue;
        	 var html = "<option value='"+node['nodeId']+"'>"+node['nodeName']+"</option>";
        	 $("#nodes").append(html);
        	 activeNodeCount++;
		  }
		if(activeNodeCount>0){loadData();}
	});
};


function drawBar(xarray,yarray) {
	$.jqplot.config.enablePlugins = true;
    var plot = $.jqplot('chartArea', [s1], {
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: yarray
            }
        },
        highlighter: { show: false }
    });

    $('#chartArea').bind('jqplotDataClick', 
        function (ev, seriesIndex, pointIndex, data) {
            
        }
    );
}


 