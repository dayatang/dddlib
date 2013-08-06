$(function() {
	// 用户列表结构
	$("#maingrid").ligerGrid(
		{
			columns : [ {
				display : "方法",
				//name : "method",
				width : 330,
				type : "text",
				align : "left",
				render : function(row){
					if(row.method != null && row.method != ""){
						var html = "<label title='"+row.method+"'>"+row.method+"</label>";
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
			}, {
				display : "是否执行成功",
				name : "successed",
				width : 90,
				align : "center",
				render : function(row){
					var successed = row.successed;
					if(successed){
						return "是";
					}else{
						return "<font color='red'>否</font>";
					}
				}
			}, {
				display : "操作",
				width : 150,
				align : "center",
				render : function(row){
					var html = "";
					html = html + "<a href='javascript:void(0);' style='color:blue;' onclick=showSqls('"+row.id+"');>查看SQLS</a>&nbsp;&nbsp;&nbsp;&nbsp;";
					html = html + "<a href='javascript:void(0);' style='color:blue;' onclick=showStack('"+row.id+"');>查看堆栈信息</a>";
					return html;
				}
			} ],
			dataAction : 'server',
			rownumbers : true,
			url : '/monitor/Monitor/methodMonitorDetail.koala?'+$("#dataForm").serialize(),
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