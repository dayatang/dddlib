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
	        var layout, tab, accordion;
	        //tabid计数器，保证tabid不会重复
	        var tabidcounter = 0;
	        //窗口改变时的处理函数
	        function f_heightChanged(options) {
	            if (tab)
	                tab.addHeight(options.diff);
	            if (accordion && options.middleHeight - 24 > 0)
	                accordion.setHeight(options.middleHeight - 24);
	        }
	        //增加tab项的函数
	        function f_addTab(tabid, text, url) {
	            if (!tab) {
	            	return;
	            }
	            if (!tabid)
	            {
	                tabidcounter++;
	                tabid = "tabid" + tabidcounter; 
	            }
	            tab.addTabItem({ tabid: tabid, text: text, url: url });
	            tab.reload(tabid);
	        }
	        //登录
	        function f_login()
	        {
	            Koala.login();
	        }
	        //修改密码
	        function f_changepassword()
	        {
	            Koala.changepassword();
	        }
	        //菜单初始化
	        $("ul li").live('mouseover', function ()
	        {
	            var jitem = $(this);
	            jitem.addClass("over");
	        }).live('mouseout', function ()
	        {
	            var jitem = $(this);
	            jitem.removeClass("over");
	        });

	        //布局初始化 
	        //layout
	        layout = $("#mainbody").ligerLayout({ height: '100%', heightDiff: -3, leftWidth: 200, onHeightChanged: f_heightChanged, minLeftWidth: 150 });
	        var bodyHeight = $(".l-layout-center:first").height();
	        //Tab
	        tab = $("#framecenter").ligerTab({ height: bodyHeight, contextmenu: true });


	        //预加载dialog的背景图片
	        Koala.prevDialogImage();

	        var mainmenu = $("#mainmenu");
	        
	        var manager = null;

	        $.getJSON('auth-Menu-findAllMenuByUser.action', function (menus)
	        {
	        	var topMenu = $('<ul class="sf-menu"></ul>');
	            $(menus.data).each(function (i, menu)
	            {
	                var item = $('<div title="' + menu.name + '"><ul></ul></div>');
	                var lis = $('<li><a href="#">' + menu.name + '</a></li>');
	                
	                topMenu.append(lis);
	                if(i==0)
	                {
	                	addMenuTree(menu.id,item,mainmenu);
	                }
	                	
	                $("#topmenu").append(topMenu);
	                lis.bind("click", function() {
	                	addMenuTree(menu.id,item,mainmenu);
	                });
	            });

	            $("#pageloading").hide();
	        });

	        function addMenuTree(id,item,mainmenu)
	        {
	        	$.getJSON('auth-Menu-findAllSubMenuByParent.action?resVO.id=' + id, function(submenu) {
	        		
	            	if(manager != null) {
	            		manager.clear();
	            	}
	            	var tree = $("ul:first", item).ligerTree({
	        			data:submenu.data,
	        			checkbox:false
	        		});
	        		manager = $("ul:first", item).ligerGetTreeManager(); 
	        		tree.bind("select", function(node) {
	        			if (node.data.menuType == "2") {
	             			return;
	             		}
	        			var url = node.data.identifier;
	        			var text = node.data.text;
	        			var tabid = $(node.target).attr("tabid");
	        			if (!url) {
	        				return;
	        			}
	        			if (!tabid) {
	        				tabidcounter++;
	        				tabid = "tabid" + tabidcounter;
	        				$(node.target).attr("tabid", tabid);
	        			}
	        			f_addTab(tabid, text, url);
	        		});
	        	});
	        	mainmenu.append(item);
	        }
	        
	        window["f_addTab"] = f_addTab;
		});
	</script>
</head>
<body style="text-align:center; background:#F0F0F0; overflow:hidden;">
    <div id="pageloading" style="display:block;"></div> 
    <div id="topmenu5" class="l-topmenu">
        <div class="l-topmenu-logo">Koala </div>
        <div class="l-topmenu-welcome"> 
            <span class="l-topmenu-username"><ss3:authentication property="principal.username" /></span>欢迎您  &nbsp; 
            [<a href="javascript:Koala.changepassword();">修改密码</a>] &nbsp; 
             [<a href="${pageContext.request.contextPath}/j_spring_security_logout">切换用户</a>]
            [<a href="${pageContext.request.contextPath}/j_spring_security_logout">退出</a>]
        </div>
        
    </div>
    <div id="topmenu" class="l-topmenu-index">

    </div>
     <div id="mainbody" class="l-mainbody" style="width:99.2%; margin:0 auto; margin-top:3px;" >
        <div position="left" title="系统导航" id="mainmenu">
        </div>  
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
