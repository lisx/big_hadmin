<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<#include "/admin/common/webuploader.ftl">

<div class="ibox-content">
    <div class="page-container">
        <p>
            <p>注意：需要上传的文件需要按照以下规则命名。可以同时上传多个文件。</p>
            <p>1.该员工正装一寸照（295×413像素，jpg格式）的图片文件，请以“员工编号”（数字）命名；</p>
            <p> 2.该员工“服务信息卡”（jpg格式）的图片文件，请以“fw+员工编号”（fw+数字）的规则命名；</p>
            <p>3.该员工的“FAS证”（jpg格式）的图片文件，请以“FAS证编号”（数字）命名；</p>
            <p>4.该员工“综控员上岗证”（jpg格式）的图片文件，请以“综控员上岗证编号”（数字）命名.</p>
        </p>
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
<!-- Web Uploader -->
<script src="${ctx!}/hadmin/js/demo/webuploader-demo.js"></script>

