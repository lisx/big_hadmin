<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#import "/admin/common/select.ftl" as my />

<div class="ibox-content">
    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/admin/notice/uploadFilePost" enctype="multipart/form-data">
        <div class="form-group">
            <label class="col-sm-3 control-label">通知标题：</label>
            <div class="col-sm-8">
                ${notice.title}
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">接收单位：</label>
            <div class="col-sm-8">
                ${notice.stationName}
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">内容：</label>
            <div class="col-sm-8">
                ${notice.content}
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">附件：</label>
            <div class="col-sm-8">
            <#list notice.bigFiles as file >
                ${file.fileName} <button class="btn btn-primary btn-xs down-btn" type="button" data-id="${file.id}" data-name="${file.fileName}"><i class="fa fa-download"></i>&nbsp;下载</button>
            </#list>
            </div>
        </div>
    </form>
</div>
<script>
    $(".down-btn").on("click",function(){
        var id=$(this).attr("data-id");
        var name=$(this).attr("data-name");
        console.log(id+"|||||"+name);
        var a = document.createElement('a');
        a.href = "${ctx!}/admin/download/"+id;
        a.download = name;
        a.click();
    })
</script>

