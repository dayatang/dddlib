<#macro TextArea keyId keyName keyType security value validationType validationExpr>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
        <textarea  name="${keyId}" id="${keyId}" class="form-control" rows="3" style="width:450px;" <#if validationType!="">dataType="${validationType}"</#if> <#if validationExpr!="">validationExpr="${validationExpr}"</#if>>${value}</textarea><#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>