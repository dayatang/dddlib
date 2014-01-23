var monitorNodeManager = {

	/**
	 * 同步监控数据配置
	 */
	openSyncDataConfDig : function(grid) {
		$.get(contextPath + '/pages/monitor/syncDataConfigTemplate.html').done(function(result) {
			var dialog = $(result);
			var scheduleActive = dialog.find('[name="schedule_active"]');
			var syncInterval = dialog.find('#syncInterval');
			scheduleActive.on('click', function() {
				scheduleActive.each(function() {
					$(this).parent().removeClass('checked');
				})
				$(this).parent().addClass('checked');
			});
			$.get(contextPath + '/monitor/NodeInfo/getScheduleConf.koala').done(function(result) {
				var data = result.data;
				if (!data.active) {
					dialog.find('[name="schedule_active"][value="1"]').removeAttr('checked', 'checked').parent().removeClass('checked');
					dialog.find('[name="schedule_active"][value="0"]').attr('checked', 'checked').parent().addClass('checked');
				}
				syncInterval.val(data.interval);
				dialog.find('#lastSyncTime').html(data.lastBeginRunTime);
			});
			dialog.modal({
				keyboard : true
			}).on('hidden', function() {
				$(this).remove();
			}).find('#save').on('click', function() {
				if (!Validation.number(dialog, syncInterval, syncInterval.val(), '数值不合法')) {
					return;
				}
				var params = {
					active : dialog.find(':radio:checked').val(),
					interval : syncInterval.val()
				};
				$.post(contextPath + '/monitor/NodeInfo/updateScheduleConf.koala', params).done(function(data) {
					if (data.result) {
						grid.message({
							type : 'success',
							content : '保存成功'
						});
						dialog.modal('hide');
					} else {
						dialog.find('.modal-content').message({
							type : 'error',
							content : '保存失败'
						});
					}
				}).fail(function(data) {
					dialog.find('.modal-content').message({
						type : 'error',
						content : '保存失败'
					});
				});
			});
		});
	},
	/**
	 * 修改监控参数
	 * @param childIndex
	 * @param index
	 */
	updateMonitorConfig : function(childIndex, index) {
		var grid = $('#monitorNodeGrid').getGrid();
		var item = grid.getItemByIndex(index);
		var childItem = item.conponents[childIndex];
		$.get(contextPath + '/pages/monitor/monitorParamTemplate.html').done(function(result) {
			var dialog = $(result);
			dialog.find('#monitorType').html(childItem.name);
			var status = dialog.find('[name="status"]');
			var syncInterval = dialog.find('#syncInterval');
			var tracetime = dialog.find('#tracetime');
			var tracetimeout = dialog.find('#tracetimeout');
			var includeUrls = dialog.find('#includeUrls');
			var excludeUrls = dialog.find('#excludeUrls');
			var stacks = dialog.find('[name="stack-status"]');
			var detectClazzs = dialog.find('#detect-clazzs');
			var detectPackages = dialog.find('#detect-packages');
			if (childItem.type != 'HTTP') {
				includeUrls.parent().hide();
				excludeUrls.parent().hide();
			}
			if (childItem.type != 'METHOD') {
				detectClazzs.parent().hide();
				detectPackages.parent().hide();
			} else {
				dialog.find('.stack').parent().show();
			}
			status.on('click', function() {
				status.each(function() {
					$(this).parent().removeClass('checked');
				})
				$(this).parent().addClass('checked');
			});
			stacks.on('click', function() {
				stacks.each(function() {
					$(this).parent().removeClass('checked');
				})
				$(this).parent().addClass('checked');
			});
			if (!childItem.active) {
				dialog.find('[name="status"][value="true"]').removeAttr('checked', 'checked').parent().removeClass('checked');
				dialog.find('[name="status"][value="false"]').attr('checked', 'checked').parent().addClass('checked');
			}
			tracetime.val(childItem.properties['trace-filter-active-time']);
			tracetimeout.val(childItem.properties['trace-timeout']);
			if (childItem.type == 'HTTP') {
				includeUrls.val(childItem.properties.includeUrls);
				excludeUrls.val(childItem.properties.excludeUrls);
			}
			if (childItem.type == 'METHOD') {
				if (childItem.properties['trace-stack'] == 'false') {
					dialog.find('[name="stack-status"][value="true"]').removeAttr('checked', 'checked').parent().removeClass('checked');
					dialog.find('[name="stack-status"][value="false"]').attr('checked', 'checked').parent().addClass('checked');
				}
				var detectClazzsVal = childItem.properties['detect-clazzs'];
				var detectPackagesVal = childItem.properties['detect-packages'];
				detectClazzsVal && detectClazzs.val(detectClazzsVal);
				detectPackagesVal && detectPackages.val(detectPackagesVal);
			}
			dialog.modal({
				keyboard : true
			}).on('hidden', function() {
				$(this).remove();
			}).find('#save').on('click', function() {
				if (!Validation.number(dialog, tracetime, tracetime.val(), '数值不合法')) {
					return;
				}
				if (!Validation.number(dialog, tracetimeout, tracetimeout.val(), '数值不合法')) {
					return;
				}
				var params = {};
				params.active = dialog.find('[name="status"]:radio:checked').val();
				params.compType = childItem.type;
				params.nodeId = item.nodeId;
				if (childItem.type == 'HTTP') {
					params.props = 'trace-filter-active-time->' + tracetime.val() + '@@@trace-timeout->' + tracetimeout.val() + '@@@includeUrls->' + includeUrls.val() + '@@@excludeUrls->' + excludeUrls.val();
				} else if (childItem.type == 'METHOD') {
					params.props = 'trace-filter-active-time->' + tracetime.val() + '@@@trace-timeout->' + tracetimeout.val() + '@@@detect-clazzs->' + detectClazzs.val() + '@@@detect-packages->' + detectPackages.val();
					params.stack = dialog.find('[name="stack-status"]:radio:checked').val();
				} else {
					params.props = 'trace-filter-active-time->' + tracetime.val() + '@@@trace-timeout->' + tracetimeout.val();
				}
				$.post(contextPath + '/monitor/NodeInfo/updateMonitorConfig.koala', params).done(function(result) {
					if (result.result) {
						$('#monitorNodeGrid').message({
							type : 'success',
							content : '保存成功'
						});
						dialog.modal('hide');
						$('#monitorNodeGrid').grid('refresh');
					} else {
						dialog.find('.modal-content').message({
							type : 'error',
							content : '保存失败'
						});
					}
				}).fail(function(result) {
					dialog.find('.modal-content').message({
						type : 'error',
						content : '保存失败'
					});
				})
			})
		})
	},

	/**
	 * 实时监控信息
	 * @param nodeId
	 */
	pageServerSummryInfo : function(nodeId) {
		var self = this;
		$.get(contextPath + '/pages/monitor/pageServerSummryInfo.html').done(function(data) {
			var dialog = $(data);
			dialog.modal({
				keyboard : true
			}).on({
				'hidden.bs.modal' : function() {
					$(this).remove();
				},
				'shown.bs.modal' : function() {
					self.initData(nodeId, $(this));
				}
			});
			//兼容IE8 IE9
			if (window.ActiveXObject) {
				if (parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10) {
					dialog.trigger('shown.bs.modal');
				}
			}
		});
	},

	initData : function(nodeId, dialog) {
		var self = this;
		$.get(contextPath + '/monitor/NodeInfo/serverSummryInfo.koala?nodeId=' + nodeId).done(function(data) {
			dialog.find('#activeCount').text(data.activeCount);
			dialog.find('#pageAvgResponseTime').html(data.pageAvgResponseTime);
			dialog.find('#maxAvgTimePage').html(data.maxAvgTimePage);
			dialog.find('#mostCallMethod').html(data.mostCallMethod);
			dialog.find('#maxAvgTimeMethod').html(data.maxAvgTimeMethod);
			var exceptionRate = dialog.find('#exceptionRate');
			dialog.find('#exceptionRateValue').html(data.exceptionRate);
			exceptionRate.css('width', data.exceptionRate);
			var serverInfo = JSON.parse(data.serverInfo);
			if (serverInfo && serverInfo.error) {
				$("div.error").html(serverInfo.error);
				return;
			}
			$("#serverinfo").show();
			var memData = serverInfo.MEMORY;
			if (memData) {
				var dataArray = [[]];
				for (var index in memData) {
					dataArray[0].push([index, memData[index]]);
				}
				self.drawMemStatusChart(dataArray);
			}
			var cpuData = serverInfo.CPU;
			if (cpuData) {
				self.drawCpuStatusChart(cpuData);
			}

			var diskData = serverInfo.DISK;
			if (diskData) {
				self.drawDiskStatusChart(diskData);
			}
		});
	},
	/**
	 * 渲染内存数据图形
	 * @param datas
	 */
	drawMemStatusChart : function(datas) {
		$.jqplot('memchart', datas, {
			axes : {
				xaxis : {
					renderer : $.jqplot.DateAxisRenderer,
					tickOptions : {
						formatString : '%m-%d %H'
					}
					//tickInterval:'2 hour'
				},
				yaxis : {
					min : 0,
					max : 100
				}
			},
			//悬浮展现控制
			highlighter : {
				show : true,
				yvalues : 1,
				tooltipAxes : "xy",
				formatString : '<table class="jqplot-highlighter"><tr><td>时段:%s</td></tr><tr><td>内存使用率:%s%</td></tr></table>'
			},
			cursor : {
				show : true
			}
		});
	},

	/**
	 * 渲染CPU监控图形
	 * @param datas
	 */
	drawCpuStatusChart : function(datas) {
		var i = 0;
		for (var index in datas) {
			var title = index;
			var dataArray = [[]];
			var data = datas[index]
			for (var index2 in data) {
				dataArray[0].push([index2, data[index2]]);
			}
			var cpuchartId = "cpuchart" + i;
			$("<div id='" + cpuchartId + "'></div>").appendTo($("#cpuchart"));
			$.jqplot(cpuchartId, dataArray, {
				title : title,
				axes : {
					xaxis : {
						renderer : $.jqplot.DateAxisRenderer,
						tickOptions : {
							formatString : '%m-%d %H'
						}
						//tickInterval:'1 hour'
					},
					yaxis : {
						min : 0,
						max : 100
					}
				},
				//悬浮展现控制
				highlighter : {
					show : true,
					yvalues : 1,
					tooltipAxes : "xy",
					formatString : '<table class="jqplot-highlighter"><tr><td>时段:%s</td></tr><tr><td>CPU使用率:%s%</td></tr></table>'
				},
				cursor : {
					show : true
				}

			});
			i++;
		}
	},

	/**
	 * 渲染硬盘监控图形
	 * @param datas
	 */
	drawDiskStatusChart : function(datas) {
		var diskInfo;
		for (var disk in datas) {
			diskInfo = datas[disk];
			var diskpieId = "diskpie" + disk.replace(/[^a-zA-Z0-9]/g, "");
			$("<div class='disk-child'><div class='diskname'>磁盘名称：" + disk.replace(/[^a-zA-Z0-9]/g, "") + "</div><div id='" + diskpieId + "'></div></div>").appendTo($("#disklist"));

			var total = diskInfo['total'];
			var used = diskInfo['used'];
			$.jqplot(diskpieId, [[['total', total], ['used', used]]], {
				gridPadding : {
					top : 0,
					bottom : 38,
					left : 0,
					right : 0
				},
				seriesDefaults : {
					renderer : $.jqplot.PieRenderer,
					trendline : {
						show : true
					},
					rendererOptions : {
						padding : 8,
						showDataLabels : true
					}
				},
				legend : {
					show : true
				}
			});
		}
	}
}