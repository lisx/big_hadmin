<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#include "/admin/common/webuploader.ftl">

    <script src="${ctx!}/hadmin/js/demo/webuploader-station-demo.js"></script>

<div class="ibox-content">
    <div class="page-container">
        <input type="hidden" name="nodeCode" id="fileNodeCode" value="${nodeCode}" >
        <p>注意：请按照对应的站点上传车站信息文件。支持Word、Excel、PPT类型，单个文件不超过10M的文档；JEPG、PNG格式，单个文件不超过10M的图片；MP4，AVI，RMVB，MPG格式，单个视频大小不超过2GB的视频。</p>
        <div class="col-md-12">
            <div class="form-group">
                <input type="hidden" id="nodeCode" name="nodeCode" value="${nodeCode}">
            </div>
        </div>
        <div id="uploader" class="wu-example">
            <div class="queueList">
                <div id="dndArea" class="placeholder">
                    <div id="filePicker"></div>
                    <p>或将照片拖到这里</p>
                </div>
            </div>
            <div class="statusBar" style="display:none;">
                <div class="progress">
                    <span class="text">0%</span>
                    <span class="percentage"></span>
                </div>
                <div class="info"></div>
                <div class="btns">
                    <div id="filePicker2"></div>
                    <div class="uploadBtn">开始上传</div>
                </div>
            </div>
        </div>
    </div>
</div>
