<#macro Text keyId keyName keyType security value>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
       <#-- 文本框  -->
       <#if keyType=="Text">
         <input type="text" class="form-control" name="${keyId}" id="${keyId}" value="${value}"/><#t>
       </#if>
<#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>