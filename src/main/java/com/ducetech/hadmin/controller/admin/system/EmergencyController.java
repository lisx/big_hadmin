package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.FileUtil;
import com.ducetech.hadmin.common.utils.PdfUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * 应急预案管理
 *  fire safety
 * @author lisx
 * @create 2017-08-15 08:47
 **/
@Controller
@RequestMapping("/admin/emergency")
public class EmergencyController  extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EmergencyController.class);
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
     * 应急预案首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("获取站点文件全部数据");
        return "admin/emergency/index";
    }
    /**
     * 查询集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<BigFile> list(String folder,String nodeCode) {
        logger.info("list:folder"+folder);
        SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        User user=getUser();
        nodeCode = Station.getQueryNodeCode(nodeCode, user,stationDao);
        if (!StringUtil.isBlank(nodeCode)&&!nodeCode.equals("undefined")) {
            builder.add("nodeCode", SpecificationOperator.Operator.likeAll.name(), nodeCode);
            builder.addOr("nodeCode", SpecificationOperator.Operator.eq.name(), "000");
        }
        if(null!=folder&&!StringUtil.isBlank(folder)) {
            builder.add("folderName", SpecificationOperator.Operator.eq.name(), folder);
        }else {
            builder.add("folderName", SpecificationOperator.Operator.isNull.name(),null);
        }
        builder.add("menuType", SpecificationOperator.Operator.eq.name(), BigConstant.Emergency);
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<BigFile> bigFilePage=fileDao.findAll(builder.generateSpecification(), getPageRequest());
        return bigFilePage;
    }
    @RequestMapping("/add")
    public String add(String nodeCode,Model map) {
        logger.info("进入应急预案添加文件夹");
        map.addAttribute("nodeCode",nodeCode);
        map.addAttribute("menu","应急预案");
        return "admin/emergency/form";
    }

    /**
     * 应急预案新增文件夹
     * @return
     */
    @RequestMapping(value= {"/saveFolder"} ,method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(BigFile folder,String nodeCode,String menu){
        logger.info("新增应急预案文件夹nodeCode{},menu{}",nodeCode,menu);
        User user=getUser();
        Station area;
        if(null!=nodeCode&&!nodeCode.equals("undefined")){
            area=stationDao.findByNodeCode(nodeCode);
        }else{
            area=stationDao.findByNodeName(user.getStationArea());
            if(null!=area)
            nodeCode=area.getNodeCode();
        }
        try {
            folder.setIfFolder(1);
            folder.setCreateTime(new Date());
            folder.setCreateId(user.getId());
            folder.setStationFile(area);
            folder.setNodeCode(nodeCode);
            folder.setMenuType(menu);
            fileDao.saveAndFlush(folder);
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    /**
     * 进入文件夹
     * @param folder
     * @param map
     * @return
     */
    @RequestMapping("/toFolder")
    public String toFolder(String folder,Model map) {
        logger.info("进入应急预案文件夹folder{}",folder);
        map.addAttribute("folder",folder);
        return "admin/emergency/folder";
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            fileDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
    /**
     * 进入培训上传页面
     * @param map
     * @param folder
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(Model map,String folder,String nodeCode) {
        System.out.println("++++++"+folder);
        map.addAttribute("folder",folder);
        map.addAttribute("nodeCode",nodeCode);
        return "admin/emergency/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, int chunk, int chunks, int size, String folder,String nodeCode){
        logger.info("进入应急预案上传文件");
        List<MultipartFile> files =request.getFiles("file");
        User user=getUser();
        MultipartFile file;
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            long flag=new Date().getTime();
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String suffix=StringUtil.suffix(file.getOriginalFilename());
                    if(suffix.equals(BigConstant.docx)||suffix.equals(BigConstant.doc)||suffix.equals(BigConstant.xlsx)||suffix.equals(BigConstant.xls)||suffix.equals(BigConstant.ppt)||suffix.equals(BigConstant.pdf)) {
                        BigFile.saveFile(folder, nodeCode, user, file,BigConstant.office,BigConstant.Emergency,flag,fileDao,stationDao);
                    }else if(suffix.equals(BigConstant.png)||suffix.equals(BigConstant.jpeg)||suffix.equals(BigConstant.jpg)){
                        BigFile.saveFile(folder, nodeCode, user, file,BigConstant.image,BigConstant.Emergency,flag,fileDao,stationDao);
                    }else{
                        try {
                            if(StringUtils.isEmpty(chunk+"")) {
                                //不分片的情况
                                logger.info("不分片的情况");
                                BigFile.saveFile(folder, nodeCode, user, file,BigConstant.video,BigConstant.Emergency,flag,fileDao,stationDao);
                            }else{
                                logger.info("分片的情况");
                                String filePath = BigConstant.upload + "chunk/" + file.getOriginalFilename();
                                byte[] bytes = file.getBytes();
                                stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                                stream.write(bytes);
                                stream.close();
                                FileUtil.randomAccessFile(BigConstant.upload+file.getOriginalFilename(), file);
                                //分片的情况
                                if (Integer.valueOf(chunk) == (Integer.valueOf(chunks) - 1)) {
                                    BigFile.saveFile(size,folder, nodeCode, user, file,BigConstant.video,BigConstant.Emergency,flag,fileDao,stationDao);
                                } else {
                                    logger.info("上传中" + file.getOriginalFilename() + " chunk:" + chunk, "");
                                }
                            }
                        } catch (Exception e) {
                            logger.info("上传失败{}",e.getMessage());
                        }
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

    private void stationFolder(String folder, String nodeCode, BigFile bf,User user) {
        if(null==folder) {
            Station area;
            if(null!=nodeCode&&!nodeCode.equals("undefined")){
                area=stationDao.findByNodeCode(nodeCode);
            }else{
                area=stationDao.findByNodeName(user.getStationArea());
            }
            if (null != area) {
                nodeCode = area.getNodeCode();
                bf.setNodeCode(nodeCode);
                bf.setStationFile(area);
            }else{
                bf.setNodeCode("0");
            }
        }else{
            bf.setFolderName(folder);
            BigFile folder1=fileDao.findByFileName(folder);
            bf.setFolderFile(folder1);
            Station station = folder1.getStationFile();
            if (null != station){
                bf.setStationFile(station);
                bf.setNodeCode(station.getNodeCode());
            }
        }
        bf.setCreateTime(new Date());
        bf.setCreateId(user.getId());
    }
}
