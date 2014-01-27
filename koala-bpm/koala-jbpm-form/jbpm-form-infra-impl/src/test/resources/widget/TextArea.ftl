<#macro TextArea keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W">
        <textarea class="form-control" rows="3" style="width:450px;"></textarea><#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>