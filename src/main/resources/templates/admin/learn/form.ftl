<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
                    <div class="ibox-content">
                        <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/resource/edit">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">文件夹名：</label>
                                <div class="col-sm-8">
                                    <input id="name" name="fileName" class="form-control">
                                </div>
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
    	      	fileName: {
    	        required: true,
    	        maxlength: 40
    	      }
    	    },
    	    messages: {},
    	    submitHandler:function(form){
    	        var url="${ctx!}/admin/train/saveFolder?nodeCode=${nodeCode}&menuType=${menuType}";
    	        console.log(url)
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: url,
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