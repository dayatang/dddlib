var Validation = {
	notNull: function($container, $element, content, errorMessage) {
		if (isNull(content)) {
			showErrorMessage($container, $element, errorMessage);
			return false;
		}
		return true;
	},
    email: function($container, $element, content, errorMessage) {
    	var reg = new RegExp(/^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/);
    	if (!reg.test(content)) {
    		showErrorMessage($container, $element, errorMessage);
    		return false;
    	}
    	return true;
    },
    serialNmuber: function($container, $element, content, errorMessage) {
    	var reg = new RegExp(/^[0-9a-zA-Z_-]*$/);
    	if (!reg.test(content)) {
    		showErrorMessage($container, $element, errorMessage);
    		return false;
    	}
    	return true;
    },
    humanName: function($container, $element, content, errorMessage) {
    	var reg = new RegExp(/(^[A-Z a-z]*$)|(^[\u4e00-\u9fa5]*$)/);
    	if (!reg.test(content)) {
    		showErrorMessage($container, $element, errorMessage);
    		return false;
    	}
    	return true;
    },
    checkByRegExp: function($container, $element, regExp, content, errorMessage){
    	var reg = new RegExp(regExp);
    	if (!reg.test(content)) {
    		showErrorMessage($container, $element, errorMessage);
    		return false;
    	}
    	return true;
    }
};

/**
 * 检查是否为空
 */
var isNull = function(item){
	if(item == 'NULL' || item == null || item == "" || item.replace(/(^\s*)|(\s*$)/g, "") == "" ){
		return true;
	}
	return false;
};

/**
 * 显示提示信息
 */
var showErrorMessage = function($container, $element, content){
	$element.popover({
		content: content,
		trigger: 'manual',
		container: $container
	}).popover('show').on({
			'blur': function(){
				$element.popover('destroy');
				$element.parent().removeClass('has-error');
			},
			'keydown': function(){
				$element.popover('destroy');
				$element.parent().removeClass('has-error');
			}
	}).focus().parent().addClass('has-error');
};
