package com.ducetech.hadmin.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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
        File tempFile=new File(BigConstant.TRAIN_PATH+temp.getOriginalFilename());
        try{
            File dirFile = new File(outPath);
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(new FileInputStream(tempFile));
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
}
