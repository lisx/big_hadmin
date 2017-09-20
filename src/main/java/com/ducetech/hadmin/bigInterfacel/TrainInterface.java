//package com.ducetech.hadmin.bigInterfacel;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ducetech.hadmin.common.utils.BigConstant;
//import com.ducetech.hadmin.dao.IBigFileDao;
//import com.ducetech.hadmin.dao.IStationDao;
//import com.ducetech.hadmin.dao.ITrainDao;
//import com.ducetech.hadmin.entity.BigFile;
//import com.ducetech.hadmin.entity.Station;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * 培训资料接口
// *
// * @author lisx
// * @create 2017-08-08 14:27
// **/
//@RestController
//@RequestMapping("/interface/train")
//public class TrainInterface {
//    private static Logger logger = LoggerFactory.getLogger(TrainInterface.class);
//
//    int state=0;
//    String msg;
//    JSONObject obj;
//    @Autowired
//    IStationDao stationDao;
//    @Autowired
//    ITrainDao trainDao;
//    @Autowired
//    IBigFileDao bigFileDao;
//    @ApiOperation(value="获取站点文件全部数据",notes="获取站点文件全部数据")
//    @RequestMapping(value="/findByStation",method = RequestMethod.GET)
//    @ApiImplicitParam(name="station",value="线路，站点，站区",dataType="string", paramType = "query")
//    public JSONObject findAll(String station) {
//        logger.info("获取站点文件全部数据");
//        obj = new JSONObject();
//        Station str = stationDao.findByNodeName(station);
//        List<BigFile> stations = bigFileDao.findByStation(str.getNodeCode());
//        if(null==stations){
//            msg="暂无数据";
//            state=0;
//        }else{
//            msg="查询成功";
//            state=1;
//        }
//        obj=new JSONObject();
//        obj.put("data", stations);
//        obj.put("msg",msg);
//        obj.put("state",state);
//        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
//    }
//    @ApiOperation(value="获取站点文件夹全部数据",notes="获取站点文件夹全部数据")
//    @RequestMapping(value="/findFolderByStation",method = RequestMethod.GET)
//    @ApiImplicitParam(name="station",value="线路，站点，站区",dataType="string", paramType = "query")
//    public JSONObject findFolderByStation(String station) {
//        logger.info("获取站点文件全部数据");
//        Station str = stationDao.findByNodeName(station);
//
//        List<BigFile> stations = bigFileDao.findByStation(str.getNodeCode());
//        if(null==stations){
//            msg="暂无数据";
//            state=0;
//        }else{
//            msg="查询成功";
//            state=1;
//        }
//        obj=new JSONObject();
//        obj.put("data", stations);
//        obj.put("msg",msg);
//        obj.put("state",state);
//        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
//    }
//
//}
