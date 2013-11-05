<#macro Time keyId keyName keyType security value validationType validationExpr keyOptions>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
<div style="width:190px;" data-role="date" class="input-group"><input type="text"  name="${keyId}" id="${keyId}" value="${value}" class="form-control time"/>
<a class="input-group-addon add-on glyphicon glyphicon-time"></a></div>
<script>FormRender.renderDatePicker("${keyId}","time");</script>
<script></script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>