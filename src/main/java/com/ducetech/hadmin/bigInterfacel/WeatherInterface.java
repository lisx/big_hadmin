package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.INoticeDao;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 天气预报
 *
 * @author lisx
 * @create 2017-09-14 14:01
 **/
@Controller
@RequestMapping("/interface")
public class WeatherInterface extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(NoticeInterface.class);
    int state=0;//1正常
    String msg;
    JSONObject obj;
    @ApiOperation(value="获取天气预报", notes="获取天气预报")
    @RequestMapping(value="/findWeather", method = RequestMethod.GET)
    public JSONObject findWeather(){
        logger.info("根据站点查询通知");
        obj=new JSONObject();
        obj.put("data", "");
        obj.put("msg",msg);
        obj.put("state",state);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
