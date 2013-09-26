<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>欢迎使用Koala</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/auth-index.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/auth/changepassword.js"></script>
	<script type="text/javascript">
		$(function() {

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
				tab.reload(tabid);
			}
			// 登录
			function f_login() {
				Koala.login();
			}
			// 修改密码
			function f_changepassword() {
				Koala.changepassword();
			}
			
			function travelTreeData(element, menus, isTop) {
				for (var i = 0; i < menus.length; i++) {
					element.push("<li><a url=" + menus[i].identifier + ">" + menus[i].name + "</a>");
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
			
			window["f_addTab"] = f_addTab;
		});
	</script>
</head>
<body>
	<div id="topmenu5" class="l-topmenu">
        <div class="l-topmenu-logo">Koala </div>
        <div class="l-topmenu-welcome"> 
            <span class="l-topmenu-username"><ss3:authentication property="principal.username" /></span>欢迎您  &nbsp; 
            [<a href="javascript:Koala.changepassword()">修改密码</a>] &nbsp; 
             [<a href="${pageContext.request.contextPath}/j_spring_security_logout">切换用户</a>]
            [<a href="${pageContext.request.contextPath}/j_spring_security_logout">退出</a>]
        </div>
    </div>
    <div id="topmenu" class="l-topmenu-index">
			<ul class="sf-menu">
			</ul>
    </div>
    <div id="mainbody" class="l-mainbody" style="width:99.2%; margin:0 auto; margin-top:3px;" >
        <div position="center" id="framecenter"> 
            <div tabid="home" title="我的主页"> 
                <iframe frameborder="0" name="home" id="home" src="${pageContext.request.contextPath}/pages/common/welcome.jsp"></iframe>
            </div> 
        </div> 
    </div>
     <form id="changepasswordPanel" style="display:none;">
		<table cellpadding="0" cellspacing="0" class="form2column" >
			<tr>
				<td class="label">旧密码:</td>
				<td class="content">
					<input name="oldPassword" type="password" id="oldPassword" class="input-common" validate="{maxlength: 50, required: true}" />
				</td>
			</tr>
			<tr>
				<td class="label">新密码:</td>
				<td class="content">
					<input name="newPassword" type="password" id="newPassword" class="input-common" validate="{maxlength: 50, required: true}" />
				</td>
			</tr>
			<tr>
				<td class="label">确认密码:</td>
				<td class="content">
					<input name="confirmPassword" type="password" id="confirmPassword" class="input-common" validate="{maxlength: 50, required: true, equalTo: '#newPassword', messages: { required: '请输入密码', equalTo: '两次密码输入不一致'}}" />
				</td>
			</tr>
		</table>
	</form>
    <script src="js/common/superfish.js" type="text/javascript"></script>
</body>
</html>
