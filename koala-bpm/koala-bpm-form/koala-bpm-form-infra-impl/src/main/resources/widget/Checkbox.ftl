<#macro Checkbox keyId keyName keyType security value validationType validationExpr keyOptions>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
     <div id="${keyId}"></div>
     <script>FormRender.renderCheck("${keyId}","${keyId}",${keyOptions});</script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>