package com.ducetech.hadmin.bigInterfacel;

import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.entity.BigFile;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.URLEncoder;

/**
 * 下载文件
 *
 * @author lisx
 * @create 2017-08-22 17:08
 **/
@Controller
@RequestMapping("/interface")
public class DownloadInteface extends BaseController {
    @Autowired
    IBigFileDao fileDao;
    //文件下载相关代码
    @ApiOperation(value="获取文件", notes="根据id获取文件")
    @RequestMapping(value="/download", method = RequestMethod.GET)
    @ApiImplicitParam(name="id",value="文件id",dataType="Integer", paramType = "query")
    public void download( Integer id) throws IOException {
        BigFile file=fileDao.findOne(id);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(file.getFileName(), "UTF-8"));// 设置文件名
        request.setCharacterEncoding("utf-8");
        String path= file.getFileUrl();
        ServletOutputStream outputStream = response.getOutputStream();
        returnFile(path,outputStream);
    }

    public static void returnFile(String filename, OutputStream out) throws FileNotFoundException, IOException {
        // A FileInputStream is for bytes
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            byte[] buf = new byte[1*1024*10]; // 4K buffer
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        } finally {
            if (fis != null)
                fis.close();
        }
    }
}
