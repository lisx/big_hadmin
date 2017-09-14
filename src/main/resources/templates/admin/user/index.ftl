<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<style>
    .table tbody tr td{
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
    }
</style>
<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>人员信息</h5>
                    <@shiro.hasPermission name="system:user:add">
                        <p>
                            <button class="btn btn-success pull-right" type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;批量导入附件</button>
                            <button class="btn btn-success pull-right uploadUser" type="button" onclick="uploadUser();"><i class="fa fa-plus"></i>&nbsp;批量导入人员</button>
                            <button class="btn btn-success pull-right" type="button" onclick="add();"><i class="fa fa-plus"></i>&nbsp;添加人员</button>
                        </p>
                    </@shiro.hasPermission>
                    </div>
                    <div class="ibox-content">
                        <div class="row row-lg">
		                    <div class="col-sm-12">
		                        <!-- Example Card View -->
		                        <div class="example-wrap">
		                            <div class="example table-responsive ">
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

    <!-- Page-Level Scripts -->
    <script>
        $(document).ready(function () {
        	//初始化表格,动态从服务器加载数据
			$("#table_list").bootstrapTable({
			    //使用get请求到服务器获取数据
			    method: "POST",
			    //必须设置，不然request.getParameter获取不到请求参数
			    contentType: "application/x-www-form-urlencoded",
			    //获取数据的Servlet地址
			    url: "${ctx!}/admin/user/list",
			    //表格显示条纹
			    striped: true,
			    //启动分页
			    pagination: true,
			    //每页显示的记录数
			    pageSize: 10,
                sortName:"id",
                sortOrder:"desc",
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
                    width:80
			    },{
			        title: "姓名",
			        field: "userName",
                    width:120
			    },{
                    title: "工号",
                    field: "userCode",
                    width:120
                },{
			        title: "所属权限",
			        field: "roles",
                    width:120,
			        formatter: function(value, row, index) {
                    	var r = "";
                    	$(value).each(function (index,role){
                    		r = r + "【" + role.name + "】";
                    	});
                    	return r;
                    }
			    },{
                    title: "站点",
                    field: "station",
                    width:120
			    },{
                    title: "站区",
                    field: "stationArea",
                    width:120
                },{
			        title: "创建时间",
			        field: "createTime",
                    width:120
			    },{
			        title: "操作",
			        field: "empty",
                    width:350,
                    formatter: function (value, row, index) {
                    	var operateHtml ='<@shiro.hasPermission name="system:user:edit"><button class="btn btn-success btn-xs" type="button" onclick="show(\''+row.id+'\')"><i class="fa fa-eye"></i>&nbsp;详情</button> &nbsp;</@shiro.hasPermission>';
                        operateHtml = operateHtml + '<@shiro.hasPermission name="system:user:edit"><button class="btn btn-primary btn-xs" type="button" onclick="edit(\''+row.id+'\')"><i class="fa fa-edit"></i>&nbsp;修改</button> &nbsp;</@shiro.hasPermission>';
                    	operateHtml = operateHtml + '<@shiro.hasPermission name="system:user:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button> &nbsp;</@shiro.hasPermission>';
                    	operateHtml = operateHtml + '<@shiro.hasPermission name="system:user:grant"><button class="btn btn-info btn-xs grantBtn" is-shown="true" type="button" onclick="grant(\''+row.id+'\')"><i class="fa fa-arrows"></i>&nbsp;选择权限</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
			    }]
			});
        });
        function show(id){
            layer.open({
                type: 2,
                title: '用户详情',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: '${ctx!}/admin/user/show/' + id,
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        }
        function edit(id){
        	layer.open({
        	      type: 2,
        	      title: '用户修改',
        	      shadeClose: true,
        	      shade: false,
        	      area: ['97%', '94%'],
        	      content: '${ctx!}/admin/user/edit/' + id,
        	      end: function(index){
        	    	  $('#table_list').bootstrapTable("refresh");
       	    	  }
        	    });
        }
        function add(){
        	layer.open({
        	      type: 2,
        	      title: '用户添加',
        	      shadeClose: true,
        	      shade: false,
                  area: ['97%', '94%'],
        	      content: '${ctx!}/admin/user/add',
        	      end: function(index){
        	    	  $('#table_list').bootstrapTable("refresh");
       	    	  }
            });
        }
        function uploadUser(){
            $(".uploadUser").attr("disabled","disabled");
            layer.open({
                type: 2,
                title: '批量上传用户',
                shadeClose: true,
                shade: false,
                area: ['50%', '50%'],
                content: '${ctx!}/admin/user/uploadUser',
                end: function(index){
                    $(".uploadUser").attr("disabled",false);
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        }
        function uploadFile(){
            layer.open({
                type: 2,
                title: '批量上传证书',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: '${ctx!}/admin/user/uploadFile',
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        }
        function grant(id){
            $(".grantBtn").attr("disabled","disabled");
            layer.open({
              type: 2,
              title: '选择权限',
              shadeClose: true,
              shade: false,
              area: ['300px', '300px'],
              content: '${ctx!}/admin/user/grant/'  + id,
              end: function(index){
                  $('#table_list').bootstrapTable("refresh");
              }
            });
        }
        function del(id){
        	layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
        		$.ajax({
    	    		   type: "POST",
    	    		   dataType: "json",
    	    		   url: "${ctx!}/admin/user/delete/" + id,
    	    		   success: function(msg){
	 	   	    			layer.msg(msg.message, {time: 2000},function(){
	 	   	    				$('#table_list').bootstrapTable("refresh");
	 	   	    				layer.close(index);
	 	   					});
    	    		   }
    	    	});
       		});
        }
    </script>
</body>

</html>
