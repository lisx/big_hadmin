package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IRunningDao;
import com.ducetech.hadmin.entity.BigFile;
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
 * 更新接口
 *
 * @author lisx
 * @create 2017-08-28 09:57
 **/
@RestController
@RequestMapping("/interface/update")
public class EditionInterface {
    private static Logger logger = LoggerFactory.getLogger(EditionInterface.class);
    int state=0;//1正常
    String msg;
    JSONObject obj;
    @Autowired
    IBigFileDao fileDao;

    @ApiOperation(value="获取更新文件",notes="获取更新文件")
    @RequestMapping(value="/getUpdateApk",method = RequestMethod.GET)
    public JSONObject getUpdateApk(){
        logger.info("获取更新文件");
        obj=new JSONObject();
        List<BigFile> files=fileDao.findByMenuType("前端版本更新");

        BigFile file=null;
        if(null!=files)
                file=files.get(files.size()-1);
        obj.put("data", file);
        obj.put("msg","查询成功");
        obj.put("state","1");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
