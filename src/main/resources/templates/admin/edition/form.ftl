<#include "/admin/common/css.ftl">

<div class="ibox-content">
    <div class="row">
        <div class="col-sm-12">
            <form role="form" id="uploadForm" action="/admin/edition/fileUpload" method="POST" enctype="multipart/form-data">
                <div class="form-group ">
                    <label>说明：请上传格式为apk的版本安装包。</label>
                    <input type="file" class="form-control" name="fileUpload" />
                </div>
                <div>
                    <button class="btn btn-sm btn-primary pull-left m-t-n-xs" onclick="uploadUserClose()" type="button"><strong>发布版本</strong>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- 全局js -->
<#include "/admin/common/js.ftl">
<script>
    function uploadUserClose(){
        $.ajax({
            url: '/admin/user/fileUpload',
            type: 'POST',
            cache: false,
            data: new FormData($('#uploadForm')[0]),
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