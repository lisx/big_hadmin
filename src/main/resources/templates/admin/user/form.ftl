<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<div class="ibox-content">
    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/user/edit">
        <input type="hidden" id="id" name="id" value="${user.id}">

        <div class="form-group">
            <label class="col-sm-3 control-label">用户名：</label>
            <div class="col-sm-8">
                <input id="userName" name="userName" class="form-control" type="text" value="${user.userName}" <#if user?exists> readonly="readonly"</#if> >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">密码：</label>
            <div class="col-sm-8">
                <input id="password" name="password" class="form-control" type="text" value="${user.password}" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">员工编号：</label>
            <div class="col-sm-8">
                <input id="userCode" name="userCode" class="form-control" type="text" value="${user.userName}" <#if user?exists> readonly="readonly"</#if> >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">员工姓名：</label>
            <div class="col-sm-8">
                <input id="nickName" name="nickName" class="form-control" type="text" value="${user.nickName}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">单位：</label>
            <div class="col-sm-8">
                <select name="unitId" class="form-control">
                    <option value="0" <#if user.sex == 0>selected="selected"</#if>>女</option>
                    <option value="1" <#if user.sex == 1>selected="selected"</#if>>男</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">电话：</label>
            <div class="col-sm-8">
                <input id="telephone" name="telephone" class="form-control" value="${user.telephone}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">服务信息卡：</label>
            <div class="col-sm-8">
                <input id="fwxxkUrl" name="fwxxkUrl" class="form-control" value="${user.email}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">综控员上岗证：</label>
            <div class="col-sm-8">
                <input id="zkysgzUrl" name="zkysgzUrl" class="form-control" value="${user.address}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">FAS证：</label>
            <div class="col-sm-8">
                <input id="faszUrl" name="faszUrl" class="form-control" value="${user.address}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">头像：</label>
            <div class="col-sm-8">
                <input id="photoUrl" name="photoUrl" class="form-control" value="${user.address}">
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
	  	//外部js调用
//	    laydate({
//	        elem: '#birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
//	        event: 'focus' //响应事件。如果没有传入event，则按照默认的click
//	    });

	    $("#frm").validate({
    	    rules: {
    	    	userName: {
    	        required: true,
    	    	maxlength: 10
    	      },
    	      	nickName: {
    	        required: true,
    	    	maxlength: 10
    	      }
    	    },
    	    messages: {},
    	    submitHandler:function(form){
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: "${ctx!}/admin/user/edit",
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

