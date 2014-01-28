<#macro Time keyId keyName keyType security value validationType validationExpr keyOptions>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
 		<div class="input-group date form_datetime">
            <input class="form-control" size="16" type="text" value="" name="${keyId}" id="${keyId}">
			<span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
        </div>
<script>FormRender.renderDatePicker("${keyId}","time");</script>
<script></script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>