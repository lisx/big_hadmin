package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.dao.IRunningDao;
import com.ducetech.hadmin.entity.Running;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
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

    @Autowired
    IRunningDao runningDao;

    @ApiOperation(value="根据线路查询运行图",notes="根据线路查询运行图")
    @RequestMapping(value="/findByLine",method = RequestMethod.GET)
    @ApiImplicitParam(name="line",value="线路",dataType="string", paramType = "query")
    public JSONObject findLineALl(String line){
        logger.info("根据线路查询运行图line:{}",line);
        int state=BigConstant.state_success;//1正常
        String msg;
        JSONObject obj=new JSONObject();;
        List<Running> runnings=runningDao.findByLineNameAndIfUse(line,0);
        if(null==runnings){
            msg=BigConstant.state_2;
        }else{
            msg=BigConstant.state_1;
        }
        obj.put("msg",msg);
        obj.put("state",state);
        obj.put("data", runnings);
        return obj;
    }
}
