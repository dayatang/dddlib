<#--控件显示定义-->
<#macro widget widgetKey widgetName widgetType widgetSecurity widgetValue>
	<div>
   		${widgetKey}
	</div>
	<div>
		<#if widgetSecurity=="W">
			
		<#elseif widgetSecurity == "R">
			
		</#if>
	</div>
</#macro>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>OA系统</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="lib/bootstrap/css/bootstrap.min.css"   rel="stylesheet">
    <link href="css/koala.css"   rel="stylesheet">
	<style>
		body {
			position:relative; 
			top: 20px;
		}
		.col-lg-8 {
			position:relative; 
			top: 8px;
		}
	</style>
</head>
<body>
<form class="form-horizontal processDetail" role="form" id="processDetail">
     <div class="form-group row">
            <div class="col-lg-6 form-group">
                <label class="col-lg-4 control-label">列1标题:</label>
                <div class="col-lg-8">
                    列1内容
                </div>
            </div>
            <div class="col-lg-6 form-group">
                <label class="col-lg-4 control-label">列2标题:</label>
                <div class="col-lg-8">
                    列2内容
                </div>
            </div>
       </div>
	    <div class="form-group row">
            <div class="col-lg-6 form-group">
                <label class="col-lg-4 control-label">列1标题:</label>
                <div class="col-lg-8">
                    列1内容
                </div>
            </div>
            <div class="col-lg-6 form-group">
                 <label class="col-lg-4 control-label">列2标题:</label>
                <div class="col-lg-8">
                    列2内容
                </div>
            </div>
       </div>
	    <div class="form-group row">
            <div class="col-lg-6 form-group">
                <label class="col-lg-4 control-label">列1标题:</label>
                <div class="col-lg-8">
                    列1内容
                </div>
            </div>
            <div class="col-lg-6 form-group">
                 <label class="col-lg-4 control-label">列2标题:</label>
                <div class="col-lg-8">
                    列2内容
                </div>
            </div>
       </div>
	    <div class="form-group row">
            <div class="col-lg-6 form-group">
                <label class="col-lg-4 control-label">列1标题:</label>
                <div class="col-lg-8">
                    列1内容
                </div>
            </div>
            <div class="col-lg-6 form-group">
                 <label class="col-lg-4 control-label">列2标题:</label>
                <div class="col-lg-8">
                    列2内容
                </div>
            </div>
       </div>
</form>

</body>
</html>