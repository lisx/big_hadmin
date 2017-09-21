<!-- 全局js -->
<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
<#include "/admin/common/ztree.ftl">
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
                    <h5>应急预案</h5>
                <p>
                <@shiro.hasPermission name="system:resource:add">
                    <button class="btn btn-success pull-right" onclick="removeAll()" type="button"><i
                            class="fa fa-plus"></i>&nbsp;批量删除
                    </button>
                    <button class="btn btn-success pull-right uploadFile" type="button" onclick="uploadFile();"><i
                            class="fa fa-plus"></i>&nbsp;上传资料
                    </button>
                    <button class="btn btn-success pull-right addFolder" type="button" onclick="addFolder();"><i
                            class="fa fa-plus"></i>&nbsp;新建文件夹
                    </button>
                    <h5 class="spanStation" style="margin-left: 20px"></h5>
                </@shiro.hasPermission>
                </p>
                <div id="hiddenBox">

                </div>
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
                                <div class="example">
                                    <table class="table table-bordered" id="table_emergency_list"></table>
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
<script type="text/javascript">
    $(document).ready(function () {
        //初始化表格,动态从服务器加载数据
        $("#table_emergency_list").bootstrapTable({
            //使用get请求到服务器获取数据
            method: "GET",
            //必须设置，不然request.getParameter获取不到请求参数
            contentType: "application/x-www-form-urlencoded",
            //获取数据的Servlet地址
            url: "${ctx!}/admin/emergency/list",
            //表格显示条纹
            striped: true,
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: "ifFolder,id",
            clickToSelect: true,                //是否启用点击选中行
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
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
            responseHandler: function (res) {
                return {
                    "rows": res.content,
                    "total": res.totalElements
                };
            },
            //数据列
            columns: [{
                checkbox: true
            }, {
                title: "编号",
                field: "id",
                sortable: true
            }, {
                title: "文件名",
                field: "empty",
                formatter: function (value, row, index) {
                    if (row.ifFolder == 1) {
                        return '<a href="javascript:void(0);" onclick="showFolder(\'' + row.id + '\',\'' + row.fileName + '\')"><i class="fa fa-folder-o"></i>' + row.fileName + '</a>';
                    } else {
                        return row.fileName;
                    }
                }
            }, {
                title: "归属",
                field: "stations",
                formatter: function (value, row, index) {
                    if (value != null) {
                        var r="";
                        $(value).each(function(index,station){
                            r=r+station.nodeName+",";
                        });

                        return r.replace(/\,$/,"");
                    } else {
                        return "运三分公司";
                    }
                }
            }, {
                title: "大小",
                field: "fileSize"
            }, {
                title: "创建时间",
                field: "createTime"
            }, {
                title: "操作",
                field: "empty",
                formatter: function (value, row, index) {
                    var operateHtml = '';
                    if (row.ifFolder == 1) {
                        operateHtml = '<@shiro.hasPermission name="system:resource:add"><button class="btn btn-success btn-xs" type="button" onclick="showFolder(\''+ row.id+'\',\''+ row.fileName+'\')"><i class="fa fa-eye"></i>&nbsp;查看</button> &nbsp;</@shiro.hasPermission>';
                    } else {
                        operateHtml = '<@shiro.hasPermission name="system:resource:add"><button class="btn btn-primary btn-xs" type="button" onclick="down(\''+ row.id+ '\',\''+ row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button> &nbsp;</@shiro.hasPermission>';
                    }

                    operateHtml = operateHtml + '<@shiro.hasPermission name="system:resource:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+ row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button></@shiro.hasPermission>';
                    return operateHtml;
                }
            }],
            <#--onClickRow: function (row) {-->
                <#--console.log("||||" + row.id)-->
                <#--$(".uploadFile").attr("folderId", row.id);-->
                <#--var opt = {-->
                    <#--url: "${ctx!}/admin/emergency/list",-->
                    <#--silent: true,-->
                    <#--query: {-->
                        <#--folderId: row.id-->
                    <#--}-->
                <#--};-->
                <#--$("#table_emergency_list").bootstrapTable('refresh', opt);-->
            <#--}-->
        });
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            check: {
                enable: true
            },
            callback: {
                onClick: onClick,
                onCheck: onCheck
            }
        };
        $.get("/admin/emergency/tree", function (data) {
            var zNodes = eval(data);
            var zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            zTreeObj.expandAll(true);
        })
    });
    function onCheck(event, treeId, treeNode) {
        console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked);
        if (treeNode.checked) {
            appendHidden(treeNode.id);
            $(".fileUploadBtton").attr("data-id", treeNode.id);
            $(".spanStation").html(treeNode.name);
        } else {
            removeHidden(treeNode.id);
        }
    };
    function appendHidden(id) {
        var hiddenString = '<input type="hidden" name="allocation" value="' + id + '">';
        $("#hiddenBox").append(hiddenString);
    }
    function removeHidden(id) {
        $("#hiddenBox>input").each(function (index, element) {
            if ($(this).val() == id) {
                $(this).remove();
            }
            if (isContains($(this).val(), id)) {
                $(this).remove();
            }
        });
    }
    function isContains(str, substr) {
        return str.indexOf(substr) >= 0;
    }
    /*单击节点显示节点详情*/
    function onClick(e, treeId, treeNode) {
        console.log("|||" + treeNode.id + "|||" + treeNode.name)
        //初始化表格,动态从服务器加载数据
        $(".uploadFile").attr("data-id", treeNode.id);
        $(".addFolder").attr("data-id", treeNode.id);
        $(".spanStation").html(treeNode.name);
        var opt = {
            url: "${ctx!}/admin/emergency/list",
            silent: true,
            query: {
                nodeCode: treeNode.id
            }
        };
        $("#table_emergency_list").bootstrapTable('refresh', opt);
    };
    //进入文件夹
    function showFolder(folderId) {
        layer.open({
            type: 2,
            title: '文件列表',
            shadeClose: true,
            shade: false,
            area: ['97%', '94%'],
            content: '${ctx!}/admin/emergency/toFolder?folderId=' + folderId,
            end: function (index) {
                $('#table_emergency_list').bootstrapTable("refresh");
            }
        });
    };
    function removeAll() {
        var obj = $('#table_emergency_list').bootstrapTable('getAllSelections');
        if (obj.length == 0) {
            alert("请先选择一条数据");
            return;
        }
        var ids = "";
        $(obj).each(function (index, data) {
            ids = ids + data.id + ",";
        });
        console.log("ids" + ids)
        layer.confirm('确定删除吗?', {icon: 3, title: '提示'}, function (index) {
            $.ajax({
                type: "DELETE",
                dataType: "json",
                url: "${ctx!}/admin/emergency/delete/" + ids,
                success: function (msg) {
                    layer.msg(msg.message, {time: 1000}, function () {
                        $('#table_emergency_list').bootstrapTable("refresh");
                        layer.close(index);
                    });
                }
            });
        });
    }
    //上传文件
    function uploadFile() {
        var id = $(".addFolder").attr("data-id");
        console.log("id:++" + id);
        var valArr = new Array;
        $("input[name='allocation']").each(function (i) {
            valArr[i] = $(this).val();
        });
        var priv = valArr.join(',');
        console.log("|||||||||||||||||||||" + priv)
        var nodeCode = priv;
        layer.open({
            type: 2,
            title: '上传文件',
            shadeClose: true,
            shade: false,
            area: ['97%', '94%'],
            content: '${ctx!}/admin/emergency/uploadFile?nodeCode=' + nodeCode,
            end: function (index) {
                $('#table_emergency_list').bootstrapTable("refresh");
            }
        });
    };
    //添加文件夹
    function addFolder() {
        var nodeCode = $(".addFolder").attr("data-id");
        $(".addFolder").attr("disabled", "disabled");
        var valArr = new Array;
        $("input[name='allocation']").each(function (i) {
            valArr[i] = $(this).val();
        });
        var priv = valArr.join(',');
        console.log("|||||||||||||||||||||" + priv)
        var nodeCode = priv;
        layer.open({
            type: 2,
            title: '新建文件夹',
            shadeClose: true,
            shade: [0.2, '#fff'],
            area: ['400px', '400px'],
            content: '${ctx!}/admin/emergency/add?nodeCode=' + nodeCode + '&menu=应急预案',
            end: function (index) {
                $('#table_emergency_list').bootstrapTable("refresh");
                $(".addFolder").attr("disabled", false);
            }
        });
    }
    //下载文件
    function down(id, name) {
        var a = document.createElement('a');
        a.href = "${ctx!}/admin/download/" + id;
        a.download = name;
        a.click();
    }
    //删除文件夹或文件
    function del(id) {
        layer.confirm('确定删除吗?', {icon: 3, title: '提示'}, function (index) {
            $.ajax({
                type: "DELETE",
                dataType: "json",
                url: "${ctx!}/admin/emergency/delete/" + id,
                success: function (msg) {
                    layer.msg(msg.message, {time: 2000}, function () {
                        $('#table_emergency_list').bootstrapTable("refresh");
                        layer.close(index);
                    });
                }
            });
        });
    };
</script>


