<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/select.ftl" as my />
<!-- layerDate plugin javascript -->
<script src="${ctx!}/hadmin/js/plugins/layer/laydate/laydate.js"></script>
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
        <div class="form-group runDate" style="display:none;">
            <label class="col-sm-3 control-label">运行时间：</label>
                <div class="col-sm-8">
                    <input placeholder="开始日期" name="startTime" class="form-control layer-date" id="start">
                    <input placeholder="结束日期" name="endTime" class="form-control layer-date" id="end">
                </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">选择运营图：</label>
            <div class="col-sm-8">
                <img id="preview" />
                <br>
                <input type="file" name="file" onchange="imgPreview(this)" />
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-8 col-sm-offset-3">
                <button class="btn btn-primary" type="submit" >保存</button>
            </div>
        </div>
    </form>
</div>
<script>

    $(document).ready(function(){
        $("#dateType").change(function(){
            var type=$("#dateType").val();
            console.log("||||"+type)
            if(type=="节假日"){
                $(".runDate").show();
            }else{
                $(".runDate").hide();
            }
        });
    })
        //日期范围限制
        var start = {
        elem: '#start',
        format: 'YYYY-MM-DD',
        min: laydate.now(), //设定最小日期为当前日期
        istime: false,
        istoday: false,
        choose: function (datas) {
        end.min = datas; //开始日选好后，重置结束日的最小日期
        end.start = datas //将结束日的初始值设定为开始日
    }
    };
        var end = {
        elem: '#end',
        format: 'YYYY-MM-DD',
        min: laydate.now(),
        istime: false,
        istoday: false,
        choose: function (datas) {
        start.max = datas; //结束日选好后，重置开始日的最大日期
    }
    };
        laydate(start);
        laydate(end);
    function imgPreview(fileDom){
        //判断是否支持FileReader
        if (window.FileReader) {
            var reader = new FileReader();
        } else {
            alert("您的设备不支持图片预览功能，如需该功能请升级您的设备！");
        }

        //获取文件
        var file = fileDom.files[0];
//        var imageType = /^image\//;
//        //是否是图片
//        if (!imageType.test(file.type)) {
//            alert("请选择图片！");
//            return;
//        }
        //读取完成
        reader.onload = function(e) {
            //获取图片dom
            var img = document.getElementById("preview");
            //图片路径设置为读取的图片
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
    $(document).ready(function () {
        $("#frm").validate({
            rules: {
                fileName: {
                    required: true,
                },
                lineName: {
                    required: true
                },
                dateType: {
                    required: true
                },
                file: {
                    required: true
                }
            },
            messages: {
                fileName:"运行图名称必填",
                lineName:"线路必选",
                dateType:"时间设置必选",
                file:"文件必选"
            },
            submitHandler:function(form){
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: '${ctx!}/admin/running/uploadFilePost',
                    data: new FormData($('#frm')[0]),
                    processData: false,
                    contentType: false,
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index);
                        });
                    }
                });
            }
        });
    });
</script>

