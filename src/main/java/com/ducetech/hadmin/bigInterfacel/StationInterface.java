package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.Station;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 站区站点线路接口
 *
 * @author lisx
 * @create 2017-08-08 11:14
 **/
@RestController
@RequestMapping("/interface/station")
public class StationInterface {
    private static Logger logger = LoggerFactory.getLogger(StationInterface.class);

    @Autowired
    IStationDao stationDao;

    @ApiOperation(value="获取线路全部数据",notes="获取线路全部数据")
    @RequestMapping(value="/findLineAll",method = RequestMethod.GET)
    public JSONObject findLineALl(){
        logger.info("获取线路全部数据");
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        List<Station> stations = stationDao.findByStationArea(6);
        if(null==stations){
            msg=BigConstant.state_2;
        }else{
            msg=BigConstant.state_1;
        }
        obj.put("data", stations);
        obj.put("msg",msg);
        obj.put("state",state);
        return obj;
    }

    @ApiOperation(value="根据线路获取站区全部数据",notes="根据线路获取站区全部数据")
    @RequestMapping(value="/findByLine",method = RequestMethod.GET)
    @ApiImplicitParam(name="line",value="线路",dataType="string", paramType = "query")
    public JSONObject findByLine(String line){
        logger.info("进入根据站区获取站点全部数据line:{}",line);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj;
        obj=new JSONObject();
        Station str=stationDao.findByNodeName(line);
        List<Station> stations=null;
        if(null!=str) {
            stations = stationDao.querySubNodesByCode(str.getNodeCode()+"___", 9);
        }
        if(null==stations){
            msg=BigConstant.state_2;
        }else{
            msg=BigConstant.state_1;
        }
        obj.put("data", stations);
        obj.put("msg",msg);
        obj.put("state",state);
        return obj;
    }
    @ApiOperation(value="根据站区获取站点全部数据",notes="根据站区获取站点全部数据")
    @RequestMapping(value="/findByArea",method = RequestMethod.GET)
    @ApiImplicitParam(name="area",value="站区",dataType="string", paramType = "query")
    public JSONObject findByArea(String area){
        logger.info("进入根据站区获取站点全部数据area:{}",area);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj;
        obj=new JSONObject();
        Station str=stationDao.findByNodeName(area);
        List<Station> stations =null;
        if(null!=str) {
            stations = stationDao.querySubNodesByCode(str.getNodeCode()+"___", 12);
        }
        if(null==stations){
            msg=BigConstant.state_2;
        }else{
            msg=BigConstant.state_1;
        }
        obj.put("data", stations);
        obj.put("msg",msg);
        obj.put("state",state);
        return obj;
    }

}
