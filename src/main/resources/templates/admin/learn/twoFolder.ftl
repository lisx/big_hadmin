<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<script src="${ctx!}/hadmin/js/demo/table-demo.js"></script>
<script src="${ctx!}/hadmin/js/demo/button-demo.js"></script>
<div class="wrapper wrapper-content  animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox ">
                <div class="col-sm-13">
                    <div class="tabs-container">
                        <div class="tab-content">
                            <div class="panel-body">
                                <p>
                                <@shiro.hasPermission name="system:train:deleteBatch">
                                    <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                                            class="fa fa-plus"></i>&nbsp;批量删除
                                    </button>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="system:train:uploadFile">
                                    <button class="btn btn-success pull-right" type="button" onclick="uploadFile();"><i
                                            class="fa fa-plus"></i>&nbsp;上传资料
                                    </button>
                                </@shiro.hasPermission>
                                </p>
                                <hr>
                                <div class="row row-lg">
                                    <div class="col-sm-12">
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

        </div>
    </div>
</div>
</div>
<!-- Page-Level Scripts -->
<script>
    //初始化表格
    $(document).ready(function () {
        var table = Table.createNew();
        table.folder("${ctx!}/admin/emergency/list?folderId=${folderId}",
                function (value, row, index) {
                    var operateHtml = '<@shiro.hasPermission name="system:train:down"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.fileId+'\',\''+row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                    operateHtml = operateHtml + '<@shiro.hasPermission name="system:train:delete"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                    return operateHtml;
                }
        );
    });
    var button = Button.createNew();
    //上传
    function uploadFile() {
        button.uploadFileFolder('${ctx!}/admin/emergency/uploadFile?menuType=${folder}&folderId=','${folderId}')
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
