<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#include "/admin/common/webuploader.ftl">
<script src="${ctx!}/hadmin/js/demo/uploader-demo.js"></script>
<div class="ibox-content">
    <div class="page-container">
        <p>练习考试批量导入附件文字：请将图片以创建题库时，下载的题型模板中“题干图片编号”一栏填入的字符，来命名将要上传的题干图片文件。</p>
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






</body>

</html>
