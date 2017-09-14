package com.ducetech.hadmin.common.utils;

/**
 * @author lisx
 * @create 2017-09-14 14:09
 **/
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;


public class Weather {
    String Ctiyid;
    URLConnection connectionData;
    StringBuilder sb;
    BufferedReader br;// 读取data数据流
    JSONObject jsonData;
    JSONObject info;

    //从天气网解析的参数
    String city ;// 城市
    String date_y;//日期
    String week ;// 星期
    String fchh ;// 发布时间

    String weather1 ;// 未来1到6天天气情况
    String weather2 ;
    String weather3 ;
    String weather4 ;
    String weather5 ;
    String weather6 ;

    String wind1;//未来1到6天风况
    String wind2;
    String wind3;
    String wind4;
    String wind5;
    String wind6;

    String fl1;//风的等级
    String fl2;
    String fl3;
    String fl4;
    String fl5;
    String fl6;


    String temp1 ;// 未来1到6天的气温
    String temp2 ;
    String temp3 ;
    String temp4 ;
    String temp5 ;
    String temp6 ;

    String index;// 今天的穿衣指数
    String index_uv ;// 紫外指数
    String index_tr ;// 旅游指数
    String index_co ;// 舒适指数
    String index_cl ;// 晨练指数
    String index_xc;//洗车指数
    String index_d;//天气详细穿衣指数



    public Weather(String Cityid) throws IOException ,NullPointerException{
        // 解析本机ip地址

        this.Ctiyid = Cityid;
        // 连接中央气象台的API
        URL url = new URL("http://m.weather.com.cn/data/" + Ctiyid + ".html");
        connectionData = url.openConnection();
        connectionData.setConnectTimeout(1000);
        try {
            br = new BufferedReader(new InputStreamReader(
                    connectionData.getInputStream(), "UTF-8"));
            sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                sb.append(line);
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时");
        } catch (FileNotFoundException e) {
            System.out.println("加载文件出错");
        }
        String datas = sb.toString();

        jsonData = JSONObject.parseObject(datas);
        //  System.out.println(jsonData.toString());
        info = jsonData.getJSONObject("weatherinfo");

        city = info.getString("city").toString();
        System.out.println(city);
        week =  info.getString("week").toString();
        date_y = info.getString("date_y").toString();
        fchh = info.getString("fchh").toString();
        //1到6天的天气
        weather1 =  info.getString("weather1").toString();
        System.out.println(weather1);
        weather2 =  info.getString("weather2").toString();
        weather3 =  info.getString("weather3").toString();
        weather4 =  info.getString("weather4").toString();
        weather5 =  info.getString("weather5").toString();
        weather6 =  info.getString("weather6").toString();
        //1到6天的气温
        temp1 = info.getString("temp1").toString();
        temp2 = info.getString("temp2").toString();
        temp3 = info.getString("temp3").toString();
        temp4 = info.getString("temp4").toString();
        temp5 = info.getString("temp5").toString();
        temp6 = info.getString("temp6").toString();
        //1到6天的风况
        wind1 = info.getString("wind1").toString();
        wind2 = info.getString("wind2").toString();
        wind3 = info.getString("wind3").toString();
        wind4 = info.getString("wind4").toString();
        wind5 = info.getString("wind5").toString();
        wind6 = info.getString("wind6").toString();
        //1到6天的风速
        fl1 = info.getString("fl1").toString();
        fl2 = info.getString("fl2").toString();
        fl3 = info.getString("fl3").toString();
        fl4 = info.getString("fl4").toString();
        fl5 = info.getString("fl5").toString();
        fl6 = info.getString("fl6").toString();
        //各种天气指数
        index = info.getString("index").toString();
        index_uv = info.getString("index_uv").toString();
        index_tr = info.getString("index_tr").toString();
        index_co= info.getString("index_co").toString();
        index_cl = info.getString("index_cl").toString();
        index_xc = info.getString("index_xc").toString();
        index_d =  info.getString("index_d").toString();

    }
    public static void main(String[] args) {
        try {
            new Weather("101010100"); // 101270803就是你的城市代码
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}