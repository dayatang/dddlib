/*
    文件说明：
        icon选取

    接口方法：
        1，打开窗口方法：f_openIconsWin
        2，保存下拉框ligerui对象：currentComboBox

    例子：
        可以这样使用(选择ICON完了以后，会把icon src保存到下拉框的inputText和valueField)：
        onBeforeOpen: function ()
        {
            currentComboBox = this;
            f_openIconsWin();
            return false;
        }

*/

//图标
var jiconlist, winicons, currentComboBox;
$(function ()
{
    jiconlist = $("body > .iconlist:first");
    if (!jiconlist.length) jiconlist = $('<ul class="iconlist"></ul>').appendTo('body');
});
 
$(".iconlist li").live('mouseover', function ()
{
    $(this).addClass("over");
}).live('mouseout', function ()
{
    $(this).removeClass("over");
}).live('click', function ()
{
    if (!winicons) return;
    var src = $("img", this).attr("src");
    src = src.replace(/^([\.\/]+)/, '');
    var editingrow = grid.getEditingRow();
    if (editingrow)
    {
        if (currentComboBox)
        {
            currentComboBox.inputText.val(src);
            currentComboBox.valueField.val(src);
        }
    }
    winicons.hide();
});

function f_openIconsWin()
{
    if (winicons)
    {
        winicons.show();
        return;
    }
    winicons = $.ligerDialog.open({
        title: '选取图标',
        target: jiconlist,
        width: 470, height: 280, modal: true
    });
    if (!jiconlist.attr("loaded"))
    {
        LG.ajax({
            type: 'AjaxSystem',
            method: 'GetIcons',
            loading: '正在加载图标中...',
            data: { HttpContext: true },
            success: function (data)
            {
                for (var i = 0, l = data.length; i < l; i++)
                {
                    var src = data[i];
                    var reg = /(lib\\icons)(.+)/;
                    var match = reg.exec(src);
                    if (!match) continue;
                    var s = "../lib/icons" + match[2].replace(/\\/g, '/');
                    jiconlist.append("<li><img src='" + s + "' /></li>");
                }
                jiconlist.attr("loaded", true);
            }
        });
    }
}