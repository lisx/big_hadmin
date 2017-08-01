<#include "/admin/common/form.ftl">

<div class="ibox-content">
    <div class="row">
        <div class="col-sm-12">
            <form role="form" id="uploadForm" action="/admin/user/fileUploadUser" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label>第一步 请下载模板并根据示例填写需要导入的员工信息，上传会根据工号查找头像；三证号找对应三证图片。</label>
                    <button class="btn btn-sm btn-primary m-t-n-xs" onclick="downUploadUser()" type="button"><strong>模版下载</strong>
                    </button>
                </div>
                <div class="form-group">
                    <label>第二步 选择已经填写完成的员工信息模板点击上传（仅支持.xls/.xlsx格式,且文件大小不能超过2M ）。</label>
                    <input type="file" class="form-control" name="fileUpload" />
                </div>
                <div>
                    <button class="btn btn-sm btn-primary pull-right m-t-n-xs" onclick="uploadUserClose()" type="button"><strong>上传文件</strong>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- 全局js -->
<#include "/admin/common/common.ftl">
<script>
    function uploadUserClose(){
        $.ajax({
            url: '/admin/user/fileUploadUser',
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
    function downUploadUser(){
        window.open("/uploadUser.xls");
    }
</script>