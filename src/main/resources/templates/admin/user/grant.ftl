    <!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>jQuery Validate 简介</h5>
                    </div>
                    <div class="ibox-content">
                        <p>为【${user.nickName}】分配角色</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>选择角色</h5>
                    </div>
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
                </div>
            </div>
        </div>

    </div>

    <script type="text/javascript">
    $(document).ready(function () {
	    $("#frm").validate({
    	    rules: {},
    	    messages: {},
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

</body>

