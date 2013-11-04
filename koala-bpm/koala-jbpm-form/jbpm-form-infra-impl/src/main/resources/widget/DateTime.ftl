<#macro DateTime keyId keyName keyType security value validationType validationExpr>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
       <!-- 日期 -->
<div style="width:190px;" data-role="date" class="input-group"><input type="text" name="${keyId}" id="${keyId}" value="${value}" class="form-control time" <#if validationType!="">dataType="${validationType}"</#if> <#if validationExpr!="">validateExpr="${validationExpr}"</#if> />
<a class="input-group-addon add-on glyphicon glyphicon-time"></a></div>
<script>
			$(function(){
	       	   $('body').one('renderDateTime', function(){
	       	   		$('[data-role="date"]').datetimepicker({
			          language: 'zh-CN',
			          pickDate: true,
			          pickTime: true
			   		});
			   });
	       	   var interVal = setInterval(function(){
	       			if($('[data-role="date"]').length > 0){
 				 		$('body').trigger('renderDateTime');
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
