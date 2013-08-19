LG.addfavorite = function (success)
{
    $(document).bind('keydown.addfavorite', function (e)
    {
        if (e.keyCode == 13)
        {
            doAddFavorite();
        }
    });

    if (!window.addfavoriteWin)
    {
        var addfavoritePanle = $("<form></form>");

        var menusTree = {
            id: 'addfavoriteMenusTree',
            url: '../handler/tree.ashx?view=MyMenus',
            checkbox: false,
            nodeWidth: 220
        };

        addfavoritePanle.ligerForm({
            fields: [
                 { display: "页面", name: "MenuID", newline: true, labelWidth: 100, width: 220, space: 30, type: "select", comboboxName: "MyMenusMenuID",
                     options: { id: 'MyMenusMenuID', treeLeafOnly: true, tree: menusTree, valueFieldID: "MenuID", valueField: "id" },
                     validate: { required: true, messages: { required: '请选择页面'} }
                 },

                 { display: "收藏备注", name: "FavoriteContent", newline: true, labelWidth: 100, width: 220, space: 30, type: "textarea" }

            ]
        });

        //验证
        jQuery.metadata.setType("attr", "validate");
        LG.validate(addfavoritePanle);

        window.addfavoriteWin = $.ligerDialog.open({
            width: 400,
            height: 190, top: 150, left: 230,
            isResize: true,
            title: '增加收藏',
            target: addfavoritePanle,
            buttons: [
            { text: '确定', onclick: function ()
            {
                doAddFavorite();
            }
            },
            { text: '取消', onclick: function ()
            {
                window.addfavoriteWin.hide();
                $(document).unbind('keydown.addfavorite');
            }
            }
            ]
        });
    }
    else
    {
        window.addfavoriteWin.show();
    }

    function doAddFavorite()
    { 
        var manager = $.ligerui.get("MyMenusMenuID"); 
        if (addfavoritePanle.valid() && manager)
        {
            LG.ajax({
                type: 'AjaxSystem',
                method: 'AddMyFavorite',
                data: { MenuID: manager.getValue(), FavoriteContent: $("#FavoriteContent").val() },
                success: function ()
                {
                    LG.showSuccess('收藏成功');
                    window.addfavoriteWin.hide();
                    $(document).unbind('keydown.addfavorite');
                    if (success)
                    {
                        success();
                    }
                },
                error: function (message)
                {
                    LG.showError(message);
                }
            });
        }

    }

};