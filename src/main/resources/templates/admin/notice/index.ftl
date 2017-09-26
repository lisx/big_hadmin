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
                url: "${ctx!}/admin/notice/list",
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
                    title: "标题",
                    field: "title"
                },{
                    title: "接收单位",
                    field: "stationName"
                },{
                    title: "发布时间",
                    field: "createTime"
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml ='<@shiro.hasPermission name="system:resource:add"><button class="btn btn-success btn-xs" type="button" onclick="show(\''+row.id+'\')"><i class="fa fa-eye"></i>&nbsp;详情</button> &nbsp;</@shiro.hasPermission>'
                         + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
                }],
            });
        });
        //通知详情
        function show(id){
            layer.open({
                type: 2,
                title: '通知详情',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: '${ctx!}/admin/notice/show?id='+id,
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };
        //新增通知
        function addRunning(){
            layer.open({
                type: 2,
                title: '新增通知',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: '${ctx!}/admin/notice/uploadFile',
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };
        //添加文件夹
        function addFolder(){
            var nodeCode=$(".addFolder").attr("data-id");
            layer.open({
                type: 2,
                title: '新建文件夹',
                shadeClose: true,
                shade: false,
                area: ['400px', '400px'],
                content: '${ctx!}/admin/notice/add?nodeCode='+nodeCode+'&menu=应急预案',
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        }
        //下载文件
        function down(id,name){
            console.log(id+"|||||"+name);
            var a = document.createElement('a');
            a.href = "${ctx!}/admin/download/"+id;
            a.download = name;
            a.click();
        }
        //删除文件夹或文件
        function del(id){
            layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: "${ctx!}/admin/notice/delete/" + id,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            $('#table_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        };
        //初始化按钮
        var button = Button.createNew();
        //删除全部
        function removeAll() {
            button.removeAll("${ctx!}/admin/notice/removeAll/");
        }
    </script>

<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>通知管理</h5>
                        <p>
                        <@shiro.hasPermission name="system:resource:add">
                            <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                                    class="fa fa-plus"></i>&nbsp;批量删除
                            </button>
                            <button class="btn btn-success pull-right" type="button" onclick="addRunning();"><i class="fa fa-plus"></i>&nbsp;发布通知</button>
                            <span class="spanStation"></span>
                        </@shiro.hasPermission>
                        </p>
                    </div>
                    <div class="ibox-content">
                        <div class="row row-lg">
                            <div class="col-sm-12">
                                <!-- Example Card View -->
                                <div class="example-wrap">
                                    <div class="example  table-responsive ">
                                        <table class="table  table-bordered"  id="table_list"  style="table-layout:fixed;word-wrap:break-word;"></table>
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
