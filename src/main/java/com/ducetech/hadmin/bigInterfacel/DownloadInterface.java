package com.ducetech.hadmin.bigInterfacel;

import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.entity.BigFile;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.URLEncoder;

/**
 * 下载文件接口
 *
 * @author lisx
 * @create 2017-08-22 17:08
 **/
@Controller
@RequestMapping("/interface")
public class DownloadInterface extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(DownloadInterface.class);

    @Autowired
    IBigFileDao fileDao;

    /**
     * 文件下载相关代码
     * @param id
     * @throws IOException
     */
    @ApiOperation(value="获取文件", notes="根据id获取文件")
    @RequestMapping(value="/download", method = RequestMethod.GET)
    @ApiImplicitParam(name="id",value="文件id",dataType="Integer", paramType = "query")
    public void download( Integer id) throws IOException {
        BigFile bigFile=fileDao.findOne(id);
        String location=bigFile.getFileUrl();
        BufferedInputStream bis = null;
        try {
            File file = new File(location);
            if (file.exists()&&file.length()>0) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
                long fileLength;
                String rangBytes = "";
                fileLength = file.length();

                // get file content
                InputStream ins = new FileInputStream(file);
                bis = new BufferedInputStream(ins);

                // tell the client to allow accept-ranges
                response.reset();
                response.setHeader("Accept-Ranges", "bytes");

                // client requests a file block download start byte
                String range = request.getHeader("Range");
                logger.debug("range:::::"+range);
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    if (rangBytes.endsWith("-")) {  // bytes=270000-
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        contentLength = fileLength - p;  // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                    }
                } else {
                    contentLength = fileLength;
                }

                // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
                // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
                response.setHeader("Content-Length", new Long(contentLength).toString());

                // 断点开始
                // 响应的格式是:
                // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = new StringBuffer("bytes ").append(new Long(p).toString()).append("-")
                            .append(new Long(fileLength - 1).toString()).append("/")
                            .append(new Long(fileLength).toString()).toString();
                    response.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + new Long(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else {
                    String contentRange = new StringBuffer("bytes ").append("0-")
                            .append(fileLength - 1).append("/")
                            .append(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                }

                String fileName = file.getName();
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

                OutputStream out = response.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bsize = 1024;
                byte[] bytes = new byte[bsize];
                if (rangeSwitch == 2) {
                    // 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bsize) {
                        n = bis.read(bytes);
                        readLength += n;
                        out.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bis.read(bytes, 0, (int) (contentLength - readLength));
                        out.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bis.read(bytes)) != -1) {
                        out.write(bytes,0,n);
                    }
                }
                out.flush();
                out.close();
                bis.close();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Error: file " + location + " not found.");
                }
            }
        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 根据code获取文件
     * @param code
     * @throws IOException
     */
    @ApiOperation(value="获取用户文件", notes="根据code获取文件")
    @RequestMapping(value="/userImg", method = RequestMethod.GET)
    @ApiImplicitParam(name="code",value="文件code",dataType="String", paramType = "query")
    public String download( String code) throws IOException {
        System.out.println("||code||"+code);
        String [] codes=code.split("=");
        BigFile file;
        if(null!=codes&&codes.length>1) {
            System.out.println("|code|" + codes[1]);
            file = fileDao.findByFileName(codes[1]+".%");
        }else{
            file = fileDao.findByFileName(codes[0]+".%");
        }
        if(null!=file) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getFileName(), "UTF-8"));// 设置文件名
            String path = file.getFileUrl();
            ServletOutputStream out = response.getOutputStream();
            FileInputStream fis = null;

            try {
                System.out.println("path"+path);
                File filePath=new File(path);
                if(filePath.exists()&&filePath.length()>0) {
                    fis = new FileInputStream(path);
                    byte[] buf = null;
                    if (fis.available() > 4 * 1024) {
                        buf = new byte[4 * 1024]; // 4K buffer
                    } else {
                        buf = new byte[fis.available()];
                    }
                    int bytesRead;
                    while ((bytesRead = fis.read(buf)) != -1) {
                        out.write(buf, 0, bytesRead);
                    }
                    out.flush();
                }else{
                    System.out.println("找不文件");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(out!=null) {
                    out.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
        }else{
            System.out.println("|数据库|空|||");
        }
        return null;
    }
}
