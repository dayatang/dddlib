<#macro Radio keyId keyName keyType security value validationType validationExpr>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
<div class="radio"><span class="checked"><input type="radio" style="opacity: 0;" checked="true"  name="${keyId}" id="${keyId}" value="${value}"></span></div>
<div class="radio"><span><input type="radio" style="opacity: 0;" checked="true"  name="${keyId}" id="${keyId}" value="${value}"></span></div>
 <script>
	       $(function(){
	       	   $('body').one('renderRadio', function(){
	       	   		$('.radio').find('input').parent().toggleClass('checked');
	       	   		$('.radio').find('span').removeClass('checked');
					$(this).parent().addClass('checked');
			   });
	       	    var interVal = setInterval(function(){
	       			if($('.radio').find('input').length > 0){
 				 		$('body').trigger('renderRadio');
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