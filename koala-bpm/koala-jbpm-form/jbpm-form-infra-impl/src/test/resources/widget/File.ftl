<#macro File keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W"> 
<div class="uploader" id="uniform-undefined"><input type="file" size="19" style="opacity: 0;"><span class="filename">No file selected</span><span class="action">Choose File</span></div>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>