package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;

import static com.ducetech.hadmin.common.JsonResult.*;

/**
 * 下载文件
 *
 * @author lisx
 * @create 2017-08-22 17:08
 **/
@Controller
@RequestMapping("/admin/edition")
public class EditionController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EditionController.class);
    @Autowired
    IBigFileDao fileDao;
    //文件下载相关代码
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String uploadUser() {
        return "admin/edition/form";
    }

    @RequestMapping(value = "/fileUploadEdition", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult fileUploadEdition(MultipartHttpServletRequest request){
        logger.info("进入版本更新上传文件");
        MultipartFile file =request.getFile("file");
        User user=getUser();
        //创建临时文件夹
        String path= properties.getUpload()+file.getOriginalFilename();
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
            bigFile.setIfUse(0);
            bigFile.setFileUrl(path);
            bigFile.setFileType("APK");
            bigFile.setByteSize(file.getSize()+"");
            bigFile.setFileName(file.getOriginalFilename());
            bigFile.setFileSize(""+Math.round(file.getSize()/1024));
            bigFile.setMenuType(BigConstant.Edition);
            fileDao.save(bigFile);
            return success("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return failure("上传失败");
        }
    }
}
