<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<script src="${ctx!}/hadmin/js/demo/button-demo.js"></script>
<style>
    .table tbody tr td {
        text-overflow: ellipsis;
        white-space: pre-wrap;
    }
</style>
    <script type="text/javascript">
        $(document).ready(function () {
            //初始化表格,动态从服务器加载数据
            $("#table_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "GET",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: "${ctx!}/admin/running/list",
                //表格显示条纹
                striped: true,
                sortable: true, //是否启用排序
                sortOrder: "desc", //排序方式
                sortName:"id",
                //启动分页
                pagination: true,
                //每页显示的记录数
                pageSize: 10,
                //当前第几页
                pageNumber: 1,
                //记录数可选列表
                pageList: [5, 10, 15, 20, 25],
                //是否启用查询
                search: true,
                //是否启用详细信息视图
                //detailView:true,
                //detailFormatter:detailFormatter,
                //表示服务端请求
                sidePagination: "server",
                //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
                //设置为limit可以获取limit, offset, search, sort, order
                queryParamsType: "undefined",
                clickToSelect: true,
                //json数据解析
                responseHandler: function(res) {
                    return {
                        "rows": res.content,
                        "total": res.totalElements
                    };
                },
                //数据列
                columns: [{checkbox:true},{
                    title: "编号",
                    field: "id",
                    sortable: true
                },{
                    title: "运行图名称",
                    field: "fileName"
                },{
                    title: "使用时间",
                    field: "dateType"
                },{
                    title: "线路",
                    field: "lineName"
                },{
                    title: "时间段",
                    field: "empty",
                    formatter: function (value, row, index) {
                        if(null!=row.startTime&&""!=row.startTime){
                            return row.startTime+"至"+row.endTime;
                        }else{
                            return "";
                        }

                    }
                },{
                    title: "创建时间",
                    field: "createTime"
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml ='<@shiro.hasPermission name="system:running:add"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.fileId+'\',\''+row.fileName+'\')"><i class="fa fa-edit"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                        operateHtml = operateHtml + '<@shiro.hasPermission name="system:running:delete"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
                }],
                onClickRow:function (row) {
                    $(".uploadFile").attr("folderId",row.id);
                    var opt = {
                        url: "${ctx!}/admin/running/list",
                        silent: true,
                        query:{
                            folderId:row.id
                        }
                    };
                    $("#table_list").bootstrapTable('refresh', opt);
                }
            });
        });

        //初始化按钮
        var button = Button.createNew();
        //上传资料文件
        function uploadFile() {
            var url="${ctx!}/admin/running/uploadFile?menuType=运行图管理";
            layer.open({
                type: 2,
                title: '上传文件',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: url,
                end: function (index) {
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };
        //下载文件
        function down(id, name) {
            button.down("${ctx!}/admin/download/" + id, name)
        }
        //删除文件夹或文件
        function del(id) {
            button.del("${ctx!}/admin/running/delete/" + id)
        };
        //删除全部
        function removeAll() {
            button.removeAll("${ctx!}/admin/running/removeAll/");
        }
    </script>

<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>运行图管理</h5>
                        <p>
                        <@shiro.hasPermission name="system:running:deleteBatch">
                            <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                                    class="fa fa-plus"></i>&nbsp;批量删除
                            </button>
                        </@shiro.hasPermission>
                            <button class="btn btn-success pull-right" type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;新增运行图</button>
                        <@shiro.hasPermission name="system:running:add">
                            <button class="btn btn-success pull-right" type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;新增运行图</button>
                        </@shiro.hasPermission>
                            <h5 class="spanStation" style="margin-left: 20px"></h5>
                        </p>
                    </div>
                    <div class="ibox-content">

                        <div class="row row-lg">
                            <div class="col-sm-12">
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
