$(function() {
	$("div.link").live("mouseover", function() {
		$(this).addClass("linkover");

	}).live("mouseout", function() {
		$(this).removeClass("linkover");

	}).live('click', function(e) {
		var text = $("a", this).html();
		var url = $(this).attr("url");
		parent.f_addTab(null, text, url);
	});

	$("div.link .close").live("mouseover", function() {
		$(this).addClass("closeover");
	}).live("mouseout", function() {
		$(this).removeClass("closeover");
	}).live('click', function() {
		var id = $(this).parent().attr("id");

		LG.ajax({
			loading : '正在删除用户收藏中...',
			type : 'AjaxSystem',
			method : 'RemoveMyFavorite',
			data : {
				ID : id
			},
			success : function() {
				loadMyFavorite();
			},
			error : function(message) {
				LG.showError(message);
			}
		});

		return false;
	});

	var links = $(".links");

	var now = new Date(), hour = now.getHours();
	if (hour > 4 && hour < 6) {
		$("#labelwelcome").html("凌晨好！");
	} else if (hour < 9) {
		$("#labelwelcome").html("早上好！");
	} else if (hour < 12) {
		$("#labelwelcome").html("上午好！");
	} else if (hour < 14) {
		$("#labelwelcome").html("中午好！");
	} else if (hour < 17) {
		$("#labelwelcome").html("下午好！");
	} else if (hour < 19) {
		$("#labelwelcome").html("傍晚好！");
	} else if (hour < 22) {
		$("#labelwelcome").html("晚上好！");
	} else {
		$("#labelwelcome").html("夜深了，注意休息！");
	}

	var lastlogintime = LG.cookies.get("CurrentUserLastLoginTime") || "从不";
	$("#labelLastLoginTime").html("您上次的登录时间是：" + lastlogintime);

	$("#usersetup").click(function() {
		var url = $(this).attr("url");
		if (!url) {
			return;
		}
		var text = "修改用户信息";
		parent.f_addTab(null, text, url);
	});

});