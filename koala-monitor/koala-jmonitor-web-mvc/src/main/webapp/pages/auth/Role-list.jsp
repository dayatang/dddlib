<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>角色管理</title>
<%@ include file="/pages/common/header.jsp" %>
</head>
<body style="padding:10px;height:100%; text-align:center;">
  <input type="hidden" id="userId" value="${userId}" />
  <input type="hidden" id="userAccount" value="${userAccount}" />
  
  <div id="search" class="searchtitle">
  	角色名称：<input id="roleNameForSearch" type="text" class="input-common" />
    <input id="searchButton" type="button" class="btn-normal" value="查询" />
  </div>
  
  <div id="maingrid"></div> 
  
  <div id="editRole" style="display: none;">
	  <form id="form">
	  	<table class="form2column">
	  		<tr>
	  			<td class="label">角色名称</td>
	  			<td class="content"><input type="text" id="roleName" name="roleName" class="input-common" dataType="Require"  /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">角色描述</td>
	  			<td class="content"><input type="text" id="roleDescription" class="input-common" name="roleDescription" /></td>
	  		</tr>
	  	</table>
	  </form>
  </div>
  
  <div id="treeDiv">
  	<ul id="menuTree">
  	</ul>
  </div>

	<div id="assignRolesToUserDialog" style="display: none;">
	  <div id="dialogSearch">
	  	角色名称：<input id="dialogRoleNameForSearch" type="text" />
	   	<input id="dialogSearchButton" type="button" value="查询" />
	  </div>
	  <div id="assignRolesToUser">
	  </div>
	</div>
  <div id="urlDiv">
	 <div id="urlResource"></div>
  </div>
  
  <script type="text/javascript">
  $(function() {
		var editRoleDiv;
		var assignUsersDialog;
		var roleName = $("#roleName");
		var roleDescription = $("#roleDescription");
		var isEdit = false;

		var selectedRow;
		var resources = [];

		var userId = $("#userId").val();
		var userAccount = $("#userAccount").val();
		var assignRolesToUserDialog;

		// 工具条
		var toolbarOptions = initToolbarOptions();

		// 列表结构
		var grid = $("#maingrid").ligerGrid({
			columns : [ {
				display : "角色名称",
				name : "name",
				width : 100,
				type : "text",
				align : "left"
			}, {
				display : "角色描述",
				name : "roleDesc",
				width : 200,
				type : "text",
				align : "left"
			} ],
			dataAction : 'server',
			pageSize : 20,
			toolbar : toolbarOptions,
			url : '/auth/Role/pageJson.koala',
			sortName : 'id',
			rownumbers : true,
			width : '98%',
			height : '100%',
			rowHeight: 28,//行默认的高度
	        headerRowHeight: 30,//表头行的高度
			heightDiff : -10,
			checkbox : true,
			parms : [ {
				name : "userAccount",
				value : userAccount
			} ]
		});

		if (userId > 0) {
			$("#search").hide();
		}

		function initToolbarOptions() {
			if (userId > 0) {
				return {
					items : [ {
						id : "add",
						text : '分配角色',
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
						id : "user",
						text : '用户',
						click : toolbarBtnItemClick,
						img : "images/icons/toolbar/page_edit.gif"
					}, {
						line : true
					}, {
						id : "menuResource",
						text : '资源授权',
						click : toolbarBtnItemClick,
						img : "images/icons/toolbar/page_edit.gif"
					} ]
				};
			}
		}

		// 工具条事件
		function toolbarBtnItemClick(item) {
			switch (item.id) {
			case "add":
				if (userId > 0) {
					openAssignRolesToUserDialog();
				} else {
					openAddRoleDialog();
				}
				break;
			case "modify":
				if (selectRow()) {
					isEdit = true;
					initEditFormValue();
					editRoleDiv = $.ligerDialog.open({
						title : "角色",
						isResize : true,
						width : 300,
						height : 150,
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
						target : $("#editRole")
					});
				}
				break;
			case "remove":
				jQuery.ligerDialog.confirm('确定删除吗?', function(confirm) {
					if (confirm)
						f_delete();
				});
				break;
			case "user":
				if (selectRow()) {
					top.f_addTab(null, selectedRow.name + '的用户管理',
							'/auth/User/list.koala?roleId=' + selectedRow.id);
				}
				break;
			case "menuResource":
				if (selectRow()) {
					initMenuTree();
				}
				break;
			case "urlResource":
				if (selectRow()) {
					initResource();
				}
				break;
			}
		}

		// 打开给用户分配角色的对话框
		function openAssignRolesToUserDialog() {
			assignRolesToUserDialog = $.ligerDialog.open({
				title : "角色",
				isResize : true,
				width : 480,
				height : 520,
				isHidden : true,
				buttons : [ {
					id : 'assignRolesToUser',
					text : '保存',
					onclick : btnClick
				}, {
					id : 'cancelAssignRolesToUser',
					text : '取消',
					onclick : btnClick
				} ],
				target : $("#assignRolesToUserDialog")
			});
			loadRoles();
		}

		// 待选角色列表结构
		function loadRoles() {
			roleGrid = $("#assignRolesToUser").ligerGrid({
				columns : [ {
					display : "角色名称",
					name : "name",
					width : 100,
					type : "text",
					align : "left"
				}, {
					display : "角色描述",
					name : "roleDesc",
					width : 200,
					type : "text",
					align : "left"
				} ],
				pageSize : 10,
				url : '/auth/Role/queryNotAssignRoleByUser.koala?userId=' + userId,
				sortName : 'id',
				rownumbers : true,
				width : 450,
				height : 430,
				checkbox : true
			});
		}

		// 打开添加角色对话框
		function openAddRoleDialog() {
			isEdit = false;
			editRoleDiv = $.ligerDialog.open({
				title : "角色",
				isResize : true,
				width : 300,
				height : 150,
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
				target : $("#editRole")
			});
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

		// 初始化菜单树
		function initMenuTree() {
			var manager = $("#menuTree").ligerGetTreeManager();
			if (manager != null) {
				manager.clear();
			}
			$.getJSON('/auth/Menu/findMenuTreeSelectItemByRole.koala?time=' + new Date().getTime() + '&roleVO.id='+ selectedRow.id, function(menus) {
				var menuDialog = $.ligerDialog.open({
					width : 400,
					// 获取所有菜单数据
					height: 300,
					target : $("#treeDiv"),
					title : "为【" + selectedRow.name + "】分配菜单资源",
					content : $("#menuTree").ligerTree({
						data : menus.data
					}),
					buttons : [{
						text : "确定",
						onclick : function() {
							var manager = $("#menuTree").ligerGetTreeManager();
							var nodes = manager.getChecked();
							var postData = ["roleVO.id=" + selectedRow.id];
							for ( var i = 0; i < nodes.length; i++) {
								postData.push("menus[" + i + "].id=" + nodes[i].data.id);
							}
							$.ajax({
								type : "post",
								url : "/auth/Role/assignMenuResources.koala",
								data : postData.join("&"),
								success : function(msg) {
									if (msg.result == "success") {
										$.ligerDialog.alert("分配成功");
										menuDialog.hide();
									} else if (msg.result == "error") {
										$.ligerDialog.alert("分配失败");
										menuDialog.hide();
									}
								}
							});
						}
					}, {
						text : "取消",
						onclick : function() {
							menuDialog.hide();
						}
					} ]
				});
			});
		}

		// 初始化URL资源
		function initResource() {
			top.f_addTab(null, '角色资源', '/auth/Role/resourceList.koala?roleVO.id=' + selectedRow.id);
		}

		// 获取树的节点个数
		function getNodeCount() {
			return $(".l-tree .l-body span").size();
		}

		// 获取某个节点的文本内容
		function getNodeText(index) {
			return $($(".l-tree .l-body span").get(index)).text();
		}

		function f_reload() {
			grid.loadData();
		}
		function f_delete() {
			var manager = $("#maingrid").ligerGetGridManager();
			selectedRow = manager.getSelectedRow();
			if (selectedRow) {
				removeRole();
			} else {
				Koala.tip('请选择行!');
			}
		}

		// 删除角色
		function removeRole() {
			var removeUrl;
			var removeData;
			if (userId > 0) {
				var data = '{"userId": "' + userId + '", ';
				$.each(grid.getCheckedRows(), function(index, element) {
					data = data + '"roles[' + index + '].id": "' + element.id
							+ '",';
				});
				data = data.substr(0, data.length - 1) + '}';

				removeUrl = '/auth/Role/removeRoleForUser.koala';
				removeData = $.parseJSON(data);
			} else {
				var data = '{';
				$.each(grid.getCheckedRows(), function(index, element) {
					data = data + '"roles[' + index + '].id": "' + element.id
							+ '",';
				});
				data = data.substr(0, data.length - 1) + '}';
				removeUrl = '/auth/Role/del.koala';
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
					Koala.showError('删除出错,请与系统管理员联系!');
				}
			});
		}

		function btnClick(button) {
			if (button.id == "save") {
				saveRole();
			} else if (button.id == "cancel") {
				closeEditDiv();
			} else if (button.id == "assignRolesToUser") {
				assignRoles();
			} else if (button.id == "cancelAssignRolesToUser") {
				assignRolesToUserDialog.hidden();
			}
		}

		function initEditFormValue() {
			roleName.val(selectedRow.name);
			roleDescription.val(selectedRow.roleDesc);
		}

		function saveRole() {
			var url;
			var data;
			if (isEdit) {
				url = "/auth/Role/update.koala";
				data = [ {
					name : 'roleVO.id',
					value : selectedRow.id
				},{
					name : 'roleVO.name',
					value : roleName.val()
				}, {
					name : 'roleVO.roleDesc',
					value : roleDescription.val()
				}];
			} else {
				url = "/auth/Role/add.koala";
				data = [ {
					name : 'roleVO.name',
					value : roleName.val()
				}, {
					name : 'roleVO.roleDesc',
					value : roleDescription.val()
				} ];
			}

			if(!Validator.Validate(document.getElementById("form"),3))return;
			Koala.ajax({
				url: url,
				data: data,
				success: function() {
					closeEditDiv();
					grid.loadData();
					$.ligerDialog.alert("保存成功");
				}
			});
		
		}

		function closeEditDiv() {
			$("#editRole :text").each(
					function() {
						$(this).val("");
						$(this).parents("td:first").next("td").find(
								"div.l-exclamation").remove();
					});
			editRoleDiv.hidden();
		}

		// 给用户分配角色
		function assignRoles() {
			var data = '{ "userVO.id": "' + userId + '",';
			$.each(roleGrid.getCheckedRows(), function(index, element) {
				data = data + '"roles[' + index + '].id": "' + element.id + '",';
			});

			data = data.substr(0, data.length - 1) + '}';

			$.ajax({
				type : 'post',
				cache : false,
				dataType : 'json',
				url : '/auth/User/assignRoles.koala',
				data : $.parseJSON(data),
				success : function(data) {
					if (data.result == "success") {
						grid.loadData();
						Koala.tip('分配角色成功!');
					} else {
						alert('分配角色出错,请与系统管理员联系!');
					}
					assignRolesToUserDialog.hidden();
				},
				error : function() {
					alert('分配角色出错,请与系统管理员联系!');
				}
			});
		}
		
		$("#searchButton").click(function() {
			search();
		});

		// 查询
		function search() {
			grid = $("#maingrid").ligerGrid({
				columns : [ {
					display : "角色名称",
					name : "name",
					width : 100,
					type : "text",
					align : "left"
				}, {
					display : "角色描述",
					name : "roleDesc",
					width : 200,
					type : "text",
					align : "left"
				} ],
				dataAction : 'server',
				pageSize : 20,
				toolbar : toolbarOptions,
				url : '/auth/Role/query.koala',
				sortName : 'id',
				rownumbers : true,
				parms : [ {
					name : "roleNameForSearch",
					value : $("#roleNameForSearch").val().trim()
				} ],
				width : '98%',
				height : '100%',
				heightDiff : -10,
				checkbox : true
			});
		}
		
		$("#dialogSearchButton").click(function() {
			dialogSearch();
		});

		// 对话框查询
		function dialogSearch() {
			roleGrid = $("#assignRolesToUser").ligerGrid({
				columns : [ {
					display : "角色名称",
					name : "name",
					width : 100,
					type : "text",
					align : "left"
				}, {
					display : "角色描述",
					name : "roleDesc",
					width : 200,
					type : "text",
					align : "left"
				} ],
				pageSize : 10,
				rownumbers : true,
				url : '/auth/Role/queryRolesForAssign.koala',
				parms : [ {
					name : "userId",
					value : userId
				}, {
					name : "userAccount",
					value : userAccount
				}, {
					name : "roleNameForSearch",
					value : $("#dialogRoleNameForSearch").val().trim()
				} ],
				sortName : 'id',
				width : 450,
				height : 430,
				checkbox : true
			});
		}
	});
  </script>
</body>
</html>
