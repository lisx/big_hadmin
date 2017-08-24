package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.FileUtil;
import com.ducetech.hadmin.common.utils.PdfUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IFolderDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Folder;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IBigFileService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
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

import javax.validation.constraints.Null;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 培训资料
 *
 * @author lisx
 * @create 2017-08-02 11:07
 **/
@Controller
@RequestMapping("/admin/train")
public class TrainController  extends BaseController {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(TrainController.class);
    @Autowired
    IBigFileDao fileDao;
    @Autowired
    IFolderDao folderDao;
    @Autowired
    IStationDao stationDao;
    /**
     * 培训资料首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("获取站点文件全部数据");
        return "admin/learn/index";
    }


    @RequestMapping("/toFolder")
    public String toFolder(String folder,Model map) {
        logger.info("进入培训资料文件夹");
        System.out.println("folder+++"+folder);
        map.addAttribute("folder",folder);
        return "admin/learn/folder";
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
        if(null!=folder&&!StringUtil.isBlank(folder)) {
            builder.add("folderName", SpecificationOperator.Operator.likeAll.name(), folder);
        }else {
            builder.add("folderName", SpecificationOperator.Operator.isNull.name(),null);
        }
        if (!StringUtil.isBlank(nodeCode)&&!nodeCode.equals("undefined")) {
            builder.add("nodeCode", SpecificationOperator.Operator.likeAll.name(), nodeCode);
        }
        builder.add("menuType", SpecificationOperator.Operator.likeAll.name(), "培训资料");
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<BigFile> bigFilePage=fileDao.findAll(builder.generateSpecification(), getPageRequest());
        return bigFilePage;
    }

    /**
     * 培训资料文件夹
     * @return
     */
    @RequestMapping(value = { "/folder" })
    @ResponseBody
    public Page<Folder> folder() {
        User user=getUser();
        Station s=stationDao.findByNodeName(user.getStationArea());
        String station=s.getNodeCode();
        SimpleSpecificationBuilder<Folder> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        if(!StringUtil.isBlank(station)){
            if(user.getStationArea().equals("运三分公司")) {
                builder.add("station", SpecificationOperator.Join.or.name(), station, null);
            }else{
                builder.add("station", SpecificationOperator.Operator.likeAll.name(), station);
            }
        }
        return folderDao.findAll(builder.generateSpecification(), getPageRequest());
    }

    /**
     * 进入培训上传页面
     * @param map
     * @param folder
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(Model map,String folder,String nodeCode) {
        map.addAttribute("folder",folder);
        map.addAttribute("nodeCode",nodeCode);
        return "admin/learn/uploadTrain";
    }

    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String add(String nodeCode,Model map) {
        logger.info("进入培训资料添加文件夹{}",nodeCode);
        map.addAttribute("nodeCode",nodeCode);
        map.addAttribute("menu","培训资料");
        return "admin/learn/form";
    }

    /**
     * 培训资料保存文件夹
     * @return
     */
    @RequestMapping(value= {"/saveFolder"} ,method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(BigFile folder,String nodeCode,String menu){
        logger.info("新增培训资料文件夹nodeCode{},menu{}",nodeCode,menu);
        User user=getUser();
        Station area;
        if(null!=nodeCode&&!nodeCode.equals("undefined")){
            area=stationDao.findByNodeCode(nodeCode);
        }else{
            area=stationDao.findByNodeName(user.getStationArea());
        }
        if (null != area) {
            nodeCode = area.getNodeCode();
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
     * 培训资料上传文件
     * @return
     */
    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, String chunk, String chunks, String size, String folder,String nodeCode){
        logger.info("进入培训资料上传文件");
        List<MultipartFile> files =request.getFiles("file");
        User user=getUser();
        MultipartFile file;
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            long flag=new Date().getTime();
            file = files.get(i);
            String filePath=BigConstant.upload+file.getOriginalFilename();
            if (!file.isEmpty()) {
                try {
                    String suffix=StringUtil.suffix(filePath);
                    if(suffix.equals(BigConstant.docx)||suffix.equals(BigConstant.doc)||suffix.equals(BigConstant.xlsx)||suffix.equals(BigConstant.xls)||suffix.equals(BigConstant.ppt)||suffix.equals(BigConstant.pdf)) {
                        BigFile.saveFile(folder, nodeCode, user, file,BigConstant.office,BigConstant.TRAIN,flag,fileDao,stationDao);
                    }else if(suffix.equals(BigConstant.png)||suffix.equals(BigConstant.jpeg)||suffix.equals(BigConstant.jpg)){
                        BigFile.saveFile(folder, nodeCode, user, file,BigConstant.image,BigConstant.TRAIN,flag,fileDao,stationDao);
                    }else{
                        try {
                            byte[] bytes = file.getBytes();
                            stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                            stream.write(bytes);
                            stream.close();
                            if(StringUtils.isEmpty(chunk)) {
                                //不分片的情况
                                BigFile.saveFile(folder, nodeCode, user, file,BigConstant.video,BigConstant.TRAIN,flag,fileDao,stationDao);
                            }else{
                                FileUtil.randomAccessFile(BigConstant.upload+"chunk/"+file.getOriginalFilename(), file);
                                //分片的情况
                                if (Integer.valueOf(chunk) == (Integer.valueOf(chunks) - 1)) {
                                    BigFile.saveFile(folder, nodeCode, user, file,BigConstant.video,BigConstant.TRAIN,flag,fileDao,stationDao);
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

}
