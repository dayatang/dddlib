<#macro DateTime keyId keyName keyType security value>
  ${keyName}:&nbsp;&nbsp;<#t>
     <#if security=="W">
       <!-- 日期 -->
<div style="width:190px;" data-role="date" class="input-group"><input type="text" class="form-control time"/>
<a class="input-group-addon add-on glyphicon glyphicon-time"></a></div>
<script>
$('[data-role="date"]').datetimepicker({
            language: 'zh-CN',
            pickDate: true,
            pickTime: true
        });
</script>
       <#t>
     <#elseif security == "R">
       ${value}<#t>
     </#if>
</#macro>