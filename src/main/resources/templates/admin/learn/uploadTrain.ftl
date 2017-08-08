<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title> - 百度Web Uploader</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctx!}/hadmin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/hadmin/css/plugins/webuploader/webuploader.css">
    <link rel="stylesheet" type="text/css" href="${ctx!}/hadmin/css/demo/webuploader-demo.css">
    <link href="${ctx!}/hadmin/css/style.css?v=4.1.0" rel="stylesheet">
<#--通过起别名的形式调用自定义的指令-->
<#import "/admin/common/select.ftl" as my/>
</head>

<div class="ibox-content">
    <div class="page-container">
        <p>您可以尝试文件拖拽，使用QQ截屏工具，然后激活窗口后粘贴，或者点击添加图片按钮，来体验此demo.</p>
        <div class="col-md-12">
            <div class="form-group">
                <label class="col-sm-3 control-label">文件夹：</label>
                <div class="col-sm-9">
                <@my.select id="folder" class="form-control" datas=folders key="name" text="name" defaultValue="请选择" />
                </div>
            </div>
        </div>

        <div id="uploader" class="wu-example">
            <div class="queueList">
                <div id="dndArea" class="placeholder">
                    <div id="filePicker"></div>
                    <p>或将照片拖到这里</p>
                </div>
            </div>
            <div class="statusBar" style="display:none;">
                <div class="progress">
                    <span class="text">0%</span>
                    <span class="percentage"></span>
                </div>
                <div class="info"></div>
                <div class="btns">
                    <div id="filePicker2"></div>
                    <div class="uploadBtn">开始上传</div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 全局js -->
<script src="${ctx!}/hadmin/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/hadmin/js/bootstrap.min.js?v=3.3.6"></script>



<!-- 自定义js -->
<script src="${ctx!}/hadmin/js/content.js?v=1.0.0"></script>


<!-- Web Uploader -->
<script src="${ctx!}/hadmin/js/plugins/webuploader/webuploader.min.js"></script>

<script src="${ctx!}/hadmin/js/demo/webuploader-learn-demo.js"></script>

<script type="text/javascript">
    $.get("${ctx!}/admin/folder/list",function(data){
        console.log("++++++")
        console.log(data);
    });
    // 添加全局站点信息
    var BASE_URL = '${ctx!}/hadmin/js/plugins/webuploader';
</script>
</body>

</html>
