<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/main.jsp" %>
<%
    String gunvorServerUrl = org.openkoala.koala.jbpm.util.EnvConfigHelper.getInstance().getProperty("gunvor.server.url");
%>

<head>
	<title>流程管理</title>
</head>

<body style="text-align:center; background:#F0F0F0; overflow:hidden;">
    <div id="pageloading" style="display:block;"></div> 
     <div id="mainbody" class="l-mainbody" style="width:99.2%; margin:0 auto; margin-top:3px;" >
        <div position="left" id="mainmenu">
        </div>
        <div position="center" id="framecenter">
            
        </div>
    </div>
    
 <div id="newPackage" style="width:200px;height:150px;margin:3px; display:none;">
    <h3>增加新包</h3>
    <div>
    
      包名: <input type="input" id="addPackageName"/><br/><br/>
      描述: <input type="input" id="addPackageDescription"/><br/><br/>
    <input type="button" id="addPackageOk" value="确定"/>&nbsp;&nbsp;<input type="button" id="addPackageCancel" value="取消"/>
    </div>
 </div>
 
 <div id="newBpmn" style="width:200px;height:150px;margin:3px; display:none;">
    <h3>增加流程</h3>
    <div>
    
      名称: <input type="input" id="addNewBpmnName"/><br/><br/>
      描述: <input type="input" id="addNewBpmnDescription"/><br/><br/>
    <input type="button" id="addNewBpmnOk" value="确定"/>&nbsp;&nbsp;<input type="button" id="addNewBpmnCancel" value="取消"/>
    </div>
 </div>
 
  <div id="publishBPMN" style="width:200px;height:150px;margin:3px; display:none;">
    <h3>发布流程</h3>
    <div>
    
    选择发布地址: <select id="publishOption"></select><br/><br/>
    <input type="button" id="publishOK" value="确定"/>&nbsp;&nbsp;<input type="button" id="publishCancel" value="取消"/>
    </div>
 </div>
    <script type="text/javascript">
    var publish;
    
    $('#publishOK').click(function(){
    	var packageName = actionNode.data.packageName;
    	var jbpmName = actionNode.data.text;
    	var url = $('#publishOption').val();
    	
    	var params  = "packageName="+packageName+"&bpmnName="+jbpmName+"&wsdl="+encodeURIComponent(url);
    	$.ajax({
    		  dataType: "json",
    		  type:"post",
    		  url: "${pageContext.request.contextPath}/jbpm-Jbpm-publish.action",
    		  data: params,
    		  success: function(data){
    			  var result = data.result;
    			  if(result==1){
    				  alert("发布成功!");
    			  }else{
    				  alert("发布失败:"+data.errors);
    			  }
    			  
    			  publish.hidden();
    		  }
       });
    	
    	
    });
    
    $('#publishCancel').click(function(){
    	publish.hidden();
    });
    
    function addNewBpmn(){
    	if(newBpmn==undefined)
    	newBpmn = $.ligerDialog.open({ target: $("#newBpmn") });
    	else
    	newBpmn.show();
    }
    
    $('#addNewBpmnOk').click(function(){
    	var name = $('#addNewBpmnName').val();
    	var packageName = actionNode.data.text;
    	var description = $('#addNewBpmnDescription').val();
    	if(name==""){
    		alert("名称不能为空");
    		return;
    	}
    	//var url = "<%=gunvorServerUrl%>/org.drools.guvnor.Guvnor/assetExtend/?packageName=" +packageName + "&assetName=" + name + "&description="+description;
    	var url = "<%=gunvorServerUrl%>/org.drools.guvnor.Guvnor/standaloneEditorServlet?locale=zh_CN&packageName=" +packageName+ "&createNewAsset=true&assetName=" + name + "&description="+description+"&assetFormat=bpmn2&client=oryx";
	
    	f_addTab(name, name, url);
    	
    	var data = [ { text: name,type:'n2'}] ;
    	
    	manager.append(actionNode.target, data);
    	
    	newBpmn.hidden();
    	
    });
    
    var newBpmn;
    $('#addNewBpmnCancel').click(function(){
    	newBpmn.hidden();
    });
    
    function deleteBpmn(){
    	if(confirm("确认删除此流程")){
    		var name = actionNode.data.text;
    		var packageName = actionNode.data.packageName;
    		var url = "${pageContext.request.contextPath}/jbpm-Jbpm-deleteBpmn.action?packageName="+packageName+"&bpmnName="+name;
        	$.getJSON(url, function(data) {
        		if(data.result==1){
        			alert("删除成功");
        			manager.remove(actionNode.target);
        		}
    	   });
    	}
    }
    
    //编辑流程
    function editCick(node,i){
    	var search = "/designer/editor/?uuid="+ actionNode.data.uuid + "&profile=jbpm";
      	var url = "<%=gunvorServerUrl%>/org.drools.guvnor.Guvnor/standaloneEditorServlet?locale=zh_CN&assetsUUIDs="+ actionNode.data.uuid + "&client=oryx";
      	
    	f_addTab(actionNode.data.uuidbid, actionNode.data.text, url);
    }
    
    //删除包
    function deletePackage(node,i){
    	if(confirm("删除包会删除其下的所有流程,确认删除?")){
    	var packageName = actionNode.data.text;
    	var url = "${pageContext.request.contextPath}/jbpm-Jbpm-deletePackage.action?packageName="+packageName;
    	$.getJSON(url, function(data) {
    		if(data.result==1){
    			alert("删除成功");
    			manager.remove(actionNode.target);
    		}
	   });
     }
    }
    
    $('#addPackageOk').click(function(){
    	var packageName = $('#addPackageName').val();
    	var description = $('#addPackageDescription').val();
    	if(packageName==""){
    		alert("包名不能为空");
    		return;
    	}
    	var url = "${pageContext.request.contextPath}/jbpm-Jbpm-createPackage.action?packageName="+packageName+"&description="+description;
    	$.getJSON(url, function(data) {
    		if(data.result==1){
    			alert("新增成功");
    			var data = [ { text: packageName,children:[],type:'package'}] ;
    			manager.append(actionNode.target, data);
    		}
	   });
    });
    
    $('#addPackageCancel').click(function(){
    	newPackage.hidden();
    	
    });
    
    
    
	//几个布局的对象
    var layout, tab, accordion;
    //tabid计数器，保证tabid不会重复
    var tabidcounter = 0;
    //窗口改变时的处理函数
    function f_heightChanged(options) {
        if (tab)
            tab.addHeight(options.diff);
        if (accordion && options.middleHeight - 24 > 0)
            accordion.setHeight(options.middleHeight - 24);
    }
    //增加tab项的函数
    function f_addTab(tabid, text, url) {
        if (!tab) {
        	return;
        }
        if (!tabid)
        {
            tabidcounter++;
            tabid = "tabid" + tabidcounter; 
        }
        tab.addTabItem({ tabid: tabid, text: text, url: url });
        tab.reload(tabid);
    }
    //登录
    function f_login()
    {
        LG.login();
    }
    //修改密码
    function f_changepassword()
    {
        LG.changepassword();
    }

    //菜单初始化
    $("ul.noClass li").live('mouseover', function ()
    {
        var jitem = $(this);
        jitem.addClass("over");
    }).live('mouseout', function ()
    {
        var jitem = $(this);
        jitem.removeClass("over");
    });

    //布局初始化 
    //layout
    layout = $("#mainbody").ligerLayout({ height: '100%', heightDiff: -3, leftWidth: 200, onHeightChanged: f_heightChanged, minLeftWidth: 200 });
    var bodyHeight = $(".l-layout-center:first").height();
    //Tab
    tab = $("#framecenter").ligerTab({ width:500,height: bodyHeight, contextmenu: true });


    //预加载dialog的背景图片
    LG.prevDialogImage();

    var mainmenu = $("#mainmenu");
    
    $('#mainmenu').append('<div  title="流程管理"><ul id="jbpmTree"></ul></div>');
    
    $('#mainmenu').append('<div  title="发布管理"><ul id="publishTree"></ul></div>');

	
	var parentMenu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
        [
        { text: '增加新包', click: addNewPackage, icon: 'add' }
        ]
        });
	
	var packageMenu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
        [
         { text: '新增流程', click: addNewBpmn, icon: 'add' },
         { text: '删除此包', click: deletePackage, icon: 'add' }
         ]
    });
	
	var bpmnMenu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
        [
         { text: '编辑流程', click: editCick, icon: 'add' },
         { text: '删除流程', click: deleteBpmn, icon: 'delete' },
         { text: '发布流程', click: itemclick, icon: 'publish' }
        ]
    });
	var newPackage;
	function addNewPackage(node,i){
		if(newPackage==undefined)
		newPackage = $.ligerDialog.open({ target: $("#newPackage") });
		else 
		newPackage.show();
	}
	
	function itemclick(){
		$.getJSON("${pageContext.request.contextPath}/jbpm-Jbpm-getPublish.action", function(data) {
			var names = data.urls;
			for(var i = 0; i < names.length; i++) {
				var content = "<option value='"+names[i]['url']+"'>"+names[i]['name']+"</option>";
				$('#publishOption').append(content);
			}
			if(publish==undefined)
			publish = $.ligerDialog.open({ target: $("#publishBPMN") });
			else
			publish.show();
	    });
	}
	
	
	var actionNode;
	function onContextmenu(node,e){
		actionNode = node;
		var type = node.data.type;
		if(type=="parent"){
			parentMenu.show({ top: e.pageY, left: e.pageX });
            return false;
		}
		if(type=="package"){
			packageMenu.show({ top: e.pageY, left: e.pageX });
            return false;
		}
		
		if(type=="bpmn"){
			bpmnMenu.show({ top: e.pageY, left: e.pageX });
            return false;
		}
	}
	function onExpand(node) {
		var type = node.data.type;
		if(type=="parent"){
			if (node.data.children && node.data.children.length == 0) {
				$.getJSON("${pageContext.request.contextPath}/jbpm-Jbpm-findPackages.action", function(data) {
					var names = data.packages;
					manager.append(node.target, names);
			});
		}
	  }
		
		if(type=="package"){
			if (node.data.children && node.data.children.length == 0) {
				$.getJSON("${pageContext.request.contextPath}/jbpm-Jbpm-findBpmns.action?packageName="+node.data.text, function(data) {
					var names = data.bpmns;
					manager.append(node.target, names);
			});
		}
	}

	}
	
	var tree;
	var manager; 
    $.getJSON('${pageContext.request.contextPath}/jbpm-Jbpm-findPackages.action', function (data){
    	var children = data.packages || [];
    	var rootNode = { text: '流程',type:"parent"};
    	rootNode.children = children;
    	tree = $('#jbpmTree').ligerTree({
    		data: [rootNode],
    		onClick:onExpand,
    		onContextmenu:onContextmenu,
    		nodeWidth:150,
    		checkbox:false
    	});
    	manager = $("#jbpmTree").ligerGetTreeManager();
    	$("div.l-tree-icon-folder-open").each(function(index){
       	 if(index>0){
       		 $(this).removeClass("l-tree-icon-folder-open").addClass("l-tree-icon-folder");
       	 }
        });

    });
    
    //Accordion
    accordion = $("#mainmenu").ligerAccordion({ height: bodyHeight - 24, speed: null });
    
    
    //
    var tree = $('#publishTree').ligerTree({
		data: [ 
		        { text: '流程引擎管理',url:"/pages/core/PublishURL-list.jsp"}
		],
		nodeWidth:150,
		checkbox:false,
		onClick:jbpmOnClick
	});
    
    function jbpmOnClick(node){
    	var url="${pageContext.request.contextPath}/pages/core/PublishURL-list.jsp";
    	f_addTab("publish", "发布管理", url);
    }

    $("#pageloading").hide();
    
    //$.getJSON('common-Login-getUserNameByCode.action?username=admin', function (user)
    //{
        $(".l-topmenu-username").html("系统管理员" + "，");
    //});

    
    </script>
</body>
</html>