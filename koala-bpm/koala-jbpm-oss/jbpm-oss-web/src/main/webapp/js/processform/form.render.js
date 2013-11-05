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
	   var checkedStyle = "checked";
	   for(var index in optsJson){
		   radioHtml = radioHtml + "<div class='radio'><span class='"+checkedStyle+"'><input type='radio' style='opacity: 0;' checked name='"+elmName+"' value='"+optsJson[index]+"'></span></div>";
		   radioHtml = radioHtml + "<span style='position: relative; top: 8px;'>"+index+"</span>";
		   radioHtml = radioHtml + "&nbsp;&nbsp;";
		   checkedStyle = "";
	   }
	   $("#"+targetId).append(radioHtml);
	   var  $radios = $('[name="'+elmName+'"]');
	   $radios.on('click', function(){
		   $radios.each(function(){
	          $(this).parent().removeClass('checked');
	       });
	       $(this).parent().addClass('checked');
	    });
   },
   renderCheck : function(targetId,elmName,optsJson){
	   var radioHtml = "";
	   for(var index in optsJson){
		   radioHtml = radioHtml + "<div class='checker'><span><input type='checkbox'  style='opacity: 0;' id='"+elmName+optsJson[index]+"'></span></div>"+index;
		   radioHtml = radioHtml + "&nbsp;&nbsp;";
	   }
	   $("#"+targetId).append(radioHtml).find(':checkbox').live('click', function(){
           $(this).parent().toggleClass('checked');
       });
   },
   renderDatePicker : function(targetId,format){
	   
   }
}
