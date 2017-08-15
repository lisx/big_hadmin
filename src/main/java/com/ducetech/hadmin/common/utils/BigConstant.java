package com.ducetech.hadmin.common.utils;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 项目常量
 *
 * @author lisx
 * @create 2017-08-03 14:17
 **/
public class BigConstant {
    public static final String IMAGE_PATH="src/main/resources/static/upload/images/";
    public static final String OFFICE_PATH="src/main/resources/static/upload/office/";
    public static final String TRAIN_PATH="src/main/resources/static/upload/train/";
    public static final String QUESTION_PATH="src/main/resources/static/upload/question/";
    public static final String STATION_PATH="src/main/resources/static/upload/station/";
    public static final String USER_PATH="src/main/resources/static/upload/users/";
    public static final String SERVICE_URL="http://192.168.4.30:8080/upload/";
    public static final String docx=".docx";
    public static final String doc=".doc";
    public static final String xlsx=".xlsx";
    public static final String xls=".xls";
    public static final String ppt=".ppt";
    public static final String pdf=".pdf";
    public static final String jpeg=".jepg";
    public static final String jpg=".jpg";
    public static final String png=".png";

    public static String getImageUrl(String name){
        return SERVICE_URL+"users"+name+jpg;
    }
    public static String getTrainVideoPathUrl(String name){
        return SERVICE_URL+"train/video/"+name;
    }
    public static String getTrainImagePathUrl(String name){
        return SERVICE_URL+"train/image/"+name;
    }
    public static String getTrainOfficePathUrl(String name){
        return SERVICE_URL+"trian/office/"+name;
    }
    public static final String TRAIN_VIDEO_PATH ="src/main/resources/static/upload/train/video/";
    public static final String TRAIN_IMAGE_PATH ="src/main/resources/static/upload/train/image/";
    public static final String TRAIN_OFFICE_PATH ="src/main/resources/static/upload/train/office/";
    public static ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if(v==null||v.equals(null)){
                return "";
            }
            return v;
        }
    };
}
