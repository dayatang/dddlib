<#macro Radio keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W">
<div class="radio"><span class="checked"><input type="radio" style="opacity: 0;" checked="true" name="isActivated"></span></div>
<div class="radio"><span><input type="radio" style="opacity: 0;" checked="true" name="isActivated"></span></div>
 <script>
	$('.radio').find('input').on('click', function(){
		$('.radio').find('span').removeClass('checked');
		$(this).parent().addClass('checked');
	})
 </script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>