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

import java.util.List;

/**
 * 通知管理接口
 *
 * @author lisx
 * @create 2017-08-28 11:04
 **/
@RestController
@RequestMapping("/interface/notice")
public class NoticeInterface {
    private static Logger logger = LoggerFactory.getLogger(NoticeInterface.class);
    int state=0;//1正常
    String msg;
    JSONObject obj;
    @Autowired
    IRunningDao runningDao;
    @ApiOperation(value="根据线路查询运行图",notes="根据线路查询运行图")
    @RequestMapping(value="/findByLine",method = RequestMethod.GET)
    @ApiImplicitParam(name="station",value="线路",dataType="string", paramType = "query")
    public JSONObject findLineALl(String line){
        logger.info("获取线路全部数据");
        obj=new JSONObject();
        List<Running> runnings=runningDao.findByLineName(line);
        obj.put("data", runnings);
        obj.put("msg","查询成功");
        obj.put("state","1");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
