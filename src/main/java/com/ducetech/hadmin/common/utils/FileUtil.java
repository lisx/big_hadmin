package com.ducetech.hadmin.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 文件类
 *
 * @author lisx
 * @create 2017-08-08 10:29
 **/
public class FileUtil {
    /**
     * 指定位置开始写入文件
     * @param temp  输入文件
     * @param outPath  输出文件的路径(路径+文件名)
     * @throws IOException
     */
    public static void randomAccessFile( String outPath,MultipartFile temp) throws IOException{
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream=null;
        File tempFile=new File(BigConstant.upload+"chunk/"+temp.getOriginalFilename());
        try{
            File dirFile = new File(outPath);
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = (BufferedInputStream) temp.getInputStream();//new BufferedInputStream(new FileInputStream(tempFile));
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
        }catch(Exception e){
            throw new IOException(e.getMessage());
        }finally{
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (raFile != null) {
                    raFile.close();
                }
            }catch(Exception e){
                throw new IOException(e.getMessage());
            }
        }
    }


    /**
     * 文件合并
     * 注意：在拼接文件路劲时，一定不要忘记文件的跟路径，否则复制不成功
     * @param destPath 目标目录
     * @param srcPaths 源文件目录
     * @param flag
     */
    public static void merge(String destPath, List<Integer> srcPaths, String destName, String guid, long flag){
        if(destPath==null||"".equals(destPath)||srcPaths==null){
            System.out.println("合并失败");
        }
        for (Integer string : srcPaths) {
            if("".equals(string)||string==null)
                System.out.println("合并失败");
        }
        //合并后的文件名
        destPath = destPath+flag+destName;//合并后的文件路径

        File destFile = new File(destPath);//合并后的文件
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            out = new FileOutputStream(destFile);
            bos = new BufferedOutputStream(out);
            for (Integer src : srcPaths) {
                System.out.println("src::"+BigConstant.uploadChunk+guid+"/"+destName+"/"+src);
                File srcFile = new File(BigConstant.uploadChunk+guid+"/"+destName+"/"+src);
                InputStream in = new FileInputStream(srcFile);
                BufferedInputStream bis = new BufferedInputStream(in);
                byte[] bytes = new byte[1024*1024];
                int len = -1;
                while((len = bis.read(bytes))!=-1){
                    bos.write(bytes, 0, len);
                }
                bis.close();
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //关闭流
            try {
                if(bos!=null)bos.close();
                if(out!=null)out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
