<#macro DropDown keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W">
     
     <div class="btn-group select"></div>
 <script>
 console.info($('.select'));
 $('.select').select(
	{
		title: '选择数据',
		contents: [
			{value: 'data1', title: '数据1'},
			{value: 'data2', title: '数据2'}
		]
	}
)
 </script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>