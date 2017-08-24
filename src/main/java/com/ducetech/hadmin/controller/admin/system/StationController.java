package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.PoiUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator.Operator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车站信息
 */
@Controller
@RequestMapping("/admin/station")
public class StationController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(StationController.class);
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
        logger.info("获取tree数据");
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
        Station station=stationDao.findByNodeName(area);
        List<String> list=stationDao.findStations(station.getNodeCode().length()+3,station.getNodeCode()+"___");
        return list;

    }
    /**
     * 删除
     * @param nodeId
     * @return
     */
    @RequestMapping(value = "/del/{nodeId}",method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult del(@PathVariable String nodeId){
        logger.info("进入删除节点nodeId{}",nodeId);
        Station station = stationDao.findByNodeCode(nodeId);
        stationDao.delete(station);
        return JsonResult.success();
    }

    /**
     * 增加节点
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Station save(String name, String pId){
        logger.info("进入新增节点name{}||pId{}",name,pId);
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
    public JSONObject update(@PathVariable String nodeCode, String name){
        logger.info("进入编辑节点nodeCode{}||name{}",nodeCode,name);
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
                                    List<Station> objs=stationDao.findByStationArea(3);
                                    nodeCode=Station.getNodeCode(objs, "");
                                    lineObj = new Station();
                                    lineObj.setNodeName(line);
                                    lineObj.setNodeCode(nodeCode);
                                    stationDao.saveAndFlush(lineObj);
                                }
                                if(null==areaObj) {
                                    List<Station> objs=stationDao.querySubNodesByCode(lineObj.getNodeCode()+"___",6);
                                    nodeCode=Station.getNodeCode(objs, lineObj.getNodeCode());
                                    areaObj = new Station();
                                    areaObj.setNodeName(area+"站区");
                                    areaObj.setNodeCode(nodeCode);
                                    stationDao.saveAndFlush(areaObj);
                                }
                                if(null==stationObj) {
                                    List<Station> objs=stationDao.querySubNodesByCode(areaObj.getNodeCode()+"___",9);
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

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(String nodeCode,Model map) {
        map.addAttribute("nodeCode",nodeCode);
        return "admin/station/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request,String nodeCode){
        List<MultipartFile> files =request.getFiles("file");
        Station s=stationDao.findByNodeCode(nodeCode);
        MultipartFile file;
        //创建临时文件夹
        File dirTempFile = new File(BigConstant.upload);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        BigFile station;
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(dirTempFile.getAbsolutePath()+"/"+file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                    station=new BigFile();
                    station.setMenuType("车站文件");
                    station.setFileName(file.getOriginalFilename());
                    station.setFileSize(""+file.getSize()/1000);
                    station.setCreateTime(new Date());
                    station.setFileUrl(BigConstant.upload+file.getOriginalFilename());
                    station.setStationFile(s);
                    station.setNodeCode(s.getNodeCode());
                    fileDao.save(station);
                } catch (Exception e) {
                    //stream =  null;
                    return JsonResult.success("You failed to upload " + i + " =>" + e.getMessage());
                }
            } else {
                return JsonResult.success("You failed to upload " + i + " becausethe file was empty.");
            }
        }
        return JsonResult.success();
    }
	@RequestMapping("/list")
	@ResponseBody
	public Page<BigFile> list(String nodeCode) {
		SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<BigFile>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("name", Operator.likeAll.name(), searchText);
		}
        if(StringUtils.isNotBlank(nodeCode)){
            builder.add("nodeCode", Operator.likeAll.name(), nodeCode);
        }
		Page<BigFile> page = fileDao.findAll(builder.generateSpecification(),getPageRequest());
		return page;
	}

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id){
        logger.info("进入删除节点Id{}",id);
        fileDao.delete(id);
        return JsonResult.success();
    }

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		List<Station> list = stationDao.findAll();
		map.put("list", list);
		return "admin/station/form";
	}


	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		Station station = stationDao.findOne(id);
		map.put("station", station);

		List<Station> list = stationDao.findAll();
		map.put("list", list);
		return "admin/station/form";
	}

	@RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Station station, ModelMap map){
		try {
			stationDao.saveAndFlush(station);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}


}
