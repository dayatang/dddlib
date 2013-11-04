<#macro Checkbox keyId keyName keyType security value validationType validationExpr>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
<div class="checker"><span><input type="checkbox"  name="${keyId}" id="${keyId}" value="${value}" value="0" style="opacity: 0;"></span></div>
 <script>
			$(function(){
	       	   $('body').one('renderCheckbox', function(){
	       	   		$('.checker').find('input').parent().toggleClass('checked');
			   });
	       	    var interVal = setInterval(function(){
	       			if($('.checker').find('input')).length > 0){
 				 		$('body').trigger('renderCheckbox');
 				 		clearInterval(interVal);
 				 	}
 			   }, 100);
			});
 </script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>