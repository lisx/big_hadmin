package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.dao.IRunningDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.Running;
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
 * 运行图接口
 *
 * @author lisx
 * @create 2017-08-28 09:23
 **/
@RestController
@RequestMapping("/interface/running")
public class RunningInterface {
    private static Logger logger = LoggerFactory.getLogger(StationInterface.class);
    int state=0;//1正常
    String msg;
    JSONObject obj;
    @Autowired
    IRunningDao runningDao;

    @ApiOperation(value="根据线路查询运行图",notes="根据线路查询运行图")
    @RequestMapping(value="/findByLine",method = RequestMethod.GET)
    @ApiImplicitParam(name="line",value="线路",dataType="string", paramType = "query")
    public JSONObject findLineALl(String line){
        logger.info("获取线路全部数据");
        List<Running> runnings=runningDao.findByLineName(line);
        if(null==runnings){
            msg="暂无数据";
            state=0;
        }else{
            msg="查询成功";
            state=1;
        }
        obj=new JSONObject();
        obj.put("msg",msg);
        obj.put("state",state);
        obj.put("data", runnings);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
