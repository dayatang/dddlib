<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<LINK TYPE="text/css" REL="stylesheet" HREF="${pageContext.request.contextPath}/css/pageCommon.css" />
<HTML>
<HEAD>
    <TITLE>权限初始化成功</TITLE>
</HEAD>   
<BODY>

<DIV ID="Title_bar">
    <DIV ID="Title_bar_Head">
        <DIV ID="Title_Head"></DIV>
        <DIV ID="Title"><!--页面标题-->
            <IMG BORDER="0" WIDTH="13" HEIGHT="13" SRC="${pageContext.request.contextPath}//images/title_arrow.gif"/> 成功
        </DIV>
        <DIV ID="Title_End"></DIV>
    </DIV>
</DIV>


<!--显示表单内容-->
<DIV ID="MainArea">
		<DIV CLASS="ItemBlock_Title1">
        </DIV> 
        
        <DIV CLASS="ItemBlockBorder" STYLE="margin-left: 15px;">
            <DIV CLASS="ItemBlock" STYLE="text-align: center; font-size: 16px;">
                          权限数据初始化成功，请返回<a href="${pageContext.request.contextPath}/login.action"><font color="red">登录页面</font></a>
            </DIV>
        </DIV>
        
        <!-- 操作 -->
</DIV>

</BODY>
</HTML>
