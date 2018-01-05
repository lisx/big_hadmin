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
        table.init("${ctx!}/admin/emergency/list?menuType=应急预案",
             function (value, row, index) {
                 var operateHtml ='';
                 if(row.ifFolder==1){
                     operateHtml='<@shiro.hasPermission name="system:emergency:show"><button class="btn btn-success btn-xs" type="button" onclick="showFolder(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-eye"></i>&nbsp;查看</button>&nbsp;</@shiro.hasPermission>';
                 }else{
                     operateHtml='<@shiro.hasPermission name="system:emergency:down"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button>&nbsp;</@shiro.hasPermission>';
                 }
                 operateHtml = operateHtml + '<@shiro.hasPermission name="system:emergency:delete"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button>&nbsp;</@shiro.hasPermission>';
                 return operateHtml;
             }
        );
    });
    //初始化tree
    var tree = Tree.createNew("${ctx!}/admin/emergency/tree");
    tree.init();
    function onClick(e, treeId, treeNode) {
        //初始化表格,动态从服务器加载数据
        $("#nodeCode").val(treeNode.id);
        $(".uploadFile").attr("data-id", treeNode.id);
        $(".addFolder").attr("data-id", treeNode.id);
        $(".spanStation").html(treeNode.name);
        var opt = {
            url: "${ctx!}/admin/emergency/list?menuType=应急预案",
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
        var url='${ctx!}/admin/emergency/toFolder?folderId=' + folderId+'&menuType=应急预案';
        button.showFolder(url)
    };
    //上传资料文件
    function uploadFile() {
        var url="${ctx!}/admin/emergency/uploadFile?menuType=应急预案";
        button.uploadFile(url)
    };
    //添加文件夹
    function addFolder() {
        button.addFolder('${ctx!}/admin/emergency/add?menuType=应急预案')
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
                    <h5>应急预案</h5>
                <p>
                <@shiro.hasPermission name="system:emergency:deleteBatch">
                    <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                            class="fa fa-plus"></i>&nbsp;批量删除
                    </button>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="system:emergency:uploadFile">
                    <button class="btn btn-success pull-right uploadFile" type="button" onclick="uploadFile();"><i
                            class="fa fa-plus"></i>&nbsp;上传资料
                    </button>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="system:emergency:addFolder">
                    <button class="btn btn-success pull-right addFolder" type="button" onclick="addFolder();"><i
                            class="fa fa-plus"></i>&nbsp;新建文件夹
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
                                <input type="hidden" id="nodeCode" name="nodeCode"/>
                                <div class="example table-responsive ">
                                    <table class="table table-bordered" id="table_list"
                                           style="table-layout:fixed;word-wrap:break-word;"></table>
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