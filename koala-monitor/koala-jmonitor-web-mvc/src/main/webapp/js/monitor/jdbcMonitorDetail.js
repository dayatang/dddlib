$(function() {
	// 用户列表结构
	$("#maingrid").ligerGrid(
		{
			columns : [ {
				display : "Connection打开数量",
				name : "connOpenCount",
				width : 130,
				type : "text",
				align : "center"
			}, {
				display : "Connection关闭数量",
				name : "connCloseCount",
				width : 130,
				type : "text",
				align : "center"
			}, {
				display : "PreparedStatement创建数量",
				name : "pstmtCreateCount",
				width : 170,
				type : "text",
				align : "center"
			}, {
				display : "PreparedStatement关闭数量",
				name : "pstmtCloseCount",
				width : 170,
				type : "text",
				align : "center"
			}, {
				display : "Statement创建数量",
				name : "stmtCreateCount",
				width : 120,
				type : "text",
				align : "center"
			}, {
				display : "Statement关闭数量",
				name : "stmtCloseCount",
				width : 120,
				type : "text",
				align : "center"
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
			}, {
				display : "操作",
				width : 150,
				align : "center",
				render : function(row){
					var html = "";
					html = html + "<a href='javascript:void(0);' style='color:blue;' onclick=showStack('"+row.id+"');>查看堆栈信息</a>&nbsp;&nbsp;&nbsp;&nbsp;";
					/*html = html + "<a href='javascript:void(0);' style='color:blue;' onclick=showSqls('"+row.id+"');>查看SQLS</a>";*/
					return html;
				}
			}  ],
			dataAction : 'server',
			rownumbers : true,
			url : '/monitor/Monitor/jdbcMonitorDetail.koala?timestamp='+new Date().getTime()+'&unit='+$("#unit").val()+'&requestDate=' + $("#requestDate").val() + '&system=' + $("#system").val(),
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