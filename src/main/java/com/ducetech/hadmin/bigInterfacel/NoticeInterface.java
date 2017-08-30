package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.dao.INoticeDao;
import com.ducetech.hadmin.dao.IRunningDao;
import com.ducetech.hadmin.entity.Notice;
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
    INoticeDao noticeDao;
    @ApiOperation(value="根据站点查询通知",notes="根据站点查询通知")
    @RequestMapping(value="/findByStation",method = RequestMethod.GET)
    @ApiImplicitParam(name="station",value="站点",dataType="string", paramType = "query")
    public JSONObject findByStation(String station){
        logger.info("根据站点查询通知");
        obj=new JSONObject();
        List<Notice> notices=noticeDao.findByStationNameIsLike("%"+station+"%");
        logger.info("|||||"+String.valueOf(notices.size()));
        obj.put("data", notices);
        obj.put("msg","查询成功");
        obj.put("state","1");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
