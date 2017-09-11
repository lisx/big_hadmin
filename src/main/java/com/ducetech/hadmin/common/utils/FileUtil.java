package com.ducetech.hadmin.common.utils;

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
        String destPathFile = destPath+flag+destName;//合并后的文件路径

        File destFile = new File(destPathFile);//合并后的文件
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            out = new FileOutputStream(destFile);
            bos = new BufferedOutputStream(out);
            for (Integer src : srcPaths) {
                System.out.println("src::"+destPath+guid+"/"+destName+"/"+src);
                File srcFile = new File(destPath+guid+"/"+destName+"/"+src);
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
            deleteAllFilesOfDir(new File(destPath+guid));
            //关闭流
            try {
                if(bos!=null)bos.close();
                if(out!=null)out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }
}
