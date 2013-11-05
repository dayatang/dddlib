<#macro Date keyId keyName keyType security value validationType validationExpr>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
         <div style="width:190px;" data-role="date" class="input-group"><input type="text"  name="${keyId}" id="${keyId}" value="${value}" class="form-control time" <#if validationType!="">dataType="${validationType}"</#if> <#if validationExpr!="">validationExpr="${validationExpr}"</#if> />
         <a class="input-group-addon add-on glyphicon glyphicon-time"></a></div>
        <script>FormRender.renderDatePicker("${keyId}","date");</script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>
