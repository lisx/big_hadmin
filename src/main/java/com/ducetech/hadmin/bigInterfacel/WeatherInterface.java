package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.DucetechProperties;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IWeatherDao;
import com.ducetech.hadmin.entity.WeatherInfo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 天气预报
 *
 * @author lisx
 * @create 2017-09-14 14:01
 **/
@RestController
@RequestMapping("/interface/weather")
public class WeatherInterface extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(WeatherInterface.class);
    int state=0;//1正常
    String msg;
    JSONObject obj;
    @Autowired
    IWeatherDao weatherDao;
    @Autowired
    DucetechProperties properties;
    @ApiOperation(value="获取天气预报", notes="获取天气预报")
    @RequestMapping(value="/detail", method = RequestMethod.GET)
    public JSONObject getWeather(){
        logger.info("获取天气预报");
        WeatherInfo info=null;
        try {
            info=weatherDao.findWeatherInfo();
            info.setUrl(properties.getIcon()+info.getCode()+BigConstant.png);
            state=1;
            msg="查询正常";
            if(null==info){
                state=0;
                msg="暂无数据";
            }
        } catch (Exception e) {
            state=-1;
            msg="数据异常";
            e.printStackTrace();
        }
        obj=new JSONObject();
        obj.put("data", info);
        obj.put("msg",msg);
        obj.put("state",state);
        JSONObject o=JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
        return o;
    }
}
