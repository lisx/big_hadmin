package com.ducetech.hadmin.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理
 * @author lisx
 * @create 2018-01-10 15:05
 **/
public class ImageUtil {
    /**
     * 宽高缩放
     * @param file
     * @param width
     * @param height
     * @param toFile
     */
    public static String size(File file, int width, int height, String toFile) {
        try {
            Thumbnails.of(file).size(width,height).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFile;
    }

    /**
     * 比例缩放
     * @param file
     * @param scale
     * @param toFile
     * @return
     */
    public static String scale(File file, double scale, String toFile){
        try {
            Thumbnails.of(file).scale(scale).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFile;
    }

    /**
     * 旋转 正数:顺时针 负数:逆时针
     * @param file
     * @param rotate 角度
     * @param toFile
     * @return
     */
    public static String rotate(File file,int rotate,String toFile){
        try {
            Thumbnails.of(file).scale(1.0).rotate(rotate).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
        return toFile;
    }

    /**
     * 水印
     * @param file
     * @param mark
     * @param toFile
     * @return
     */
    public static String waterMark(File file,File mark,String toFile){
        try {
            Thumbnails.of(file).scale(1.0).watermark(Positions.BOTTOM_CENTER, ImageIO.read(mark),1.0f)
                    .outputQuality(0.8f).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFile;
    }

    /**
     * 裁剪图片
     * @param file
     * @param x
     * @param y
     * @param width
     * @param height
     * @param toFile
     * @return
     */
    public static String sourceRegion(File file,int x, int y, int width, int height,String toFile){
        try {
            Thumbnails.of(file).sourceRegion(x,y, width,height).scale(1.0).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFile;
    }

    public static String outputQuality(File file, double quality, String toFile){
        try {
            Thumbnails.of(file).scale(1.0).outputQuality(quality).toFile(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFile;
    }
}
