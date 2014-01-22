var department = function(){
	var baseUrl =  contextPath + '/organization/';
	var dialog = null;    //对话框
	var departmentName = null;   //部门名称
	var departmentSN = null;    //部门编号
	var description = null;//部门描述
	var org = null;
	/*
	 *新增部门
	 */
	var addDepartment = function(id, organizationType, $element){
		$.get(contextPath + '/pages/organisation/departmentTemplate.html').done(function(data){
			init(data,  id , 'addDepartment', organizationType, $element);
		});
	};
	/*
	 *新增子公司
	 */
	var addCompany = function(id, $element){
		$.get( contextPath + '/pages/organisation/departmentTemplate.html').done(function(data){
			init(data, id , 'addCompany', null, $element);
		});
	};
	/*
	 * 修改公司信息
	 */
	var updateCompany = function(id, $element){
		$.get( contextPath + '/pages/organisation/departmentTemplate.html').done(function(data){
			init(data,  id , 'updateCompany', null, $element);
			setData(id);
		});
	};
	/*
	 * 修改部门信息
	 */
	var updateDepartment = function(id, $element){
		$.get( contextPath + '/pages/organisation/departmentTemplate.html').done(function(data){
			init(data,  id , 'updateDepartment', null, $element);
			setData(id);
		});
	};
	/*
	 删除方法
	 */
	var del = function(org, type, $element){
        delete org.title;
		var url = 'terminate-department.koala';
		if(type == 'company'){
			url = 'terminate-company.koala';
		}
		$.post(baseUrl + url, org).done(function(data){
			if(data.result == 'success'){
				$('body').message({
					type: 'success',
					content: '撤销成功'
				});
                if($element.hasClass('tree-item')){
                    $('#departmentTree').tree('removeChildren', $element);
                }else{
                    $('#departmentTree').tree('removeChildren', $element.parent());
                }
			}else{
				$('body').message({
					type: 'error',
					content: data.result
				});
			}
		}).fail(function(data){
				$('body').message({
					type: 'error',
					content: '撤销失败'
				});
			});
	};
	/**
	 * 初始化
	 */
	var init = function(data, id, type, organizationType, $element){
		dialog = $(data);
		var title = '';
		switch(type){
			case 'addDepartment':
				title = '创建下级部门';
				break;
			case 'addCompany':
				title = '创建子公司';
				break;
			case 'updateCompany':
				title = '修改机构信息';
				break;
			case 'updateDepartment':
				title = '修改机构信息';
				break;
		}
		dialog.find('.modal-header').find('.modal-title').html(title);
		departmentSN = dialog.find('#sn');
		departmentName = dialog.find('#name');
		description = dialog.find('#description');
		dialog.find('#save').on('click',function(){
			save(id, type, organizationType);
		}).end().modal({
				keyboard: false
			}).on({
				'hidden.bs.modal': function(){
					$(this).remove();
				},
				'complete': function(event, type){
					$('body').message({
						type: 'success',
						content: '保存成功'
					});
                    if(type == 'updateCompany' || type == 'updateDepartment'){
                        showDepartmentDetail(id);
                        var elementData = $element.data();
                        elementData.sn = departmentSN.val();
                        elementData.name = departmentName.val();
                        elementData.description = description.val();
                        $element.data(elementData);
                        $element.find('.tree-item-name, .tree-folder-name').html(elementData.name);
                    }else{
                        $('#departmentTree').off().empty().data('koala.tree', null)
                        getTree();
                    }
					$(this).modal('hide');
				}
			});
	};
	/*
	 *设置值
	 */
	var setData = function(id){
		$.get(baseUrl+'getOrg.koala?id='+id).done(function(data){
			org = data.org;
			departmentSN.val(org.sn);
			departmentName.val(org.name);
			description.val(org.description);
		});
	};
	/*
	 *   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(id, type, organizationType){
		if(!validate()){
			return false;
		}
		var url = '';
		switch(type){
			case 'addDepartment':
				url =  baseUrl + 'create-department.koala?parentId='+id+'&parentType='+organizationType;
				break;
			case 'addCompany':
				url =  baseUrl + 'create-company.koala?parentId='+id;
				break;
			case 'updateCompany':
				url =  baseUrl + 'update-company.koala';
				break;
			case 'updateDepartment':
				url =  baseUrl + 'update-department.koala';
				break;
		}
		var data = getAllData(id, type);
		$.post(url, data).done(function(data){
			if(data.result == 'success'){
				dialog.trigger('complete', type);
			}else{
				dialog.message({
					type: 'error',
					content: data.result
				});	
			}
		}).fail(function(data){
				dialog.message({
					type: 'error',
					content: '保存失败'
				});
		});
	};
	/*
	 *获取表单数据
	 */
	var getAllData = function(id, type){
			if(type == 'addDepartment'){
				var department = {};
				department.sn = departmentSN.val();
				department.name = departmentName.val();
				department.description = description.val();
				return department;
			}else if(type == 'addCompany'){
				var company = {};
				company.sn = departmentSN.val();
				company.name = departmentName.val();
				company.description = description.val();
				return company;
		   }else {
				org.sn = departmentSN.val();
				org.name = departmentName.val();
				org.description = description.val();
				return org;
			}
	};
	/**
	 * 表单验证 通过返回true  失败返回false
	 */
	var validate = function(){
		if(!Validation.notNull(dialog, departmentSN, departmentSN.val(), '请填写机构编号！')){
			return false;
		}
		if(!Validation.serialNmuber(dialog, departmentSN, departmentSN.val(), '请填写正确的机构编号！')){
			return false;
		}
		if(!Validation.notNull(dialog, departmentName, departmentName.val(), '请填写机构名称！')){
			return false;
		}
		return true;
	};
	/**
	 * 显示提示信息
	 */
	var showErrorMessage = function($element, content){
		$element.popover({
			content: content,
			trigger: 'manual',
			container: dialog
		}).popover('show').on({
				'blur': function(){
					$element.popover('destroy');
					$element.parent().removeClass('has-error');
				},
				'keydown': function(){
					$element.popover('destroy');
					$element.parent().removeClass('has-error');
				}
			}).focus().parent().addClass('has-error');
	};
	/**
	 * 检查变量是否不为空  true:不空   false:空
	 */
	var checkNotNull = function(item){
		//不能为空和空格
		if(item==null || item=="" || item.replace(/(^\s*)|(\s*$)/g, "")=="" ){
			return false;
		}else{
			return true;
		}
	};
	/**
	 * 生成部门树
	 */
	var getTree = function(){
        $.get(baseUrl + 'orgTree.koala').done(function(data){
            var zNodes = new Array();
            $.each(data, function(){
                var zNode = {};
                if(this.organizationType == 'company'){
                    zNode.type = 'parent';
                }else{
                    zNode.icon = 'glyphicon glyphicon-list-alt'
                }
                this.title = this.name;
                zNode.menu = this;
                if(this.children && this.children.length > 0){
                    zNode.children = getChildrenData(new Array(), this.children);
                }
                zNodes.push(zNode);
            });
            var dataSourceTree = {
                data: zNodes,
                delay: 400
            };
            $('#departmentTree').tree({
                dataSource: dataSourceTree,
                loadingHTML: '<div class="static-loader">Loading...</div>',
                multiSelect: false,
                cacheItems: true
            }).on({
                    'contextmenu': function(e){
                        return false;
                    },
                    'rightClick': function(e, originalEvent){
                        createRightMenu(originalEvent);
                    },
                    'selectParent': function(event, data){
                        showDepartmentDetail(data.data.id);
                    },
                    'selectChildren': function(event, data){
                        showDepartmentDetail(data.id);
                    },
                    'addCompany': function(event, data){
                        var $element = $(data);
                        var data = $element.data();
                        addCompany(data.id, $element);
                    },
                    'addDepartment': function(event, data){
                        var $element = $(data);
                        var data = $element.data();
                        addDepartment(data.id, data.organizationType, $element);
                    },
                    'update': function(event, data){
                        var $element = $(data);
                        var data = $element.data();
                        if(data.organizationType == 'company'){
                            updateCompany(data.id, $element);
                        }else{
                            updateDepartment(data.id, $element);
                        }
                    },
                    'delete': function(event, data){
                        var $element = $(data);
                        var data = $element.data();
                        $(this).confirm({
                            content: '确定要撤销该机构吗?',
                            callBack: function(){
                                var type = data.organizationType;
                                delete data.children;
                                delete data.organizationType;
                                department().del(data, type, $element);
                            }
                        });
                    }
                }).find('.tree-folder-header').click();
        });
	};
    var createRightMenu = function(ev){
        var $element = $(ev.currentTarget);
        var menuData = null;
        menuData = [
            {title:'创建分公司', action: 'addCompany'},
            {title:'创建下级部门', action: 'addDepartment'},
            {title:'修改机构信息', action: 'update'},
            {title:'撤销', action: 'delete'}
        ];
        if($element.data('organizationType') == 'company'){
            menuData = [
                {title:'创建分公司', action: 'addCompany'},
                {title:'创建下级部门', action: 'addDepartment'},
                {title:'修改机构信息', action: 'update'},
                {title:'撤销', action: 'delete'}
            ];
        }else{
            menuData = [
                {title:'创建下级部门', action: 'addDepartment'},
                {title:'修改机构信息', action: 'update'},
                {title:'撤销', action: 'delete'}
            ];
        }
        $('#departmentTree').tree('createRightMenu', {event:ev, element: $element, data:menuData});
    };

    var getChildrenData = function(nodes, items){
        $.each(items, function(){
            var zNode = {};
            if(this.organizationType == 'company'){
                zNode.type = 'parent';
            }else{
                zNode.icon = 'glyphicon glyphicon-list-alt'
            }
            this.title = this.name;
            zNode.menu = this;
            if(this.children && this.children.length > 0){
                zNode.children = getChildrenData(new Array(), this.children);
            }
            nodes.push(zNode);
        });
        return nodes;
    };

	var showDepartmentDetail =  function(id){
		$.get(contextPath + '/organization/getOrg.koala?id='+id).done(function(data){
			var org = data.org;
			var departmentDetail = $('.right-content');
			departmentDetail.find('[data-role="id"]').val(org.id);
			departmentDetail.find('[data-role="number"]').html(org.sn);
			departmentDetail.find('[data-role="name"]').html(org.name);
			departmentDetail.find('[data-role="description"]').html(org.description);
			departmentDetail.find('[data-role="principalName"]').html(org.principalName);
			departmentDetail.find('[data-role="organizationType"]').val(org.organizationType);
			//loadEmployeeList(org.id);
			if(org.organizationType == 'company'){
				$('#addCompany').show();
				$('#updateCompany').show();
				$('#updateDepartment').hide();
			}else{
				$('#addCompany').hide();
				$('#updateCompany').hide();
				$('#updateDepartment').show();
			}
		}).fail(function(data){
				$(window).message({
					type: 'error',
					content: '无法获取该部门信息'
				});
			});
	};
	var showEmployeeList = function(id){
		$.get( contextPath + '/pages/organisation/departmentEmployeeList.html').done(function(data){
			var employeeListDialog = $(data);

			employeeListDialog.find('#deleteRelation').on('click',function(){
				var grid = employeeListDialog.find('#employeeList');
				deleteEmployeeRelation(id, grid.data('koala.grid').selectedRows() , grid);
			}).end().modal({
					keyboard: false
				}).on({
					'shown.bs.modal': function(){
						loadEmployeeList(employeeListDialog,id);
					},
					'hidden.bs.modal': function(){
						$(this).remove();
					}
				});
                //兼容IE8 IE9
                if(window.ActiveXObject){
                   if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
                       employeeListDialog.trigger('shown.bs.modal');
                   }
                }
		});
	};
	var loadEmployeeList = function(employeeListDialog, id){
		var cols = [
			{ title:'编号', name:'sn' , width: '150px'},
			{ title:'姓名', name:'name', width: '150px'},
			{ title:'性别', name:'gender' , width: '100px',
				render: function(item, name, index){
					if(item[name] == 'FEMALE'){
						return '女';
					}else{
						return '男';
					}
				}
			},
			{ title:'岗位', name:'postName' , width: 'auto'}
		];
		var buttons = [{content: '<div class="queryAllChildren"><div class="checker" id="queryAllChildren"><span></span></div>&nbsp;显示下属机构员工</div>', action: 'queryAllChildren'}];
		var  departmentEmployeeGrid = employeeListDialog.find('#employeeList');
		departmentEmployeeGrid.grid({
			identity: 'id',
			columns: cols,
			buttons: buttons,
			querys: [{title: '姓名', value: 'example.name'}],
			url:  contextPath + '/employee/pagingquery-by-org.koala?organizationId='+id
		});
		departmentEmployeeGrid.find('#queryAllChildren').on('click', function(){
			var $span = $(this).find('span');
			departmentEmployeeGrid.data('koala.grid').search({queryAllChildren: !$span.hasClass('checked')});
			$span.toggleClass('checked');
		});
	};
	var deleteEmployeeRelation = function(id, items, grid){
		if(items.length == 0){
			$('body').message({
				type: 'warning',
				content: '请选择要解除关系的员工'
			});
			return;
		}
		grid.confirm({
            backdrop: false,
			content: '确定要与所选员工解除关系吗?',
			callBack: function(){
				$.ajax({
					headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json'
					},
					'type': "Post",
					'url':  contextPath + '/organization/terminate_eoRelations.koala?organizationId=' + id,
					'data': JSON.stringify(items),
					'dataType': 'json'
				}).done(function(data){
						if(data.result == 'success'){
							$('body').message({
								type: 'success',
								content: '解除关系成功'
							});
							grid.grid('refresh');
						}else{
							$('body').message({
								type: 'error',
								content: data.result
							});
						}
					});
			}
		});
	};
	return {
		addDepartment: addDepartment,
		addCompany: addCompany,
		updateDepartment: updateDepartment,
		updateCompany: updateCompany,
		del: del,
		getTree: getTree,
		showEmployeeList:showEmployeeList
	};
};
