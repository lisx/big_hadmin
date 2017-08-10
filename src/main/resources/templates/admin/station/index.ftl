<!-- 全局js -->
<#include "/admin/common/common.ftl">
<#include "/admin/common/form.ftl">
    <link rel="stylesheet" href="${ctx!}/hadmin/js/plugins/zTree/css/demo.css" type="text/css">
    <link rel="stylesheet" href="${ctx!}/hadmin/js/plugins/zTree/css/metroStyle/metroStyle.css" type="text/css">
    <script type="text/javascript" src="${ctx!}/hadmin/js/plugins/zTree/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="${ctx!}/hadmin/js/plugins/zTree/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="${ctx!}/hadmin/js/plugins/zTree/js/jquery.ztree.exedit.js"></script>
    <script type="text/javascript">
        <!--
        var setting = {
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                selectedMulti: false
            },
            edit: {
                enable: true,
                editNameSelectAll: false,
                removeTitle: '删除',
                renameTitle: '编辑'

            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeDrag: beforeDrag,
                beforeEditName: beforeEditName,
                beforeRemove: beforeRemove,
                beforeRename: beforeRename,
                onRemove: onRemove,
                onRename: onRename,
               // onClick: onClick
            }
        };

        $(document).ready(function(){
            $.get("/admin/station/tree",function(data){
                var zNodes =eval(data);
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            })
        });

        function beforeDrag(treeId, treeNodes) {
            return false;
        }
        function beforeEditName(treeId, treeNode) {
            //return confirm("确认编辑节点 " + treeNode.name +" 吗？");
        }
        function beforeRemove(treeId, treeNode) {
            var zTree = getTree();
            zTree.selectNode(treeNode);
            return confirm("确认删除节点 " + treeNode.name + " 吗？");
        }
        /*删除节点*/
        function onRemove(e, treeId, treeNode) {
            var nodeId = treeNode.id;
            $.post("/admin/station/del/"+nodeId+"?x-http-method-override=DELETE",null,function(data){

            });
        }

        function beforeRename(treeId, treeNode, newName) {
            newName = $.trim(newName);
            if (newName.length == 0) {
                alert("节点名称不能为空.");
                var zTree = getTree();
                setTimeout(function(){zTree.editName(treeNode)}, 10);
                return false;
            }
            return true;
        }
        /*修改节点*/
        function onRename(e,treeId,treeNode){
            $.post("/admin/station/update/"+treeNode.id,treeNode,function(data){

            });
        }

        /*点击新增增加节点*/
        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.id).length>0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.id
                    + "' title='新增' ></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_"+treeNode.id);
            if (btn) btn.bind("click", function(){
                saveNode(treeNode);
                return false;
            });
        };

        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.id).unbind().remove();
        };

        /*保存新的节点*/
        function saveNode(parentNode){
            var zTree = getTree();
            var _nodeName="新节点";
            $.post('/admin/station/save',{pId:parentNode.id,name:_nodeName},function(data){
                var newCode = {id:data.nodeCode,pId:parentNode.id,name:_nodeName};
                zTree.addNodes(parentNode,newCode);
            },"json");
        }

        function getTree(){
            return $.fn.zTree.getZTreeObj("treeDemo");
        }
        function upload(){
            layer.open({
                type: 2,
                title: '上传',
                shadeClose: true,
                shade: false,
                area: ['600px', '600px'],
                content: '${ctx!}/admin/station/upload',
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        }
    </script>

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
                                <button class="btn btn-success " type="button" onclick="upload();"><i class="fa fa-plus"></i>&nbsp;上传</button>
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
