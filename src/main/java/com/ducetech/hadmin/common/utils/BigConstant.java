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
    public static final String pdf=".pdf";
    public static final String jpeg=".jepg";
    public static final String jpg=".jpg";
    public static final String png=".png";
    public static final String upload="/Users/lisx/upload/";

    public static ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if(v==null||v.equals(null)){
                return "";
            }
            return v;
        }
    };

    public static final String ADMIN ="运三分公司";
    public static final String office="office";
    public static final String video="video";
    public static final String image="image";
    public static final String TRAIN="培训资料";
    public static final String Fire="消防安全";
    public static final String ADMINCODE="000";
    public static final String Roll="首页滚播图";
    public static final String Rules="规章制度";
    public static final String Emergency="应急预案";

    public static String getImageUrl(String file) {
        return  upload+file+".jpg";
    }

}
