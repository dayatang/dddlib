<#macro DropDown keyId keyName keyType security value validationType validationExpr>
  <#--${keyName}:&nbsp;&nbsp;<#t>-->
     <#if security=="W">
     
     <div class="btn-group select"></div>
 <script>
  		$(function(){
	       	   $('body').one('renderSelect', function(){
	       	   		console.info(111);
	       	   		$('.select').select({
						title: '选择数据',
						contents: [
							{value: 'data1', title: '数据1'},
							{value: 'data2', title: '数据2'}
						]
					})
			   });
	       	   var interVal = setInterval(function(){
	       			if($('.select').length > 0){
 				 		$('body').trigger('renderSelect');
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
