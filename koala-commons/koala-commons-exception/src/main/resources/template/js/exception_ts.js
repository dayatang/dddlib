/* 错误提示 */
function checkError(data) {
	var title = "错误提示";
	try {
		var json;
		var text = data.responseText;
		if (!text) {
			json = data;
		} else {
			json = eval("(" + text + ")");
		}

		if (json.actionError) {
			var content = json.actionError;
			warnDialog(content);
			return true;
		}
		return false;
	} catch (e) {
		warnDialog("响应异常");
		return true;
	}
}

//错误弹出提示
function warnDialog(msg){
	alert(msg);
}

/*
 * ajax response 2 json
 */
function response2Json(response) {
	try {
		var text = response.responseText;
		var json = eval("(" + text + ")");
		return json;
	} catch (e) {
		return null;
	}

}

 