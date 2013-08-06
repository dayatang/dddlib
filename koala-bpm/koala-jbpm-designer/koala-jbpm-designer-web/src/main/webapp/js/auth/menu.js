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
		url : 'auth-Menu-findAllMenuTreeRows.action',
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
			url : "auth-Menu-updateMenuOrder.action"
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
	
	function initComboBox() {
		return $("#menuType").ligerComboBox({
			data: [{
				id: 1, 
				text: "菜单"
			}, {
				id: 2,
				text: "目录"
			}],
			onSelected: function(value) {
				menuType = value;
			}
		});
	}

	function initValue() {
		$("#name").val("");
		$("#identifier").val("");
		$("#menuIcon").attr("src", "/images/icons/menu/attibutes.gif");
		$("#menuIcon").attr("alt", "图片为空");
		$("#resDesc").val("");
		initComboBox();
	}

	function f_delete() {
		if (grid.getCheckedRows().length > 1) {
			$.ligerDialog.alert("只能选中一个");
			return;
		}

		if (grid.getCheckedRows().length = 1) {
			Koala.ajax({
				type : "post",
				url : "auth-Menu-del.action",
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
		var combo = initComboBox();
		combo.selectValue(selectedRow.menuType);
	}

	function saveMenu() {
		var url;
		var data;
		if (isEdit) {
			url = "auth-Menu-update.action";
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
				url = "auth-Menu-add.action";
				data = {
					'resVO.name' : $("#name").val(),
					'resVO.identifier' : $("#identifier").val(),
					'resVO.desc' : $("#resDesc").val(),
					'resVO.icon' : $("#menuIcon").attr("src"),
					"resVO.menuType" : menuType
				};
			} else {
				url = "auth-Menu-addAndAssignParent.action";
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

		if (Koala.validate($("#form"))) {
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
	}

	$.getJSON("auth-Menu-getIconNames.action", function(result) {
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
