$(function() {
	// 用户列表结构
	$("#maingrid").ligerGrid(
		{
			columns : [ {
				display : "URI",
				//name : "uri",
				width : 200,
				type : "text",
				align : "left",
				//isSort : false,
				render : function(row){
					if(row.uri != null && row.uri != ""){
						var html = "<label title='"+row.uri+"'>"+row.uri+"</label>";
						return html;
					}
				}
			}, {
				display : "耗时（毫秒）",
				name : "timeConsume",
				width : 80,
				type : "text",
				align : "center"
			},{
				display : "IP",
				name : "ip",
				width : 100,
				type : "text",
				align : "center"
			}, {
				display : "用户",
				name : "principal",
				width : 120,
				type : "text",
				align : "center"
			}, {
				display : "来路",
				//name : "referer",
				width : 100,
				type : "text",
				align : "center",
				//isSort : false,
				render : function(row){
					if(row.referer != null && row.referer != ""){
						var html = "<label title='"+row.referer+"'>"+row.referer+"</label>";
						return html;
					}
				}
			}, {
				display : "参数",
				//name : "parameters",
				width : 170,
				type : "text",
				align : "left",
				//isSort : false,
				render : function(row){
					if(row.parameters != null && row.parameters != ""){
						var html = "<label title='"+row.parameters+"'>"+row.parameters+"</label>";
						return html;
					}
				}
			}, {
				display : "开始时间",
				name : "beginTime",
				width : 130,
				type : "date",
				format : "yyyy-MM-dd hh:mm:ss",
				align : "center"
			}
			],
			dataAction : 'server',
			rownumbers : true,
			url : '/monitor/Monitor/httpMonitorDetail.koala?timestamp='+new Date().getTime()+'&unit='+$("#unit").val()+'&requestDate=' + $("#requestDate").val() + '&system=' + $("#system").val(),
			sortName : 'timeConsume',
			sortOrder : 'DESC',
			width : '98%',
			height : '100%',
			rowHeight: 28,//行默认的高度
	        headerRowHeight: 30,//表头行的高度
			heightDiff : -10,
			checkbox : false
		});
});