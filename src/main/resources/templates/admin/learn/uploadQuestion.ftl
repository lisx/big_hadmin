

    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctx!}/hadmin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/plugins/steps/jquery.steps.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/style.css?v=4.1.0" rel="stylesheet">


                    <form id="form" action="/admin/learn/question/uploadFile"  method="POST" enctype="multipart/form-data" class="wizard-big">
                        <h1>上传题库</h1>
                        <fieldset>
                            <div class="row">
                                <div class="col-sm-8">
                                    <div class="form-group">
                                        <label>题库名称 *</label>
                                        <input name="questionName" type="text" class="form-control required">
                                    </div>
                                    <div class="form-group">
                                        <label>题目类型 *</label>
                                        <input  name="questionType" type="text" class="form-control required">
                                    </div>
                                    <div class="form-group">
                                        <label>题库文件 *</label>
                                        <input name="file" type="file" class="form-control required">
                                    </div>
                                    <div class="form-group">
                                        1. 请下载模板并根据示例填写需要上传的题库内容 下载模板
                                        <br>
                                        2.填写题库信息选择已经填写完成的题库模板文件上传（仅支持.xls/.xlsx格式,且文件大小不能超过2M ）
                                        <input value="提交" type="submit" class="form-control required">
                                    </div>
                                </div>
                                <div class="col-sm-4">
                                    <div class="text-center">
                                        <div style="margin-top: 20px">
                                            <i class="fa fa-sign-in" style="font-size: 180px;color: #e5e5e5 "></i>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </fieldset>
                        <h1>执行导入</h1>
                        <fieldset>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label>姓名 *</label>
                                        <input id="name" name="name" type="text" class="form-control required">
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label>Email *</label>
                                        <input id="email" name="email" type="text" class="form-control required email">
                                    </div>
                                    <div class="form-group">
                                        <label>地址 *</label>
                                        <input id="address" name="address" type="text" class="form-control">
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                        <h1>完成</h1>
                        <fieldset>
                            <h2>条款</h2>
                            <input id="acceptTerms" name="acceptTerms" type="checkbox" class="required">
                            <label for="acceptTerms">我同意注册条款</label>
                        </fieldset>
                    </form>


<!-- 全局js -->
<script src="${ctx!}/hadmin/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/hadmin/js/bootstrap.min.js?v=3.3.6"></script>



<!-- 自定义js -->
<script src="${ctx!}/hadmin/js/content.js?v=1.0.0"></script>


<!-- Steps -->
<script src="${ctx!}/hadmin/js/plugins/staps/jquery.steps.min.js"></script>

<!-- Jquery Validate -->
<script src="${ctx!}/hadmin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx!}/hadmin/js/plugins/validate/messages_zh.min.js"></script>


<script>
    $(document).ready(function () {
        $("#wizard").steps();
        $("#form").steps({
            bodyTag: "fieldset",
            onStepChanging: function (event, currentIndex, newIndex) {

                var form = $(this);

                // Clean up if user went backward before
                if (currentIndex < newIndex) {
                    // To remove error styles
                    $(".body:eq(" + newIndex + ") label.error", form).remove();
                    $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
                }

                // Disable validation on fields that are disabled or hidden.
                form.validate().settings.ignore = ":disabled,:hidden";

                // Start validation; Prevent going forward if false
                return form.valid();
            },
            onStepChanged: function (event, currentIndex, priorIndex) {
                // Suppress (skip) "Warning" step if the user is old enough.
                if (currentIndex === 2 && Number($("#age").val()) >= 18) {
                    $(this).steps("next");
                }

                // Suppress (skip) "Warning" step if the user is old enough and wants to the previous step.
                if (currentIndex === 2 && priorIndex === 3) {
                    $(this).steps("previous");
                }
            },
            onFinishing: function (event, currentIndex) {
                var form = $(this);

                // Disable validation on fields that are disabled.
                // At this point it's recommended to do an overall check (mean ignoring only disabled fields)
                form.validate().settings.ignore = ":disabled";

                // Start validation; Prevent form submission if false
                return form.valid();
            },
            onFinished: function (event, currentIndex) {
                var form = $(this);

                // Submit form input
                form.submit();
            }
        }).validate({
            errorPlacement: function (error, element) {
                element.before(error);
            },
        });
    });
</script>
