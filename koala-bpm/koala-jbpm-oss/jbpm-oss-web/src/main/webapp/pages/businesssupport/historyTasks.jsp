<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<html>
	<head>
	
	<title>已办任务</title>
	
	<script type="text/javascript">
		$(document).ready(function() {

			$.ajax({
				method: "post",
				url: "${pageContext.request.contextPath}/businessSupport/getProcesses.koala",
				success: function(result) {
					var items = new Array();
					if(result.Rows){
						for (var i = 0, j=result.Rows.length; i < j; i++) {
							items.push({title: result.Rows[i].name + "_" + result.Rows[i].id, value:result.Rows[i].id});
						}
					}
					$('#processesHistory').select({
						title: '请选择流程类型',
						contents : items
					}).on('change', function() {
						initGrid($(this).getValue());
					});
					if(result.Rows && result.Rows.length>0){
						$('#processesHistory').setValue(result.Rows[0].id);
					}
				}
			});
		});
		
		function initGrid(processId) {
			var columns = [{
				title: "流程名称",
				name: "processName",
				width: 250
			}/* , {
				title: "创建人",
				name: "creator",
				width: 100
			} */];
			$.ajax({
				method: "post",
				async: false,
				url: "${pageContext.request.contextPath}/businessSupport/pageQueryDoneTask.koala?page=1&pagesize=1&processId=" + processId,
				success: function(result) {
					if(!result.Rows || result.Rows.length == 0){
						return;
					}
					var dynamicColumns = result.Rows[0].dynamicColumns;
					
					for (var i = 0; i < dynamicColumns.length; i++) {
						columns.push({
							title: dynamicColumns[i].keyName,
							width: 250,
							name: dynamicColumns[i].keyValueForShow,
							render: function(item, name, rowIndex, columnIndex) {
								return item.dynamicColumns[columnIndex - 1].keyValueForShow;
							}
						});
					}
					columns.push({
						title: '操作',
						width: 'auto',
						render: function(item, name, rowIndex, columnIndex){
							return "<a href='${pageContext.request.contextPath}/businessSupport/toHistoryPage.koala?processInstanceId="+item.processInstanceId+"&processId="+item.processId+"&taskId="+item.taskId+"'>查看</a>";
						}
					});
					$("#doneTasksGrid").grid({
						columns: columns,
						url: "${pageContext.request.contextPath}/businessSupport/pageQueryDoneTask.koala?processId=" + processId
					});
				}
			});
		}
		
		var view = function(id) {
			window.open('pages/businesssupport/taskview.html?id=' + id, '查看');
		};
	</script>
	
	
	</head>
	
	<body>
		<form class="form-horizontal">
		  <div class="form-group col-lg-7">
		    <label class="col-lg-2 control-label">流程类型:</label>
		    <div class="col-lg-10">
				<div class="col-lg-10 btn-group select" id="processesHistory">
					<button data-toggle="item" class="btn btn-default" type="button">请选择流程类型</button>
					<button data-toggle="dropdown" class="btn btn-default dropdown-toggle" type="button">
						<span class="caret"></span>
					</button>
					<input type="hidden" data-toggle="value">
				</div>
		    </div>
		  </div>
		</form>
		<div style="clear:both;"></div>
		<div id="doneTasksGrid"></div>
	</body>

</html>

