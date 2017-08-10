<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>配置考试类型</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/hadmin/css/bootstrap.min.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/plugins/bootstrap-table/bootstrap-table.min.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css?v=${version!}" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/style.css?v=${version!}" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>配置考试类型</h5>
                    </div>
                    <div class="col-sm-13">
                        <div class="ibox-content">
                            <form class="form-inline m-t" id="frm" method="post" action="${ctx!}/admin/exam/save">
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">试卷名称：</label>
                                    <div class="col-sm-8">
                                        <input type="hidden" id="id" name="id" value="${exam.id}">
                                        <input  name="examName" class="form-control" value="${exam.examName}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">单选题：</label>
                                    <div class="col-sm-8">
                                        <input  name="singleNum" class="form-control" value="${exam.singleNum}">道 每道题<input  name="singleScore" class="form-control col-sm-3">分
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">多选题：</label>
                                    <div class="col-sm-8">
                                        <input  name="multipleNum" class="form-control">道 每道题<input  name="multipleScore" class="form-control col-sm-3">分
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">判断题：</label>
                                    <div class="col-sm-8">
                                        <input  name="judgeNum" class="form-control">道 每道题<input  name="judgeScore" class="form-control col-sm-3">分
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">排序题：</label>
                                    <div class="col-sm-8">
                                        <input  name="rankNum" class="form-control">道 每道题<input  name="rankScore" class="form-control col-sm-3">分
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-8 col-sm-offset-3">
                                        <button class="btn btn-primary" type="submit">提交</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!-- 全局js -->
	<#include "/admin/common/common.ftl">
<script>
    $(document).ready(function () {
        $("#frm").validate({
            rules: {
                name: {
                    required: true,
                    maxlength: 40
                }
            },
            messages: {},
            submitHandler:function(form){
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "${ctx!}/admin/exam/save",
                    data: $(form).serialize(),
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



</body>

</html>
