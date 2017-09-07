<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<style>
    img{
        cursor: pointer;
        transition: all 0.6s;
    }
    img:hover{
        z-index: 999;
        text-align: center;
        transform: scale(2.5);
    }
</style>
<div class="row">
    <div class="col-sm-9">
                <div class="ibox-content">
                    <div class="row">
                                <lable class="col-sm-2">姓名：</lable>
                                <span class="col-sm-2">${user.userName}</span>
                                <lable class="col-sm-2">工号：</lable>
                                <span class="col-sm-2">${user.userCode}</span>
                                <lable class="col-sm-2">权限：</lable>
                                <span class="col-sm-2">
                                <#list user.roles as role>
                                    ${role.name}
                                </#list>
                                </span>
                    </div>
                    <div class="row">
                                <lable class="col-sm-2">线路：</lable>
                                <span class="col-sm-2">${user.line}</span>
                                <lable class="col-sm-2">站区：</lable>
                                <span class="col-sm-2">${user.stationArea}</span>
                                <lable class="col-sm-2">站区：</lable>
                                <span class="col-sm-2">${user.station}</span>
                    </div>
                    <div class="row">
                        <lable class="col-sm-1">照片：</lable>
                        <div class="image">
                            <img alt="image" class="img-responsive col-sm-3" src="${user.photoUrl}">
                        </div>
                    </div>
                    <div class="row col-sm-14">
                        <lable class="col-sm-1">证书：</lable>
                        <div class="image">
                            <img alt="image" class="img-responsive col-sm-3" src="${user.fwxxkUrl}">
                        </div>
                        <div class="image">
                            <img alt="image" class="img-responsive col-sm-4" src="${user.faszUrl}">
                        </div>
                        <div class="image">
                            <img alt="image" class="img-responsive col-sm-4" src="${user.zkysgzUrl}">
                        </div>
                    </div>
                </div>
    </div>
</div>

