/**
 * 员工选择
 */
var selectEmployee = function(){

	var dialog = null;//对话框
    var departmentTree = null;//部门树
	var employeeGrid = null; //列表
	var selectedEmployee = null;//已选员工
	var selectedItem = {};//已选员工数据
	var init = function(hostOrgId){
		$.get( contextPath + '/pages/organisation/selectEmployeeTemplate.html').done(function(data){
			dialog = $(data);
			departmentTree = dialog.find('#departmentTree');
			employeeGrid = dialog.find('#employeeGrid');
			selectedEmployee = dialog.find('#selectedEmployee');
			dialog.find('.noDepartmentEmployee').on('click', function(){
				departmentTree.find('.tree-selected').removeClass('tree-selected');
				loadEmployeeList(0, hostOrgId);
			});
			dialog.find('#save').on('click',function(){
				var items = new Array();
				for(var prop in selectedItem){
					items.push(selectedItem[prop]);
				}
				$.ajax({
				    headers: { 
				        'Accept': 'application/json',
				        'Content-Type': 'application/json' 
				    },
				    'type': "Post",
				    'url':  contextPath + '/organization/assign-employees-to-organization.koala?organizationId='+hostOrgId,
				    'data': JSON.stringify(items),
				    'dataType': 'json'
				 }).done(function(data){
					 if(data.result == 'success'){
						 dialog.modal('hide');
						 $('body').message({
								type: 'success',
								content: '添加成功'
						 });
						 $('#department-employee-list').grid('refresh');
					 }
				 });
			}).end().modal({
					keyboard: false
				}).on({
					'shown.bs.modal': function(){
						loadDepartmentTree(hostOrgId);
					},
					'hidden.bs.modal': function(){
						$(this).remove();
					}
				});
			 //兼容IE8 IE9
	        if(window.ActiveXObject){
	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
	        	   dialog.trigger('shown.bs.modal');
	           }
	        }
		});
	};

	/**
	 * 加载部门树
	 */
	var loadDepartmentTree = function(hostOrgId){
		departmentTree.tree({
			url:  contextPath + '/organization/orgTree.koala',
			loadingHTML: '<div class="static-loader">Loading...</div>',
			multiSelect: false,
			cacheItems: true
		}).on('selected', function(event, data){
			 loadEmployeeList(data.id, hostOrgId);
		})
	};
	/**
	 * 加载员工列表
	 */
	var loadEmployeeList = function(id, hostOrgId){
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
			{ title:'职务', name:'jobName' , width: 'auto'}
		];
		if(employeeGrid.data('koala.grid')){
			employeeGrid.data('koala.grid', null);
			employeeGrid.empty();
		}
		employeeGrid.off('selectedRow') ;
		employeeGrid.grid({
			identity: 'id',
			columns: cols,
			querys: [{title: '姓名', value: 'example.name'}],
			url:  contextPath + '/employee/pagingquery-by-org.koala?organizationId='+id+'&hostOrgId='+hostOrgId
		}).on({
			'selectedRow': function(event, data){
			    var  checked = data.checked;
				var item = data.item;
				if(checked){
					if(selectedEmployee.find('[data-value="'+item.id+'"]').length == 0){
						selectedItem[item.id] = item;
						$('<div class="selected-employee" data-value="'+item.id+'">'+item.name+'<a class="glyphicon glyphicon-remove"></a></div>')
							.appendTo(selectedEmployee)
							.find('a').on('click', function(){
								delete selectedItem[item.id];
								$(this).parent().remove();
								var checkbox = employeeGrid.find('[data-role="indexCheckbox"][value="'+item.id+'"]')
								checkbox.checked && checkbox.click();
							})
					}
				}else{
					 delete selectedItem[item.id];
					 selectedEmployee.find('[data-value="'+item.id+'"]').remove();
				}
			}
		});
	};

	return {
		init: init
	};
}
