package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.dao.ILearnDao;
import com.ducetech.hadmin.entity.Learn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * 学习园地
 *
 * @author lisx
 * @create 2017-08-02 11:07
 **/
@Controller
@RequestMapping("/admin/training")
public class LearnController {
    @Autowired
    ILearnDao learnDao;
    @RequestMapping("/index")
    public String index() {
        return "admin/learn/index";
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile() {
        return "admin/station/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(HttpServletRequest request){
        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file;
        //创建临时文件夹
        File dirTempFile = new File("/Users/lisx/Ducetech/logs/");
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        Learn learn;
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(dirTempFile.getAbsolutePath()+"/"+file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                    learn=new Learn();
                    learn.setFileName(file.getOriginalFilename());
                    learn.setFileSize(""+file.getSize()/1024/1024);
                    learn.setCreateTime(new Date());
                    learnDao.save(learn);
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
}
