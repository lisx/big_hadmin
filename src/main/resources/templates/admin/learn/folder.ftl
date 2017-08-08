<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>车站信息列表</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/hadmin/css/bootstrap.min.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/plugins/bootstrap-table/bootstrap-table.min.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/style.css?v=${version!}" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <#--<div class="ibox-title">-->
                        <#--<h5>学习园地</h5>-->
                    <#--</div>-->
                    <div class="col-sm-13">
                        <div class="tabs-container">
                            <div class="tab-content">
                                <div class="panel-body">
                                        <p>
                                        <@shiro.hasPermission name="system:resource:add">
                                            <button class="btn btn-success " type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;上传</button>
                                        </@shiro.hasPermission>
                                        </p>
                                        <hr>
                                        <div class="row row-lg">
                                            <div class="col-sm-12">
                                                <!-- Example Card View -->
                                                <div class="example-wrap">
                                                    <div class="example">
                                                        <table id="table_folder_train_list"></table>
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
    <!-- 全局js -->
	<#include "/admin/common/common.ftl">
    <!-- 自定义js -->
    <script src="${ctx!}/hadmin/js/content.js?v=${version!}"></script>
    <!-- Page-Level Scripts -->
    <script>
        $(document).ready(function () {
            console.log("++++++++++++++++++++++")
			//初始化表格,动态从服务器加载数据
			$("#table_folder_train_list").bootstrapTable({
			    //使用get请求到服务器获取数据
			    method: "POST",
			    //必须设置，不然request.getParameter获取不到请求参数
			    contentType: "application/x-www-form-urlencoded",
			    //获取数据的Servlet地址
			    url: "${ctx!}/admin/train/list?folder=${folder}",
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
			   // detailView:true,
			   // detailFormatter:detailFormatter,
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
			        title: "ID",
			        field: "id",
			        sortable: true
			    },{
                    title: "文件名",
                    field: "fileName",
                },{
                    title: "文件夹",
                    field: "folder",
                },{
			        title: "创建时间",
			        field: "createTime",
			        sortable: true
			    },{
			        title: "操作",
			        field: "empty",
                    formatter: function (value, row, index) {
                    	var operateHtml = '<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="down(\'http://127.0.0.1:8080/upload/train/'+row.fileType+'/'+row.fileName+'\')"><i class="fa fa-edit"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                    	operateHtml = operateHtml + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
			    }]
			});
        });
        function uploadFile(){
            layer.open({
                type: 2,
                title: '批量上传资料',
                shadeClose: true,
                shade: false,
                area: ['600px', '600px'],
                content: '${ctx!}/admin/train/uploadFile?folder=${folder}',
                end: function(index){
                    $('#table_folder_train_list').bootstrapTable("refresh");
                }
            });
        }
        function down(url){
            window.location.href=url;
        }
        function del(id){
        	layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
        		$.ajax({
    	    		   type: "DELETE",
    	    		   dataType: "json",
    	    		   url: "${ctx!}/admin/train/delete/" + id,
    	    		   success: function(msg){
	 	   	    			layer.msg(msg.message, {time: 2000},function(){
	 	   	    				$('#table_folder_train_list').bootstrapTable("refresh");
	 	   	    				layer.close(index);
	 	   					});
    	    		   }
    	    	});
       		});
        }
    </script>
</body>

</html>
