<#macro TextArea keyId keyName keyType security value validationType validationExpr keyOptions>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
   <textarea  name="${keyId}" id="${keyId}" class="form-control" rows="3" style="width:450px;" <#if validationType!="">dataType="${validationType}"</#if> <#if validationExpr!="">validationExpr="${validationExpr}"</#if> <#if security=="R">readonly</#if>>${value}</textarea><#t>  
</#macro>