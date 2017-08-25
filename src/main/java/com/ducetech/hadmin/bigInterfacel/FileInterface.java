package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.dao.ITrainDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Station;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 培训资料接口
 *
 * @author lisx
 * @create 2017-08-08 14:27
 **/
@RestController
@RequestMapping("/interface/file")
public class FileInterface {
    private static Logger logger = LoggerFactory.getLogger(FileInterface.class);

    int state=0;
    String msg;
    JSONObject obj;
    @Autowired
    IStationDao stationDao;
    @Autowired
    IBigFileDao bigFileDao;
    @ApiOperation(value="获取站点文件全部数据",notes="获取站点文件全部数据")
    @RequestMapping(value="/findByStation",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="station",value="线路，站点，站区",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="menuType",value="功能类型",dataType="String", paramType = "query")
    })
    public JSONObject findAll(String station,String menuType) {
        logger.info("获取站点文件全部数据");
        obj = new JSONObject();
        Station str = stationDao.findByNodeName(station);
        String nodeCode=str.getNodeCode();
        logger.debug("||||||"+nodeCode);
        String area="";
        if(nodeCode.length()==12)
        area=nodeCode.substring(0,nodeCode.length()-3);
        List<BigFile> stations = bigFileDao.findByStationFileOrStationFileAndMenuType(str.getNodeCode()+"%","000",area,menuType);
        obj.put("data", stations);
        obj.put("msg","查询成功");
        obj.put("state","1");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
    @ApiOperation(value="获取文件夹内全部数据",notes="获取文件夹内全部数据")
    @RequestMapping(value="/findByFolder",method = RequestMethod.GET)
    @ApiImplicitParam(name="folderId",value="文件夹名字",dataType="Integer", paramType = "query")
    public JSONObject findFolder(Integer folderId) {
        logger.info("获取文件夹内全部数据");
        BigFile fold=bigFileDao.findOne(folderId);
        List<BigFile> files=bigFileDao.findByFolderFile(fold);
        obj = new JSONObject();
        obj.put("data", files);
        obj.put("msg","查询成功");
        obj.put("state","1");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
