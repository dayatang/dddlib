<#macro DropDown keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W">
     
     <div class="btn-group select"></div>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>