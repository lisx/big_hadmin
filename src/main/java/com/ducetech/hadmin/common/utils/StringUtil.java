package com.ducetech.hadmin.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
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

    /**
     * 数组格式化字符串
     * @param array
     * @param separator
     * @return
     */
    public static String join(Object[] array, String separator) {
        return array == null?null:join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if(array == null) {
            return null;
        } else {
            if(separator == null) {
                separator = "";
            }

            int noOfItems = endIndex - startIndex;
            if(noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for(int i = startIndex; i < endIndex; ++i) {
                    if(i > startIndex) {
                        buf.append(separator);
                    }

                    if(array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }
}
