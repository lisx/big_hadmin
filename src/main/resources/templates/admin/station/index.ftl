<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<#include "/admin/common/ztree.ftl">
<style>
    .table tbody tr td{
        text-overflow:ellipsis;
        white-space: pre-wrap;
    }
</style>
    <script type="text/javascript">
        $(document).ready(function () {
            //初始化表格,动态从服务器加载数据
            $("#table_station_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "POST",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: "${ctx!}/admin/station/list",
                //表格显示条纹
                striped: true,
                sortOrder: "desc", //排序方式
                sortName:"id",
                clickToSelect: true,                //是否启用点击选中行
                uniqueId: "id",                     //每一行的唯一标识，一般为主键列
                showExport: true,                     //是否显示导出
                exportDataType: "basic",              //basic', 'all', 'selected'.
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
                //json数据解析
                responseHandler: function(res) {
                    return {
                        "rows": res.content,
                        "total": res.totalElements
                    };
                },
                //数据列
                columns: [{
                    checkbox: true
                },{
                    title: "编号",
                    field: "id"
                },{
                    title: "文件名",
                    field: "fileName"
                },{
                    title: "大小",
                    field: "fileSize",
                },{
                    title: "归属",
                    field: "stations",
                    formatter: function(value ,row,index) {
                        if (value!=null) {
                            var r = "";
                            $(value).each(function (index,station){
                                r = r + station.nodeName+",";
                            });
                            return r.replace(/(\,$)/g, "");
                        }else{
                            return "运三分公司";
                        }
                    }
                },{
                    title: "创建时间",
                    field: "createTime"
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml = '<@shiro.hasPermission name="system:user:edit"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                        operateHtml = operateHtml + '<@shiro.hasPermission name="system:user:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button> &nbsp;</@shiro.hasPermission>';
                        return operateHtml;
                    }
                }]
            });
            $.get("${ctx!}/admin/station/tree",function(data){
                var zNodes =eval(data);
                var zTreeObj=$.fn.zTree.init($("#treeDemo"), setting, zNodes);
                zTreeObj.expandAll(true);
                $("#addLeaf").bind("click", {isParent:false}, add);
                $("#edit").bind("click", edit);
                $("#remove").bind("click", remove);
            })
        });
        var setting = {
            view: {
                selectedMulti: false
            },
            check: {
                enable: true
            },
            edit: {
                enable: true,
                showRemoveBtn: false,
                showRenameBtn: false,
                removeTitle: '删除',
                renameTitle: '编辑'

            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeDrag: beforeDrag,
                beforeRemove: beforeRemove,
                beforeRename: beforeRename,
                onRemove: onRemove,
                onRename: onRename,
                onClick: onClick,
                onCheck: onCheck
            }
        };
        function onCheck(event, treeId, treeNode) {
            console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked);
            if(treeNode.checked){
                appendHidden(treeNode.id);
                $(".fileUploadBtton").attr("data-id",treeNode.id);
                $(".spanStation").html(treeNode.name);
            }else{
                removeHidden(treeNode.id);
            }
        };
        function appendHidden(id){
            var hiddenString = '<input type="hidden" name="allocation" value="'+id+'">';
            $("#hiddenBox").append(hiddenString);
        }
        function removeHidden(id){
            $("#hiddenBox>input").each(function(index, element) {
                if($(this).val() == id){
                    $(this).remove();
                }
                if(isContains($(this).val(),id)){
                    $(this).remove();
                }
            });
        }
        function isContains(str, substr) {
            return str.indexOf(substr) >= 0;
        }
        var log, className = "dark";
        function beforeDrag(treeId, treeNodes) {
            return false;
        }
        function getTime() {
            var now= new Date(),
                    h=now.getHours(),
                    m=now.getMinutes(),
                    s=now.getSeconds(),
                    ms=now.getMilliseconds();
            return (h+":"+m+":"+s+ " " +ms);
        }
        function beforeRemove(treeId, treeNode) {
            return confirm("确认删除 -- " + treeNode.name + " 吗？");
        }
        function onRemove(e, treeId, treeNode) {
            var nodeId = treeNode.id;
            console.log(nodeId)
            $.ajax({
                url: '/admin/station/del/'+nodeId,
                type: 'DELETE',
                data: {}
            });
        }
        /*修改节点*/
        function onRename(e,treeId,treeNode){
            console.log("treeId:"+treeId);
            $.post("/admin/station/update/"+treeNode.id,treeNode,function(data){});
        };
        function beforeRename(treeId, treeNode, newName) {
            if (newName.length == 0) {
                alert("节点名称不能为空.");
                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                setTimeout(function(){zTree.editName(treeNode)}, 10);
                return false;
            }
            return true;
        }
        var newCount = 1;
        /*保存新的节点*/
        function saveNode(parentNode){
            console.log(parentNode);
            var zTree = getTree();
            var _nodeName="新节点";
            $.post('/admin/station/save',{pId:parentNode.id,name:_nodeName},function(data){
                console.log("data.nodeCode"+data.nodeCode)
                var newCode = {id:data.nodeCode,pId:parentNode.id,name:_nodeName};
                newCode=zTree.addNodes(parentNode,newCode);
                zTree.editName(newCode[0]);
            },"json");
        };
        function add(e) {
            var zTree = getTree();
                    isParent = e.data.isParent,
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];
            if (nodes.length == 0) {
                alert("请先选择一个站区或站点");
                return;
            }
            if (treeNode) {
                saveNode(treeNode);
            }
        };
        function edit() {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];
            if (nodes.length == 0) {
                alert("请先选择一个站区或站点");
                return;
            }
            zTree.editName(treeNode);
        };
        function remove(e) {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];
            if (nodes.length == 0) {
                alert("请先选择一个站点");
                return;
            }
            var callbackFlag = true;
            zTree.removeNode(treeNode, callbackFlag);
        };
        function getTree(){
            return $.fn.zTree.getZTreeObj("treeDemo");
        };
        /*单击节点显示节点详情*/
        function onClick(e,treeId,treeNode){
            //初始化表格,动态从服务器加载数据
            $(".fileUploadBtton").attr("data-id",treeNode.id);
            $(".spanStation").html(treeNode.name);
            var opt = {
                url: "${ctx!}/admin/station/list",
                silent: true,
                query:{
                    nodeCode:treeNode.id
                }
            };

            $("#table_station_list").bootstrapTable('refresh', opt);
        };

        function upload(){

            console.log("id:++"+id);
            layer.open({
                type: 2,
                title: '上传',
                shadeClose: true,
                shade: false,
                area: ['600px', '600px'],
                content: '${ctx!}/admin/station/upload?nodeCode='+id,
                end: function(index){
                    $('#table_station_list').bootstrapTable("refresh");
                }
            });
        };

        function uploadFile(){
            var id=$(".addFolder").attr("data-id");
            var valArr = new Array;
            $("input[name='allocation']").each(function(i){
                valArr[i] = $(this).val();
            });
            var priv = valArr.join(',');
            console.log("|||||||||||||||||||||"+priv)
            var id=priv;
            layer.open({
                type: 2,
                title: '上传文件',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: '${ctx!}/admin/station/uploadFile?nodeCode='+id,
                end: function(index){
                    $('#table_station_list').bootstrapTable("refresh");
                }
            });
        };


        //下载文件
        function down(id,name){
            console.log(id+"|||||"+name);
            var a = document.createElement('a');
            a.href = "${ctx!}/admin/download/"+id;
            a.download = name;
            a.click();
        }
        function del(id){
            layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: "${ctx!}/admin/station/delete/" + id,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            $('#table_station_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        };
        function removeAll(){
            var obj=$('#table_station_list') .bootstrapTable('getAllSelections');
            if (obj.length == 0) {
                alert("请先选择一条数据");
                return;
            }
            var ids="";
            $(obj).each(function(index,data){
                ids=ids+data.id+",";
            });
            console.log("ids"+ids)
            layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: "${ctx!}/admin/station/delete/" + ids,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            $('#table_station_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        }
    </script>

    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>车站信息</h5>
                        <p>
                        <@shiro.hasPermission name="system:station:uploadFile">
                            <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i class="fa fa-plus"></i>&nbsp;批量删除文件</button>
                            <button class="btn btn-success pull-right fileUploadBtton" type="button" onclick="uploadFile();"><i class="fa fa-plus"></i>&nbsp;上传文件</button>
                            <button class="btn btn-success pull-right remove" id="remove" type="button"><i class="fa fa-plus"></i>&nbsp;删除车站</button>
                            <button class="btn btn-success pull-right edit" id="edit" type="button"><i class="fa fa-plus"></i>&nbsp;编辑车站</button>
                            <button class="btn btn-success pull-right addLeaf" id="addLeaf" type="button"><i class="fa fa-plus"></i>&nbsp;新增车站</button>
                            <h5 class="spanStation" style="margin-left: 20px"></h5>
                        </@shiro.hasPermission>
                        </p>
                        <div id="hiddenBox">

                        </div>
                    </div>
                    <div class="ibox-content">
                        <div class="row row-lg">
		                    <div class="col-sm-3">
                                    <div class='tree'><ul id="treeDemo" class="ztree"></ul></div>
                            </div>
                            <div class="col-sm-9">
		                        <!-- Example Card View -->
		                        <div class="example-wrap">
		                            <div class="example">
		                            	<table class="table table-bordered" id="table_station_list"></table>
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
