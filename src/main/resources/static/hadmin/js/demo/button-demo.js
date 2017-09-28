var Button = {
    createNew: function () {
        var button = {};
        button.addFolder=function(url){
            var valArr = new Array;
            $("input[name='allocation']").each(function (i) {
                valArr[i] = $(this).val();
            });
            if (valArr.length == 0) {
                alert("请先选择一条数据");
                return;
            }
            var priv = valArr.join(',');
            var nodeCode = priv;
            layer.open({
                type: 2,
                title: '新建文件夹',
                shadeClose: true,
                shade: [0.2, '#fff'],
                area: ['400px', '400px'],
                content: url+"&nodeCode="+nodeCode,
                end: function (index) {
                    $('#table_list').bootstrapTable("refresh");
                    $(".addFolder").attr("disabled", false);
                }
            });
        };
        button.showFolder=function(url){
            layer.open({
                type: 2,
                title: '文件列表',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: url,
                end: function(index){
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };
        button.uploadFile=function(url){
            var valArr = new Array;
            $("input[name='allocation']").each(function (i) {
                valArr[i] = $(this).val();
            });
            if (valArr.length == 0) {
                alert("请先选择一条数据");
                return;
            }
            var priv = valArr.join(',');
            var nodeCode = priv;
            layer.open({
                type: 2,
                title: '上传文件',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: url+'&nodeCode=' + nodeCode,
                end: function (index) {
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };
        button.uploadFile=function(url,id){
            var valArr = new Array;
            $("input[name='allocation']").each(function (i) {
                valArr[i] = $(this).val();
            });
            if (valArr.length == 0) {
                alert("请先选择一条数据");
                return;
            }
            var priv = valArr.join(',');
            var nodeCode = priv;
            layer.open({
                type: 2,
                title: '上传文件',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: url+id+'&nodeCode=' + nodeCode,
                end: function (index) {
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };
        button.uploadFileFolder=function(url,id){
            layer.open({
                type: 2,
                title: '上传文件',
                shadeClose: true,
                shade: false,
                area: ['97%', '94%'],
                content: url+id,
                end: function (index) {
                    $('#table_list').bootstrapTable("refresh");
                }
            });
        };
        button.down=function(url,name){
            var a = document.createElement('a');
            a.href = url;
            a.download = name;
            a.click();
        };
        button.del=function(url){
            layer.confirm('确定删除吗?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: url,
                    success: function (msg) {
                        layer.msg(msg.message, {time: 1000}, function () {
                            $('#table_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        };
        button.removeAll=function (url) {
            var obj = $('#table_list').bootstrapTable('getAllSelections');
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
                    url: url + ids,
                    success: function (msg) {
                        layer.msg(msg.message, {time: 1000}, function () {
                            $('#table_list').bootstrapTable("refresh");
                            layer.close(index);
                        });
                    }
                });
            });
        };
        return button;
    }
}
