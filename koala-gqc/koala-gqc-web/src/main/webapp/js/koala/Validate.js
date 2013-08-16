Validator = {
Require : {expression:/.+/,errorMsg:"该字段不能为空"},
Email : {expression:/^\w+([-+.]\w+)*@\w+([-.]\\w+)*\.\w+([-.]\w+)*$/,errorMsg:"Email格式不正确"},
Mobile:{expression:/^(1[3|5|8]{1}\d{9})$/,errorMsg:"手机格式不正确"},
TelePhone:{expression:/^(((0\d{2,3}-)?\d{7,8}(-\d{4})?))$/,errorMsg:"电话号码格式不正确"},
IdCard:{expression:/^\d{15}(\d{2}[A-Za-z0-9])?$/,errorMsg:"身份证号码格式不正确"},
Number:{expression:/^-?(\d+|[1-9]\d*\.\d+|0\.\d*[1-9]\d*|0?\.0+|0)$/,errorMsg:"仅支持数字"},
English:{expression:/^[A-Za-z]+$/,errorMsg:"仅支持英文字符"},
Chinese:{expression:/^[\u0391-\uFFE5]+$/,errorMsg:"仅支持中文字符"},
URL:{expression:/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,errorMsg:"URL地址格式不正确"},
Regex:{errorMsg:"格式不正确"},
UnSafe : /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,
IsSafe : function(str){return !this.UnSafe.test(str);},
SafeString : {expression:"this.IsSafe(value)",errorMsg:"仅支持中文字符"},
ErrorItem : [document.forms[0]],
ErrorMessage : ["以下原因导致提交失败：\t\t\t\t"],
Validate : function(theForm, mode){
var obj = theForm || event.srcElement;
var count = obj.elements.length;
this.ErrorMessage.length = 1;
this.ErrorItem.length = 1;
this.ErrorItem[0] = obj;
//window.console.log('');
for(var i=0;i<count;i++){
with(obj.elements[i]){
if(disabled)continue;
var _dataType = getAttribute("dataType");
if(typeof(_dataType) == "object" || typeof(this[_dataType]) == "undefined") continue;
this.ClearState(obj.elements[i]);
if(getAttribute("require") == "false" && value == "") continue;
var errorMsg = getAttribute("errorMsg")? getAttribute("errorMsg") : this[_dataType].errorMsg;
errorMsg =  errorMsg || "invalid";
var validateExpr = getAttribute("validateExpr");
validateExpr = validateExpr || this[_dataType].expression;
switch(_dataType){
case "Date" :
case "Repeat" :
case "Range" :
case "Compare" :
case "Custom" :
case "Group" : 
case "Limit" :
case "LimitB" :
case "SafeString" :
if(!eval(validateExpr)) {
this.AddError(i, errorMsg);
}
break;
default :
if(!eval(validateExpr).test(value)){
this.AddError(i, errorMsg);
}
break;
}
}
}
if(this.ErrorMessage.length > 1){
mode = mode || 1;
var errCount = this.ErrorItem.length;
switch(mode){
case 2 :
for(var i=1;i<errCount;i++)
this.ErrorItem[i].style.color = "red";
case 1 :
alert(this.ErrorMessage.join("\n"));
this.ErrorItem[1].focus();
break;
case 3 :
try{
//$(this.ErrorItem[1]).addClass('highlight');
var content = this.ErrorMessage[1].replace(/\d+:/,"&nbsp;");
$(this.ErrorItem[1]).ligerTip({content: content});
//window.console.log(content);
}
catch(e){alert(e.description);}

this.ErrorItem[1].focus();
break;
default :
alert(this.ErrorMessage.join("\n"));
break;
}
return false;
}
return true;
},
limit : function(len,min, max){
min = min || 0;
max = max || Number.MAX_VALUE;
return min <= len && len <= max;
},
LenB : function(str){
return str.replace(/[^\x00-\xff]/g,"**").length;
},
ClearState : function(elem){
with(elem){
if(style.color == "red")
style.color = "";
var ligertipid = getAttribute("ligertipid"); 
if(ligertipid && ligertipid != ""){
	removeAttribute("ligertipid");
	$(elem).ligerHideTip();
}
}
},
AddError : function(index, str){
this.ErrorItem[this.ErrorItem.length] = this.ErrorItem[0].elements[index];
this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length + ":" + str;
},
Exec : function(op, reg){
return new RegExp(reg,"g").test(op);
},
compare : function(op1,operator,op2){
switch (operator) {
case "NotEqual":
return (op1 != op2);
case "GreaterThan":
return (op1 > op2);
case "GreaterThanEqual":
return (op1 >= op2);
case "LessThan":
return (op1 < op2);
case "LessThanEqual":
return (op1 <= op2);
default:
return (op1 == op2); 
}
},
MustChecked : function(name, min, max){
var groups = document.getElementsByName(name);
var hasChecked = 0;
min = min || 1;
max = max || groups.length;
for(var i=groups.length-1;i>=0;i--)
if(groups[i].checked) hasChecked++;
return min <= hasChecked && hasChecked <= max;
},
IsDate : function(op, formatString){
formatString = formatString || "ymd";
var m, year, month, day;
switch(formatString){
case "ymd" :
m = op.match(new RegExp("^\\s*((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})\\s*$"));
if(m == null ) return false;
day = m[6];
month = m[5]--;
year = (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
break;
case "dmy" :
m = op.match(new RegExp("^\\s*(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))\\s*$"));
if(m == null ) return false;
day = m[1];
month = m[3]--;
year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
break;
default :
break;
}
var date = new Date(year, month, day);
return (typeof(date) == "object" && year == date.getFullYear() && month == date.getMonth() && day == date.getDate());
function GetFullYear(y){return ((y<30 ? "20" : "19") + y)|0;}
}
}