<!--
系统全局公共javascript-
作者：lisx
qq：799078779
-->

<!--jquery-->
<script src="${ctx!}/hadmin/js/jquery.min.js"></script>
<!--bootstrap-->
<script src="${ctx!}/hadmin/js/bootstrap.min.js"></script>
<script>
$(function(){
//菜单点击
$(".J_menuItem").on('click',function(){
var url = $(this).attr('href');
$("#J_iframe").attr('src',url);
return false;
});
});
</script>
<!-- Bootstrap table -->
<script src="${ctx!}/hadmin/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx!}/hadmin/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx!}/hadmin/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<!-- layer -->
<script src="${ctx!}/hadmin/js/plugins/layer/layer.min.js"></script>
<!-- 自定义js -->
<script src="${ctx!}/hadmin/js/content.js"></script>
<!--jquery validate-->
<script src="${ctx!}/hadmin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx!}/hadmin/js/plugins/validate/messages_zh.min.js"></script>
<!-- 全局js -->
<script src="${ctx!}/hadmin/js/plugins/metisMenu/jquery.metisMenu.js"></script>
