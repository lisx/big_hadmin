<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
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
<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-content">
                        <div class="row row-lg">
		                    <div class="col-sm-12">
		                        <!-- Example Card View -->
		                        <div class="example-wrap">
		                            <div class="example">
		                            	<table id="table_question_list"  style="table-layout:fixed;overflow:hidden;"></table>
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
			$("#table_question_list").bootstrapTable({
			    //使用get请求到服务器获取数据
			    method: "POST",
			    //必须设置，不然request.getParameter获取不到请求参数
			    contentType: "application/x-www-form-urlencoded",
			    //获取数据的Servlet地址
			    url: "${ctx!}/admin/question/list?id=${id}",
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
                //是否启用详细信息视图
                detailView:true,
                detailFormatter:detailFormatter,
			    //是否启用查询
			    search: true,
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
                    width:50
			    },{
                    title: "类型",
                    field: "menuType",
                    width:50
                },{
			        title: "问题",
			        field: "title",
			    },{
			        title: "答案",
			        field: "proper",
			    },{
			        title: "创建时间",
			        field: "createTime",
			        sortable: true,
                    width:200,
			    },{
			        title: "操作",
			        field: "empty",
                    width:80,
                    formatter: function (value, row, index) {
                    	var operateHtml = '<@shiro.hasPermission name="system:user:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button> &nbsp;</@shiro.hasPermission>';
                        return operateHtml;
                    }
			    }]
			});
        });
        function detailFormatter(index, row) {
            var html = [];
            html.push('<span><b>问题:</b> ' + row.title + '</span>'+'<span><b>答案:</b> ' + row.proper + '</span>');
            return html.join('');
        }
        $("#table_question_list").on("click","tr td:nth-child(4)",function(){
            var content=$(this).text();
            layer.open({
                type: 4,
                close:false,
                content: [content, $(this)] //数组第二项即吸附元素选择器或者DOM
            });
        })
        $("#table_question_list").on("click","tr td:nth-child(5)",function(){
            var content=$(this).text();
            layer.open({
                type: 4,
                shade:[0.2,'#fff'],
                content: [content, $(this)] //数组第二项即吸附元素选择器或者DOM
            });
        })
        function del(id){
        	layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
        		$.ajax({
    	    		   type: "DELETE",
    	    		   dataType: "json",
    	    		   url: "${ctx!}/admin/question/delete/" + id,
    	    		   success: function(msg){
	 	   	    			layer.msg(msg.message, {time: 2000},function(){
	 	   	    				$('#table_question_list').bootstrapTable("refresh");
	 	   	    				layer.close(index);
	 	   					});
    	    		   }
    	    	});
       		});
        }

    </script>
