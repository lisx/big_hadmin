package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.FileUtil;
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
 * 首页滚播图管理
 *  fire safety
 * @author lisx
 * @create 2017-08-15 08:47
 **/
@Controller
@RequestMapping("/admin/rollPlay")
public class RollPlayController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(RollPlayController.class);
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
     * 查询集合
     * @return Page<BigFile>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<BigFile> list(String folder,String nodeCode) {
        User user=getUser();
        SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
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
        builder.add("menuType", SpecificationOperator.Operator.eq.name(), BigConstant.Roll);
        builder.add("ifUse", SpecificationOperator.Operator.eq.name(), 0);
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<BigFile> bigFilePage=fileDao.findAll(builder.generateSpecification(), getPageRequest());
        return bigFilePage;
    }
    /**
     * 首页滚播图首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("获取站点文件全部数据");
        return "admin/rollPlay/index";
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
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(Model map,String nodeCode) {
        map.addAttribute("nodeCode",nodeCode);
        return "admin/rollPlay/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, String folder,String nodeCode){
        logger.info("进入首页滚播图上传文件");
        List<MultipartFile> files =request.getFiles("file");
        User user=getUser();
        MultipartFile file;
        //创建临时文件夹
        File dirTempFile = new File(properties.getUpload());
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            String filePath=properties.getUpload()+file.getOriginalFilename();
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                    stream.write(bytes);
                    stream.close();
                    BigFile bf=new BigFile();
                    bf.setFileSize(""+Math.round(file.getSize()/1024));
                    bf.setByteSize(file.getSize()+"");
                    bf.setMenuType(BigConstant.Roll);
                    bf.setFileType(BigConstant.image);
                    bf.setIfUse(0);
                    bf.setFileName(file.getOriginalFilename());
                    bf.setFileUrl(filePath);
                    BigFile.stationFolder(folder, nodeCode, bf, user,fileDao,stationDao);
                    fileDao.saveAndFlush(bf);

                } catch (Exception e) {
                    return JsonResult.success("You failed to upload " + i + " =>" + e.getMessage());
                }
            } else {
                return JsonResult.success("You failed to upload " + i + " becausethe file was empty.");
            }
        }
        return JsonResult.success();
    }


}
