<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#include "/admin/common/summernote.ftl">
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
            ${notice.files}
            </div>
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {
        var text='${notice.content}';
        $('.noticeContent').html(text);
    });

</script>

