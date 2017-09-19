package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.INoticeDao;
import com.ducetech.hadmin.dao.IRunningDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Notice;
import com.ducetech.hadmin.entity.Running;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Autowired
    IBigFileDao fileDao;
    @ApiOperation(value="根据站点查询通知",notes="根据站点查询通知")
    @RequestMapping(value="/findByStation",method = RequestMethod.GET)

    @ApiImplicitParams({
            @ApiImplicitParam(name="station",value="站点",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="date",value="时间",dataType="string", paramType = "query")
    })
    public JSONObject findByStation(String station,String date){
        logger.info("根据站点查询通知{}||{}",station,date);
        obj=new JSONObject();
        List<Notice> notices=noticeDao.findByStationNameIsLikeAndCreateTimeLikeAndIfUse("%"+station+"%","%"+date+"%",0);
        for(int i=0;i<notices.size();i++){
            List<BigFile> files=notices.get(i).getBigFiles();
            logger.info(files.size()+"||");
            if(null==files){
                files=fileDao.findByNotice(notices.get(i));
                notices.get(i).setBigFiles(files);
            }
        }
        if(null!=notices&&notices.size()>0){
            state=1;
            msg="查询成功";
        }else{
            notices=new ArrayList<>();
            state=1;
            msg="暂无数据";
        }
        logger.info("|||||"+String.valueOf(notices.size()));
        obj.put("data", notices);
        obj.put("msg",msg);
        obj.put("state",state);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
