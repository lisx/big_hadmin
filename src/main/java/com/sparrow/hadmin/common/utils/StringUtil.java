package com.sparrow.hadmin.common.utils;

/**
 * 字符串工具
 *
 * @author lisx
 * @create 2017-07-31 11:14
 **/
public class StringUtil {
    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("");
    }

    public static String trim(String str){
        str = str ==null?"":str.trim();
        return str;
    }
    public static String trimToNull(Object obj) {
        String str = String.valueOf(obj);
        str = (str == null) ? null : str.trim();
        return str;
    }
}
