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
    $.ajax({
    type: "GET",
    dataType: "json",
    url: "/admin/resource/menuTree",
    success: function(data){
    var $li,$menu_f_ul;
    $.each(data,function(index,item){
    if(item.type==0){
    $li=$('<li ></li>');
    var $menu_f=$('<a href="#">\n'+
    '<i class="fa '+item.icon+'"></i>\n'+
    '<span  class="nav-label">'+item.name+'</span>\n'+
    '</a>');
    $li.append($menu_f);
    $menu_f_ul=$('<ul class="nav nav-second-level collapse"></ul>');
    $li.append($menu_f_ul);
    $("ul#side-menu").append($li);
    }else if(item.type==1){
    var $menu_s=$(
    '<li>\n'+
    '<a  class="J_menuItem fa '+item.icon+'" href="${ctx!}'+item.sourceUrl+'">'+item.name+'</a>\n'+
    '</li>'
    );
    $menu_f_ul.append($menu_s);
    }

    });
    }
    });
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
<!-- Peity -->
<script src="${ctx!}/hadmin/js/plugins/peity/jquery.peity.min.js"></script>
<!-- layer -->
<script src="${ctx!}/hadmin/js/plugins/layer/layer.min.js"></script>
<!-- 自定义js -->
<script src="${ctx!}/hadmin/js/content.js"></script>
<!--jquery validate-->
<script src="${ctx!}/hadmin/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx!}/hadmin/js/plugins/validate/messages_zh.min.js"></script>
<!-- 全局js -->
<script src="${ctx!}/hadmin/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<#--<script src="${ctx!}/hadmin/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>-->
