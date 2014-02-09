<#macro Password keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W">
       <input type="password" class="form-control" style="width:180px;" /><#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>