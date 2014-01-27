<#macro DropDown keyId keyName keyType security value validationType validationExpr keyOptions>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
     
     <div class="btn-group select" id="${keyId}"></div>
     <script>FormRender.renderSelect("${keyId}",${keyOptions});</script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>
