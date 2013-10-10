$(function() {
	var userDialog;
	var assignRolesDialog;

	var userName = $("#userName");
	var userDescription = $("#userDescription");
	var userAccount = $("#userAccount");
	var password = $("#password");
	var verifyPassword = $("#verifyPassword");

	var isEdit = false;
	var selectedRow;
	var roleId = $("#roleId").val();

	var assignUsersToRoleDialog;
	var searchTest = 'no condition.';
	
	var valid = true;
	var editValid = true;

	// 工具条
	var toolbarOptions = initToolbarOptions();
	
	// 用户列表结构
	var grid = $("#maingrid").ligerGrid(
			{
				columns : [ {
					display : "用户名称",
					name : "name",
					width : 100,
					type : "text",
					align : "left"
				}, {
					display : "用户帐号",
					name : "userAccount",
					width : 100,
					type : "text",
					align : "left"
				}, {
					display : "用户描述",
					name : "userDesc",
					width : 200,
					type : "text",
					align : "left"
				}, {
					display : "是否有效",
					name : "valid",
					width : 50,
					type : "text",
					align : "left",
					render : function(rowdata) {
						return rowdata.valid == true ? "是" : "否";
					}
				}, {
					display : "最后登录时间",
					name : "lastLoginTime",
					width : 100,
					type : "text",
					align : "left"
				}, {
					display : "最后更改时间",
					name : "lastModifyTime",
					width : 100,
					type : "text",
					align : "left"
				} ],
				dataAction : 'server',
				toolbar : toolbarOptions,
				rownumbers : true,
				url : 'auth-User-pageJson.action?roleId=' + roleId
						+ '&searchTest=' + searchTest,
				sortName : 'id',
				width : '98%',
				height : '100%',
				rowHeight: 28,//行默认的高度
		        headerRowHeight: 30,//表头行的高度
				heightDiff : -10,
				checkbox : true
			});

	if (roleId > 0) {
		$("#search").hide();
	}

	function initToolbarOptions() {
		if (roleId > 0) {
			return {
				items : [ {
					id : "add",
					text : '分配用户',
					click : toolbarBtnItemClick,
					img : "images/icons/toolbar/add.png"
				}, {
					line : true
				}, {
					id : "remove",
					text : '删除',
					click : toolbarBtnItemClick,
					img : "images/icons/toolbar/page_delete.gif"
				} ]
			};
		} else {
			return {
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
					id : "assignRoles",
					text : '角色',
					click : toolbarBtnItemClick,
					img : "images/icons/toolbar/page_edit.gif"
				} ]
			};
		}
	}

	// 工具条事件(添加，编辑，删除，分配角色)
	function toolbarBtnItemClick(item) {
		switch (item.id) {
		case "add":
			if (roleId > 0) {
				openAssignUserToRoleDialog();
			} else {
				openAddUserDialog();
			}
			break;
		case "modify":
			if (selectRow()) {
				isEdit = true;
				initEditFormValue();
				userDialog = $.ligerDialog.open({
					title : "用户",
					isResize : true,
					width : 400,
					height : 300,
					isHidden : true,
					rownumbers : true,
					buttons : [ {
						id : 'save',
						text : '保存',
						onclick : btnClick
					}, {
						id : 'cancel',
						text : '取消',
						onclick : btnClick
					} ],
					target : $("#editUser")
				});
			}
			break;
		case "remove":
			jQuery.ligerDialog.confirm('确定删除吗?', function(confirm) {
				if (confirm)
					f_delete();
			});
			break;
		case "assignRoles":
			if (selectRow()) {
				top.f_addTab(null, selectedRow.name + '的角色管理',
						'auth-Role-list.action?userAccount='
								+ selectedRow.userAccount + '&userId='
								+ selectedRow.id);
			}
			break;
		}
	}

	function selectRow() {
		var manager = $("#maingrid").ligerGetGridManager();
		selectedRow = manager.getSelectedRow();

		if (!selectedRow) {
			$.ligerDialog.alert("请选择行");
			return false;
		}

		if (grid.getCheckedRows().length > 1) {
			$.ligerDialog.alert("只能选中一个");
			return false;
		}

		return true;
	}

	// 打开分配用户给角色的对话框
	function openAssignUserToRoleDialog() {
		assignUsersToRoleDialog = $.ligerDialog.open({
			title : "用户",
			isResize : true,
			width : 480,
			height : 520,
			isHidden : true,
			rownumbers : true,
			buttons : [ {
				id : 'assignUsersToRole',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancelAssignUsersToRole',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#assignUserToRoleDialog")
		});
		loadUsers();
	}

	var userGrid;

	// 待选用户列表结构
	function loadUsers() {
		userGrid = $("#assignUsersToRole").ligerGrid({
			columns : [ {
				display : "用户名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "用户帐号",
				name : "userAccount",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "是否有效",
				name : "valid",
				width : 50,
				type : "text",
				align : "left",
				render: function(rowdata) {
					return rowdata.valid == true ? "是" : "否";
				}
			} ],
			pageSize : 15,
			rownumbers : true,
			url : 'auth-User-queryNotAssignUserByRole.action?roleId=' + roleId,
			width : 450,
			height : 430,
			checkbox : true
		});
	}

	// 打开添加用户对话框
	function openAddUserDialog() {
		isEdit = false;
		userDialog = $.ligerDialog.open({
			title : "用户",
			isResize : true,
			width : 400,
			height : 300,
			isHidden : true,
			rownumbers : true,
			buttons : [ {
				id : 'save',
				text : '保存',
				onclick : btnClick
			}, {
				id : 'cancel',
				text : '取消',
				onclick : btnClick
			} ],
			target : $("#addUser")
		});
	}

	function f_reload() {
		grid.loadData();
	}

	// 删除用户
	function f_delete() {
		var manager = $("#maingrid").ligerGetGridManager();
		selectedRow = manager.getSelectedRow();
		if (selectedRow) {
			removeUser();
		} else {
			Koala.tip('请选择行!');
		}
	}

	// 删除用户
	function removeUser() {
		var removeUrl;
		var removeData;
		if (roleId > 0) {
			var data = '{"roleId": "' + roleId + '", ';
			$.each(grid.getCheckedRows(), function(index, element) {
				data = data + '"users[' + index + '].id": "' + element.id
						+ '",';
			});
			data = data.substr(0, data.length - 1) + '}';

			removeUrl = 'auth-User-removeUserForRole.action';
			removeData = $.parseJSON(data);
		} else {
			var data = '{';
			$.each(grid.getCheckedRows(), function(index, element) {
				data = data + '"users[' + index + '].id": "' + element.id
						+ '",' + '"users[' + index + '].userAccount": "' + element.userAccount + '",';
				
			});
			data = data.substr(0, data.length - 1) + '}';

			removeUrl = 'auth-User-del.action';
			removeData = $.parseJSON(data);
		}

		$.ajax({
			type : 'post',
			cache : false,
			dataType : 'json',
			url : removeUrl,
			loading : '正在删除中...',
			data : removeData,
			success : function(data) {
				if (data.result == "success") {
					grid.loadData();
					Koala.showSuccess('删除成功');
					return;
				} else {
					Koala.showError('删除出错,请与系统管理员联系!');
				}
			},
			error : function(message) {
				Koala.showError('删除成功');
			}
		});
	}

	// 点击弹出框按钮触发的时间
	function btnClick(button) {
		if (button.id == "save") {
			saveUser();
		} else if (button.id == "cancel") {
			closeAddDialog();
		} else if (button.id == "assignUsersToRole") {
			assignUsers();
		} else if (button.id == "cancelAssignUsersToRole") {
			assignUsersToRoleDialog.hidden();
		}
	}

	// 初始化编辑对话框的表单值
	function initEditFormValue() {
		$("#editUserName").val(selectedRow.name);
		$("#editUserDescription").val(selectedRow.userDesc);
		$("#editUserAccount").val(selectedRow.userAccount);
		if (isEdit) {
			$("#passwordTR").hide();
			$("#editPassword").attr("dataType", "");
		}
		$("#editValid").attr("checked", selectedRow.valid);
	}

	// 保存用户信息
	function saveUser() {
		var url;
		var data;
		var form;
		if (isEdit) {
			url = "auth-User-update.action";
			form = document.getElementById("editform");
			data = [ {
				name : 'userVO.id',
				value : selectedRow.id
			}, {
				name : 'userVO.name',
				value : $("#editUserName").val()
			}, {
				name : 'userVO.userAccount',
				value : selectedRow.userAccount
			}, {
				name : 'userVO.userDesc',
				value : $("#editUserDescription").val()
			}, {
				name : 'userVO.valid',
				value : editValid
			} ];
		} else {
			url = "auth-User-add.action";
			form = document.getElementById("addform");
			data = [ {
				name : 'userVO.name',
				value : userName.val()
			}, {
				name : 'userVO.userDesc',
				value : userDescription.val()
			}, {
				name : 'userVO.userAccount',
				value : userAccount.val()
			}, {
				name : 'userVO.userPassword',
				value : password.val()
			}, {
				name : 'userVO.valid',
				value : valid
			} ];
		}

		if(!Validator.Validate(form,3))return;
		Koala.ajax({
			url: url,
			data: data,
			success: function() {
				grid.loadData();
				closeAddDialog();
				$.ligerDialog.alert("保存成功！");
			}
		});
	}

	function closeAddDialog() {
		$("form").each(
				function() {
					$(this).find("input").each(
							function() {
								$(this).val("");
								$(this).parents("td:first").next("td").find(
										"div.l-exclamation").remove();
							});
				});
		userDialog.hidden();
	}

	// 给用户分配角色
	function assignUsers() {
		var data = '{ "roleVO.id": "' + roleId + '",';
		$.each(userGrid.getCheckedRows(), function(index, element) {
			data = data + '"users[' + index + '].id": "' + element.id + '",';
		});

		data = data.substr(0, data.length - 1) + '}';

		$.ajax({
			type : 'post',
			cache : false,
			dataType : 'json',
			url : 'auth-Role-assignUsers.action',
			data : $.parseJSON(data),
			success : function(data) {
				if (data.result == "success") {
					grid.loadData();
					Koala.tip('分配用户成功!');
				} else {
					alert('分配用户出错,请与系统管理员联系!');
				}
				assignUsersToRoleDialog.hidden();
			},
			error : function() {
				alert('分配用户出错,请与系统管理员联系!');
			}
		});
	}
	
	$("#valid").click(function() {
		valid = $(this).attr("checked") == "checked" ? true : false;
	});
	
	$("#editValid").click(function() {
		editValid = $(this).attr("checked") == "checked" ? true : false;
	});

	// 查询
	$("#searchButton").click(function() {
		grid = $("#maingrid").ligerGrid({
			columns : [ {
				display : "用户名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "用户帐号",
				name : "userAccount",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "用户描述",
				name : "userDesc",
				width : 200,
				type : "text",
				align : "left"
			}, {
				display : "是否有效",
				name : "valid",
				width : 50,
				type : "text",
				align : "left",
				render : function(rowdata) {
					return rowdata.valid ? "是" : "否";
				}
			}, {
				display : "最后登录时间",
				name : "lastLoginTime",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "最后更改时间",
				name : "lastModifyTime",
				width : 100,
				type : "text",
				align : "left"
			} ],
			dataAction : 'server',
			pageSize : 20,
			toolbar : toolbarOptions,
			url : "auth-User-query.action",
			sortName : 'id',
			rownumbers : true,
			width : '98%',
			height : '100%',
			heightDiff : -10,
			checkbox : true,
			parms : [ {
				name : "userNameForSearch",
				value : $("#userNameForSearch").val()
			}, {
				name : "userAccountForSearch",
				value : $("#userAccountForSearch").val()
			} ]
		});
	});
	
	$("#dialogSearchButton").click(function() {
		dialogSearch();
	});

	// 对话框查询
	function dialogSearch() {
		userGrid = $("#assignUsersToRole").ligerGrid({
			columns : [ {
				display : "用户名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "用户帐号",
				name : "userAccount",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "是否有效",
				name : "valid",
				width : 50,
				type : "text",
				align : "left",
				render : function(rowdata) {
						return rowdata.valid == true ? "是" : "否";
					}
			}],
			pageSize : 15,
			rownumbers : true,
			url : "auth-User-queryUsersForAssign.action",
			sortName : 'id',
			width : 450,
			height : 430,
			checkbox : true,
			parms : [ {
				name : "roleId",
				value : roleId
			}, {
				name : "userNameForSearch",
				value : $("#dialogUserNameForSearch").val()
			}, {
				name : "userAccountForSearch",
				value : $("#dialogUserAccountForSearch").val()
			} ]
		});
	}
});