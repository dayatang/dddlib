FormRender = {
  renderSelect : function(targetId,optsJson){
		var optArr = [];
		for(var index in optsJson){
	 	   var opt = {};
	 	   opt.title = index;
	 	   opt.value = optsJson[index];
	 	   optArr.push(opt);
		}
		var intervalId = setInterval(function(){
			var target = $('#'+targetId);
			if(target.length > 0){
				target.select({
					title: '选择数据',
					contents: optArr
				});
				clearInterval(intervalId);
			}
		}, 100); 
   },
   
   renderRadio : function(targetId,elmName,optsJson){
	   var radioHtml = "";
	   var checkedStyle = " checked";
	   for(var index in optsJson){
		   radioHtml = radioHtml + "<label class='radio-inline'><input type='radio' name='"+elmName+"' value='"+optsJson[index]+"'"+checkedStyle+">"+index+"</label>";
		   checkedStyle = "";
	   }
	   var intervalId = setInterval(function(){
			var target = $('#'+targetId);
			if(target.length > 0){
				target.append(radioHtml);
				clearInterval(intervalId);
			}
		}, 100); 
   },
   renderCheck : function(targetId,elmName,optsJson){
	   var checkboxHtml = "";
	   for(var index in optsJson){
		   checkboxHtml = checkboxHtml + "<label class='checkbox-inline'><input type='checkbox' name='"+elmName+"' value='"+optsJson[index]+"'>"+index+"</label>";
	   }
	   var intervalId = setInterval(function(){
			var target = $('#'+targetId);
			if(target.length > 0){
				target.append(checkboxHtml);
				clearInterval(intervalId);
			}
		}, 100); 
   },
   renderDatePicker : function(targetId,format){
	   var pickDate = format.indexOf('date')>=0;
	   var pickTime = format.indexOf('time')>=0;
	   var intervalId = setInterval(function(){
			var target = $('#'+targetId);
			if(target.length > 0){
				var time = target.parent();
				if(pickDate && pickTime){
					time.datetimepicker({
				        language:  'zh-CN',
				        weekStart: 1,
				        todayBtn:  1,
						autoclose: 1,
						todayHighlight: 1,
						startView: 2,
						forceParse: 0,
				        showMeridian: 1
				    });
				}else if(pickDate && !pickTime){
					time.datetimepicker({
				        language:  'zh-CN',
				        weekStart: 1,
				        todayBtn:  1,
						autoclose: 1,
						todayHighlight: 1,
						startView: 2,
						minView: 2,
						forceParse: 0
				    });
				}else {
					time.datetimepicker({
				        language:  'zh-CN',
				        weekStart: 1,
				        todayBtn:  1,
						autoclose: 1,
						todayHighlight: 1,
						startView: 1,
						minView: 0,
						maxView: 1,
						forceParse: 0
				    });
				}
				clearInterval(intervalId);
			}
		}, 100); 
   }
}
