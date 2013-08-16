	//几个布局的对象
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
            LG.login();
        }
        //修改密码
        function f_changepassword()
        {
            LG.changepassword();
        }

        //菜单初始化
        $("ul.noClass li").live('mouseover', function ()
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
        LG.prevDialogImage();

        var mainmenu = $("#mainmenu");
        
        $('#mainmenu').append('<div  title="流程管理"><ul id="jbpmTree"></ul></div>');
        
        //Accordion
        accordion = $("#mainmenu").ligerAccordion({ height: bodyHeight - 24, speed: null });

        $("#pageloading").hide();

        $.getJSON('auth-Menu-findAllMenuByUser.action', function (menus)
        {
            $(menus.data).each(function (i, menu)
            {
            	$('#mainmenu').append('<div id="main_'+menu.id+'" title="' + menu.name + '"><ul id="sub_'+menu.id+'"></ul></div>');
                //var item = $('<div title="' + menu.name + '"><ul></ul></div>');
                
                $.getJSON('auth-Menu-findAllSubMenuByParent.action?resVO.id=' + menu.id, function(submenu) {
                	var tree = $("#sub_"+menu.id).ligerTree({
                		data:submenu.data,
                		checkbox:false
                	});
                	//alert(item.html());
                	tree.bind("select", function(node) {
                		var url = node.data.url;
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
                //mainmenu.append(item);
            });

            //Accordion
            accordion = $("#mainmenu").ligerAccordion({ height: bodyHeight - 24, speed: null });

            $("#pageloading").hide();
        });

        //$.getJSON('common-Login-getUserNameByCode.action?username=admin', function (user)
        //{
            $(".l-topmenu-username").html("系统管理员" + "，");
        //});
