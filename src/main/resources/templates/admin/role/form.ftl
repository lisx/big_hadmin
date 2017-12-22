<#include "/admin/common/css.ftl">
<#include "/admin/common/js.ftl">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                    </div>
                    <div class="ibox-content">
                        <form autocomplete="off"  class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/role/edit">
                        	<input type="hidden" id="id" name="id" value="${role.id}">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">权限key：</label>
                                <div class="col-sm-8">
                                    <input id="roleKey" name="roleKey" class="form-control" type="text" value="${role.roleKey}" <#if role?exists> readonly="readonly"</#if> >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">权限名称：</label>
                                <div class="col-sm-8">
                                    <input id="name" name="name" class="form-control" type="text" value="${role.name}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">描述：</label>
                                <div class="col-sm-8">
                                    <input id="description" name="description" class="form-control" value="${role.description}">
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
    <script type="text/javascript">
    $(document).ready(function () {
	    $("#frm").validate({
    	    rules: {
    	    	roleKey: {
    	        required: true,
    	    	maxlength: 30
    	      },
    	        name: {
    	        required: true,
    	    	maxlength: 30
    	      },
    	        status: {
    	        required: true
    	      },
    	      	description: {
    	        required: true,
    	        maxlength: 40
    	      }
    	    },
    	    messages: {},
    	    submitHandler:function(form){
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: "${ctx!}/admin/role/edit",
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
