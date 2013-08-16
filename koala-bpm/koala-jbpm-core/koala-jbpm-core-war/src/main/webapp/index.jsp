<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/header.jsp" %>
<head>
	<title>流程管理</title>
</head>

<body style="text-align:center; background:#F0F0F0; overflow:hidden;">
    <div id="pageloading" style="display:block;"></div> 
     <div id="mainbody" class="l-mainbody" style="width:99.2%; margin:0 auto; margin-top:3px;" >
        
        <div position="left" id="mainmenu">
          <div  title="流程查看"><ul id="jbpmView"></ul></div>
          <div  title="流程管理"><ul id="jbpmManager"></ul></div>
        </div>
        
        <div position="center" id="framecenter">
        </div>
        
    </div>
    
    <script type="text/javascript">
    
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
    
    //布局初始化 
    //layout
    var cliehtHeight = document.body.clientHeight;
    layout = $("#mainbody").ligerLayout({ height: cliehtHeight, heightDiff: -3, leftWidth: 250, onHeightChanged: f_heightChanged, minLeftWidth: 200 });
    var bodyHeight = $(".l-layout-center:first").height();
    //Tab
    tab = $("#framecenter").ligerTab({  height: bodyHeight, contextmenu: true });
    
    //Accordion
    accordion = $("#mainmenu").ligerAccordion({ height: bodyHeight - 24, speed: null });
    
    var tree = $('#jbpmManager').ligerTree({
		data: [ 
		        { text: '变量管理',url:"/pages/core/KoalaJbpmVariable-list.jsp"},
		        { text: '会签管理',url:"/pages/core/JoinAssign-list.jsp"},
		        { text: '委托设置',url:"/pages/core/KoalaAssignInfo-list.jsp"},
		        { text: '运行中流程',url:"/pages/jbpm/JbpmRunning-list.jsp"},
		        { text: '运行完流程',url:"/pages/jbpm/JbpmComplete-list.jsp"}
		],
		nodeWidth:150,
		checkbox:false,
		onclick:jbpmManagerClick
	});
    
    $("#pageloading").hide();
    
    function jbpmManagerClick(node){
    	var url = node.data.url;
    	f_addTab(node.data.text, node.data.text, url);	
    }
   
	var rootNode = { text: 'ROOT',type:"parent"};
	
    $.getJSON('${pageContext.request.contextPath}/jbpm-JbpmProcess-findPackages.action', function (data){
    	var children = new Array();
    	for(var i=0;i<data.packages.length;i++){
    		children.push({ text:data.packages[i],type:'package',isLeaf:false,parentIcon:"folder",children:[]});
    	}
    	rootNode.children = children;
    	tree = $('#jbpmView').ligerTree({
    			data: [rootNode],
    			nodeWidth:150,
    			onClick:onExpand,
    			checkbox:false
        });
    	manager = $("#jbpmView").ligerGetTreeManager();
    });
    
    function onExpand(node){
    	var type = node.data.type;
    	if(type=="package"){
    		if (node.data.children && node.data.children.length == 0) {
    		var packageName = node.data.text;
    		 $.getJSON('${pageContext.request.contextPath}/jbpm-JbpmProcess-queryProcessByPackage.action?packageName='+packageName,function(data){
    			    var children = new Array();
    		    	for(var i=0;i<data.processes.length;i++){
    		    		children.push({ text:data.processes[i],type:'processes',isLeaf:true});
    		    	}
    				 manager.append(node.target,children);
    		 });
    		}
    	}else if(type=="processes"){
    		
    		
    		var url = "/pages/jbpm/JbpmProcess-list.jsp?processId="+node.data.text;
    		f_addTab(node.data.text.replace(new RegExp("\\.","gm"),""), "流程:"+node.data.text, url);	
    	}
    }
    </script>
    
</body>
</html>