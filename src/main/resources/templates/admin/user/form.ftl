<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/select.ftl" as my />
<div class="ibox-content">
    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/user/edit">
        <input type="hidden" id="id" name="id" value="${user.id}">

        <div class="form-group">
            <label class="col-sm-3 control-label">工号：</label>
            <div class="col-sm-8">
                <input id="userCode" name="userCode" class="form-control" type="text" value="${user.userCode}" <#if user?exists> readonly="readonly"</#if> >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">登录密码：</label>
            <div class="col-sm-8">
                <input id="password" name="password" class="form-control" type="text" value="123456" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">姓名：</label>
            <div class="col-sm-8">
                <input id="userName" name="userName" class="form-control" type="text" value="${user.userName}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">职位：</label>
            <div class="col-sm-8">
                <select name="position" id="position" value="${user.position}" class="form-control">
                    <option value="站务员">站务员</option>
                    <option value="综控员">综控员</option>
                    <option value="值班站长">值班站长</option>
                    <option value="副站长">副站长</option>
                    <option value="站长">站长</option>
                    <option value="副站区长">副站区长</option>
                    <option value="站区长">站区长</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">线路：</label>
            <div class="col-sm-8">
            <@my.select id="line" class="form-control" value=user.line datas=lines defaultValue="请选择"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">站区：</label>
            <div class="col-sm-8">
        <#--<@my.select id="stationArea" class="form-control" value=user.stationArea datas=areas defaultValue="请选择"/>-->
            <select id="stationArea" name="stationArea" value="${user.stationArea}" class="form-control" >
                <option value="请选择">请选择</option>
            </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">站点：</label>
            <div class="col-sm-8">
            <select id="station" name="station" value="${user.station}" class="form-control" >
                <option value="请选择">请选择</option>
            </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">服务信息卡编号：</label>
            <div class="col-sm-8">
                <input id="fwxxkUrl" name="fwxxkUrl" class="form-control" value="${fwxxkUrl}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">综控员上岗证编号：</label>
            <div class="col-sm-8">
                <input id="zkysgzUrl" name="zkysgzUrl" class="form-control" value="${zkysgzUrl}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">FAS证编号：</label>
            <div class="col-sm-8">
                <input id="faszUrl" name="faszUrl" class="form-control" value="${faszUrl}">
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
        $("#line").change(function(){
            var line=$("#line").val();
            console.log("||||"+line);
            $.ajax({
                url: '/admin/station/getStation?area='+line,
                type: 'GET',
                dataType: 'json',
                cache: false,
                processData: false,
                contentType: false
            }).done(function(areas) {
                console.log(areas);
                for(var i in areas){
                    $("#stationArea").append("<option>"+areas[i]+"</option>");
                };
                $("#stationArea").val("${user.stationArea}");
                $("#stationArea").change(function(){
                    var area=$("#stationArea").val();
                    console.log("||||"+area);
                    $.ajax({
                        url: '/admin/station/getStation?area='+area,
                        type: 'GET',
                        dataType: 'json',
                        cache: false,
                        processData: false,
                        contentType: false
                    }).done(function(stations) {
                        console.log(stations);
                        for(var i in stations){
                            $("#station").append("<option>"+stations[i]+"</option>");
                        };
                        $("#station").val("${user.station}");
                    });
                }).change();
            });
        }).change();

    $(document).ready(function () {
        $("#position").val("${user.position}");
	    $("#frm").validate({
    	    rules: {
    	    	userName: {
    	        required: true,
    	    	maxlength: 10
    	      },
    	      	userCode: {
    	        required: true,
    	    	maxlength: 10
    	      }
    	    },
    	    messages: {
    	        userName:"姓名必填",
                userCode:"工号必填"
            },
    	    submitHandler:function(form){
                $(".btn-primary").attr("disabled","disabled");
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

