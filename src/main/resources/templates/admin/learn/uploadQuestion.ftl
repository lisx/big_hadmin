<#include "/admin/common/form.ftl">
<#import "/admin/common/select.ftl" as my />
<div class="ibox-content">
    <div class="row">
        <div class="col-sm-12">
            <form role="form" id="uploadForm" action="/admin/user/fileUploadUser" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label>题库名称：</label>
                    <input type="text"  name="questionName" class="form-control">
                </div>
                <div class="form-group">
                    <label>题目类型：</label>
                    <@my.select id="questionType" class="form-control" datas=["单选","多选","判断","排序"] defaultValue="请选择"/>
                </div>
                <div class="form-group">
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