<#macro Password keyId keyName keyType security value validationType validationExpr keyOptions>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
       <input type="password"  name="${keyId}" id="${keyId}" value="${value}" class="form-control" style="width:180px;" /><#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>