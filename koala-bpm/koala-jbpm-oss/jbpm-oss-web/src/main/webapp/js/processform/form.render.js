$(function(){
	
});

FormRender = {
  renderSelect : function(targetId,optsJson){
		var optArr = [];
		for(var index in optsJson){
	 	   var opt = {};
	 	   opt.title = index;
	 	   opt.value = optsJson[index];
	 	   optArr.push(opt);
		}
		
		$('#'+targetId).select({
			title: '选择数据',
			contents: optArr
		});
   },
   
   renderRadio : function(targetId,elmName,optsJson){
	   var radioHtml = "";
	   var checkedStyle = " checked";
	   for(var index in optsJson){
		   radioHtml = radioHtml + "<label class='radio-inline'><input type='radio' name='"+elmName+"' value='"+optsJson[index]+"'"+checkedStyle+">"+index+"</label>";
		   checkedStyle = "";
	   }
	   $("#"+targetId).append(radioHtml);
   },
   renderCheck : function(targetId,elmName,optsJson){
	   var checkboxHtml = "";
	   for(var index in optsJson){
		   checkboxHtml = checkboxHtml + "<label class='checkbox-inline'><input type='checkbox' name='"+elmName+"' value='"+optsJson[index]+"'>"+index+"</label>";
	   }
	   $("#"+targetId).append(checkboxHtml);
   },
   renderDatePicker : function(targetId,format){
	   var pickDate = format.indexOf('date')>=0;
	   var pickTime = format.indexOf('time')>=0;
	   $("#"+targetId).parent().datetimepicker({
	       language: 'zh-CN',
	       pickDate: pickDate,
	       pickTime: pickTime
	   	});
   }
}
