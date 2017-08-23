<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
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
                                        <input  name="singleNum" class="form-control" style="margin-bottom: 10px;" value="${exam.singleNum}" placeholder="题数">
                                        <input  name="singleScore" placeholder="每题分数" class="form-control col-sm-3">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">多选题：</label>
                                    <div class="col-sm-3">
                                        <input  name="multipleNum" class="form-control" style="margin-bottom: 10px;"  placeholder="题数"> <input  name="multipleScore" placeholder="每题分数" class="form-control col-sm-3">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">判断题：</label>
                                    <div class="col-sm-8">
                                        <input  name="judgeNum" class="form-control" style="margin-bottom: 10px;"  placeholder="题数"> <input  name="judgeScore" placeholder="每题分数" class="form-control col-sm-3">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">排序题：</label>
                                    <div class="col-sm-8">
                                        <input  name="rankNum" class="form-control" style="margin-bottom: 10px;"  placeholder="题数"> <input  name="rankScore" placeholder="每题分数" class="form-control col-sm-3">
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
                    type: "post",
                    dataType: "json",
                    url: "${ctx!}/admin/exam/saveExam",
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