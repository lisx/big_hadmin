var Table = {
    createNew: function () {
        var table = {};
        table.init=function(url){
            $("#table_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "GET",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: url,
                //表格显示条纹
                striped: true,
                sortable: true, //是否启用排序
                sortOrder: "desc", //排序方式
                sortName:"ifFolder,id",
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
                    width: 50
                },{
                    title: "文件名",
                    field: "empty",
                    formatter: function(value ,row,index) {
                        if (row.ifFolder == 1) {
                            return '<a href="javascript:void(0);" onclick="showFolder(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-folder-o"></i>' + row.fileName + '</a>';
                        }else{
                            return row.fileName;
                        }
                    }
                },{
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
                },{
                    title: "大小",
                    field: "fileSize",
                },{
                    title: "创建时间",
                    field: "createTime",
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml ='';
                        if(row.ifFolder==1){
                            operateHtml='<button class="btn btn-success btn-xs" type="button" onclick="showFolder(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-eye"></i>&nbsp;查看</button>';
                        }else{
                            operateHtml='<button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.id+'\',\''+row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button>';
                        }
                        operateHtml = operateHtml + '<button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button>';
                        return operateHtml;
                    }
                }],
            });
        };
        table.folder=function(url){
            $("#table_list").bootstrapTable({
                //使用get请求到服务器获取数据
                method: "POST",
                //必须设置，不然request.getParameter获取不到请求参数
                contentType: "application/x-www-form-urlencoded",
                //获取数据的Servlet地址
                url: url,
                //表格显示条纹
                striped: true,
                sortOrder: "desc", //排序方式
                sortName:"ifFolder,id",
                clickToSelect: true,                //是否启用点击选中行
                uniqueId: "id",
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
                columns: [{checkbox:true},{
                    title: "编号",
                    field: "id",
                    sortable: true
                },{
                    title: "文件名",
                    field: "fileName",
                },{
                    title: "大小",
                    field: "fileSize",
                },{
                    title: "创建时间",
                    field: "createTime",
                    sortable: true
                },{
                    title: "操作",
                    field: "empty",
                    formatter: function (value, row, index) {
                        var operateHtml = '<button class="btn btn-primary btn-xs" type="button" onclick="down(\''+row.fileId+'\',\''+row.fileName+'\')"><i class="fa fa-download"></i>&nbsp;下载</button> &nbsp;';
                        operateHtml = operateHtml + '<button class="btn btn-danger btn-xs" type="button" onclick="del(\''+row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button>';
                        return operateHtml;
                    }
                }]
            });
        };
        return table;
    }
}
