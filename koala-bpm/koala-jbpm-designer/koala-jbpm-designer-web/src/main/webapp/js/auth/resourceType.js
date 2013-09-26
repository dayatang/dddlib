$(function() {

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
		url : 'auth-ResourceType-pageJson.action',
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
				url : 'auth-ResourceType-delete.action',
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
			url = "auth-ResourceType-update.action";
			data = {
				"resourceTypeVO.name": $("#name").val(),
				"resourceTypeVO.id" : selectedRow.id
			};
		} else {
			url = "auth-ResourceType-save.action";
			data = {
				"resourceTypeVO.name": $("#name").val()
			};
		}

		var valid = Koala.validate($("#form"));
		if (valid) {
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
	}
});
