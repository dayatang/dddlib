$(function() {
	var employeeId = $('.employee-detail').parent().attr('data-value');
	$.get(contextPath + '/employee/get/' + employeeId + '.koala').done(function(result) {
		var data = result.data;
		var employeeDetail = $('.employee-detail');
		employeeDetail.find('[data-id="sn"]').html(data.sn);
		employeeDetail.find('[data-id="name"]').html(data.name);
		employeeDetail.find('[data-id="gender"]').html(data.gender == 'MALE' ? '男' : '女');
		employeeDetail.find('[data-id="idNumber"]').html(data.idNumber);
		employeeDetail.find('[data-id="organizationName"]').html(data.organizationName);
		employeeDetail.find('[data-id="postName"]').html(data.postName);
		employeeDetail.find('[data-id="mobilePhone"]').html(data.mobilePhone);
		employeeDetail.find('[data-id="familyPhone"]').html(data.familyPhone);
		employeeDetail.find('[data-id="email"]').html(data.email);
		employeeDetail.find('[data-id="entryDate"]').html(data.entryDate);
		employeeDetail.find('[data-id="additionalPostNames"]').html(data.additionalPostNames);
		employeeDetail.find('[data-id="changeDepartment"],[data-id="changeJob"]').off('click');
		employeeDetail.find('[data-id="returnBtn"]').on('click', function() {
			$('.g-mainc').find('[href="#employeeDetail"]').find('button').click();
		});
		employeeDetail.find('[data-id="changePostBtn"]').on('click', function() {
			changePost().init(employeeId);
		});
	});
});
