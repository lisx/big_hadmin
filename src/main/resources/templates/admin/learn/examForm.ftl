<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/select.ftl" as my />
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                        <div class="ibox-content">
                            <form id="frm" method="post" action="${ctx!}/admin/exam/save">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">选择题库：</label>
                                    <div class="col-sm-4">
                                        <input type="hidden" id="bankName" name="bankName" >
                                        <@my.select id="bankId"   class="form-control margin_bottom10" datas=banks key="id" text="name" value="${exam.bankId}" defaultValue="全部"/>
                                    </div>
                                    <label class="col-sm-2 control-label">试卷名称：</label>
                                    <div class="col-sm-4">
                                        <input type="hidden" id="id" name="id" value="${exam.id}">
                                        <input name="examName" class="form-control margin_bottom10" value="${exam.examName}"  >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">单选题：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="singleNum" class="form-control margin_bottom10"  value="${exam.singleNum}" placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="singleScore" placeholder="每题分数" class="form-control margin_bottom10" value="${exam.singleScore}"  >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">多选题：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="multipleNum" class="form-control margin_bottom10"  value="${exam.multipleNum}"  placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="multipleScore" placeholder="每题分数" class="form-control margin_bottom10" value="${exam.multipleScore}"  >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">判断题：</label>
                                    <div class="col-sm-4">
                                        <input type="number" min="0" name="judgeNum" class="form-control margin_bottom10"  value="${exam.judgeNum}" placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="judgeScore" placeholder="每题分数" class="form-control margin_bottom10" value="${exam.judgeScore}" >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">排序题：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="rankNum" class="form-control margin_bottom10"  value="${exam.rankNum}" placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="rankScore" placeholder="每题分数" class="form-control margin_bottom10" value="${exam.rankScore}" >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-12 col-sm-offset-3">
                                        <button class="btn btn-primary" type="submit">提交</button>
                                    </div>
                                </div>
                            </form>
                        </div>
            </div>
        </div>
    </div>
<script>

    $(document).ready(function () {
        console.log("|||")

        $("#bankId").change(function(){
            console.log("change");
            var checkText=$("#bankId").find("option:selected").text();
            var id=$("#bankId").find("option:selected").val();
            $("#bankName").val(checkText);
            $.ajax({
                type:"get",
                dataType:"json",
                url:"${ctx!}/admin/exam/getBank/"+id,
                success:function(data){
                    console.log(data);
                    $("input[name='judgeNum']").attr("max",data.judgeNum).attr("placeholder","有"+data.judgeNum+"道题");
                    $("input[name='multipleNum']").attr("max",data.multipleNum).attr("placeholder","有"+data.multipleNum+"道题");
                    $("input[name='rankNum']").attr("max",data.rankNum).attr("placeholder","有"+data.rankNum+"道题");
                    $("input[name='singleNum']").attr("max",data.singleNum).attr("placeholder","有"+data.singleNum+"道题");
                }
            })
        });
        $("#bankId").change();
        var icon = "<i class='fa fa-times-circle'></i> ";
        $("#frm").validate({
            rules: {
                examName: {
                    required: true,
                    maxlength: 15
                },
                bankId: {
                    required: true
                }
            },
            messages: {
                examName: icon + "请输入试卷名称",
                bankId: icon + "请选择题库"
            },
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