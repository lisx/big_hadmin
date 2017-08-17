<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/select.ftl" as my />
<link href="${ctx!}/hadmin/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<!-- Data picker -->
<script src="${ctx!}/hadmin/js/plugins/datapicker/bootstrap-datepicker.js"></script>
<div class="ibox-content">
    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/running/uploadFilePost" enctype="multipart/form-data">
        <div class="form-group">
            <label class="col-sm-3 control-label">运行图名称：</label>
            <div class="col-sm-8">
                <input id="fileName" name="fileName" class="form-control" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">选择线路：</label>
            <div class="col-sm-8">
            <@my.select id="lineName" class="form-control" datas=stations defaultValue="请选择"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">时间设置：</label>
            <div class="col-sm-8">
            <@my.select id="dateType" class="form-control" datas=["工作日","双休日","节假日"] defaultValue="请选择"/>
            </div>
        </div>
        <div class="form-group" id="data_5">
            <label class="font-noraml">运行时间：</label>
            <div class="input-daterange input-group" id="datepicker">
                <input type="text" class="input-sm form-control" name="startTime" value="2014-11-11">
                <span class="input-group-addon">到</span>
                <input type="text" class="input-sm form-control" name="endTime" value="2014-11-17">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">选择运营图：</label>
            <div class="col-sm-8">
                <img id="preview" />
                <br />
                <input type="file" name="file" onchange="imgPreview(this)" />
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-8 col-sm-offset-3">
                <button class="btn btn-primary" type="button" onclick="uploadClose()" >保存</button>
            </div>
        </div>
    </form>
</div>
<script>
    $('#datepicker').datetimepicker({
        format: 'yyyy-mm-dd'
    });
    function imgPreview(fileDom){
        //判断是否支持FileReader
        if (window.FileReader) {
            var reader = new FileReader();
        } else {
            alert("您的设备不支持图片预览功能，如需该功能请升级您的设备！");
        }

        //获取文件
        var file = fileDom.files[0];
        var imageType = /^image\//;
        //是否是图片
        if (!imageType.test(file.type)) {
            alert("请选择图片！");
            return;
        }
        //读取完成
        reader.onload = function(e) {
            //获取图片dom
            var img = document.getElementById("preview");
            //图片路径设置为读取的图片
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
    function uploadClose(){
        $.ajax({
            url: '${ctx!}/admin/running/uploadFilePost',
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

