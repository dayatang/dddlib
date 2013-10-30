<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>进入历史页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/datetimepicker.css'/>" rel="stylesheet">
    <script type="text/javascript" src="<c:url value='/js/jquery/jquery-1.8.3.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/koala-ui.plugin.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/datetimepicker.js' />" ></script>
	<script type="text/javascript">
		function colse(){
			window.close();
		}
	</script>
    
	<style>
		body {
			position:relative; 
			top: 20px;
		}
		.col-lg-8 {
			position:relative; 
			top: 8px;
		}
		.approval {
            width: 75%;
            margin: 10px auto;
        }
        .approval .panel-footer {
            padding-left: 82%;
        }
        .approval .panel-footer button {
            margin-right: 15px;
        }
        .approval .pass {
           float: left;
           width: 60px;
           margin-right: 20px;
           margin-left: 50px;
        }
        .approval .pass label {
            float: right;
            position: relative;
            top: 2px;
        }
        .approval .noPass {
            float: left;
            width: 60px;
        }
        .approval .noPass label {
            float: right;
            position: relative;
            top: 2px;
        }
        .approval .noPass:after {
            clear: both;
        }
        .approval .reson {
            clear: both;
            padding-top: 25px;
        }
        .approval .reson label {
            float: left;
            position: relative;
            top: 25px;
            padding-right: 15px;
        }
        .approval .reson textarea {
            float: left;
            width: 45%;
        }
        .approval .row {
            margin-bottom: 2px;
        }
        .approval .row .content {
            position: relative;
            top: 6px;
        }
	</style>
	
</head>
<body>
	<form class="form-horizontal processDetail" role="form" id="form1" method="post">
		<div class="panel panel-primary approval">
	        <div class="panel-heading">
	            <h3 class="panel-title">流程审批</h3>
	        </div>
	        <div class="panel-body">
	            <div class="panel panel-default">
	                <div class="panel-heading">
	                    <h3 class="panel-title">信息</h3>
	                </div>
	                <div class="panel-body">
	                    ${taskInstance.templateHtmlCode }
	                </div>
	            </div>
	            <div class="panel panel-default">
	                <div class="panel-heading">
	                    <h3 class="panel-title">历史</h3>
	                </div>
	                <div class="panel-body">
	                	<table class="table table-responsive table-striped">
	                		<tr>
	                			<td>名称</td>
	                			<td>执行人</td>
	                			<td>执行日期</td>
	                			<td>审批结果</td>
	                			<td>备注</td>
	                		</tr>
		                	<c:forEach items="${taskInstance.historyLogs }" var="history">
		                		<tr>
		                			<td>${history.nodeName }</td>
		                			<td>${history.user }</td>
		                			<td>${fn:substring(history.createDate, 0, 10)} ${fn:substring(history.createDate, 11, 19)}</td>
		                			<td>${history.result }</td>
		                			<td>${history.comment }</td>
		                		</tr>
		                	</c:forEach>
	                	</table>
	                </div>
	                <div class="panel-footer">
	                 	<a class="btn btn-info" target="_blank" href="<c:url value='/businessSupport/viewProcessImage.koala?processInstanceId=${taskInstance.processInstanceId }' />" >查看流程图</a>
	                 </div>
	            </div>
	            <%-- <div class="panel panel-default">
	                <div class="panel-heading">
	                    <h3 class="panel-title">结果</h3>
	                </div>
	                <div class="panel-body">
	                	<div class="pass">
	                        <button class="btn btn-success" type="button">${taskVerifyDTO.taskChoice.name }</button>
	                    </div>
	                    <div class="reson">
	                        <label class="control-label">意见:</label>
	                        <textarea class="form-control" rows="3" name="comment" readonly="readonly" id="form-description">${taskVerifyDTO.comment }</textarea>
	                    </div>
	                </div>
	            </div> --%>
	        </div>
	        <div class="panel-footer">
	            <button data-dismiss="modal" class="btn btn-default" type="button" id="cancelBtn" onclick="colse();">关闭</button>
	            <input type="hidden" name="processInstanceId" value="${taskInstance.processInstanceId }"/>
	            <input type="hidden" name="taskId" value="${taskInstance.taskId }"/>
	            <%-- <input type="hidden" name="processId" value="${taskInstance.processId }"/> --%>
	        </div>
	    </div>
	</form>
</body>
</html>