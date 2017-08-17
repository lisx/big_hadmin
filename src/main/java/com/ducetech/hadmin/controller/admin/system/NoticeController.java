package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.INoticeDao;
import com.ducetech.hadmin.dao.INoticeDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.Notice;
import com.ducetech.hadmin.entity.Notice;
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
 * 通知管理
 *  fire safety
 * @author lisx
 * @create 2017-08-15 08:47
 **/
@Controller
@RequestMapping("/admin/notice")
public class NoticeController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(NoticeController.class);
    @Autowired
    private IStationDao stationDao;
    @Autowired
    INoticeDao noticeDao;

    /**
     * 通知首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.debug("进入通知管理首页");
        return "admin/notice/index";
    }

    @RequestMapping("/show")
    public String form(Model map,Integer id) {
        logger.debug("详情{}",id);
        Notice notice=noticeDao.findOne(id);
        map.addAttribute("notice",notice);
        logger.debug("进入通知详情页");
        return "admin/notice/show";
    }

    /**
     * 查询集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<Notice> list() {
        SimpleSpecificationBuilder<Notice> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return  noticeDao.findAll(builder.generateSpecification(), getPageRequest());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            noticeDao.delete(id);
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
    public String uploadFile(Model map) {
        List<String> stations=stationDao.findLines(3);
        map.addAttribute("stations",stations);
        map.addAttribute("menu","通知");
        return "admin/notice/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, Notice notice){
        logger.debug("进入通知上传文件");
        User user=getUser();
        notice.setCreateId(user.getId());
        notice.setCreateTime(new Date());
        noticeDao.save(notice);
//        MultipartFile file =request.getFile("file");
//        //创建临时文件夹
//        File dest = new File(BigConstant.TRAIN_IMAGE_PATH+notice.getFileName()+".jpg");
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//        try {
//            BufferedOutputStream stream;
//            byte[] bytes = file.getBytes();
//            stream = new BufferedOutputStream(new FileOutputStream(dest));
//            stream.write(bytes);
//            stream.close();
//            notice.setCreateId(user.getId());
//            notice.setCreateTime(new Date());
//            notice.setFileSize(""+Math.round(file.getSize()/1024));
//            noticeDao.save(notice);
//            return JsonResult.success();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return JsonResult.success();
    }

    private void stationFolder(String folder, String nodeCode, Notice bf,User user) {
        if(null==folder) {
            Station area;
            if(null!=nodeCode&&!nodeCode.equals("undefined")){
                area=stationDao.findByNodeCode(nodeCode);
            }else{
                area=stationDao.findByNodeName(user.getStationArea());
            }
            if (null != area) {
                nodeCode = area.getNodeCode();
            }else{
            }
        }else{
        }
        bf.setCreateTime(new Date());
        bf.setCreateId(user.getId());
    }
}
