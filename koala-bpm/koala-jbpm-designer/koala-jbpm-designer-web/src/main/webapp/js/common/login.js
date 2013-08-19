(function($) {
	var FromUrl = getQueryStringByName("FromUrl");
	if (!FromUrl) {
		FromUrl = encodeURIComponent("index.jsp");
	}
	$(function() {
		$(".login-text").focus(function() {
			$(this).addClass("login-text-focus");
		}).blur(function() {
			$(this).removeClass("login-text-focus");
		});

		$("#btnLogin").keydown(function(e) {
			if (e.keyCode == 13) {
				dologin();
			}
		});

		$("#btnLogin").click(function() {
			dologin();
		});

		function dologin() {
			var username = $("#j_username").val();
			var password = $("#j_password").val();
			if (username == "") {
				alert('账号不能为空!');
				$("#j_username").focus();
				return;
			}
			if (password == "") {
				alert('密码不能为空!');
				$("#j_password").focus();
				return;
			}
			$.ajax({
				type : 'post',
				url : 'j_spring_security_check',
				data : [ {
					name : 'j_username',
					value : username
				}, {
					name : 'j_password',
					value : password
				} ],
				success : function(data) {
					location.href = decodeURIComponent(FromUrl);
				},
				error : function(data) {
					alert(data);
					alert('发送系统错误,请与系统管理员联系!');
				},
				beforeSend : function() {
					$.ligerDialog.waitting("正在登陆中,请稍后...");
					$("#btnLogin").attr("disabled", true);
				},
				complete : function() {
					$.ligerDialog.closeWaitting();
					$("#btnLogin").attr("disabled", false);
				}
			});
		}
	});

})(jQuery);
