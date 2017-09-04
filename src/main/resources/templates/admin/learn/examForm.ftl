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
                                        <@my.select id="bankId" class="form-control" datas=banks key="id" text="name" defaultValue="全部"/>
                                    </div>
                                    <label class="col-sm-2 control-label">试卷名称：</label>
                                    <div class="col-sm-4">
                                        <input type="hidden" id="id" name="id" value="${exam.id}">
                                        <input name="examName" class="form-control" value="${exam.examName}"  style="margin-bottom: 10px;">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">单选题：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="singleNum" class="form-control" style="margin-bottom: 10px;" value="${exam.singleNum}" placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="singleScore" placeholder="每题分数" class="form-control"  style="margin-bottom: 10px;">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">多选题：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="multipleNum" class="form-control" style="margin-bottom: 10px;"  placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="multipleScore" placeholder="每题分数" class="form-control "  style="margin-bottom: 10px;">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">判断题：</label>
                                    <div class="col-sm-4">
                                        <input type="number" min="0" name="judgeNum" class="form-control" style="margin-bottom: 10px;"  placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="judgeScore" placeholder="每题分数" class="form-control " style="margin-bottom: 10px;">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">排序题：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="rankNum" class="form-control" style="margin-bottom: 10px;"  placeholder="题数">
                                    </div>
                                    <label class="col-sm-2 control-label"> 每道题分数：</label>
                                    <div class="col-sm-4">
                                        <input  type="number" min="0" name="rankScore" placeholder="每题分数" class="form-control "  style="margin-bottom: 10px;">
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
        $("#bankId").change(function(){
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
        })
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