<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/select.ftl" as my />
<div class="ibox-content">
    <div class="row">
        <div class="col-sm-12">
            <form role="form" id="uploadForm" action="/admin/question/uploadFilePost" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label class="col-sm-2  control-label" >题库名称：</label>
                    <div  class="col-sm-4">
                        <input type="text"  name="bankName" class="form-control">
                    </div>
                    <label class="col-sm-2  control-label" ></label>
                    <div  class="col-sm-4">
                    </div>
                </div>
                <br><br>
                <div class="form-group">
                    <label class="col-sm-2  control-label">站区：</label>
                    <div class="col-sm-4">
                    <@my.select id="area" class="form-control" datas=areas defaultValue="全部"/>
                    </div>
                    <label class="col-sm-2 control-label">站点：</label>
                    <div class="col-sm-4">
                    <select id="station" name="station" class="form-control" >
                        <option value="全部">全部</option>
                    </select>
                    </div>
                </div>
                <br><br>
                <div class="form-group">
                    <button class="btn btn-sm btn-primary m-t-n-xs" onclick="downUpload('单选')" type="button"><strong>单选模版下载</strong>
                    </button>
                    <button class="btn btn-sm btn-primary m-t-n-xs" onclick="downUpload('多选')" type="button"><strong>多选模版下载</strong>
                    </button>
                    <button class="btn btn-sm btn-primary m-t-n-xs" onclick="downUpload('判断')" type="button"><strong>判断模版下载</strong>
                    </button>
                    <button class="btn btn-sm btn-primary m-t-n-xs" onclick="downUpload('排序')" type="button"><strong>排序模版下载</strong>
                    </button>
                </div>
                <br><br>
                <div class="form-group">
                    <label class="col-sm-2 control-label">上传单选试题：</label>
                    <div class="col-sm-4">
                    <input type="file" class="form-control" name="radioFile" />
                    </div>
                    <label class="col-sm-2 control-label">上传多选试题：</label>
                    <div class="col-sm-4">
                    <input type="file" class="form-control" name="multipleFile" />
                    </div>
                </div>
                <br><br>
                <div class="form-group">
                    <label class="col-sm-2 control-label">上传判断试题：</label>
                    <div class="col-sm-4">
                    <input type="file" class="form-control" name="opinionFile" />
                    </div>
                    <label class="col-sm-2 control-label">上传排序试题：</label>
                    <div class="col-sm-4">
                    <input type="file" class="form-control" name="sortFile" />
                    </div>
                </div>
                <br><br>
                <div>
                    <button class="btn btn-sm btn-primary pull-left m-t-n-xs" type="submit"><strong>完成创建</strong>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

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
                    url: "${ctx!}/admin/question/uploadFilePost",
                    data: new FormData($('#uploadForm')[0]),
                    success: function(msg){
                        layer.msg(msg.message, {time: 2000},function(){
                            if(msg.code==0){
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index);
                            }
                        });
                    }
                });
            }
        });
    });
//    function uploadUserClose(){
//        $.ajax({
//            url: '/admin/question/uploadFilePost',
//            type: 'POST',
//            cache: false,
//            data: new FormData($('#uploadForm')[0]),
//            processData: false,
//            contentType: false
//        }).done(function(msg) {
//            layer.msg(msg.message, {time: 2000},function(){
//                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
//                parent.layer.close(index);
//            });
//        }).fail(function(res) {});
//    }
    $("#area").change(function(){
        var area=$("#area").val();
        area=area.replace("#","%23");
        console.log("||||"+area);
        $.ajax({
            url: '/admin/station/getStation?area='+area,
            type: 'GET',
            dataType: 'json',
            cache: false,
            processData: false,
            contentType: false
        }).done(function(stations) {
            console.log(stations);
            for(var i in stations){
                $("#station").append("<option>"+stations[i]+"</option>");
            };
        });
    });
    function downUpload(obj){
        var a = document.createElement('a');
        if(obj=='单选'){
            a.href = "/upload/radio.xls";
            a.download = "单项选择题模版.xls";
        }else if(obj=='多选'){
            a.href = "/upload/multiple.xls";
            a.download="多项选择题模版.xls";
        }else if(obj=='判断'){
            a.href = "/upload/opinion.xls";
            a.download="判断题模版.xls";
        }else if(obj=='排序'){
            a.href = "/upload/sort.xls";
            a.download="排序题模版.xls";
        }
        a.click();
//        window.open("/upload/人员信息模版.xls");
    }
</script>