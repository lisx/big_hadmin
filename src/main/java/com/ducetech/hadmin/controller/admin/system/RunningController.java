package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IRunningDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Running;
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
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 运行图管理
 *  fire safety
 * @author lisx
 * @create 2017-08-15 08:47
 **/
@Controller
@RequestMapping("/admin/running")
public class RunningController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(RunningController.class);
    @Autowired
    private IStationDao stationDao;
    @Autowired
    IRunningDao runningDao;
    @Autowired
    IBigFileDao fileDao;

    /**
     * 运行图首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("获取站点文件全部数据");
        return "admin/running/index";
    }

    /**
     * 查询集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<Running> list() {
        SimpleSpecificationBuilder<Running> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return  runningDao.findAll(builder.generateSpecification(), getPageRequest());
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
     * 进入运行图上传页面
     * @param map
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(Model map) {
        List<String> stations=stationDao.findLines(6);
        map.addAttribute("stations",stations);
        map.addAttribute("menu",BigConstant.Running);
        return "admin/running/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, Running running){
        logger.info("进入运行图上传文件");
        MultipartFile file =request.getFile("file");
        User user=getUser();
        //创建临时文件夹
        long flag=new Date().getTime();
        String path=BigConstant.upload+flag+file.getOriginalFilename();
        File dest = new File(path);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            BufferedOutputStream stream;
            byte[] bytes = file.getBytes();
            stream = new BufferedOutputStream(new FileOutputStream(dest));
            stream.write(bytes);
            stream.close();
            BigFile bigFile=new BigFile();
            running.setCreateId(user.getId());
            running.setCreateTime(new Date());
            running.setIfUse(0);
            running.setFileName(file.getOriginalFilename());
            bigFile.setFileUrl(path);
            bigFile.setFileType(BigConstant.image);
            bigFile.setFileName(file.getOriginalFilename());
            bigFile.setFileSize(""+Math.round(file.getSize()/1024));
            bigFile.setMenuType(BigConstant.Running);
            fileDao.save(bigFile);
            running.setFileId(bigFile.getId());
            runningDao.save(running);
            return JsonResult.success("处理成功了亲");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResult.success();
    }
}
