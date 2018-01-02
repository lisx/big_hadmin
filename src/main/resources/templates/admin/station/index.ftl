<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<#include "/admin/common/ztree.ftl">
<script src="${ctx!}/hadmin/js/demo/table-demo.js"></script>
<script src="${ctx!}/hadmin/js/demo/button-demo.js"></script>
<style>
    .table tbody tr td {
        text-overflow: ellipsis;
        white-space: pre-wrap;
    }
</style>
<script type="text/javascript">
    $(document).ready(function () {
        var table = Table.createNew();
        table.init("${ctx!}/admin/emergency/list?menuType=车站信息",
            function (value, row, index) {
                var operateHtml = '<@shiro.hasPermission name="system:station:down"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+ row.id+ '\',\''+ row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                operateHtml = operateHtml + '<@shiro.hasPermission name="system:station:deleteFile"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+ row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button> &nbsp;</@shiro.hasPermission>';
                return operateHtml;
            }
        );
        $.get("${ctx!}/admin/emergency/tree", function (data) {
            var zNodes = eval(data);
            var zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            zTreeObj.expandAll(true);
            $("#addLeaf").bind("click", {isParent: false}, add);
            $("#edit").bind("click", edit);
            $("#remove").bind("click", remove);
        })
    });
    var setting = {
        view: {
            selectedMulti: false
        },
        check: {
            enable: true
        },
        edit: {
            enable: true,
            showRemoveBtn: false,
            showRenameBtn: false,
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
            beforeRemove: beforeRemove,
            beforeRename: beforeRename,
            onRemove: onRemove,
            onRename: onRename,
            onClick: onClick,
            onCheck: onCheck
        }
    };
    function onCheck(event, treeId, treeNode) {
        console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked);
        if (treeNode.checked) {
            appendHidden(treeNode.id);
            $(".fileUploadBtton").attr("data-id", treeNode.id);
            $(".spanStation").html(treeNode.name);
        } else {
            removeHidden(treeNode.id);
        }
    };
    function appendHidden(id) {
        var hiddenString = '<input type="hidden" name="allocation" value="' + id + '">';
        $("#hiddenBox").append(hiddenString);
    }
    function removeHidden(id) {
        $("#hiddenBox>input").each(function (index, element) {
            if ($(this).val() == id) {
                $(this).remove();
            }
            if (isContains($(this).val(), id)) {
                $(this).remove();
            }
        });
    }
    function isContains(str, substr) {
        return str.indexOf(substr) >= 0;
    }
//    var log, className = "dark";
    function beforeDrag(treeId, treeNodes) {
        return false;
    }
//    function getTime() {
//        var now = new Date(),
//                h = now.getHours(),
//                m = now.getMinutes(),
//                s = now.getSeconds(),
//                ms = now.getMilliseconds();
//        return (h + ":" + m + ":" + s + " " + ms);
//    }
    function beforeRemove(treeId, treeNode) {
        return confirm("确认删除 -- " + treeNode.name + " 吗？");
    }
    function onRemove(e, treeId, treeNode) {
        var nodeId = treeNode.id;
        console.log(nodeId)
        $.ajax({
            url: '/admin/station/del/' + nodeId,
            type: 'DELETE',
            data: {}
        });
    }
    /*修改节点*/
    function onRename(e, treeId, treeNode) {
        console.log("treeId:" + treeId);
        $.post("/admin/station/update/" + treeNode.id, treeNode, function (data) {
        });
    };
    function beforeRename(treeId, treeNode, newName) {
        if (newName.length == 0) {
            alert("节点名称不能为空.");
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            setTimeout(function () {
                zTree.editName(treeNode)
            }, 10);
            return false;
        }
        return true;
    }
//    var newCount = 1;
    /*保存新的节点*/
    function saveNode(parentNode) {
        console.log(parentNode);
        var zTree = getTree();
        var _nodeName = "新节点";
        $.post('/admin/station/save', {pId: parentNode.id, name: _nodeName}, function (data) {
            console.log("data.nodeCode" + data.nodeCode)
            var newCode = {id: data.nodeCode, pId: parentNode.id, name: _nodeName};
            newCode = zTree.addNodes(parentNode, newCode);
            zTree.editName(newCode[0]);
        }, "json");
    };
    function add(e) {
        var zTree = getTree();
        isParent = e.data.isParent,
                nodes = zTree.getSelectedNodes(),
                treeNode = nodes[0];
        if (nodes.length == 0) {
            alert("请先选择一个站区或站点");
            return;
        }
        if (treeNode) {
            saveNode(treeNode);
        }
    };
    function edit() {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
                nodes = zTree.getSelectedNodes(),
                treeNode = nodes[0];
        if (nodes.length == 0) {
            alert("请先选择一个站区或站点");
            return;
        }
        zTree.editName(treeNode);
    };
    function remove(e) {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
                nodes = zTree.getSelectedNodes(),
                treeNode = nodes[0];
        if (nodes.length == 0) {
            alert("请先选择一个站点");
            return;
        }
        var callbackFlag = true;
        zTree.removeNode(treeNode, callbackFlag);
    };
    function getTree() {
        return $.fn.zTree.getZTreeObj("treeDemo");
    };
    /*单击节点显示节点详情*/
    function onClick(e, treeId, treeNode) {
        //初始化表格,动态从服务器加载数据
        $(".fileUploadBtton").attr("data-id", treeNode.id);
        $(".spanStation").html(treeNode.name);
        console.log("nodeCode"+JSON.stringify(treeNode))
        var opt = {
            url: "${ctx!}/admin/emergency/list?menuType=车站信息",
            silent: true,
            query: {
                nodeCode: treeNode.id
            }
        };

        $("#table_list").bootstrapTable('refresh', opt);
    };


    //初始化按钮
    var button = Button.createNew();
 
    //上传资料文件
    function uploadFile() {
        var url = "${ctx!}/admin/emergency/uploadFile?menuType=车站信息";
        console.log("uploadFile"+url);
        button.uploadFile(url)
    };

    //下载文件
    function down(id, name) {
        button.down("${ctx!}/admin/download/" + id, name)
    }
    //删除文件夹或文件
    function del(id) {
        button.del("${ctx!}/admin/emergency/delete/" + id)
    };
    //删除全部
    function removeAll() {
        button.removeAll("${ctx!}/admin/emergency/removeAll/");
    }
</script>

<div class="wrapper wrapper-content  animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox ">
                <div class="ibox-title">
                    <h5>车站信息</h5>
                <p>
<@shiro.hasPermission name="system:station:deleteBatch">
                    <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                            class="fa fa-plus"></i>&nbsp;批量删除文件
                    </button>
</@shiro.hasPermission>
<@shiro.hasPermission name="system:station:uploadFile">
                    <button class="btn btn-success pull-right fileUploadBtton" type="button" onclick="uploadFile();"><i
                            class="fa fa-plus"></i>&nbsp;上传文件
                    </button>
</@shiro.hasPermission>
<@shiro.hasPermission name="system:station:delete">
                    <button class="btn btn-success pull-right remove" id="remove" type="button"><i
                            class="fa fa-plus"></i>&nbsp;删除车站
                    </button>
</@shiro.hasPermission>
<@shiro.hasPermission name="system:station:edit">
                    <button class="btn btn-success pull-right edit" id="edit" type="button"><i class="fa fa-plus"></i>&nbsp;编辑车站
                    </button>
</@shiro.hasPermission>
<@shiro.hasPermission name="system:station:add">
                    <button class="btn btn-success pull-right addLeaf" id="addLeaf" type="button"><i
                            class="fa fa-plus"></i>&nbsp;新增车站
                    </button>
</@shiro.hasPermission>
                    <h5 class="spanStation" style="margin-left: 20px"></h5>
                    </p>
                    <div id="hiddenBox">

                    </div>
                </div>
                <div class="ibox-content">
                    <div class="row row-lg">
                        <div class="col-sm-3">
                            <div class='tree'>
                                <ul id="treeDemo" class="ztree"></ul>
                            </div>
                        </div>
                        <div class="col-sm-9">
                            <!-- Example Card View -->
                            <div class="example-wrap">
                                <div class="example">
                                    <table class="table table-bordered" id="table_list"></table>
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
