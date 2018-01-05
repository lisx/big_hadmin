<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#include "/admin/common/ztree.ftl">
<script src="${ctx!}/hadmin/js/demo/tree-demo.js"></script>
<style>
    .table tbody tr td {
        text-overflow: ellipsis;
        white-space: pre-wrap;
    }
</style>
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>考试记录</h5>
                        <p>

                        </p>
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
		                            <div class="example table-responsive">
		                            	<table class="table table-bordered"  id="table_list"></table>
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

    <!-- Page-Level Scripts -->
    <script>
        //初始化tree
        var tree = Tree.createNew("${ctx!}/admin/emergency/tree");
        tree.init();
        function onClick(e, treeId, treeNode) {
            //初始化表格,动态从服务器加载数据
            $("#nodeCode").val(treeNode.name);
            var opt = {
                url: "${ctx!}/admin/examlog/user",
                silent: true,
                query: {
                    station: treeNode.name
                }
            };
            $("#table_list").bootstrapTable('refresh', opt);
        }
        $(document).ready(function () {
        	//初始化表格,动态从服务器加载数据
			$("#table_list").bootstrapTable({
			    //使用get请求到服务器获取数据
			    method: "GET",
			    //必须设置，不然request.getParameter获取不到请求参数
			    contentType: "application/x-www-form-urlencoded",
			    //获取数据的Servlet地址
			    url: "${ctx!}/admin/examlog/user",
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
                queryParams:function(params){
                    params["nodeCode"]=$("#nodeCode").val();
                    console.log(JSON.stringify(params));
                    return params;
                },
			    //表示服务端请求
			    sidePagination: "server",
			    //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
			    //设置为limit可以获取limit, offset, search, sort, order
			    queryParamsType: "undefined",
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
			        title: "所属站区",
			        field: "stationArea"
			    },{
                    title: "所属车站",
                    field: "station"
                },{
			        title: "员工姓名",
                    field: "userName"
			    },{
                    title: "考试次数",
                    field: "logs",
                    formatter: function (value, row, index) {
                        return value.size;
                    }
                },{
			        title: "操作",
			        field: "empty",
                    formatter: function (value, row, index) {
                    	var operateHtml = '<@shiro.hasPermission name="system:examlog:show"><button class="btn btn-success btn-xs" type="button" onclick="userLog(\''+row.id+'\',\''+row.userName+'\')"><i class="fa fa-eye"></i>&nbsp;查看</button> &nbsp;</@shiro.hasPermission>';
                        return operateHtml;
                    }
			    }]
			});
        });
        function userLog(id,name){
            console.log(name+"id"+id);
            layer.open({
                type: 2,
                title: name+'的考试记录',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: '${ctx!}/admin/examlog/show/' + id,
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        }

    </script>
