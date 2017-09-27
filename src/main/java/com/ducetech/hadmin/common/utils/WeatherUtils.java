package com.ducetech.hadmin.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.weather.City;
import com.ducetech.hadmin.common.utils.weather.Root;
import com.ducetech.hadmin.dao.IWeatherDao;
import com.ducetech.hadmin.entity.WeatherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


/**
 * java调用中央气象局天气预报接口
 *
 * @author Administrator
 *
 */
@Configuration
@EnableScheduling // 启用定时任务
public class WeatherUtils {
    @Autowired
    IWeatherDao weatherDao;
    @Scheduled(cron = "0 */30 * * * ?") // 每20秒执行一次
    public void setWeather()  throws IOException{
        try {
            //测试获取实时天气1(包含风况，湿度)
            Root root = getTodayWeather1();

            //测试获取实时天气2(包含天气，温度范围)
            City city = getTodayWeather2();
            WeatherInfo info=new WeatherInfo(root.cond.code, root.cond.txt, root.wind.deg, root.wind.dir, root.wind.sc, root.wind.spd, root.fl, root.hum, root.pcpn, root.pres, root.tmp, root.vis, city.aqi, city.co, city.no2, city.o3, city.pm10, city.pm25, city.qlty, city.so2);
            info.setCreateTime(DateUtil.dateFormat(new Date(),"yyyy-MM-dd"));
            weatherDao.saveAndFlush(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static Root getTodayWeather1()
            throws IOException, NullPointerException {
        // 连接中央气象台的API
        URL url = new URL("https://free-api.heweather.com/v5/now?city=北京&key=56caa113a4084e4d89c39e94582b6f86");
        URLConnection connectionData = url.openConnection();
        connectionData.setConnectTimeout(1000);
        Root root=null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connectionData.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                sb.append(line);
            String datas = sb.toString();
            System.out.println(datas);
            JSONObject jsonData = JSONObject.parseObject(datas);
            JSONArray array = jsonData.getJSONArray("HeWeather5");
            JSONObject object=JSONObject.parseObject(String.valueOf(array.get(0)));
            root=JSONObject.parseObject(object.get("now").toString(),Root.class);
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时");
        } catch (FileNotFoundException e) {
            System.out.println("加载文件出错");
        }

        return root;

    }


    /**
     *
     * 获取空气质量<br>
     */
    public static City getTodayWeather2()
            throws IOException, NullPointerException {
        // 连接中央气象台的API
        URL url = new URL("https://free-api.heweather.com/v5/aqi?city=北京&key=56caa113a4084e4d89c39e94582b6f86");
        URLConnection connectionData = url.openConnection();
        connectionData.setConnectTimeout(1000);
        City city=null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connectionData.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                sb.append(line);
            String datas = sb.toString();
            System.out.println(datas);
            JSONObject jsonData = JSONObject.parseObject(datas);
            JSONArray array = jsonData.getJSONArray("HeWeather5");
            JSONObject object=JSONObject.parseObject(String.valueOf(array.get(0)));
            JSONObject aqi= (JSONObject) object.get("aqi");
            city=JSONObject.parseObject(aqi.get("city").toString(),City.class);
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时");
        } catch (FileNotFoundException e) {
            System.out.println("加载文件出错");
        }

        return city;

    }

    public static void main(String[] args) {

        try {
            //测试获取实时天气1(包含风况，湿度)
            //Root root = getTodayWeather1();
            //测试获取实时天气2(包含天气，温度范围)
            City city=getTodayWeather2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}