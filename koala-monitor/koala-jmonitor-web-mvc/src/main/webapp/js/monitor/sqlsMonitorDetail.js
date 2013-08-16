$(function() {
	// 用户列表结构
	$("#maingrid").ligerGrid(
		{
			columns : [ {
				display : "SQL",
				name : "sql",
				width : 330,
				type : "text",
				align : "left",
				render : function(row){
					if(row.sql != null && row.sql != ""){
						var html = "<label title='"+row.sql+"'>"+row.sql+"</label>";
						return html;
					}
				}
			}, {
				display : "耗时（毫秒）",
				name : "timeConsume",
				width : 100,
				type : "text",
				align : "center"
			}, {
				display : "开始时间",
				name : "beginTime",
				width : 130,
				type : "date",
				format : "yyyy-MM-dd hh:mm:ss",
				align : "center"
			} ],
			dataAction : 'server',
			rownumbers : true,
			url : '/monitor/Monitor/sqlsMonitorDetail.koala?methodId='+$("#methodId").val(),
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