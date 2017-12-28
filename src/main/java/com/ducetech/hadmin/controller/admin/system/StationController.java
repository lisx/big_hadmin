package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.*;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IBigFileService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.util.*;

/**
 * 车站信息
 */
@Controller
@RequestMapping("/admin/station")
public class StationController extends BaseController {
//    private static Logger logger = LoggerFactory.getLogger(StationController.class);
	@Autowired
	private IStationDao stationDao;
    @Autowired
    IBigFileDao fileDao;

    /**
     * 树形菜单
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public JSONArray tree(){
//        logger.info("获取tree数据");
        User user=getUser();
        return Station.getZtrees(user,stationDao);
    }

    /**
     * 根据站区获取站点
     * @param area
     * @return
     */
    @RequestMapping("/getStation")
    @ResponseBody
    public List<String> getStation(String area){
//        logger.info("根据站区名获取站点数据{}",area);
        Station station=stationDao.findByNodeName(area);
        List<String> list=null;
        if(null!=station)
                list=stationDao.findStations(station.getNodeCode().length()+3,station.getNodeCode()+"___");
        return list;

    }
    /**
     * 删除
     * @param nodeId
     * @return
     */
    @RequestMapping(value = "/del/{nodeId}",method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject del(@PathVariable String nodeId){
//        logger.info("进入删除节点nodeId{}",nodeId);
        List<Station> stations=stationDao.findByNodeCodeStartingWith(nodeId);
        stationDao.delete(stations);
        JSONObject obj=new JSONObject();
        obj.put("node",nodeId);
        return obj;
    }

    /**
     * 增加节点
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Station save(String name, String pId){
//        logger.info("进入新增节点name{}||pId{}",name,pId);
        String pcode =StringUtil.trim(pId);
        List<Station> stations = stationDao.querySubNodesByCode(pcode+"___",pcode.length()+3);
        String nodeCode = Station.getNodeCode(stations,pcode);
        Station node = new Station();
        node.setNodeCode(nodeCode);
        node.setNodeName(name);
        stationDao.save(node);
        return (node);
    }
    /**
     * 编辑节点
     */
    @RequestMapping(value = "/update/{nodeCode}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject update(@PathVariable String nodeCode, String name){
//        logger.info("进入编辑节点nodeCode{}||name{}",nodeCode,name);
        nodeCode = StringUtil.trim(nodeCode);
        String nodeName = StringUtil.trim(name);
        Station node = stationDao.findByNodeCode(nodeCode);
        node.setNodeName(nodeName);
        node.setUpdateTime(new Date());
        stationDao.saveAndFlush(node);
        JSONObject obj=new JSONObject();
        obj.put("node",node);
        return obj;
    }
	@RequestMapping("/index")
	public String index() {
		return "admin/station/index";
	}

    @RequestMapping("/upload")
    public String file() {
        return "admin/station/upload";
    }

    @RequestMapping(value = "/fileUploadStation", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadStationExcel(@RequestParam("fileUpload") MultipartFile fileUpload) {
        try {
            if (fileUpload != null && !fileUpload.isEmpty()) {
                List<List<List<String>>> data = PoiUtil.readExcelToList(fileUpload, 1);
                if (null != data && !data.isEmpty()) {
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            for (List<String> row : sheet) {
                                String line = StringUtil.trim(row.get(0));
                                String area = StringUtil.trim(row.get(1));
                                String station = StringUtil.trim(row.get(2));
                                Station lineObj = stationDao.findByNodeName(line);
                                Station areaObj = stationDao.findByNodeName(area+"站区");
                                Station stationObj = stationDao.findByNodeName(station);
                                String nodeCode;
                                if(null==lineObj) {
                                    List<Station> objs=stationDao.findByStationArea(6);
                                    nodeCode=Station.getNodeCode(objs, "");
                                    lineObj = new Station();
                                    lineObj.setNodeName(line);
                                    lineObj.setNodeCode(nodeCode);
                                    stationDao.saveAndFlush(lineObj);
                                }
                                if(null==areaObj) {
                                    List<Station> objs=stationDao.querySubNodesByCode(lineObj.getNodeCode()+"___",9);
                                    nodeCode=Station.getNodeCode(objs, lineObj.getNodeCode());
                                    areaObj = new Station();
                                    areaObj.setNodeName(area+"站区");
                                    areaObj.setNodeCode(nodeCode);
                                    stationDao.saveAndFlush(areaObj);
                                }
                                if(null==stationObj) {
                                    List<Station> objs=stationDao.querySubNodesByCode(areaObj.getNodeCode()+"___",12);
                                    nodeCode=Station.getNodeCode(objs, areaObj.getNodeCode());
                                    stationObj = new Station();
                                    stationObj.setNodeName(station);
                                    stationObj.setNodeCode(nodeCode);
                                    stationDao.saveAndFlush(stationObj);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success("上传成功！");
    }
}
