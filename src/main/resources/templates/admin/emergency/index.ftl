<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<#include "/admin/common/ztree.ftl">
    <script type="text/javascript">
        $(document).ready(function () {
            //初始化表格,动态从服务器加载数据
            $("#table_folder_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "GET",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: "${ctx!}/admin/emergency/folder",
                //表格显示条纹
                striped: true,
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
                columns: [{
                    title: "ID",
                    field: "id",
                    sortable: true
                },{
                    title: "文件夹",
                    field: "name"
                },{
                    title: "归属",
                    field: "area.nodeName"
                },{
                    title: "创建时间",
                    field: "createTime"
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml = '<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="showFolder(\''+row.name+'\')"><i class="fa fa-edit"></i>&nbsp;查看</button> &nbsp;</@shiro.hasPermission>';
                        operateHtml = operateHtml + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="delFolder(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
                }],
                onClickRow:function (row) {
                    console.log("||||"+row.id)
                    $(".uploadFile").attr("folderId",row.id);
                    var opt = {
                        url: "${ctx!}/admin/emergency/list",
                        silent: true,
                        query:{
                            folderId:row.id
                        }
                    };
                    $("#table_emergency_list").bootstrapTable('refresh', opt);
                }
            });

            $.get("/admin/emergency/tree",function(data){
                console.log("|||"+data);
                var zNodes =eval(data);
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            })
        });
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: onClick
            }
        };


        /*单击节点显示节点详情*/
        function onClick(e,treeId,treeNode){
            console.log("|||"+treeNode.id+"|||"+treeNode.name)
            //初始化表格,动态从服务器加载数据
            $(".addFolder").attr("dataid",treeNode.id);
            $(".spanStation").html(treeNode.name);
            var opt = {
                url: "${ctx!}/admin/emergency/folder",
                silent: true,
                query:{
                    nodeCode:treeNode.id
                }
            };
            $("#table_folder_list").bootstrapTable('refresh', opt);
        };

        function showFolder(station){
            layer.open({
                type: 2,
                title: '文件列表',
                shadeClose: true,
                shade: false,
                area: ['600px', '600px'],
                content: '${ctx!}/admin/emergency/toFolder?folder='+station,
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };

        function uploadFile(){
            var id=$(".addFolder").attr("dataid");
            console.log("id:++"+id);
            layer.open({
                type: 2,
                title: '上传文件',
                shadeClose: true,
                shade: false,
                area: ['600px', '600px'],
                content: '${ctx!}/admin/emergency/uploadFile?nodeCode='+id,
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };

        function addFolder(){
            var nodeCode=$(".addFolder").attr("dataid");
            layer.open({
                type: 2,
                title: '新建文件夹',
                shadeClose: true,
                shade: false,
                area: ['400px', '400px'],
                content: '${ctx!}/admin/folder/add?station='+nodeCode+'&menu=应急预案',
                end: function(index){
                    $('#table_emergency_list').bootstrapTable("refresh");
                }
            });
        }
        function down(url){
            window.open(url);
            //window.location.href=url;
        }
        function del(id){
            layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: "${ctx!}/admin/emergency/delete/" + id,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            $('#table_emergency_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        };
    </script>

<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>应急预案管理</h5>
                    </div>
                    <div class="ibox-content">
                        <p>
                        	<@shiro.hasPermission name="system:resource:add">
                                <button class="btn btn-success uploadFile" type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;上传</button>
                                <button class="btn btn-success addFolder" type="button" onclick="addFolder();"><i class="fa fa-plus"></i>&nbsp;新建文件夹></button>
                                <span class="spanStation"></span>
                        	</@shiro.hasPermission>
                        </p>
                        <hr>
                        <div class="row row-lg">
		                    <div class="col-sm-2">
                                    <div class='tree'><ul id="treeDemo" class="ztree"></ul></div>
                            </div>
                            <div class="col-sm-10">
                                <!-- Example Card View -->
                                <div class="example-wrap">
                                    <div class="example">
                                        <table id="table_folder_list"></table>
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
