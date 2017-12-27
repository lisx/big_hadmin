package com.ducetech.hadmin.common.utils;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 项目常量
 *
 * @author lisx
 * @create 2017-08-03 14:17
 **/
public class BigConstant {
    
    public static final String docx=".docx";
    public static final String doc=".doc";
    public static final String xlsx=".xlsx";
    public static final String xls=".xls";
    public static final String ppt=".ppt";
    public static final String pptx=".pptx";
    public static final String pdf=".pdf";
    public static final String jpeg=".jepg";
    public static final String jpg=".jpg";
    public static final String png=".png";
    public static final String office="office";
    public static final String video="video";
    public static final String image="image";
    public static ValueFilter filter = (obj, s, v) -> {
        if(v==null||v.equals(null)){
            return "";
        }
        return v;
    };

    public static final String ADMINCODE="000";
    public static final String ADMIN ="运三分公司";
    public static final String User="人员信息";
    public static final String Station="车站信息";
    public static final String TRAIN="培训资料";
    public static final String Question="考试练习";
    public static final String Emergency="应急预案";
    public static final String Rules="规章制度";
    public static final String Fire="消防安全文件";
    public static final String Running="运行图管理";
    public static final String Notice="通知管理";
    public static final String Roll="首页滚播图";
    public static final String Edition="前端版本更新";
    public static final String trainFolder1="题库";
    public static final String trainFolder2="文档资料";
    public static final String trainFolder3="信号平面图";
    public static final String trainFolder4="案例库";
}
