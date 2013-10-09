<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>资源管理</title>
<%@ include file="/pages/common/header.jsp" %>
</head>
<body style="padding:10px;height:100%; text-align:center;">
   <input type="hidden" id="MenuNo" value="MemberManageUser" />
  <div id="maingrid"></div> 
  
  <div id="editUrl" style="display: none;">
	  <form id="form">
	  	<table class="form2column">
	  		<tr id="parentTR">
	  			<td class="label">父资源名称</td>
	  			<td class="content"><input type="text" id="parent" disabled="disabled" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">资源名称</td>
	  			<td class="content"><input type="text" id="name" name="name" class="input-common"  dataType="Require" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">资源标识</td>
	  			<td class="content"><input type="text" id="resUrl" name="identifier" class="input-common"  dataType="Require" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">资源描述</td>
	  			<td class="content"><input type="text" id="resDesc" name="desc"  class="input-common" /></td>
	  		</tr>
	  		<tr>
	  			<td class="label">资源类型</td>
	  			<td class="content">
	  				<select style="width:155px;height:25px" class="input-common" id="resourceType" dataType="Require">
	  					<option>--请选择--</option>
	  				</select>
	  			</td>
	  		</tr>
	  	</table>
	  </form>
  </div>
  
  <script type="text/javascript">
  $(function() {
		var editUrlDiv;
		var name = $("#name");
		var resUrl = $("#resUrl");
		var resDesc = $("#resDesc");
		var isEdit = false;
		var typeId;
		var combo;

		var toolbarOptions = {
			items : [ {
				id : "add",
				text : '增加',
				click : toolbarBtnItemClick,
				img : rootPath + "/images/icons/toolbar/add.png"
			}, {
				line : true
			}, {
				id : "modify",
				text : '修改',
				click : toolbarBtnItemClick,
				img : rootPath + "/images/icons/toolbar/page_edit.gif"
			}, {
				line : true
			}, {
				id : "remove",
				text : '删除',
				click : toolbarBtnItemClick,
				img : rootPath + "/images/icons/toolbar/page_delete.gif"
			} ]
		};

		$.getJSON(rootPath+"/auth/Resource/findAllResourceType.koala", function(result) {
			for (var i = 0; i < result.data.length; i++) {
				$("#resourceType").append('<option value="' + result.data[i].id + '">' + result.data[i].name + '</option>');
			}
		});
		
		$("#resourceType").change(function() {
			typeId = $(this).val();
		});

		// 列表结构
		var grid = $("#maingrid").ligerGrid({
			columns : [ {
				display : "资源名称",
				name : "name",
				type : "text",
				align : "left",
				id : "name"
			}, {
				display : "资源标识",
				name : "identifier",
				type : "text",
				align : "center"
			}, {
				display : "资源描述",
				name : "desc",
				type : "text",
				align : "center"
			}, {
				display : "资源类型",
				name : "typeName",
				type : "text",
				align : "center"
			} ],
			dataAction : 'server',
			pageSize : 100,
			toolbar : toolbarOptions,
			url : rootPath+'/auth/Resource/findAllReourceTree.koala',
			sortName : 'id',
			width : '98%',
			height : '100%',
			rowHeight: 28,//行默认的高度
	        headerRowHeight: 30,//表头行的高度
			heightDiff : -10,
			checkbox : true,
			autoCheckChildren : false,
			tree : {
				columnId : "name"
			}
		});

		// 工具条事件
		function toolbarBtnItemClick(item) {
			switch (item.id) {
			case "add":
				$("#parentTR").show();
				var rows = $("#maingrid").ligerGetGridManager().getCheckedRows();
				if (rows.length > 1) {
					$.ligerDialog.alert('只能选中一行!');
					return;
				}
				isEdit = false;
				$("#parent").val(rows.length == 0 ? "" : rows[0].name);
				clear();
				editUrlDiv = $.ligerDialog.open({
					title : "增加资源",
					isResize : true,
					width : 330,
					height : 250,
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
				var rows = $("#maingrid").ligerGetGridManager().getCheckedRows();
				if (rows.length > 1) {
					$.ligerDialog.alert('只能选中一行!');
					return;
				}
				if (rows.length == 0) {
					$.ligerDialog.alert('请选择行!');
					return;
				}
				selectedRow = rows[0];
				isEdit = true;
				initEditFormValue();
				editUrlDiv = $.ligerDialog.open({
					title : "修改资源",
					isResize : true,
					width : 330,
					height : 250,
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
			if (manager.getCheckedRows().length == 0) {
				$.ligerDialog.alert('请选择行!');
				return;
			}
			
			if (manager.getCheckedRows().length > 1) {
				$.ligerDialog.alert('只能选中一行!');
				return;
			}
			
			var data = "resourceVO.id=" + selectedRow.id;
			Koala.ajax({
				type : "post",
				url : rootPath+'/auth/Resource/del.koala',
				data : data,
				loading : "正在删除中...",
				success : function(msg) {
					f_reload();
				}
			});
		}

		function btnClick(button) {
			if (button.id == "save") {
				saveUrlResource();
			} else if (button.id == "cancel") {
				editUrlDiv.hidden();
			}
		}

		function initEditFormValue() {
			$("#name").val(selectedRow.name);
			$("#resUrl").val(selectedRow.identifier);
			$("#resDesc").val(selectedRow.desc);
			$("#resourceType option[value='" + selectedRow.typeId + "']").attr("selected", true);
			typeId = selectedRow.typeId;
			var rows = $("#maingrid").ligerGetGridManager().getCheckedRows();
			$("#parentTR").hide();
		}
		
		function clear() {
			$("#name").val("");
			$("#resUrl").val("");
			$("#resDesc").val("");
			$("#resourceType option[value='']").attr("selected", true);
		}

		function saveUrlResource() {
			var url;
			var data;
			var selectedRow = $("#maingrid").ligerGetGridManager().getSelectedRow();
			if (isEdit) {
				url = rootPath+"/auth/Resource/update.koala";
				data = [ {
					name : "resourceVO.id",
					value : selectedRow.id
				}, {
					name : "resourceVO.name",
					value : $("#name").val()
				}, {
					name : "resourceVO.identifier",
					value : $("#resUrl").val()
				}, {
					name : "resourceVO.desc",
					value : $("#resDesc").val()
				}, {
					name : "resourceVO.typeId",
					value : typeId
				} ];
			} else {
				if (!selectedRow) {
					url = rootPath+"/auth/Resource/add.koala";
					data = {
						'resourceVO.name' : $("#name").val(),
						'resourceVO.identifier' : $("#resUrl").val(),
						'resourceVO.desc' : $("#resDesc").val(),
						'resourceVO.typeId' : typeId
					};
				} else {
					url = rootPath+"/auth/Resource/addAndAssignParent.koala";
					data = {
						'parentVO.id' : selectedRow.id,
						'parentVO.level' : selectedRow.level,
						'childVO.name' : $("#name").val(),
						'childVO.identifier' : $("#resUrl").val(),
						'childVO.desc' : $("#resDesc").val(),
						'childVO.typeId' : typeId
					};
				}
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
