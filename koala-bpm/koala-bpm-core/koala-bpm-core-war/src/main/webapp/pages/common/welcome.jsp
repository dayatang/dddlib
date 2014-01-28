<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/pages/common/header.jsp" %>
	<style type="text/css">
			/* 欢迎页 - 带按钮的段落 */
		.withicon{ display:block; position:relative; margin-top:4px; margin-bottom:4px;}
		.withicon .icon{ position:absolute; left:7px; top:6px; width:20px; height:20px;}
		.withicon span{ line-height:32px; margin-left:33px; display:block;}
		.withicon img{width:20px; height:20px;}
		
		/* 欢迎页 - 导航条 */
		.navbar{line-height:33px;height:33px;background:url('${pageContext.request.contextPath}/images/background/navbar.gif') repeat-x; width:99%; position:relative; margin-top:8px; margin-bottom:8px;}
		.navbar-l,.navbar-r{ width:2px;height:33px;background:url('${pageContext.request.contextPath}/images/background/navbar.gif') no-repeat; position:absolute; }
		.navbar-l{ left:0px; background-position:0px -33px;}
		.navbar-r{ right:0px;background-position:0px -66px;}
		.navbar-icon{ position:absolute; left:7px; top:6px;}
		.navbar img{ width:20px; height:20px;}
		.navbar-inner{ margin-left:30px;}
		.navbar a{ color:#3186C8;}
		
		
		
		
		/* 欢迎页 - 快捷按钮 (图片+文字) */
		.links{ display:block; position:relative; margin-top:4px; margin-bottom:4px;}
		.link{ float:left;position:relative; width:60px; height:70px; margin-left:10px; text-align:center;border:1px solid #FFFFFF; cursor:pointer; }
		.link img{ display:block; margin:5px auto; width:32px; height:32px;}
		
		.linkover{ border:1px solid #d3d3d3; background:#f1f1f1;}
		
		
		.link .close{ position:absolute; top:1px; right:1px; display:none; width:11px; height:11px; background:white;border:1px solid #f1f1f1;background: url(${pageContext.request.contextPath}/images/icons/11x11/icon-close.gif);
		}
		.linkover .close{ display:block;}
		.link .closeover{ border:1px solid #d3d3d3;background:url(${pageContext.request.contextPath}/images/icons/11x11/icon-close-over.gif);}
		
		#guides{}
#guides li{ list-style:none; padding-left:15px; margin-top:8px;}
#guides span{ font-size:14px;}
#guides a{ padding-left:5px; color:#94AAD6;}
	</style>
	<script type="text/javascript">
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

			Koala.ajax({
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
					Koala.showError(message);
				}
			});

			return false;
		});

		var links = $(".links");

		$("#usersetup").click(function() {
			var url = $(this).attr("url");
			if (!url) {
				return;
			}
			var text = "修改用户信息";
			parent.f_addTab(null, text, url);
		});

	});
	</script>   
</head>
<body style="padding:10px; overflow:auto; text-align:center;background:#FFFFFF;"> 
        <div class="navbar"><div class="navbar-l"></div><div class="navbar-r"></div>
        <div class="navbar-icon"><img src="${pageContext.request.contextPath}/images/icons/32x32/hire_me.gif" /></div>
        <div class="navbar-inner"> 
        <b><span id="labelusername"><ss3:authentication property="principal.username" /></span><span>，</span><span id="labelwelcome"></span><span>欢迎登录 Koala</span></b>
        <a href="javascript:void(0)" id="usersetup" style="display:none">账号设置</a>
        </div>
        </div>

        <div class="withicon">
            <span id="labelLastLoginTime"><a href="${pageContext.request.contextPath}/dbinit" target="_blank"><font color='red'>点击这里</font></a>进行权限数据的初始化</span>
        </div>


        <div class="navline">
        </div>
        
        <div class="links"> 
        </div>

        <div class="l-clear"></div>

        <div class="navbar"><div class="navbar-l"></div><div class="navbar-r"></div>
        <div class="navbar-icon"><img src="${pageContext.request.contextPath}/images/icons/32x32/collaboration.gif" /></div>
        <div class="navbar-inner"> 
        <b>Koala帮助</b> 
        </div>
        </div>

         <div id="guides">
	     <ul>
	     <li><span>访问<a href="http://58.215.177.99:8080/display/koala/Home" target="_blank">Koala主页</a>，获取最新资讯与信息</span></li>
	     <br/>
	     <li><span>如何使用Koala，查看<a href="http://58.215.177.99:8080/display/koalaDocs/Guides" target="_blank">在线使用指南</a></span></li>
	     <br/>
	     <li><span>提交BUG或改进意义，请访问<a href="http://58.215.177.99:9000/browse/PUBKOALA" target="_blank">BUG提交</a></span></li>
	     </ul>
	   </div>
	   
	   
        <div class="navline">
        	</br>
        	</br>
        	</br>
        	</br>
        	</br>
        	</br>
        	</br>
        	</br>
        	</br>
        </div>
</body>
</html>