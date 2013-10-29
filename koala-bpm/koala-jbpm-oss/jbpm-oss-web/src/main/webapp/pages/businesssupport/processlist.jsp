<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- <script type="text/javascript">
	/**
	 *弹出层，显示开启流程页面
	*/
	function toStartProcessPage(processName){
		$.layer({
			type : 2,
		    title : ['<h3>开启流程</h3>',true],
		    iframe : {src : '${pageContext.request.contextPath}/pages/businesssupport/processstart.jsp?processName='+processName},
		    area : ['1024px' , '500px'],
		    offset : ['20%' , '']
		});
	}
</script> -->
</head>
<body>
	<div id="processGrid"></div>
<script>
    $(function(){
       var cols = [
            { title:'流程ID', name:'id' , width: '250px'},
            { title:'流程名称', name:'name' , width: '250px'},
            { title:'操作', width: 'auto', render:function(item, name, index){
                return "<a href='/businessSupport/toStartProcessPage.koala?processId="+item.id+"' target='_blank'><span class='glyphicon glyphicon-transfer'></span>发起流程</a>";
            }}
        ];
       /* 
        var buttons = [
            {content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;新增</button>', action: 'add'},
            {content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button>', action: 'modify'},
            {content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;刪除</button>', action: 'delete'}
        ];
          */
        $('#processGrid').grid({
             identity: 'id',
             columns: cols,
             isShowIndexCol: false,
             //buttons: buttons,
             querys: [{title: '流程名称', value: 'name'}],
             url: '/businessSupport/getProcesses.koala'
        });/* 
        .on({
                    'add': function(){
                        job().add( $(this));
                    },
                    'modify': function(event, data){
                        var indexs = data.data;
                        var $this = $(this);
                        if(indexs.length == 0){
                            $this.message({
                                type: 'warning',
                                content: '请选择一条记录进行修改'
                            });
                            return;
                        }
                        if(indexs.length > 1){
                            $this.message({
                                type: 'warning',
                                content: '只能选择一条记录进行修改'
                            });
                            return;
                        }
                        job().modify(indexs[0], $this);
                    },
                    'delete': function(event, data){
                        var indexs = data.data;
                        var $this = $(this);
                        if(indexs.length == 0){
                            $this.message({
                                type: 'warning',
                                content: '请选择要删除的记录'
                            });
                            return;
                        }
                        $this.confirm({
                            content: '确定要删除所选记录吗?',
                            callBack: function(){ job().del(data.item, $this);}
                        });
                    }
        });
          */
    });
</script>
</body>
</html>