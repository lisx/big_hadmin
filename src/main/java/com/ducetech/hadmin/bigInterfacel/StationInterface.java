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
    int state=0;
    String msg;
    JSONObject obj;
    @Autowired
    IStationDao stationDao;

    @ApiOperation(value="获取站区全部数据",notes="获取站区全部数据")
    @RequestMapping(value="/findAll",method = RequestMethod.GET)
    @ApiImplicitParam(name="station",value="线路，站点，站区",dataType="string", paramType = "query")
    public JSONObject findAll(){
        logger.debug("获取站区全部数据");
        obj=new JSONObject();
        List<Station> stations = stationDao.findByStationArea(3);
        obj.put("data", stations);
        obj.put("msg","查询成功");
        obj.put("state","1");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }

    @ApiOperation(value="根据站区获取站点全部数据",notes="根据站区获取站点全部数据")
    @RequestMapping(value="/findByStationArea",method = RequestMethod.GET)
    @ApiImplicitParam(name="station",value="线路，站点，站区",dataType="string", paramType = "query")
    public JSONObject findByStationArea(String station){
        logger.debug("进入根据站区获取站点全部数据{}",station);
        obj=new JSONObject();
        Station str=stationDao.findByNodeName(station);
        if(null!=str) {
            List<Station> stations = stationDao.querySubNodesByCode(str.getNodeCode()+"%", 6);
            obj.put("data", stations);
        }else{
            obj.put("data","");
        }
        obj.put("msg","查询成功");
        obj.put("state","1");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }

}
