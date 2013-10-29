<#macro Date keyId keyName keyType security value>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
         <div style="width:190px;" data-role="date" class="input-group"><input type="text"  name="${keyId}" id="${keyId}" value="${value}" class="form-control time"/>
         <a class="input-group-addon add-on glyphicon glyphicon-time"></a></div>
        <script>
	       	$(function(){
	       	   $('body').one('renderDate', function(){
	       	   		$('[data-role="date"]').datetimepicker({
			          language: 'zh-CN',
			          pickDate: true,
			          pickTime: false
			   		});
			   });
	       	    var interVal = setInterval(function(){
	       			if($('[data-role="date"]').length > 0){
 				 		$('body').trigger('renderDate');
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
