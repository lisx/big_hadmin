<!-- 全局js -->
<#include "/admin/common/js.ftl">
<#include "/admin/common/css.ftl">
<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-content">
                        <p>
                            <button class="btn btn-success uploadFile" type="button" onclick="exportLog();"><i class="fa fa-plus"></i>&nbsp;导出</button>
                        </p>
                        <div class="row row-lg">
                            <div class="col-sm-12">
                                <div class="row" style="font-size: medium;font-style: normal;">
                                    <lable class="col-sm-1">姓名：</lable>
                                    <span class="col-sm-2">${user.userName}</span>
                                    <lable class="col-sm-1">工号：</lable>
                                    <span class="col-sm-2">${user.userCode}</span>
                                    <lable class="col-sm-1">站区：</lable>
                                    <span class="col-sm-2">${user.stationArea}</span>
                                    <lable class="col-sm-1">站点：</lable>
                                    <span class="col-sm-2">${user.station}</span>
                                </div>
                            </div>
                        </div>
                        <div class="row row-lg">
		                    <div class="col-sm-12">
		                        <!-- Example Card View -->
		                        <div class="example-wrap">
		                            <div class="example">
		                            	<table id="table_list" class="table table-hover table-striped">
                                            <tr>
                                                <td>考试时间</td>
                                                <td>题库名称</td>
                                                <td>试卷类型</td>
                                                <td>用时</td>
                                                <td>分数</td>
                                            </tr>
                                            <#list logs as log>
                                                <tr>
                                                    <td>${log.createTime}</td>
                                                    <td>${log.bank.name}</td>
                                                    <td>${log.exam.examName}</td>
                                                    <td>${log.endTime}</td>
                                                    <td>${log.score}</td>
                                                </tr>
                                            </#list>
                                        </table>
		                            </div>
		                        </div>
		                        <!-- End Example Card View -->
		                    </div>
	                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script>
    function exportLog(){
        //下载文件
        var a = document.createElement('a');
        a.href = "${ctx!}/admin/examlog/exportLog/${user.id}";
        a.download = "${user.userName}的考试记录.xls";
        a.click();
    }
</script>
