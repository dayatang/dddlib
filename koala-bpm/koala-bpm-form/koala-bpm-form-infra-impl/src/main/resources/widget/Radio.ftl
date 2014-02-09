<#macro Radio keyId keyName keyType security value validationType validationExpr keyOptions>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
     <div id="${keyId}"></div>
     <script>FormRender.renderRadio("${keyId}","${keyId}",${keyOptions});</script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>