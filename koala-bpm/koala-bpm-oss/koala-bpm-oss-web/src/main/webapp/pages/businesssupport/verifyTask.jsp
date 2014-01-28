<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>进入编辑页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />"   rel="stylesheet">
    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value='/lib/jquery-1.8.3.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />" ></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />" ></script>
    
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
            padding-left: 88%;
            padding-top: 5px;
            padding-bottom: 6px;
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
        .approval .reson:after {
        	clear: both;
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
        .processDetail .bottons:before {
        	clear: both;
        }
        .processDetail .bottons {
         	padding-left: 5%;
    		padding-top: 1%;
        }
        .processDetail .bottons button {
        	margin-right: 15px;
        }
	</style>
	
	<script type="text/javascript">
		function verify(obj){
			//alert($(obj).attr("value"));
			//var form = $('#form1');
			//console.info(form.serialize())
			//$("#form1").attr("action","/businessSupport/verifyTask.koala?choice=" + $(obj).attr("value"));
			//$("#form1").submit();
			$.post('${pageContext.request.contextPath}/businessSupport/verifyTask.koala?choice=' + $(obj).attr("value"), $('#form1').serialize()).done(function(data){
				if(data.success){
					$('body').message({
						type: 'success',
						content: data.success
					});
					window.location.href = '${pageContext.request.contextPath}/index.koala';
				}else{
					$('body').message({
						type: 'error',
						content: data.error
					});
				}
			})
		}
	</script>
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
	            <div class="panel panel-default">
	                <div class="panel-heading">
	                    <h3 class="panel-title">结果</h3>
	                </div>
	                <div class="panel-body">
	                    <div class="reson">
	                        <label class="control-label">意见:</label>
	                        <textarea class="form-control" rows="3" name="comment" id="form-description"></textarea>
	                    </div>
	                    <div style="clear:both;"></div>
	                    <div class="bottons">
		                    <c:forEach items="${taskInstance.taskChoices }" var="taskChoice">
		                		 <button class="btn btn-success" onclick="verify(this);" type="button" value="${taskChoice.key }===${taskChoice.value }===${taskChoice.valueType}===${taskChoice.name }">${taskChoice.name }</button>
			                </c:forEach>
		                </div>
	                </div>
	            </div>
	        </div>
	        <div class="panel-footer">
	        	<button data-dismiss="modal" class="btn btn-default" type="button" id="cancelBtn" onclick="javascript:window.back();">返回</button>
	        	<input type="hidden" name="processInstanceId" value="${taskInstance.processInstanceId }"/>
	            <input type="hidden" name="taskId" value="${taskInstance.taskId }"/>
	        </div>
	    </div>
	</form>
</body>
</html>