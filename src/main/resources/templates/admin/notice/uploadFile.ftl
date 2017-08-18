<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#include "/admin/common/summernote.ftl">
<#import "/admin/common/select.ftl" as my />

<div class="ibox-content">
    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/notice/uploadFilePost" enctype="multipart/form-data">
        <div class="form-group">
            <label class="col-sm-3 control-label">通知标题：</label>
            <div class="col-sm-8">
                <input id="title" name="title" class="form-control" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">内容：</label>
            <input type="hidden" name="content" class="noticeContent">
            <div class="col-sm-8">
            <div class="summernote">
                <p>hAdmin是一个完全响应式，基于Bootstrap3.3.6最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术，她提供了诸多的强大的可以重新组合的UI组件，并集成了最新的jQuery版本(v2.1.1)，当然，也集成了很多功能强大，用途广泛的就jQuery插件，她可以用于所有的Web应用程序，如<b>网站管理后台</b>，<b>网站会员中心</b>，<b>CMS</b>，<b>CRM</b>，<b>OA</b>等等，当然，您也可以对她进行深度定制，以做出更强系统。</p>
            </div>
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
    $(document).ready(function () {
        $('.summernote').summernote({
            lang: 'zh-CN'
        });

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
        var str= $('.summernote').code();
        $(".noticeContent").val(str);
        console.log("++++"+str)
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

