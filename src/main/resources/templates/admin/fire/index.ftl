<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<#include "/admin/common/ztree.ftl">
    <script type="text/javascript">
        $(document).ready(function () {
            //初始化表格,动态从服务器加载数据
            $("#table_fire_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "GET",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: "${ctx!}/admin/fire/list",
                //表格显示条纹
                striped: true,
                sortable: true, //是否启用排序
                sortOrder: "desc", //排序方式
                sortName:"ifFolder,id",
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
                    title: "编号",
                    field: "id",
                    sortable: true
                },{
                    title: "文件名",
                    field: "empty",
                    formatter: function(value ,row,index) {
                        if (row.ifFolder == 1) {
                            return '<a href="javascript:void(0);" onclick="showFolder(\''+row.fileName+'\')"><i class="fa fa-folder-o"></i>' + row.fileName + '</a>';
                        }else{
                            return row.fileName;
                        }
                    }
                },{
                    title: "归属",
                    field: "stationFile",
                    formatter: function(value ,row,index) {
                        if (value!=null) {
                            return value.nodeName;
                        }else{
                            return "运三分公司";
                        }
                    }
                },{
                    title: "大小",
                    field: "fileSize"
                },{
                    title: "创建时间",
                    field: "createTime"
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml ='';
                            if(row.ifFolder==1){
                                operateHtml='<@shiro.hasPermission name="system:resource:add"><button class="btn btn-success btn-xs" type="button" onclick="showFolder(\''+row.fileName+'\')"><i class="fa fa-eye"></i>&nbsp;查看</button> &nbsp;</@shiro.hasPermission>';
                            }else{
                                operateHtml='<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                            }
                        operateHtml = operateHtml + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
                }],
                onClickRow:function (row) {
                    console.log("||||"+row.id)
                    $(".uploadFile").attr("folderId",row.id);
                    var opt = {
                        url: "${ctx!}/admin/fire/list",
                        silent: true,
                        query:{
                            folderId:row.id
                        }
                    };
                    $("#table_fire_list").bootstrapTable('refresh', opt);
                }
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
            $.get("/admin/fire/tree",function(data){
                var zNodes =eval(data);
                var zTreeObj=$.fn.zTree.init($("#treeDemo"), setting, zNodes);
                zTreeObj.expandAll(true);
            })
        });

        /*单击节点显示节点详情*/
        function onClick(e,treeId,treeNode){
            console.log("|||"+treeNode.id+"|||"+treeNode.name)
            //初始化表格,动态从服务器加载数据
            $(".uploadFile").attr("dataid",treeNode.id);
            $(".addFolder").attr("dataid",treeNode.id);
            $(".spanStation").html(treeNode.name);
            var opt = {
                url: "${ctx!}/admin/fire/list",
                silent: true,
                query:{
                    nodeCode:treeNode.id
                }
            };
            $("#table_fire_list").bootstrapTable('refresh', opt);
        };
        //进入文件夹
        function showFolder(folder){
            layer.open({
                type: 2,
                title: '文件列表',
                shadeClose: true,
                shade: false,
                area: ['100%', '100%'],
                content: '${ctx!}/admin/fire/toFolder?folder='+folder,
                end: function(index){
                    $('#table_fire_list').bootstrapTable("refresh");
                }
            });
        };
        //上传文件
        function uploadFile(){
            var id=$(".addFolder").attr("dataid");
            console.log("id:++"+id);
            layer.open({
                type: 2,
                title: '上传文件',
                shadeClose: true,
                shade: false,
                area: ['100%', '100%'],
                content: '${ctx!}/admin/fire/uploadFile?nodeCode='+id,
                end: function(index){
                    $('#table_fire_list').bootstrapTable("refresh");
                }
            });
        };
        //添加文件夹
        function addFolder(){
            var nodeCode=$(".addFolder").attr("dataid");
            layer.open({
                type: 2,
                title: '新建文件夹',
                shadeClose: true,
                shade: false,
                area: ['400px', '400px'],
                content: '${ctx!}/admin/fire/add?nodeCode='+nodeCode+'&menu=应急预案',
                end: function(index){
                    $('#table_fire_list').bootstrapTable("refresh");
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
                    url: "${ctx!}/admin/fire/delete/" + id,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            $('#table_fire_list').bootstrapTable("refresh");
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
                    <div class="ibox-content">
                        <p>
                        	<@shiro.hasPermission name="system:resource:add">
                                <button class="btn btn-success uploadFile" type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;上传资料</button>
                                <button class="btn btn-success addFolder" type="button" onclick="addFolder();"><i class="fa fa-plus"></i>&nbsp;新建文件夹</button>
                                <span class="spanStation"></span>
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
                                        <table id="table_fire_list"></table>
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
