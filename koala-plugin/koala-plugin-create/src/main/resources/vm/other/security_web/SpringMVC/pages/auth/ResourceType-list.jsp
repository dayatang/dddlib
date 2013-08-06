<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>资源类型管理</title>
<%@ include file="/pages/common/header.jsp" %>
</head>
<body style="padding:10px;height:100%; text-align:center;">
  <div id="maingrid"></div> 
  
  <div id="editUrl" style="display: none;">
	  <form id="form">
	  	<table class="form2column">
	  		<tr><td colspan="2">&nbsp;</td></tr>
	  		<tr>
	  			<td class="label">资源类型名称</td>
	  			<td class="content"><input type="text" id="name" name="name" class="input-common" dataType="Require"  /></td>
	  		</tr>
	  	</table>
	  </form>
  </div>
  
  <script type="text/javascript">
  $(function() {

		var toolbarOptions = {
			items : [ {
				id : "add",
				text : '增加',
				click : toolbarBtnItemClick,
				img : "/images/icons/toolbar/add.png"
			}, {
				line : true
			}, {
				id : "modify",
				text : '修改',
				click : toolbarBtnItemClick,
				img : "/images/icons/toolbar/page_edit.gif"
			}, {
				line : true
			}, {
				id : "remove",
				text : '删除',
				click : toolbarBtnItemClick,
				img : "/images/icons/toolbar/page_delete.gif"
			} ]
		};

		var grid = $("#maingrid").ligerGrid({
			columns : [ {
				display : "资源类型名称",
				name : "name",
				type : "text",
				align : "center",
				id : "name"
			} ],
			dataAction : 'server',
			pageSize : 10,
			toolbar : toolbarOptions,
			url : rootPath+'/auth/ResourceType/pageJson.koala',
			sortName : 'id',
			width : '98%',
			height : '100%',
			rowHeight: 28,//行默认的高度
	        headerRowHeight: 30,//表头行的高度
			heightDiff : -10,
			checkbox : true
		});

		// 工具条事件
		function toolbarBtnItemClick(item) {
			switch (item.id) {
			case "add":
				var manager = $("#maingrid").ligerGetGridManager();
				selectedRow = manager.getSelectedRow();
				isEdit = false;
				editUrlDiv = $.ligerDialog.open({
					title : "资源类型",
					isResize : true,
					width : 350,
					height : 200,
					isHidden : true,
					buttons : [ {
						id : 'save',
						text : '保存',
						onclick : btnClick
					}, {
						id : 'cancel',
						text : '取消',
						onclick : btnClick
					} ],
					target : $("#editUrl")
				});
				break;
			case "modify":
				var manager = $("#maingrid").ligerGetGridManager();
				selectedRow = manager.getSelectedRow();
				if (!selectedRow) {
					$.ligerDialog.alert('请选择行!');
					return;
				}

				isEdit = true;
				initEditFormValue();
				editUrlDiv = $.ligerDialog.open({
					title : "资源类型",
					isResize : true,
					width : 300,
					height : 220,
					isHidden : true,
					buttons : [ {
						id : 'save',
						text : '保存',
						onclick : btnClick
					}, {
						id : 'cancel',
						text : '取消',
						onclick : btnClick
					} ],
					target : $("#editUrl")
				});
				break;
			case "remove":
				var manager = $("#maingrid").ligerGetGridManager();
				selectedRow = manager.getSelectedRow();
				if (!selectedRow) {
					$.ligerDialog.alert('请选择行!');
					return;
				}
				jQuery.ligerDialog.confirm('确定删除吗?', function(confirm) {
					if (confirm) {
						f_delete();
					}
				});
				break;
			}
		}

		function f_reload() {
			grid.loadData();
		}

		function f_delete() {
			var manager = $("#maingrid").ligerGetGridManager();
			var rows = manager.getCheckedRows();
			if (rows.length > 0) {
				var data = [];
				for (var i = 0; i < rows.length; i++) {
					data.push("resourceTypeVOs[" + i + "].id=" + rows[i].id);
				}
				Koala.ajax({
					type : "post",
					url : rootPath+'/auth/ResourceType/delete.koala',
					data : data.join("&"),
					loading : "正在删除中...",
					success : function(msg) {
						Koala.showSuccess("删除成功");
						f_reload();
					}
				});
			} else {
				$.ligerDialog.alert('请选择行!');
			}
		}

		function btnClick(button) {
			if (button.id == "save") {
				saveResourceType();
			} else if (button.id == "cancel") {
				editUrlDiv.hidden();
			}
		}

		function initEditFormValue() {
			$("#name").val(selectedRow.name);
		}

		function saveResourceType() {
			var url;
			var data;
			if (isEdit) {
				url = rootPath+"/auth/ResourceType/update.koala";
				data = {
					"resourceTypeVO.name": $("#name").val(),
					"resourceTypeVO.id" : selectedRow.id
				};
			} else {
				url = rootPath+"/auth/ResourceType/save.koala";
				data = {
					"resourceTypeVO.name": $("#name").val()
				};
			}

			if(!Validator.Validate(document.getElementById("form"),3))return;
			Koala.ajax({
				type : "post",
				url : url,
				data : data,
				success : function() {
					Koala.showSuccess("保存成功");
					f_reload();
				},
				complete : function() {
					$("#editUrl :text").each(
							function() {
								$(this).val("");
								$(this).parents("td:first").next("td").find(
										"div.l-exclamation").remove();
							});
					editUrlDiv.hidden();
				}
			});
		 }
	});

  </script>
</body>
</html>
