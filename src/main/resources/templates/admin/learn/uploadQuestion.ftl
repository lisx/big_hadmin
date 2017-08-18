<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/select.ftl" as my />
<div class="ibox-content">
    <div class="row">
        <div class="col-sm-12">
            <form role="form" id="uploadForm" action="/admin/question/uploadFilePost" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label>题库名称：</label>
                    <input type="text"  name="bankName" class="form-control">
                </div>
                <div class="form-group">
                    <label>题目类型：</label>
                    <@my.select id="questionType" class="form-control" datas=["单选","多选","判断","排序"] defaultValue="请选择"/>
                </div>
                <div class="form-group">
                    <label>站区：</label>
                <@my.select id="area" class="form-control" datas=areas defaultValue="请选择"/>
                </div>
                <div class="form-group">
                    <label>站点：</label>
                <@my.select id="station" class="form-control" datas=["单选","多选","判断","排序"] defaultValue="请选择"/>
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

<script>
    function uploadUserClose(){
        $.ajax({
            url: '/admin/question/uploadFilePost',
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
    $("#area").change(function(){
        var area=$("#area").val();
        console.log("||||"+area);
        $.ajax({
            url: '/admin/station/getStation?area='+area,
            type: 'GET',
            cache: false,
            processData: false,
            contentType: false
        }).done(function(stations) {
            console.log(stations);
        });
    });
    function downUploadUser(){
        window.open("/uploadUser.xls");
    }
</script>