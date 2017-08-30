package com.ducetech.hadmin.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lisx
 * @create 2017-08-18 09:33
 **/
public class DateUtil {
    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    /**
     * 格式化日期到字符串
     * @param date Date
     * @param formatStr 格式化字符串
     * @return
     */
    public static String dateFormat(Date date,String formatStr){
        SimpleDateFormat formater=null;
        try{
            formater=new SimpleDateFormat(formatStr);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return formater.format(date);
    }
}
