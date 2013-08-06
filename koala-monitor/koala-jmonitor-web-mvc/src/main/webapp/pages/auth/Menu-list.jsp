<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>菜单资源管理</title>
<%@ include file="/pages/common/header.jsp" %>
</head>
<body style="padding:10px;height:100%; text-align:center;">
  <div id="maingrid"></div> 
  
  <div id="editMenu" style="display: none;">
	  <form id="form">
	  	<table class="form2column" >
	  		<tr id="parentTR">
	  			<td class="label">父菜单名称</td>
	  			<td class="content"><input type="text" id="parent" disabled="disabled" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单类型</td>
	  			<td class="content">
	  				<select id="menuType" class="input-common" dataType="Require" style="width:155px;height:25px;">
	  					<option>--请选择--</option>
	  				</select>
	  			</td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单名称</td>
	  			<td class="content"><input type="text" id="name" name="name" class="input-common" dataType="Require" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单标识</td>
	  			<td class="content"><input type="text" id="identifier" class="input-common" name="identifier" dataType="Require" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单图片</td>
	  			<td><img id="menuIcon" name="menuIcon" width="20" height="20" />
	  			<input id="iconBtn" type="button" value="浏览图片" />
	  			</td>
	  		</tr>
	  		<tr>
	  			<td class="label">菜单描述</td>
	  			<td class="content"><input type="text" id="resDesc" name="desc" class="input-common"/></td>
	  		</tr>
	  	</table>
	  </form>
  </div>
  
  <div id="icons"></div>
  
  <script type="text/javascript">
  $(function() {
		var editUrlDiv;
		var isEdit = false;
		var selectedRow;
		var menuType;
		
		var toolbarOptions = {
			items : [ {
				id : "add",
				text : '增加',
				click : toolbarBtnItemClick,
				img : "images/icons/toolbar/add.png"
			}, {
				line : true
			}, {
				id : "modify",
				text : '修改',
				click : toolbarBtnItemClick,
				img : "images/icons/toolbar/page_edit.gif"
			}, {
				line : true
			}, {
				id : "remove",
				text : '删除',
				click : toolbarBtnItemClick,
				img : "images/icons/toolbar/page_delete.gif"
			}, {
				line : true
			}, {
				id : "up",
				text : "上移",
				img : "images/icons/16x16/up.gif",
				click : up
			}, {
				line : true
			}, {
				id : "down",
				text : "下移",
				img : "images/icons/16x16/down.gif",
				click : down
			} ]
		};
		
		$.getJSON("/auth/Menu/findMenuType.koala", function(result) {
			for (var i = 0; i < result.data.length; i++) {
				if (result.data[i].name == "KOALA_MENU") {
					$("#menuType").append('<option value="' + result.data[i].id + '">菜单</option>');
				}
				if (result.data[i].name == "KOALA_DIRETORY") {
					$("#menuType").append('<option value="' + result.data[i].id + '">目录</option>');
				}
			}
		});
		
		$("#menuType").change(function() {
			menuType = $(this).val();
		});

		// 列表结构
		var grid = $("#maingrid").ligerGrid({
			columns : [ {
				display : "菜单名称",
				name : "name",
				type : "text",
				id : "name",
				align : "left"
			}, {
				display : "菜单标识",
				name : "identifier",
				type : "text",
				align : "center"
			}, {
				display : "菜单图片",
				name : "icon",
				align : "center",
				render : function(row) {
					return "<img src='" + row.icon + "'/>";
				}
			}, {
				display : "菜单描述",
				name : "desc",
				align : "center"
			} ],
			dataAction : 'server',
			pageSize : 100,
			toolbar : toolbarOptions,
			url : '/auth/Menu/findAllMenuTreeRows.koala',
			sortName : 'id',
			width : '98%',
			height : '100%',
			heightDiff : -10,
			checkbox : true,
			alternatingRow : false,
			autoCheckChildren : false,
			rowHeight: 28,//行默认的高度
	        headerRowHeight: 30,//表头行的高度
	        pageSizeOptions:[10, 20, 30, 40, 50, 100],
			tree : {
				columnId : "name"
			}
		});

		// 上移节点
		function up() {
			if (grid.getSelectedRows().length > 1) {
				$.ligerDialog.alert("只能选中一行");
				return;
			}
			grid.up(grid.getSelectedRow());
			changeSort();
		}

		// 下移节点
		function down() {
			if (grid.getSelectedRows().length > 1) {
				$.ligerDialog.alert("只能选中一行");
				return;
			}
			grid.down(grid.getSelectedRow());
			changeSort();
		}

		function changeSort() {
			var data = [];
			for ( var i = 0; i < grid.rows.length; i++) {
				var rowNo = i + 1;
				data.push("resourceVOs[" + i + "].sortOrder=" + rowNo);
				data.push("resourceVOs[" + i + "].id=" + grid.rows[i].id);
			}
			$.ajax({
				type : "post",
				data : data.join("&"),
				url : "/auth/Menu/updateMenuOrder.koala"
			});
		}

		// 工具条事件
		function toolbarBtnItemClick(item) {
			switch (item.id) {
			case "add":
				isEdit = false;
				initValue();
				$("#parentTR").show();
				var rows = grid.getCheckedRows();
				if (rows.length > 1) {
					$.ligerDialog.alert("只能选中一个");
					return;
				}
				var manager = $("#maingrid").ligerGetGridManager();
				selectedRow = manager.getSelectedRow();
				$("#parent").val(selectedRow == null ? "" : selectedRow.name);
				editUrlDiv = $.ligerDialog.open({
					title : "菜单资源",
					isResize : true,
					width : 400,
					height : 300,
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
					target : $("#editMenu")
				});
				break;
			case "modify":
				var manager = $("#maingrid").ligerGetGridManager();
				selectedRow = manager.getSelectedRow();
				if (!selectedRow) {
					$.ligerDialog.alert("请选择行");
					return;
				}
				if (grid.getCheckedRows().length > 1) {
					$.ligerDialog.alert("只能选中一个");
					return;
				}
				$("#parentTR").hide();
				isEdit = true;
				initEditFormValue();
				editUrlDiv = $.ligerDialog.open({
					title : "资源",
					isResize : true,
					width : 400,
					height : 300,
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
					target : $("#editMenu")
				});
				break;
			case "remove":
				var manager = $("#maingrid").ligerGetGridManager();
				selectedRow = manager.getSelectedRow();
				if (!selectedRow) {
					$.ligerDialog.alert("请选择行");
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
		
		$("#menuType").change(function() {
			menuType = $(this).val();
		});

		function initValue() {
			$("#name").val("");
			$("#identifier").val("");
			$("#menuIcon").attr("src", "/images/icons/menu/attibutes.gif");
			$("#menuIcon").attr("alt", "图片为空");
			$("#resDesc").val("");
			$("#menuType option[value='']").attr("selected", true);
		}

		function f_delete() {
			if (grid.getCheckedRows().length > 1) {
				$.ligerDialog.alert("只能选中一个");
				return;
			}

			if (grid.getCheckedRows().length = 1) {
				Koala.ajax({
					type : "post",
					url : "/auth/Menu/del.koala",
					loading : "正在删除中...",
					data : "resVO.id=" + selectedRow.id,
					success : function() {
						Koala.showSuccess("删除成功");
						f_reload();
					}
				});
				return;
			}
			$.ligerDialog.alert("请选择行");
		}

		function btnClick(button) {
			if (button.id == "save") {
				saveMenu();
			} else if (button.id == "cancel") {
				editUrlDiv.hidden();
			}
		}

		function initEditFormValue() {
			$("#name").val(selectedRow.name);
			$("#identifier").val(selectedRow.identifier);
			$("#menuIcon").attr("src", selectedRow.icon);
			$("#resDesc").val(selectedRow.desc);
			$("#menuLevel").val(selectedRow.level);
			$("#menuType option[value='" + selectedRow.menuType + "']").attr("selected", true);
			menuType = selectedRow.menuType;
		}

		function saveMenu() {
			var url;
			var data;
			if (isEdit) {
				url = "/auth/Menu/update.koala";
				data = [ {
					name : 'resVO.id',
					value : selectedRow.id
				}, {
					name : 'resVO.name',
					value : $("#name").val()
				}, {
					name : 'resVO.identifier',
					value : $("#identifier").val()
				}, {
					name : 'resVO.desc',
					value : $("#resDesc").val()
				}, {
					name : 'resVO.icon',
					value : $('#menuIcon').attr("src")
				}, {
					name : "resVO.menuType",
					value : menuType
				} ];
			} else {
				if (!selectedRow) {
					url = "/auth/Menu/add.koala";
					data = {
						'resVO.name' : $("#name").val(),
						'resVO.identifier' : $("#identifier").val(),
						'resVO.desc' : $("#resDesc").val(),
						'resVO.icon' : $("#menuIcon").attr("src"),
						"resVO.menuType" : menuType
					};
				} else {
					url = "/auth/Menu/addAndAssignParent.koala";
					data = {
						'parent.id' : selectedRow.id,
						'resVO.name' : $("#name").val(),
						'resVO.identifier' : $("#identifier").val(),
						'resVO.desc' : $("#resDesc").val(),
						'resVO.icon' : $("#menuIcon").attr("src"),
						'parent.level' : selectedRow.level,
						"resVO.menuType" : menuType
					};
				}
			}

			if(!Validator.Validate(document.getElementById("form"),3))return;
			Koala.ajax({
				url : url,
				data : data,
				success : function() {
					grid.loadData();
				},
				complete : function() {
					$("#editMenu :text").each(
							function() {
								$(this).val("");
								$(this).parents("td:first").next("td").find(
										"div.l-exclamation").remove();
							});
					editUrlDiv.hidden();
				}
			});
		}

		$.getJSON("/auth/Menu/getIconNames.koala", function(result) {
			var icons = result.data;
			var content = [];
			var dialog;
			for ( var i = 0; i < icons.length; i++) {
				content.push("<img src='" + icons[i] + "' />");
			}
			var imgs = $(content.join(""));
			$("#iconBtn").click(function() {
				dialog = $.ligerDialog.open({
					width : 200,
					height : 300,
					title : "图片资源",
					content : imgs
				});
			});

			imgs.click(function() {
				$("#menuIcon").attr("src", $(this).attr("src"));
				dialog.hide();
			});
		});

	});

  </script>
</body>
</html>
