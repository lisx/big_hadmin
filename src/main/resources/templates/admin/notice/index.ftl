<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<script src="${ctx!}/hadmin/js/demo/button-demo.js"></script>
<style>
    .detail{
        width:50px;
    }
    .table tbody tr td{
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
    }
    span{word-break:normal; width:auto; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;}
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
                detailView:true,
                detailFormatter:detailFormatter,
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
                    width: 80
                },{
                    title: "标题",
                    field: "title"
                },{
                    title: "内容",
                    field: "content"
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
                        var operateHtml ='<@shiro.hasPermission name="system:notice:show"><button class="btn btn-success btn-xs" type="button" onclick="show(\''+row.id+'\')"><i class="fa fa-eye"></i>&nbsp;详情</button> &nbsp;</@shiro.hasPermission>'
                         + '<@shiro.hasPermission name="system:notice:delete"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
                }],
            });
        });
        function detailFormatter(index, row) {
            var html = [];
            html.push('<span><b>标题:</b> ' + row.title + '</span>'+'<br/><span><b>内容:</b> ' + row.content + '</span>'+'<br/><span><b>接收单位:</b> ' + row.stationName + '</span>');
            return html.join('');
        }
        $("#table_list").on("click","tr td:nth-child(4)",function(){
            var content=$(this).text();
            layer.open({
                type: 4,
                close:false,
                content: [content, $(this)] //数组第二项即吸附元素选择器或者DOM
            });
        })
        $("#table_list").on("click","tr td:nth-child(5)",function(){
            var content=$(this).text();
            layer.open({
                type: 4,
                shade:[0.2,'#fff'],
                content: [content, $(this)] //数组第二项即吸附元素选择器或者DOM
            });
        })
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
        //初始化按钮
        var button = Button.createNew();
        //上传资料文件

        function uploadFile(){
            var url = "${ctx!}/admin/notice/uploadFile?menuType=通知管理";
            layer.open({
                type: 2,
                title: '发布通知',
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
        };
        //删除文件夹或文件
        function del(id) {
            button.del("${ctx!}/admin/notice/delete/" + id)
        };
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
                        <@shiro.hasPermission name="system:notice:deleteBatch">
                            <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                                    class="fa fa-plus"></i>&nbsp;批量删除
                            </button>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="system:notice:add">
                            <button class="btn btn-success pull-right" type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;发布通知</button>
                        </@shiro.hasPermission>
                            <span class="spanStation"></span>
                        </p>
                    </div>
                    <div class="ibox-content">
                        <div class="row row-lg">
                            <div class="col-sm-12">
                                <!-- Example Card View -->
                                <div class="example-wrap">
                                    <div class="example  table-responsive ">
                                        <table class="table  table-bordered"  id="table_list"   style="table-layout:fixed;overflow:hidden;"></table>
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
