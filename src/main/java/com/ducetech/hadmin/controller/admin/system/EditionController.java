package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.entity.BigFile;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 下载文件
 *
 * @author lisx
 * @create 2017-08-22 17:08
 **/
@Controller
@RequestMapping("/admin/edition")
public class EditionController extends BaseController {
    @Autowired
    IBigFileDao fileDao;
    //文件下载相关代码
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String uploadUser() {
        return "admin/edition/form";
    }

    @RequestMapping(value = "/fileUploadUser", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadUserExcel(@RequestParam("fileUpload") MultipartFile fileUpload) {
        return null;
    }
}
