package com.ducetech.hadmin.bigInterfacel;

import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.entity.BigFile;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(DownloadInteface.class);

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
        // 设置文件名
        request.setCharacterEncoding("utf-8");
        String path= file.getFileUrl();
        logger.info("path:"+path);
        String suffix= StringUtil.suffix(file.getFileName());
        if(file.getFileType().equals(BigConstant.office)){
            if(suffix.equals(BigConstant.pdf)){
                response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(file.getFileName(), "UTF-8"));
            }else{
                response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(file.getFileName()+BigConstant.pdf, "UTF-8"));
                path=path+BigConstant.pdf;
            }
        }else{
            response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(file.getFileName(), "UTF-8"));
        }
        ServletOutputStream outputStream = response.getOutputStream();
        returnFile(path,outputStream);
    }
    @ApiOperation(value="获取用户文件", notes="根据code获取文件")
    @RequestMapping(value="/userImg", method = RequestMethod.GET)
    @ApiImplicitParam(name="code",value="文件code",dataType="String", paramType = "query")
    public void download( String code) throws IOException {
        System.out.println("|||||"+code);
        BigFile file=fileDao.findByFileName(code+".jpg");
        if(null!=file) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getFileName(), "UTF-8"));// 设置文件名
            request.setCharacterEncoding("utf-8");
            String path = file.getFileUrl();
            ServletOutputStream outputStream = response.getOutputStream();
            returnFile(path, outputStream);
        }else{
            System.out.println("|||空|||");
        }
    }

    public static void returnFile(String path, OutputStream out) throws FileNotFoundException, IOException {
        // A FileInputStream is for bytes
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] buf = null;
            if(fis.available() > 4*1024){
                buf = new byte[4*1024]; // 4K buffer
            }else {
                buf = new byte[fis.available()];
            }
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.flush();

        } finally {
            if(out!=null)
                out.close();
            if (fis != null)
                fis.close();
        }
    }
}
