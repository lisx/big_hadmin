
//自定义js

//公共配置


$(document).ready(function () {

    // MetsiMenu
    $('#side-menu').metisMenu();

    // 打开右侧边栏
    $('.right-sidebar-toggle').click(function () {
        console.log(1)
        $('#right-sidebar').toggleClass('sidebar-open');
    });

    //固定菜单栏
    $(function () {
        console.log(2)
        $('.sidebar-collapse').slimScroll({
            height: '100%',
            railOpacity: 0.9,
            alwaysVisible: false
        });
    });


    // 菜单切换
    $('.navbar-minimalize').click(function () {
        console.log(3)
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    });
    // $('#side-menu').delegate(".navbar-minimalize","click",function () {
    //     console.log(3)
    //     $("body").toggleClass("mini-navbar");
    //     SmoothlyMenu();
    // });

    // 侧边栏高度
    function fix_height() {
        console.log(4)
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        console.log("heightWithoutNavbar："+heightWithoutNavbar)
        $(".sidebard-panel").css("min-height", heightWithoutNavbar + "px");
    }
    fix_height();

    $(window).bind("load resize click scroll", function () {
        console.log(5)
        if (!$("body").hasClass('body-small')) {
            fix_height();
        }
    });

    //侧边栏滚动
    $(window).scroll(function () {
        console.log(6)
        if ($(window).scrollTop() > 0 && !$('body').hasClass('fixed-nav')) {
            $('#right-sidebar').addClass('sidebar-top');
        } else {
            $('#right-sidebar').removeClass('sidebar-top');
        }
    });

    $('.full-height-scroll').slimScroll({
        height: '100%'
    });

    $('#side-menu').delegate("li","click",function () {
        console.log(8)
        if ($('body').hasClass('mini-navbar')) {
            NavToggle();
        }
    });
    $('#side-menu').delegate("li li a","click",function () {
        console.log(9)
        if ($(window).width() < 769) {
            NavToggle();
        }
    });
    // $('#side-menu>li').click(function () {
    //     if ($('body').hasClass('mini-navbar')) {
    //         NavToggle();
    //     }
    // });
    // $('#side-menu>li li a').click(function () {
    //     if ($(window).width() < 769) {
    //         NavToggle();
    //     }
    // });

    $('.nav-close').click(NavToggle);

    //ios浏览器兼容性处理
    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
        $('#content-main').css('overflow-y', 'auto');
    }

});

$(window).bind("load resize", function () {
    console.log(10)
    if ($(this).width() < 769) {
        $('body').addClass('mini-navbar');
        $('.navbar-static-side').fadeIn();
    }
});

function NavToggle() {
    console.log(11)
    $('.navbar-minimalize').trigger('click');
}

function SmoothlyMenu() {
    console.log(12)
    if (!$('body').hasClass('mini-navbar')) {
        $('#side-menu').hide();
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 100);
    } else if ($('body').hasClass('fixed-sidebar')) {
        $('#side-menu').hide();
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 300);
    } else {
        $('#side-menu').removeAttr('style');
    }
}
