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
    private static Logger logger = LoggerFactory.getLogger(StationController.class);
	@Autowired
	private IStationDao stationDao;
    @Autowired
    IBigFileDao fileDao;
    @Autowired
    DucetechProperties properties;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    IBigFileService fileService;


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
        logger.info("根据站区名获取站点数据{}",area);
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
        logger.info("进入删除节点nodeId{}",nodeId);
        //Station station = stationDao.findByNodeCode(nodeId);
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
    @ResponseBody
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

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(String nodeCode,Model map) {
        map.addAttribute("nodeCode",nodeCode);
        map.addAttribute("menuType",BigConstant.Station);
        return "admin/station/uploadFile";
    }

    @RequestMapping(value = "/uploadFileCheck", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFileCheck(String md5,Integer fileSize,String fileType,String fileName,String nodeCode,String folder,String menuType){
        logger.debug("md5:{},fileSize:{},fileType:{},nodeCode{},param{}",md5,fileSize,fileType,nodeCode,menuType);
        String fileUrl=stringRedisTemplate.opsForValue().get("fileMd5"+md5);
        User user=getUser();
        if(!StringUtil.isBlank(fileUrl)){
            BigFile bf = new BigFile();
            bf.setFileSize("" + Math.round(fileSize / 1024));
            bf.setMenuType(menuType);
            bf.setMd5(md5);
            String suffix="."+fileType;
            if(suffix.equals(BigConstant.docx)||suffix.equals(BigConstant.doc)||suffix.equals(BigConstant.xlsx)||suffix.equals(BigConstant.xls)||suffix.equals(BigConstant.ppt)||suffix.equals(BigConstant.pdf)) {
                bf.setFileType(BigConstant.office);
            }else if(suffix.equals(BigConstant.png)||suffix.equals(BigConstant.jpeg)||suffix.equals(BigConstant.jpg)){
                bf.setFileType(BigConstant.image);
            }else {
                bf.setFileType(BigConstant.video);
            }
            bf.setFileName(fileName);
            bf.setFileUrl(fileUrl);
            bf.setByteSize(fileSize+"");
            bf.setIfUse(0);
            bf.stationFolder(folder, nodeCode, bf, user,fileDao,stationDao);
            fileDao.saveAndFlush(bf);
            return JsonResult.success(fileUrl);
        }else{
            return JsonResult.failure(fileUrl);
        }
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, Integer chunk, Integer chunks, Integer size, String folder,String nodeCode,String md5,String upStatus){
        logger.info("进入上传文件{}"+upStatus);
        List<MultipartFile> files =request.getFiles("file");
        User user=getUser();
        MultipartFile file;
        BufferedOutputStream stream;
        FileInputStream fis= null;
        BigFile bf=null;
        for (int i =0; i< files.size(); ++i) {
            long flag=new Date().getTime();
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String suffix=StringUtil.suffix(file.getOriginalFilename());
                    try {
                        if(null==chunks) {
                            logger.info("不分片的情况");
                            //不分片的情况

                            if(suffix.equals(BigConstant.docx)||suffix.equals(BigConstant.doc)||suffix.equals(BigConstant.xlsx)||suffix.equals(BigConstant.xls)||suffix.equals(BigConstant.ppt)||suffix.equals(BigConstant.pdf)) {
                                bf=BigFile.saveFile(md5,properties.getUpload(),folder, nodeCode, user, file,BigConstant.office,BigConstant.Station,flag,fileDao,stationDao);
                            }else if(suffix.equals(BigConstant.png)||suffix.equals(BigConstant.jpeg)||suffix.equals(BigConstant.jpg)){
                                bf=BigFile.saveFile(md5,properties.getUpload(),folder, nodeCode, user, file,BigConstant.image,BigConstant.Station,flag,fileDao,stationDao);
                            }else {
                                bf=BigFile.saveFile(md5,properties.getUpload(),folder, nodeCode, user, file, BigConstant.video, BigConstant.Station, flag, fileDao, stationDao);
                            }
                            stringRedisTemplate.opsForValue().set("fileMd5"+md5,bf.getFileUrl());
                        }else{
                            logger.info("分片的情况");
                            String tempFileDir = properties.getUpload()+md5+"/";
                            String realname = file.getOriginalFilename();
                            // 临时目录用来存放所有分片文件
                            File parentFileDir = new File(tempFileDir+realname+"/");
                            if (!parentFileDir.exists()) {
                                parentFileDir.mkdirs();
                            }
                            // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
                            File tempPartFile = new File(parentFileDir, chunk+"");
                            byte[] bytes = file.getBytes();
                            stream = new BufferedOutputStream(new FileOutputStream(tempPartFile));
                            stream.write(bytes);
                            stream.close();
                            // 是否全部上传完成
                            // 所有分片都存在才说明整个文件上传完成
                            boolean uploadDone = true;
                            for (int c = 0; c < chunks; c++) {
                                File partFile = new File(parentFileDir, c+"");
                                if (!partFile.exists()) {
                                    uploadDone = false;
                                    break;
                                }
                            }
                            // 所有分片文件都上传完成
                            // 将所有分片文件合并到一个文件中
                            logger.info("|||||||"+uploadDone);
                            if (uploadDone) {
                                File[] array = parentFileDir.listFiles();
                                List<Integer> fileNames=new ArrayList<>();
                                for (int a=0;a<array.length;a++){
                                    logger.info("arr"+array[a].getName());
                                    fileNames.add(Integer.parseInt(array[a].getName()));
                                }
                                fileNames= new ArrayList(new TreeSet(fileNames));
                                Collections.sort(fileNames);
                                //     得到 destTempFile 就是最终的文件
                                FileUtil.merge(properties.getUpload(),fileNames,realname,md5,flag);
                                // 删除临时目录中的分片文件
                                FileUtils.deleteDirectory(parentFileDir);
                                if(suffix.equals(BigConstant.docx)||suffix.equals(BigConstant.doc)||suffix.equals(BigConstant.xlsx)||suffix.equals(BigConstant.xls)||suffix.equals(BigConstant.ppt)||suffix.equals(BigConstant.pdf)) {
                                    bf=BigFile.saveFile(md5,properties.getUpload(),size,folder, nodeCode, user, file,BigConstant.office,BigConstant.Station,flag,fileDao,stationDao);
                                }else if(suffix.equals(BigConstant.png)||suffix.equals(BigConstant.jpeg)||suffix.equals(BigConstant.jpg)){
                                    bf=BigFile.saveFile(md5,properties.getUpload(),size,folder, nodeCode, user, file,BigConstant.image,BigConstant.Station,flag,fileDao,stationDao);
                                }else {
                                    bf=BigFile.saveFile(md5,properties.getUpload(),size,folder, nodeCode, user, file, BigConstant.video, BigConstant.Station, flag, fileDao, stationDao);
                                }
                                stringRedisTemplate.opsForValue().set("fileMd5"+md5,bf.getFileUrl());
                            } else {
                                logger.info("上传中 chunks" + chunks + " chunk:" + chunk, "");
                            }
                        }
                    } catch (Exception e) {
                        logger.info("上传失败{}",e.getMessage());
                    }

                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            } else {
                return JsonResult.failure("You failed to upload " + i + " becausethe file was empty.");
            }
        }
        return JsonResult.success();
    }
    /**
     * 查询集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<BigFile> list(String folder,String nodeCode) {
        logger.info("list:folder"+folder+"|||||"+properties.getUploadChunk());

        SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        User user=getUser();
        nodeCode = Station.getQueryNodeCode(nodeCode, user,stationDao);
        if (!StringUtil.isBlank(nodeCode)&&!nodeCode.equals("undefined")) {
            builder.add("nodeCode", SpecificationOperator.Operator.likeAll.name(), nodeCode);
            builder.addOr("nodeCode", SpecificationOperator.Operator.eq.name(), BigConstant.ADMINCODE);
        }
        if(null!=folder&&!StringUtil.isBlank(folder)) {
            builder.add("folderName", SpecificationOperator.Operator.eq.name(), folder);
        }else {
            builder.add("folderName", SpecificationOperator.Operator.isNull.name(),null);
        }
        builder.add("ifUse", SpecificationOperator.Operator.eq.name(), 0);
        builder.add("menuType", SpecificationOperator.Operator.eq.name(), BigConstant.Station);
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<BigFile> bigFilePage=fileDao.findAll(builder.generateSpecification(), getPageRequest());
        return bigFilePage;
    }


    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer[] ids){
        logger.debug(ids.toString());
        try {
            for(int i=0;i<ids.length-1;i++) {
                fileService.delete(ids[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

}
