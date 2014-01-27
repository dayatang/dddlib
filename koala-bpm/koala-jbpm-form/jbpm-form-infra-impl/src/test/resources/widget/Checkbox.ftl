<#macro Checkbox keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W">
<div class="checker"><span><input type="checkbox" value="0" style="opacity: 0;"></span></div>
 <script>
	$('.checker').find('input').on('click', function(){
		$(this).parent().toggleClass('checked');	
	})
 </script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>