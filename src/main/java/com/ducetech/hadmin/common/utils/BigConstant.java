package com.ducetech.hadmin.common.utils;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 项目常量
 *
 * @author lisx
 * @create 2017-08-03 14:17
 **/
public interface BigConstant {

    //0错误 1正常 data数据空为null
    int state_error=0;
    int state_success=1;

    String state_1="查询成功！";
    String state_2="暂无数据！";
    String state_3="缺少用户！";
    String user_state_2="密码错误！";
    String user_state_3="工号错误！";
    String user_state_4="工号不能为空！";
    String user_state_5="该用户已删除！";
    String user_state_6="修改密码成功！";




    String docx=".docx";
    String doc=".doc";
    String xlsx=".xlsx";
    String xls=".xls";
    String ppt=".ppt";
    String pptx=".pptx";
    String pdf=".pdf";
    String jpeg=".jepg";
    String jpg=".jpg";
    String png=".png";
    String office="office";
    String video="video";
    String image="image";
    public static ValueFilter filter = (obj, s, v) -> {
        if(v==null||v.equals(null)){
            return "";
        }
        return v;
    };

    String ADMINCODE="000";
    String ADMIN ="运三分公司";
    String User="人员信息";
    String Station="车站信息";
    String TRAIN="培训资料";
    String Question="考试练习";
    String Emergency="应急预案";
    String Rules="规章制度";
    String Fire="消防安全文件";
    String Running="运行图管理";
    String Notice="通知管理";
    String Roll="首页滚播图";
    String Edition="前端版本更新";
    String trainFolder1="题库";
    String trainFolder2="文档资料";
    String trainFolder3="信号平面图";
    String trainFolder4="案例库";
}
