<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>车站信息列表</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctx!}/hadmin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/style.css?v=4.1.0" rel="stylesheet">
    <link rel="stylesheet" href="${ctx!}/hadmin/js/plugins/zTree/css/demo.css" type="text/css">
    <link rel="stylesheet" href="${ctx!}/hadmin/js/plugins/zTree/css/metroStyle/metroStyle.css" type="text/css">
    <script type="text/javascript" src="${ctx!}/hadmin/js/plugins/zTree/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="${ctx!}/hadmin/js/plugins/zTree/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="${ctx!}/hadmin/js/plugins/zTree/js/jquery.ztree.exedit.js"></script>
    <SCRIPT type="text/javascript">
        <!--
        var setting = {
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                selectedMulti: false
            },
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            edit: {
                enable: true
            }
        };

        var zNodes =[
            { id:1, pId:0, name:"父节点1", open:true},
            { id:11, pId:1, name:"父节点11"},
            { id:111, pId:11, name:"叶子节点111"},
            { id:112, pId:11, name:"叶子节点112"},
            { id:113, pId:11, name:"叶子节点113"},
            { id:114, pId:11, name:"叶子节点114"},
            { id:12, pId:1, name:"父节点12"},
            { id:121, pId:12, name:"叶子节点121"},
            { id:122, pId:12, name:"叶子节点122"},
            { id:123, pId:12, name:"叶子节点123"},
            { id:124, pId:12, name:"叶子节点124"},
            { id:13, pId:1, name:"父节点13", isParent:true},
            { id:2, pId:0, name:"父节点2"},
            { id:21, pId:2, name:"父节点21", open:true},
            { id:211, pId:21, name:"叶子节点211"},
            { id:212, pId:21, name:"叶子节点212"},
            { id:213, pId:21, name:"叶子节点213"},
            { id:214, pId:21, name:"叶子节点214"},
            { id:22, pId:2, name:"父节点22"},
            { id:221, pId:22, name:"叶子节点221"},
            { id:222, pId:22, name:"叶子节点222"},
            { id:223, pId:22, name:"叶子节点223"},
            { id:224, pId:22, name:"叶子节点224"},
            { id:23, pId:2, name:"父节点23"},
            { id:231, pId:23, name:"叶子节点231"},
            { id:232, pId:23, name:"叶子节点232"},
            { id:233, pId:23, name:"叶子节点233"},
            { id:234, pId:23, name:"叶子节点234"},
            { id:3, pId:0, name:"父节点3", isParent:true}
        ];

        $(document).ready(function(){
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        });

        var newCount = 1;
        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                    + "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_"+treeNode.tId);
            if (btn) btn.bind("click", function(){
//                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
//                zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
                saveNode(treeNode);
                return false;
            });
        };
        /*保存新的节点*/
        function saveNode(parentNode){
            var zTree = getTree();
            var _nodeName="新节点";
            $.post('/admin/station/tree',{pId:parentNode.id,name:_nodeName},function(data){
                var newCode = {id:data.nodeCode,pId:parentNode.id,name:_nodeName};
                zTree.addNodes(parentNode,newCode);
            },"json");
        }
        function getTree(){
            return $.fn.zTree.getZTreeObj("treeDemo");
        }
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
        };
        //-->
    </SCRIPT>
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>车站信息管理</h5>
                    </div>
                    <div class="ibox-content">
                        <p>
                        	<@shiro.hasPermission name="system:resource:add">
                        		<button class="btn btn-success " type="button" onclick="add();"><i class="fa fa-plus"></i>&nbsp;添加</button>
                                <button class="btn btn-success " type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;上传文件</button>
                        	</@shiro.hasPermission>
                        </p>
                        <hr>
                        <div class="row row-lg">
		                    <div class="col-sm-3">
                                    <div class='tree'><ul id="treeDemo" class="ztree"></ul></div>
                            </div>
                            <div class="col-sm-9">
		                        <!-- Example Card View -->
		                        <div class="example-wrap">
		                            <div class="example">
		                            	<table id="table_list"></table>
		                            </div>
		                        </div>
		                        <!-- End Example Card View -->
		                    </div>
	                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>





</body>

</html>
