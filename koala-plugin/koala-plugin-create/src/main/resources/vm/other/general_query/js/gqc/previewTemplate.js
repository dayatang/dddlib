$(function() {
	var id = window.location.search.split('=')[1];
	$.get(contextPath + '/preview/' + id + '.koala').done(function(data) {
		init(data.generalQuery);
	});
	var init = function(data) {
		var previewQuery = $('#previewQuery');
		var visiblePreQueryConditions = data.visiblePreQueryConditions;
		var trHtml = new Array();
		for (var i = 0, j = visiblePreQueryConditions.length; i < j; i++) {
			var preQueryCondition = visiblePreQueryConditions[i];
			if (preQueryCondition.visible) {
				trHtml.push('<tr>');
				trHtml.push('<td class="column-name">' + preQueryCondition.fieldName + '</td>')
				trHtml.push('<td class="query-condition">' + showQueryCondition(preQueryCondition.queryOperation) + '</td>')
				if (preQueryCondition.queryOperation == 'BETWEEN') {
					trHtml.push('<td class="query-value">' + preQueryCondition.startValue + '&nbsp;&nbsp;AND &nbsp;&nbsp;' + preQueryCondition.endValue + '</td>')
				} else {
					trHtml.push('<td class="query-value">' + preQueryCondition.value + '</td>')
				}
				trHtml.push('</tr>');
			}
		}
		var dynamicQueryConditions = data.dynamicQueryConditions;
		var flag = false;
		for (var i = 0, j = dynamicQueryConditions.length; i < j; i++) {
			var dynamicQueryCondition = dynamicQueryConditions[i];
			trHtml.push('<tr data-role="dynamicQueryCondition" class="dynamicQueryCondition"><input data-role="widgetType" type="hidden" value="' + dynamicQueryCondition.widgetType + '"/>');
			trHtml.push('<td class="column-name">' + dynamicQueryCondition.label + '<input data-role="fieldName" type="hidden" value="' + dynamicQueryCondition.fieldName + '"></td>')
			trHtml.push('<td class="query-condition">' + showQueryCondition(dynamicQueryCondition.queryOperation) + '<input data-role="queryOperation" type="hidden" value="' + dynamicQueryCondition.queryOperation + '"/></td>')
			if (dynamicQueryCondition.widgetType == 'TEXT') {
				if (dynamicQueryCondition.queryOperation == 'BETWEEN') {
					flag = true;
					trHtml.push('<td class="query-value"><input data-role="startValue" class="form-control" style="display:inline;"/><span>&nbsp;&nbsp;AND&nbsp;&nbsp;<span><input data-role="endValue" class="form-control" required="true" style="display:inline;"/></td>');
				} else {
					trHtml.push('<td class="query-value"><input data-role="value" class="form-control"/></td>')
				}
			} else {
				if (dynamicQueryCondition.queryOperation == 'BETWEEN') {
					flag = true;
					trHtml.push('<td class="query-value"><div style="width:18%;" data-role="startTime" class="input-group date form_datetime" style="width:160px;"><input class="form-control" size="16" type="text" value="" style="width:156px!important;"><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div>' + '<div class="span">&nbsp;AND&nbsp;</div>' + '<div style="width:18%;" data-role="endTime" class="input-group date form_datetime" style="width:160px;"><input class="form-control" size="16" type="text" value="" style="width:156px!important;"><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div></td>');
				} else {
					trHtml.push('<td class="query-value"><div data-role="time" class="input-group date form_datetime" style="width:160px;"><input class="form-control" size="16" type="text" value="" style="width:156px!important;"><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div></td>');
				}
			}
			trHtml.push('</tr>');
		}
		previewQuery.append($(trHtml.join('')));
		if (flag) {
			$('.query-body').css('width', '83%');
			previewQuery.find('.column-name, .query-condition').css('width', '10%').end().find('.query-value').css('width', '80%');
		}
		$('.buttons').css('top', previewQuery.height() - 36);
		previewQuery.find('[data-role="time"],[data-role="startTime"],[data-role="endTime"]').datetimepicker({
			language : 'zh-CN',
			format : "yyyy-mm-dd",
			autoclose : true,
			todayBtn : true,
			minView : 2,
			pickerPosition : 'bottom-left'
		});
		if(visiblePreQueryConditions.length == 0 && dynamicQueryConditions.length == 0){
			$('.query').hide();
			$('#previewGrid').css('margin-top', '10px');
		}

		var fieldDetails = data.fieldDetails;
		var columns = new Array();
		for (var i = 0, j = fieldDetails.length; i < j; i++) {
			var fieldDetail = fieldDetails[i];
			columns.push({
				title : fieldDetail.label,
				name : fieldDetail.fieldName,
				width : '200px'
			});
		}
		$('#previewGrid').grid({
			isShowButtons : false,
			isShowIndexCol : false,
			columns : columns,
			url : contextPath + '/search/' + id + '.koala'
		});
		$('#searchBtn').on('click', function() {
			var params = {};
			previewQuery.find('[data-role="dynamicQueryCondition"]').each(function() {
				var $this = $(this);
				var fieldName = $this.find('[data-role="fieldName"]').val();
				var widgetType = $this.find('[data-role="widgetType"]').val();
				if ($this.find('[data-role="queryOperation"]').val() == 'BETWEEN') {
					var startValue = null;
					var endValue = null;
					if (widgetType == 'TEXT') {
						startValue = $this.find('[data-role="startValue"]').val();
						endValue = $this.find('[data-role="endValue"]').val();
					} else {
						startValue = $this.find('[data-role="startTime"]').find('input').val();
						endValue = $this.find('[data-role="endTime"]').find('input').val();
					}
					params[fieldName + 'Start@'] = trim(startValue);
					params[fieldName + 'End@'] = trim(endValue);
				} else {
					var value = null;
					if (widgetType == 'TEXT') {
						value = $this.find('[data-role="value"]').val();
					} else {
						value = $this.find('[data-role="time"]').find('input').val();
					}
					params[fieldName] = trim(value);
				}
			});
			$('#previewGrid').data('koala.grid').search(params);
		})
	}
	var showQueryCondition = function(condition) {
		switch (condition) {
			case 'EQ':
				return '等于';
			case 'GT':
				return '大于';
			case 'GE':
				return '大于等于';
			case 'LT':
				return '小于';
			case 'LE':
				return '小于等于';
			case 'NE':
				return '不等于';
			case 'LIKE':
				return 'LIKE';
			case 'BETWEEN':
				return 'BETWEEN';
		}
	}
	var trim = function(s) {
		return s.replace(/(^\s*)|(\s*$)/g, "");
	}
})
