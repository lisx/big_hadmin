<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
                <div class="ibox-content">
                    <form class="form-horizontal" id="frm" method="post" action="${ctx!}/admin/user/save">
                        <input type="hidden" id="id" name="id" value="${user.id}">
                        <div class="form-group">
                        <#list roles as role>
                            <div class="col-sm-12">
                                <div class="checkbox i-checks">
                                    <label>
                                        <#if roleIds?seq_contains(role.id)>
                                            <input type="checkbox" value="${role.id}" name="roleIds" checked="checked"> <i></i> ${role.name}
                                        <#else>
                                            <input type="checkbox" value="${role.id}" name="roleIds"> <i></i> ${role.name}
                                        </#if>
                                    </label>
                                </div>
                            </div>
                        </#list>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-8 col-sm-offset-3">
                                <button class="btn btn-primary" type="submit">提交</button>
                            </div>
                        </div>
                    </form>
                </div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#frm").validate({
            rules: {
                roleIds:{
                    required: true
                }
            },
            messages: {
                roleIds:"请选择权限"
            },
            errorPlacement: function (error, element) { //指定错误信息位置
                if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
                    var eid = element.attr('name'); //获取元素的name属性
                    error.appendTo(element.parent().parent().parent().parent()); //将错误信息添加当前元素的父结点后面
                } else {
                    error.insertAfter(element);
                }
            },
            submitHandler:function(form){
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "${ctx!}/admin/user/grant/" + ${user.id},
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
