package com.ducetech.hadmin.common.utils;

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
    /**
     * 将字符串的左侧填充某个值
     * @param scale
     * @return
     */
    public static String leftJoin(Object obj,int scale,String val){
        String str = String.valueOf(obj);
        int len = str.length();
        if(len<scale){
            int diff = scale-len;
            String prefix="";
            for(int i=0;i<diff;i++){
                prefix+=val;
            }
            str = prefix+str;
        }
        return str;
    }
    public static String suffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }
}
