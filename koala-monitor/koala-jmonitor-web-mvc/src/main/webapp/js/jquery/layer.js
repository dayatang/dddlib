/**************************************************************

 @Name : layer v1.5.1 弹层组件开发版
 @author: 贤心
 @date: 2013-04-17
 @blog: http://sentsin.com
 @微博：http://weibo.com/SentsinXu
		http://t.qq.com/xian_xin
 @QQ群：176047238(layer交流群)
 @Copyright: Sentsin Xu(贤心)
 @官网说明：http://sentsin.com/jquery/layer
 @赞助layer: https://me.alipay.com/sentsin
		
 *************************************************************/

;void function(window, undefined){		

var pathType = true, //是否采用自动获取绝对路径。false：将采用下述变量中的配置
pathUrl = '../../lily/lib/layer/', //当前js所在路径，上述变量为false才有效

layerRun = function(){

var $ = jQuery, w = $(window), ready = {
	getPath: function(){
		var js = document.scripts || $('script'), jsPath = js[js.length - 1].src;
		return jsPath.substring(0, jsPath.lastIndexOf("/") + 1);
	}, append: function(){
		pathType && (pathUrl = this.getPath());
		var head = $('head')[0], link = document.createElement("link");
		link.setAttribute('type', 'text/css');
		link.setAttribute('rel', 'stylesheet');
		link.setAttribute('href', pathUrl +'skin/layer.css');
		head.appendChild(link);
	}, global: {
		iE6 : !-[1,] && !window.XMLHttpRequest,
		times : 0
	}
};

//默认内置方法，其它形式都可通过$.layer()任意拓展。
window['layer'] = {
	v : '1.5.1',	//版本号
	ready: function(callback){
		var load = '#layerCss';
		return $(load).ready(function(){
			callback();
		});
	}, alert: function(alertMsg , alertType, alertTit , alertYes){	//普通对话框，类似系统默认的alert()
		return $.layer({
			dialog : {msg : alertMsg, type : alertType, yes : alertYes},
			title : alertTit,
			area: ['auto', 'auto']
		});
	}, confirm: function(conMsg  , conYes , conTit , conNo){ //询问框，类似系统默认的confirm()
		return $.layer({
			dialog : {msg : conMsg, type : 4, btns : 2, yes : conYes, no : conNo},
			title : conTit
		}); 
	}, msg: function(msgText , msgTime , msgType , callback){ //普通消息框，一般用于行为成功后的提醒,默认两秒自动关闭
		(msgText == '' || msgText == undefined) && (msgText = '&nbsp;');
		(msgTime == undefined || msgTime == '') && (msgTime = 2);
		return $.layer({
			dialog : {msg : msgText, type : msgType},
			time : msgTime,
			title : ['', false],
			closeBtn : ['', false], end: function(){callback && callback()}
		});	
	}, tips: function(html , follow , time , maxWidth, guide, style){
		return $.layer({
			type : 4,
			shade : false,
			time : time,
			maxWidth : maxWidth,
			tips : {msg: html, guide: guide, follow: follow, style: style}	
		})
	}, load: function(loadTime , loadgif , loadShade){
		var border = true;
		loadgif === 3 && (border = false);
		return $.layer({
			time : loadTime,
			shade : loadShade,
			loading : {type : loadgif},
			border :[10,0.3,'#000',border],
			type : 3,
			title : ['',false],
			closeBtn : [0 , false]
		}); 
	}
};

var Class = function(setings){	
	ready.global.times++;
	this.index = ready.global.times;
	var config = this.config;
	this.config = $.extend({} , config , setings);
	this.config.dialog = $.extend({}, config.dialog , setings.dialog);
	this.config.page = $.extend({}, config.page , setings.page);
	this.config.iframe = $.extend({}, config.iframe , setings.iframe);	
	this.config.loading = $.extend({}, config.loading , setings.loading);
	this.config.tips = $.extend({}, config.tips , setings.tips);
	this.adjust();
};

Class.pt = Class.prototype;

//默认配置
Class.pt.config = {
	skin : 0,
	type : 0,
	shade : [0.5 , '#000' , true],
	shadeClose : false,
	fix : true,
	move : ['.xubox_title' , true],
	title : ['信息' , true],
	offset : ['200px' , '50%'],
	area : ['310px' , 'auto'],
	closeBtn : [0 , true],
	time : 0,
	border : [10 , 0.3 , '#000', true],
	zIndex : 19891014, 
	maxWidth : 400,
	fadeIn : [300 , false],
	dialog : {btns : 1, btn : ['确定','取消'], type : 3, msg : '', yes : function(index){ layer.close(index);}, no : function(index){ layer.close(index);}
	},
	page : {dom: '#xulayer', html: ''},
	iframe : {src: 'http://sentsin.com'},
	loading : {type: 0},
	tips : {msg: '', follow: '', guide: 0, style: ['background-color:#FF9900; color:#fff;', '#FF9900']},
	success : function(layer){}, //创建成功后的回调
	close : function(index){ layer.close(index);}, //右上角关闭回调
	end : function(){} //终极销毁回调
};

Class.pt.type = ['dialog', 'page', 'iframe', 'loading', 'tips'];

//层容器
Class.pt.space = function(html){
	var html = html || '', times = this.index, config = this.config, dialog = config.dialog, frame = [
		'<div class="xubox_dialog"><span class="xubox_msg xulayer_png32 xubox_msgico xubox_msgtype' + dialog.type + '"></span><span class="xubox_msg xubox_text">' + dialog.msg + '</span></div>',	
		'<div class="xubox_page xubox_page_' + config.skin + '">'+ html +'</div>',
		'<iframe allowtransparency="true" id="xubox_iframe" name="xubox_iframe" onload="javascript:$(\'.xubox_layer\').find(\'.xubox_iframe\').removeClass(\'xubox_load\');" class="xubox_iframe" frameborder="0" src="' + config.iframe.src + '"></iframe>',				
		'<span class="xubox_loading xubox_loading_'+ config.loading.type +'"></span>',
		'<div class="xubox_tips xubox_tips_' + config.skin +'" style="'+ config.tips.style[0] +'"><div class="xubox_tipsMsg">'+ config.tips.msg +'</div><i class="layerTipsG"></i></div>'
	];		
	
	var shade = '' , border = '';
	this.zIndex = config.zIndex + times;
	
	var zIndex = this.zIndex, shadeStyle = "z-index:"+ zIndex +"; background-color:"+ config.shade[1] +"; opacity:"+ config.shade[0] +"; filter:alpha(opacity="+ config.shade[0]*100 +");";
	config.shade[2] && (shade = '<div times="'+ times +'" id="xubox_shade' + times + '" class="xubox_shade xubox_shade_' + config.skin + '" style="'+ shadeStyle +'"></div>');	
	
	var borderStyle = "z-index:"+ (zIndex-1) +";  background-color: "+ config.border[2] +"; opacity:"+ config.border[1] +"; filter:alpha(opacity="+ config.border[1]*100 +"); top:-"+ config.border[0] +"px; left:-"+  config.border[0] +"px;";
	config.border[3] && (border = '<div id="xubox_border'+ times +'" class="xubox_border" style="'+ borderStyle +'"></div>');
	var boxhtml = '<div times="'+ times +'" showtime="'+ config.time +'" style="z-index:'+ zIndex +'" id="xubox_layer'+ times +'" class="xubox_layer xubox_layer_' + config.skin + '">'	
		+ '<div style="z-index:'+ zIndex +'" class="xubox_main xubox_main_' + config.skin +'">'
		+ frame[config.type]
		+ '<h2 class="xubox_title xubox_title_' + config.skin + '"><em>' + config.title[0] + '</em></h2>'
		+ '<a class="xubox_close xulayer_png32 xubox_close' + config.closeBtn[0] + '_'+ config.skin +'" href="javascript:;"></a>'
		+ '<span class="xubox_botton"></span>'
		+ '</div>'+ border + '</div>';
		
	return [shade , boxhtml];
};

//插入layer
Class.pt.adjust = function(){
	var othis = this , space = '', config = this.config, dialog = config.dialog, title = othis.config.title;
	title.constructor === Array || (othis.config.title = [title, true]);
	title === false && (othis.config.title = [title, false]);
	var page = config.page, setSpace = function(html){
		var html = html || ''
		space = othis.space(html);
		$("body").append(space[0]);
	};
	switch(config.type){
		case 1:
			if(page.html !== ''){
				setSpace('<div id="xuboxPageHtml">'+ page.html +'</div>');
				$("body").append(space[1]);
			}else{
				if($(page.dom).parents('.xubox_page').length == 0){
					setSpace();
					$(page.dom).show().wrap(space[1]);
				}else{
					return false;	
				}
			}
		break;
		case 2:
			$('.xubox_layer').find('.xubox_iframe').length > 0 && layer.close($('.xubox_iframe').parents('.xubox_layer').attr('times'));
			setSpace();
			$("body").append(space[1]);
		break;
		case 3:
			 config.title = ['',false];
			 config.area = ['auto','auto']; 
			 config.closeBtn = ['',false];
			 $('.xubox_layer').find('.xubox_loading').length > 0 && layer.close($('.xubox_loading').parents('.xubox_layer').attr('times'));
			 setSpace();
			 $("body").append(space[1]);
		break;
		case 4:
			config.title = ['',false];
			config.area = ['auto','auto'];
			config.fix = false;
			config.border = [0,0,0,false];
			$('.xubox_layer').find('.xubox_tips').length > 0 && layer.close($('.xubox_tips').parents('.xubox_layer').attr('times'));
			setSpace();
			$("body").append(space[1]);
			$('.xubox_tips').fadeIn(200).parents('.xubox_layer').find('.xubox_close').css({top : 5 , right : 5});
		break;
		default : 
			config.title[1] || (config.area = ['auto','auto']);
			$('.xubox_layer').find('.xubox_dialog').length > 0 && layer.close($('.xubox_dialog').parents('.xubox_layer').attr('times'));
			setSpace();
			$("body").append(space[1]);
		break;
	};
	var times = ready.global.times;
	this.layerS = $('#xubox_shade' + times);
	this.layerB = $('#xubox_border' + times);
	this.layerE = $('#xubox_layer' + times);
	var layerE = this.layerE;		
	//设置layer面积坐标等数据 
	if(config.offset[1].indexOf("px") != -1){
		var _left = parseInt(config.offset[1]);
	}else{
		if(config.offset[1] == '50%'){
			var _left =  config.offset[1];
		}else{
			var _left =  parseInt(config.offset[1])/100*w.width();
		}
	};
	layerE.css({left : _left + config.border[0] , width : config.area[0] , height : config.area[1]});
	config.fix ? layerE.css({top : parseInt(config.offset[0]) + config.border[0]}) : layerE.css({top : parseInt(config.offset[0]) + w.scrollTop() + config.border[0] , position : 'absolute'});	
	this.layerMian = layerE.find('.xubox_main');
	this.layerTitle = layerE.find('.xubox_title');
	this.layerText = layerE.find('.xubox_text');
	this.layerPage = layerE.find('.xubox_page');
	this.layerBtn = layerE.find('.xubox_botton');			
	
	//配置按钮 对话层形式专有
	if(config.type == 0 && config.title[1]){
		switch(dialog.btns){
			case 0:
				this.layerBtn.html('').hide();
			break;
			case 2:
				this.layerBtn.html('<a href="javascript:;" class="xubox_yes xubox_botton2_'+ config.skin +'">'+ dialog.btn[0] +'</a>' + '<a href="javascript:;" class="xubox_no xubox_botton3_'+ config.skin +'">'+ dialog.btn[1] + '</a>');
			break;
			default:
				this.layerBtn.html('<a href="javascript:;" class="xubox_yes xubox_botton1_'+ config.skin +'">'+ dialog.btn[0] +'</a><a class="xubox_no" style="displa:none;"></a>');
		}
	};
	if(layerE.css('left') === 'auto'){
		layerE.hide();
		setTimeout(function(){
			layerE.show();
			othis.pagetype(times);
		}, 500);
	}else{
		this.pagetype(times);
	}
	this.callback();
};

//调整layer	
Class.pt.pagetype = function(times){
	var othis = this, layerE = this.layerE, config = this.config, page = config.page;
	othis.autoArea(times);
	config.time <= 0 || this.autoclose();
	config.move[1] ? layerE.find('.xubox_title').css({'cursor':'move'}) : layerE.find('.xubox_title').css({'cursor':'auto'});		
	config.closeBtn[1] || layerE.find('.xubox_close').remove().hide();
	if(!config.title[1]){
		layerE.find('.xubox_title').remove().hide();
		config.type != 4 && layerE.find('.xubox_close').removeClass('xubox_close0_' + config.skin).addClass('xubox_close1_' + config.skin);
	}else{
		this.layerTitle.css({width : layerE.outerWidth()});	
	};		
	config.border || layerE.removeClass('xubox_layerBoder');
	var maRight = parseInt(this.layerMian.css('margin-right')),
	paBottom = parseInt(layerE.css('padding-bottom'));
	layerE.attr({'type' :  othis.type[config.type]});
	switch(config.type){
		case 1: 	
			layerE.find(page.dom).addClass('layer_pageContent');
			this.layerPage.css({'width' : layerE.width() - 2*maRight});
			config.shade && layerE.css({'z-index' : othis.zIndex + 1});
			config.title[1] ? this.layerPage.css({'top' : maRight + this.layerTitle.outerHeight()}) : this.layerPage.css({'top' : maRight});
		break;
		case 2:
			layerE.find('.xubox_iframe').addClass('xubox_load').css({'width' : layerE.width() - 2*maRight});
			config.title[1] ? layerE.find('.xubox_iframe').css({'top' : this.layerTitle.height() + paBottom ,'height' : layerE.height() - this.layerTitle.height()}): layerE.find('.xubox_iframe').css({top : paBottom , height : layerE.height()});
			 ready.global.iE6 && layerE.find('#xubox_iframe').attr("src" , config.iframe.src);
		break;
		case 4 :
			var fow = $(config.tips.follow), ftop = fow.offset().top, top = ftop - layerE.outerHeight();
			var fleft = fow.offset().left, left = fleft, color = config.tips.style[1];
			var fHeight = fow.outerHeight(), fWidth = fow.outerWidth(), tipsG = layerE.find('.layerTipsG');
			fWidth > config.maxWidth && tipsG.width(config.maxWidth);
			if(config.tips.guide === 1){
				var offleft = w.width() - left - fWidth - layerE.outerWidth() - 10, top = ftop;
				if(offleft > 0){
					left = left + fow.outerWidth() + 10;
					tipsG.removeClass('layerTipsL').addClass('layerTipsR').css({'border-right-color': color});
				}else{
					left = left - layerE.outerWidth() - 10
					tipsG.removeClass('layerTipsR').addClass('layerTipsL').css({'border-left-color': color});
				}
			}else{
				if(top - w.scrollTop() - 12 <= 0){
					top = ftop + fHeight + 10;
					tipsG.removeClass('layerTipsT').addClass('layerTipsB').css({'border-bottom-color': color});
				}else{
					top = top - 10;
					tipsG.removeClass('layerTipsB').addClass('layerTipsT').css({'border-top-color': color});
				}
			}
			
			layerE.css({top : top , left : left});
		break;	
		default :
			this.layerMian.css({'background-color': '#fff'});
			if(config.title[1]){
				this.layerText.css({paddingTop : 18 + this.layerTitle.outerHeight()});
			}else{
				layerE.find('.xubox_msgico').css({top : '10px'});
				this.layerText.css({marginTop : 12})	
			}
		break;
	};
	var fadeTime = 0; config.fadeIn[1] && (fadeTime = config.fadeIn[0]);
	layerE.animate({opacity : 1 , filter : 'alpha(opacity='+ 100 +')'},fadeTime);
	this.move();
};

//自适应宽高
Class.pt.autoArea = function(times){
	var othis = this, layerE = $('#xubox_layer' + times), config =this.config, page = config.page,
	layerMian = layerE.find('.xubox_main'), layerBtn = layerE.find('.xubox_botton'), layerText = layerE.find('.xubox_text'),
	layerPage = layerE.find('.xubox_page'), layerB = $('#xubox_border' + times);
	if(config.area[0] == 'auto' && layerMian.outerWidth() >= config.maxWidth){	
		layerE.css({width : config.maxWidth});
	}
	config.title[1] ? titHeight =  layerE.find('.xubox_title').innerHeight() : titHeight = 0;
	switch(config.type){
		case 0:
			var aBtn = layerBtn.find('a'),
			outHeight =  layerText.outerHeight() + 20;
			if(aBtn.length > 0){
				var btnHeight = layerBtn.find('a').outerHeight() +  20;
			}else{
				var btnHeight = 0;
			}
		break;
		case 1:
			var btnHeight = 0,outHeight = $(page.dom).outerHeight();
			config.area[0] == 'auto' && layerE.css({width : layerPage.outerWidth()});
		break;
		case 3:
			var btnHeight = 0; var outHeight = $(".xubox_loading").outerHeight(); 
			layerMian.css({width : $(".xubox_loading").width()});
		break;
	};
	(config.offset[1] == '50%' || config.offset[1] == '') && (config.type !== 4) ? layerE.css({marginLeft : -layerE.outerWidth()/2}) : layerE.css({marginLeft : 0});
	(config.area[1] == 'auto') && layerMian.css({height : titHeight + outHeight + btnHeight});
	layerB.css({width : layerE.outerWidth() + 2*config.border[0] , height : layerE.outerHeight() + 2*config.border[0]});
	(ready.global.iE6 && config.area[0] != 'auto') && layerMian.css({width : layerE.outerWidth()});
};

//拖拽层
Class.pt.move = function(){
	var config = this.config, layerMove = this.layerE.find(config.move[0]), layerE, ismove;
	var moveX, moveY, move, setY = 0;
	config.move[1] && layerMove.attr('move','ok');
	$(config.move[0]).on('mousedown', function(M){	
		M.preventDefault();
		if($(this).attr('move') === 'ok'){
			ismove = true;
			layerE = $(this).parents('.xubox_layer'), index = layerE.attr('times');
			var xx = layerE.offset().left, yy = layerE.offset().top, ww = layerE.width() - 6, hh = layerE.height() - 6;
			if(!$('#xubox_moves')[0]){
				$('body').append('<div id="xubox_moves" class="xubox_moves" style="left:'+ xx +'px; top:'+ yy +'px; width:'+ ww +'px; height:'+ hh +'px; z-index:30000000"></div>');
			}
			move = $('#xubox_moves');
			moveX = M.pageX - move.position().left;
			moveY = M.pageY - move.position().top;
			layerE.css('position') !== 'fixed' || (setY = w.scrollTop());
		}
	});
	$(document).mousemove(function(M){			
		if(ismove){
			M.preventDefault();
			var x = M.pageX - moveX;
			if(layerE.css('position') === 'fixed'){
				var y = M.pageY - moveY;	
			}else{
				var y = M.pageY - moveY;	
			}
			move.css({left: x, top: y});									
		}					  						   
	}).mouseup(function(){
		if(ismove){
			if(parseInt(layerE.css('margin-left')) == 0){
				var lefts = parseInt(move.css('left'));
			}else{
				var lefts = parseInt(move.css('left')) + (-parseInt(layerE.css('margin-left')))
			}
			layerE.css('position') === 'fixed' || (lefts = lefts - layerE.parent().offset().left);
			layerE.css({left: lefts, top: parseInt(move.css('top')) - setY});
			move.remove();
		}
		ismove = false;
	});
};

//自动关闭layer
Class.pt.autoclose = function(){
	var othis = this, time = this.config.time;
	var maxLoad = function(){
		time--;
		if(time === 0){
			layer.close(othis.index);
			clearInterval(othis.autotime);
		}
	};
	this.autotime = setInterval(maxLoad , 1000);
};

ready.config = {
	end : {}
};

Class.pt.callback = function(){
	this.openLayer();
	var othis = this, layerE = this.layerE, config = this.config, dialog = config.dialog;
	this.config.success(layerE);
	ready.global.iE6 && this.IE6();
	layerE.find('.xubox_close').off('click').on('click', function(e){
		e.preventDefault();
		config.close(othis.index);
	});
	layerE.find('.xubox_yes').off('click').on('click',function(e){
		e.preventDefault();
		dialog.yes(othis.index);
	});
	layerE.find('.xubox_no').off('click').on('click',function(e){
		e.preventDefault();
		dialog.no(othis.index);
	});
	this.layerS.off('click').on('click', function(e){
		e.preventDefault();
		othis.config.shadeClose && layer.close(othis.index);
	});
	ready.config.end[this.index] = config.end;
};

Class.pt.IE6 = function(){
	var othis = this, layerE = this.layerE, select = $('select');
	var _ieTop =  layerE.offset().top;	
	
	 //ie6的固定与相对定位
	if(this.config.fix){
		var ie6Fix = function(){
			layerE.css({top : $(document).scrollTop() + _ieTop});
		};	
	}else{
		var ie6Fix = function(){
			layerE.css({top : _ieTop});	
		};
	}
	ie6Fix();
	w.scroll(ie6Fix);
	
	 //隐藏select
	$.each(select, function(index , value){
		var sthis = $(this);
		if(!sthis.parents('.xubox_layer')[0]){
			sthis.css('display') == 'none' || sthis.attr({'layer' : '1'}).hide();
		}
	});
	
	 //恢复select
	this.reselect = function(){
		$.each(select, function(index , value){
			var sthis = $(this);
			if(!sthis.parents('.xubox_layer')[0]){
				(sthis.attr('layer') == 1 && $('.xubox_layer').length < 1) && sthis.removeAttr('layer').show(); 
			}
		});
	};
};

//给layer对象拓展方法
Class.pt.openLayer = function(){
	var othis = this;
	
	//自适应宽高
	layer.autoArea = function(index){
		return othis.autoArea(index);
	};
	
	//获取layer当前索引
	layer.getIndex = function(selector){
		return $(selector).parents('.xubox_layer').attr('times');	
	};
	
	//获取子iframe的DOM
	layer.getChildFrame = function(selector){
		return $("#xubox_iframe").contents().find(selector);	
	};
	
	//得到iframe层的索引，子iframe时使用
	layer.getFrameIndex = function(){
		return $('#xubox_iframe').parents('.xubox_layer').attr('times');
	};
	
	//iframe层自适应宽高
	layer.iframeAuto = function(){
		var wh = [this.getChildFrame('body').outerWidth(), this.getChildFrame('body').outerHeight()],
		lbox = $('#xubox_iframe').parents('.xubox_layer'), ids = lbox.attr('times'),
		tit = lbox.find('.xubox_title'), titHt = 0;
		!tit || (titHt = tit.height());
		lbox.css({width: wh[0], height: wh[1] + titHt});
		tit.css({width: wh[0]});
		var bs = -parseInt($('#xubox_border'+ ids).css('left'));
		$('#xubox_border'+ ids).css({width: wh[0] + 2*bs, height: wh[1] + 2*bs + titHt});
		$('#xubox_iframe').css({width: wh[0], height: wh[1]});
	};
	
	//关闭layer
	layer.close = function(index){
		var layerNow = $('#xubox_layer' + index), shadeNow = $('#xubox_shade' + index);
		if(layerNow.attr('type') == othis.type[1]){
			if(layerNow.find('#xuboxPageHtml')[0]){
				layerNow.remove();
			}else{
				layerNow.find('.xubox_close,.xubox_botton,.xubox_title,.xubox_border').remove();
				for(var i = 0 ; i < 3 ; i++){
					layerNow.find('.layer_pageContent').unwrap().hide();
				}
			}
		}else{
			!-[1,] || layerNow.find('#xubox_iframe').remove();
			layerNow.remove();
		}
		shadeNow.remove();
		ready.global.iE6 && othis.reselect();
		typeof ready.config.end[index] === 'function' && ready.config.end[index]();
		delete ready.config.end[index];
	};
	
	//关闭加载层，仅loading私有
	layer.loadClose = function(){
		var parent = $('.xubox_loading').parents('.xubox_layer'),
		index = parent.attr('times');
		layer.close(index);
	};
	
	//出场内置动画
	layer.shift = function(type, rate){
		var config = othis.config, iE6 = ready.global.iE6, layerE = othis.layerE, cutWth = 0, ww = w.width(), wh = w.height();
		(config.offset[1] == '50%' || config.offset[1] == '') ? cutWth = layerE.outerWidth()/2 : cutWth = layerE.outerWidth();
		var anim = {
			t: {top : config.border[0]},
			b: {top : wh - layerE.outerHeight() - config.border[0]},
			cl: cutWth + config.border[0],
			ct: -layerE.outerHeight(),
			cr: ww - cutWth - config.border[0],
			fn: function(){
				iE6 && othis.IE6();	
			}
		};
		switch(type){
			case 'left-top':
				layerE.css({left: anim.cl, top: anim.ct}).animate(anim.t, rate, anim.fn);
			break; 
			case 'right-top':
				layerE.css({left: anim.cr, top: anim.ct}).animate(anim.t, rate, anim.fn);
			break; 
			case 'left-bottom':
				layerE.css({left: anim.cl, top: wh}).animate(anim.b, rate, anim.fn);
			break; 
			case 'right-bottom':
				layerE.css({left: anim.cr, top: wh}).animate(anim.b, rate, anim.fn);
			break;
		};	
	};
	
	layer.setMove = function(){
		return othis.move();
	}
};

ready.append();
$.layer = function(deliver){
	var o = new Class(deliver);
	return o.index;
};

};

//为支持CMD规范的模块加载器
var require = '../../init/jquery'; //若采用seajs，需正确配置jquery的相对路径。未用可无视此处。
if(typeof define === 'function' && typeof seajs === 'object'){
	define([require], function(require, exports, module){
		layerRun();
		exports.layer = [window.layer, window['$'].layer];
	});
}else{
	layerRun();
}

}(window);