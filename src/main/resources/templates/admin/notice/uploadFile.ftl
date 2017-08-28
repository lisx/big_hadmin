<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/treeselect.ftl" as my />
<script type='text/javascript' src='${ctx!}/hadmin/js/plugins/treeselect/jquery.tree-multiselect.js'></script>
<link rel='stylesheet' type='text/css' href='${ctx!}/hadmin/js/plugins/treeselect/jquery.tree-multiselect.css' />
<div class="ibox-content">
    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/notice/uploadFilePost" enctype="multipart/form-data">
        <div class="form-group">
            <label class="col-sm-3 control-label">通知标题：</label>
            <div class="col-sm-8">
                <input id="title" name="title" class="form-control" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">接收单位：</label>
            <div class="col-sm-8">
            <@my.select id="area" multiple="multiple" section="站区" class="form-control" datas=stations />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">内容：</label>
            <div class="col-sm-8">
                <textarea name="content" class="form-control">

                </textarea>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">附件：</label>
            <div class="col-sm-8">
                <input type="file" name="file" multiple="multiple" />
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-8 col-sm-offset-3">
                <button class="btn btn-primary" type="button" onclick="uploadClose()" >保存</button>
            </div>
        </div>
    </form>
</div>
<script type='text/javascript'>
    $("#area").treeMultiselect({enableSelectAll: true, allowBatchSelection: true, searchable: true, startCollapsed: true, allSelectedText: '全选'});
    function uploadClose(){
        $.ajax({
            url: '${ctx!}/admin/notice/uploadFilePost',
            type: 'POST',
            cache: false,
            data: new FormData($('#frm')[0]),
            processData: false,
            contentType: false
        }).done(function(msg) {
            layer.msg(msg.message, {time: 2000},function(){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index);
            });
        }).fail(function(res) {});
    }
</script>

