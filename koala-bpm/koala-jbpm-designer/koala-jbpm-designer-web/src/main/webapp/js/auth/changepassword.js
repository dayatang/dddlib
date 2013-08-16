Koala.changepassword = function ()
{
    $(document).bind('keydown.changepassword', function (e)
    {
        if (e.keyCode == 13)
        {
            doChangePassword();
        }
    });

    var changepasswordPanel = null;
    if (!window.changePasswordWin)
    {
    	changepasswordPanel = $("#changepasswordPanel");

        window.changePasswordWin = $.ligerDialog.open({
            width: 400,
            height: 190, 
            top: 200,
            isResize: true,
            title: '用户修改密码',
            target: changepasswordPanel,
            buttons: [
            { text: '确定', onclick: function ()
            {
                doChangePassword();
            }
            },
            { text: '取消', onclick: function ()
            {
                window.changePasswordWin.hide();
                $(document).unbind('keydown.changepassword');
            }
            }
            ]
        });
    }
    else
    {
        window.changePasswordWin.show();
    }

    function doChangePassword()
    {
        var OldPassword = $("#oldPassword").val();
        var LoginPassword = $("#newPassword").val();
        var data = "oldPassword=" + OldPassword + "&userVO.userPassword=" + LoginPassword;
        //验证
        if (Koala.validate(changepasswordPanel))
        {
            $.ajax({
            	method:"post",
            	url:"auth-User-updatePassword.action",
            	data:data,
            	success:function(msg) {
            		if (msg.result == "success") {
            			$.ligerDialog.alert("密码修改成功");
            			changePasswordWin.hidden();
            		} else if (msg.result == "failure") {
            			$.ligerDialog.alert("原密码不正确");
            		}else{
            			$.ligerDialog.alert(msg.result);
            		}
            	}
            });
        }
    }

};