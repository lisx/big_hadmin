<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#include "/admin/common/webuploader.ftl">
<#include "/admin/common/ztree.ftl">
<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="col-sm-13">
                        <div class="tabs-container">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true"> 培训资料</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">练习/考试</a>
                                </li>
                                <li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">试卷类型</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane active">
                                    <div class="panel-body">
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
                                                            <table id="table_train_list"></table>
                                                        </div>
                                                    </div>
                                                    <!-- End Example Card View -->
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                <div id="tab-2" class="tab-pane">
                                    <div class="panel-body">
                                            <p>
                                            <@shiro.hasPermission name="system:resource:add">
                                                <button class="btn btn-success " type="button" onclick="uploadQuestion();"><i class="fa fa-plus"></i>&nbsp;创建题库</button>
                                                <button class="btn btn-success " type="button" onclick="uploadImage();"><i class="fa fa-plus"></i>&nbsp;批量导入附件</button>
                                            </@shiro.hasPermission>
                                            </p>
                                        <hr>
                                        <div class="row row-lg">
                                            <div class="col-sm-12">
                                                <!-- Example Card View -->
                                                <div class="example-wrap">
                                                    <div class="example">
                                                        <table id="table_bank_list"></table>
                                                    </div>
                                                </div>
                                                <!-- End Example Card View -->
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="tab-3" class="tab-pane">
                                    <div class="panel-body">
                                        <p>
                                        <@shiro.hasPermission name="system:resource:add">
                                            <button class="btn btn-success " type="button" onclick="configExam();"><i class="fa fa-plus"></i>&nbsp;配置试卷类型</button>
                                        </@shiro.hasPermission>
                                        </p>
                                        <hr>
                                        <div class="row row-lg">
                                            <div class="col-sm-12">
                                                <!-- Example Card View -->
                                                <div class="example-wrap">
                                                    <div class="example">
                                                        <table id="table_exam_list"></table>
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
    <script type="text/javascript">
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
            $(".uploadFile").attr("dataid",treeNode.id);
            $(".addFolder").attr("dataid",treeNode.id);
            $(".spanStation").html(treeNode.name);
            var opt = {
                url: "${ctx!}/admin/train/list",
                silent: true,
                query:{
                    nodeCode:treeNode.id
                }
            };
            $(".nodeCodeTrain").val(treeNode.id);
            $("#table_train_list").bootstrapTable('refresh', opt);

        }
    </script>
    <!-- Page-Level Scripts -->
    <script>
        $(document).ready(function () {
            console.log("++++++++++++++++++++++")
			//初始化表格,动态从服务器加载数据
			$("#table_train_list").bootstrapTable({
			    //使用get请求到服务器获取数据
			    method: "POST",
			    //必须设置，不然request.getParameter获取不到请求参数
			    contentType: "application/x-www-form-urlencoded",
			    //获取数据的Servlet地址
			    url: "${ctx!}/admin/train/list",
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
                            operateHtml='<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="showFolder(\''+row.fileName+'\')"><i class="fa fa-edit"></i>&nbsp;查看</button> &nbsp;</@shiro.hasPermission>';
                        }else{
                            operateHtml='<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-edit"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                        }
                    	operateHtml = operateHtml + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="delFolder(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
			    }]
			});
            $.get("${ctx!}/admin/station/tree",function(data){
                var zNodes =eval(data);
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            })
            //初始化表格,动态从服务器加载数据
            $("#table_bank_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "GET",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: "${ctx!}/admin/question/bank",
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
                    title: "问题库",
                    field: "name",
                },{
                    title: "归属",
                    field: "station.nodeName",
                },{
                    title: "创建时间",
                    field: "createTime",
                    sortable: true
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml = '<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="bankShow(\''+row.id+'\')"><i class="fa fa-edit"></i>&nbsp;查看</button> &nbsp;</@shiro.hasPermission>';
                        operateHtml = operateHtml + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
                }]
            });
            $("#table_exam_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "GET",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: "${ctx!}/admin/exam/list",
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
                    title: "试卷名称",
                    field: "examName",
                },{
                    title: "单选题",
                    field: "singleNum",
                },{
                    title: "分数",
                    field: "singleScore",
                },{
                    title: "多选题",
                    field: "multipleNum",
                },{
                    title: "分数",
                    field: "multipleScore",
                },{
                    title: "判断题",
                    field: "judgeNum",
                },{
                    title: "分数",
                    field: "judgeScore",
                },{
                    title: "排序题",
                    field: "rankNum",
                },{
                    title: "分数",
                    field: "rankScore",
                },{
                    title: "创建时间",
                    field: "createTime",
                    sortable: true
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml = '<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="examEdit(\''+row.id+'\')"><i class="fa fa-edit"></i>&nbsp;编辑</button> &nbsp;</@shiro.hasPermission>';
                        operateHtml = operateHtml + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="examDel(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                        return operateHtml;
                    }
                }]
            });
        });
        //下载文件
        function down(id,name){
            console.log(id+"|||||"+name);
            var a = document.createElement('a');
            a.href = "${ctx!}/admin/download/"+id;
            a.download = name;
            a.click();
        }
        //上传培训资料
        function uploadFile(){
            var id=$(".addFolder").attr("dataid");
            console.log("id:++"+id);
            layer.open({
                type: 2,
                title: '批量上传资料',
                shadeClose: true,
                shade: false,
                area: ['600px', '600px'],
                content: '${ctx!}/admin/train/uploadFile?nodeCode='+id,
                end: function(index){
                    $('#table_train_list').bootstrapTable("refresh");
                }
            });
        }
        function addFolder(){
            var nodeCode=$(".addFolder").val();
            layer.open({
                type: 2,
                title: '新建文件夹',
                shadeClose: true,
                shade: false,
                area: ['400px', '400px'],
                content: '${ctx!}/admin/train/add?nodeCode='+nodeCode+'&menu="培训资料"',
                end: function(index){
                    $('#table_train_list').bootstrapTable("refresh");
                }
            });
        }
        function showFolder(station){
            layer.open({
                type: 2,
                title: '查看文件夹',
                shadeClose: true,
                shade: false,
                area: ['98%', '98%'],
                content: '${ctx!}/admin/train/toFolder?folder='+station,
                end: function(index){
                    $('#table_train_list').bootstrapTable("refresh");
                }
            });
        }
        function delFolder(id){
            layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: "${ctx!}/admin/train/delete/" + id,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            $('#table_train_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        }


        function bankShow(id){
            layer.open({
                type: 2,
                title: '查看试题',
                shadeClose: true,
                shade: false,
                area: ['100%', '100%'],
                content: '${ctx!}/admin/question/index',
                end: function(index){
                    $('#table_bank_list').bootstrapTable("refresh");
                }
            });
        }
        function configExam(){
            layer.open({
                type: 2,
                title: '配置试卷类型',
                shadeClose: true,
                shade: false,
                area: ['600px', '700px'],
                content: '${ctx!}/admin/exam/add',
                end: function(index){
                    $('#table_exam_list').bootstrapTable("refresh");
                }
            });
        }
        function examEdit(id){
            layer.open({
                type: 2,
                title: '配置试卷类型',
                shadeClose: true,
                shade: false,
                area: ['600px', '600px'],
                content: '${ctx!}/admin/exam/examEdit?id='+id,
                end: function(index){
                    $('#table_exam_list').bootstrapTable("refresh");
                }
            });
        }

        function examDel(id){
            layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: "${ctx!}/admin/exam/delete/" + id,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            $('#table_exam_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        }



        function uploadQuestion(){
            layer.open({
                type: 2,
                title: '创建题库',
                shadeClose: true,
                shade: false,
                area: ['500px', '600px'],
                content: '${ctx!}/admin/question/uploadQuestion',
                end: function(index){
                    $('#table_bank_list').bootstrapTable("refresh");
                }
            });
        }
        function uploadImage(){
            layer.open({
                type: 2,
                title: '批量上传附件',
                shadeClose: true,
                shade: false,
                area: ['400px', '400px'],
                content: '${ctx!}/admin/question/uploadImage',
                end: function(index){
                    $('#table_bank_list').bootstrapTable("refresh");
                }
            });
        }
    </script>
