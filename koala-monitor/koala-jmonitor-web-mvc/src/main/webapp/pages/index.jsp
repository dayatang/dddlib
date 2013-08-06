<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>欢迎使用Koala</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/auth-index.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/auth/changepassword.js"></script>
<script type="text/javascript">
//父窗之上的弹出框
var topDialog;

var tab = null;
var accordion = null;
//tabid计数器，保证tabid不会重复
var tabidcounter = 0;
$(function() {
	// 布局
	$("#main-content").ligerLayout({
		leftWidth : 190,
		height : '100%',
		heightDiff : -34,
		space : 4,
		onHeightChanged : layoutHeiheightChangeEvent
	});

	var height = $(".l-layout-center").height();
	// Tab
	$("#framecenter").ligerTab({
		height : height
	});
	// 面板
	$("#accordion").ligerAccordion({
		height : height - 24,
		speed : null
	});

	$(".l-link").hover(function() {
		$(this).addClass("l-link-over");
	}, function() {
		$(this).removeClass("l-link-over");
	});

	tab = $("#framecenter").ligerGetTabManager();
	accordion = $("#accordion").ligerGetAccordionManager();
	//加载导航菜单
	loadMonitorMenu();
	loadLeftMenu();
	
	window['f_addTab'] = addTabEvent;

});

function loadLeftMenu() {
	showLoading(true);
	 var $leftmenu = $("#leftmenu");
	 //加载栏目
     $.getJSON('${pageContext.request.contextPath}/auth/Menu/findTopMenuByUser.koala', function (menus)
     {
         $(menus.data).each(function (i, menu)
         {
        	 $leftmenu.append('<div id="main_'+menu.id+'" title="' + menu.name + '" class="l-scroll"><ul id="sub_'+menu.id+'"></ul></div>');
        	 //加载栏目菜单
        	 $.getJSON('${pageContext.request.contextPath}/auth/Menu/findAllSubMenuByParent.koala?resVO.id=' + menu.id, function(submenu) {
             	var tree = $("#sub_"+menu.id).ligerTree({
             		data:submenu.data,
             		checkbox:false
             	});
             	
             	tree.bind("select", function(node) {
             		var url = node.data.identifier;
             		var text = node.data.text;
             		var tabid = $(node.target).attr("tabid");
             		if (!url) {
             			return;
             		}
             		if (node.data.menuType == "2") {
             			return;
             		}
                if (!tabid) {
                    tabidcounter++;
                    tabid = "tabid" + tabidcounter;
                    $(node.target).attr("tabid", tabid);
                }
                addTabEvent(tabid, text, url);
             	});
             });//加载栏目菜单end
         });
         
         //Accordion
         accordion = $leftmenu.ligerAccordion({ height: $(".l-layout-center").height() - 24, speed: null });
         showLoading(false);
     });//加载栏目end 
}

function layoutHeiheightChangeEvent(options) {
	if (tab)
		tab.addHeight(options.diff);
	if (accordion && options.middleHeight - 24 > 0)
		accordion.setHeight(options.middleHeight - 24);
}

function addTabEvent(tabid, text, url) {
	tab.addTabItem({
		tabid : tabid,
		text : text,
		url : url
	});
	tab.reload(tabid);
}


var monitorMenu = 
	[
	    { text: '监控数据',isexpand:true, children: [ 
			{url:"pages/monitor/httpNodes.jsp",text:"HTTP监控"},
			{url:"pages/monitor/methodNodes.jsp",text:"方法监控"},
			{url:"pages/monitor/dbNodes.jsp",text:"数据库监控"}
		]
	    },
	    { text: '监控节点', isexpand: false, children: [
			{ url: "pages/monitor/showAllNodes.jsp", text: "监控节点列表" }
		]
	    },
	    { text: '监控服务', isexpand: false, children: [
	        { url: "pages/monitor/schedulers.jsp", text: "定时任务" }
	      ]
	    }
	];
	
function loadMonitorMenu(){
	$("#monitorTree").ligerTree({
	    data : monitorMenu,
	    checkbox: false,
	    slide: false,
	    nodeWidth: 120,
	    attribute: ['nodename', 'url'],
	    onSelect: function (node)
	    {
	        if (!node.data.url) return;
	        var tabid = $(node.target).attr("tabid");
	        if (!tabid) {
	            tabidcounter++;
	            tabid = "tabid" + tabidcounter;
	            $(node.target).attr("tabid", tabid);
	        }
	        addTabEvent(tabid, node.data.text, node.data.url);
	    }
	});
}

function showLoading(flag){
	if(flag){$('#pageloading').show();}
	else{$('#pageloading').hide();}
}

</script>
</head>
<body style="padding:0px;background:#EAEEF5;">  
<div id="pageloading"></div>  
<div id="topmenu" class="l-topmenu">
        <div class="l-topmenu-logo">Koala</div>
        <div class="l-topmenu-welcome"> 
            <span class="l-topmenu-username">[<ss3:authentication property="principal.username" />]</span>欢迎您  &nbsp; 
            [<a href="javascript:Koala.changepassword()">修改密码</a>] &nbsp; 
             [<a href="${pageContext.request.contextPath}/j_spring_security_logout">切换用户</a>]
            [<a href="${pageContext.request.contextPath}/j_spring_security_logout">退出</a>]
        </div>
  </div>
  
  <div id="main-content" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
        <div position="left"  title="主要菜单" id="leftmenu">
           <div title="系统监控" class="l-scroll">
              <ul id="monitorTree" style="margin-top:3px;">
                 
              </ul>
           </div>
        </div>
        <div position="center" id="framecenter"> 
            <div tabid="home" title="我的主页" style="height:300px" >
                <iframe frameborder="0" name="home" id="home" src="${pageContext.request.contextPath}/pages/common/welcome.jsp"></iframe>
            </div> 
        </div> 
        
    </div>
    <div  style="height:32px; line-height:32px; text-align:center;">
            Copyright © 2011-2012 koala
    </div>
    <div style="display:none"></div>
    <form id="changepasswordPanel" style="display:none;">
		<table cellpadding="0" cellspacing="0" class="form2column" >
			<tr>
				<td class="label">旧密码:</td>
				<td class="content">
					<input name="oldPassword" type="password" id="oldPassword" class="input-common" dataType="Require" />
				</td>
			</tr>
			<tr>
				<td class="label">新密码:</td>
				<td class="content">
					<input name="newPassword" type="password" id="newPassword" class="input-common" dataType="Require" maxLength="16"" />
				</td>
			</tr>
			<tr>
				<td class="label">确认密码:</td>
				<td class="content">
					<input name="confirmPassword" type="password" id="confirmPassword" class="input-common" dataType="Require" maxLength="16" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>