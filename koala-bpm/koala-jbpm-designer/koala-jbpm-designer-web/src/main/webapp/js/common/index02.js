
	//几个布局的对象
	var layout, tab, accordion;
	// tabid计数器，保证tabid不会重复
	var tabidcounter = 0;
	
	// 窗口改变时的处理函数
	function f_heightChanged(options) {
		if (tab)
			tab.addHeight(options.diff);
		if (accordion && options.middleHeight - 24 > 0)
			accordion.setHeight(options.middleHeight - 24);
	}
	
	// 增加tab项的函数
	function f_addTab(tabid, text, url) {
		if (!tab) {
			return;
		}
		if (!tabid) {
			tabidcounter++;
			tabid = "tabid" + tabidcounter;
		}
		tab.addTabItem({
			tabid : tabid,
			text : text,
			url : url
		});
	}
	// 登录
	function f_login() {
		LG.login();
	}
	// 修改密码
	function f_changepassword() {
		LG.changepassword();
	}
	
	function travelTreeData(element, menus, isTop) {
		for (var i = 0; i < menus.length; i++) {
			element.push("<li><a url=" + menus[i].url + ">" + menus[i].name + "</a>");
			if (menus[i]["children"].length > 0) {
				element.push("<ul>");
				travelTreeData(element, menus[i]["children"], false);
				element.push("</ul></li>");
			}
		}
	}
	
	layout = $("#mainbody").ligerLayout({ height: '100%', heightDiff: -3, leftWidth: 200, onHeightChanged: f_heightChanged, minLeftWidth: 150 });
    var bodyHeight = $(".l-layout-center:first").height();
    //Tab
    tab = $("#framecenter").ligerTab({ height: bodyHeight, contextmenu: true });

	var ul = $("ul.sf-menu");
	ul.superfish();
	$.getJSON("auth-Menu-findAllMenuByUser.action", function(menus) {
		var element = [];
		travelTreeData(element, menus.data, true);
		ul.append(element.join(""));
		$("ul.sf-menu li a").bind("click", function() {
			var url = $(this).attr("url");
			var text = $(this).text();
			var tabid = $(this).attr("tabid");
			if (!url) {
				return;
			}
			if (!tabid) {
				tabidcounter++;
				tabid = "tabid" + tabidcounter;
				$(this).attr("tabid", tabid);
			}
			f_addTab(tabid, text, url);
		});
	});
	
