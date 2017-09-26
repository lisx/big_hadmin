<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<#include "/admin/common/ztree.ftl">
<script src="${ctx!}/hadmin/js/demo/tree-demo.js"></script>
<script src="${ctx!}/hadmin/js/demo/table-demo.js"></script>
<script src="${ctx!}/hadmin/js/demo/button-demo.js"></script>
<style>
    .table tbody tr td {
        text-overflow: ellipsis;
        white-space: pre-wrap;
    }
</style>
<script type="text/javascript">
    //初始化表格
    $(document).ready(function () {
        var table = Table.createNew();
        table.init("${ctx!}/admin/emergency/list?menuType=首页滚播图");
    });
    //初始化tree
    var tree = Tree.createNew("${ctx!}/admin/emergency/tree");
    tree.init();
    function onClick(e, treeId, treeNode) {
        //初始化表格,动态从服务器加载数据
        $(".uploadFile").attr("data-id", treeNode.id);
        $(".addFolder").attr("data-id", treeNode.id);
        $(".spanStation").html(treeNode.name);
        var opt = {
            url: "${ctx!}/admin/emergency/list?menuType=首页滚播图",
            silent: true,
            query: {
                nodeCode: treeNode.id
            }
        };
        $("#table_list").bootstrapTable('refresh', opt);
    }
    //初始化按钮
    var button = Button.createNew();
    //进入文件夹
    function showFolder(folderId) {
        var url = '${ctx!}/admin/fire/toFolder?folderId=' + folderId + '&menuType=首页滚播图';
        console.log("url+++++++" + url)
        button.showFolder(url)
    };
    //上传资料文件
    function uploadFile() {
        var url = "${ctx!}/admin/emergency/uploadFile?menuType=首页滚播图";
        console.log("url+++++++" + url);
        button.uploadFile(url)
    };
    //添加文件夹
    function addFolder() {
        button.addFolder('${ctx!}/admin/emergency/add?menuType=首页滚播图')
    }
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

<body class="gray-bg">
<div class="wrapper wrapper-content  animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox ">
                <div class="ibox-title">
                    <h5>首页滚播图</h5>
                <p>
                <@shiro.hasPermission name="system:resource:add">
                    <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                            class="fa fa-plus"></i>&nbsp;批量删除
                    </button>
                    <button class="btn btn-success pull-right uploadFile" type="button" onclick="uploadFile();"><i
                            class="fa fa-plus"></i>&nbsp;上传图片
                    </button>
                    <h5 class="spanStation" style="margin-left: 20px;"></h5>
                </@shiro.hasPermission>
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
