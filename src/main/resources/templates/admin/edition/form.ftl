<#include "/admin/common/css.ftl">

<div class="ibox-content">
    <div class="row">
        <div class="col-sm-12">
            <form role="form" id="uploadForm" action="/admin/edition/fileUploadEdition" method="POST" enctype="multipart/form-data">
                <div class="form-group ">
                    <label>说明：请上传格式为apk的版本安装包。</label>
                    <input type="file" class="form-control" name="file" />
                </div>
                <div>
                    <button class="btn btn-sm btn-primary pull-left m-t-n-xs" type="submit"><strong>发布版本</strong>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- 全局js -->
<#include "/admin/common/js.ftl">
<script>
    $(document).ready(function () {
        $("#uploadForm").validate({
            rules: {
                bankName: {
                    required: true,
                    maxlength: 10
                }
            },
            messages: {},
            submitHandler:function(form){
                $.ajax({
                    type: "POST",
                    cache: false,
                    dataType: "json",
                    processData: false,
                    contentType: false,
                    url: "${ctx!}/admin/edition/fileUploadEdition",
                    data: new FormData($('#uploadForm')[0]),
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        });
                    }
                });
            }
        });
    });
</script>