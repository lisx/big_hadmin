package com.ducetech.hadmin.common.utils;

/**
 * @author lisx
 * @create 2017-09-08 19:17
 **/
import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.DefaultOfficeManagerBuilder;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;

import java.io.File;
import java.util.regex.Pattern;

public class Office2PdfUtil {

    private static Office2PdfUtil office2PdfUtil = new Office2PdfUtil();
    private static OfficeManager officeManager;
    // 服务端口
    private static int OPEN_OFFICE_PORT[] = { 8100, 8101, 8102, 8103 };

    public static Office2PdfUtil getOffice2PdfUtil() {
        return office2PdfUtil;
    }

    /**
     *
     * office2Pdf 方法
     *
     * @descript：TODO
     *            文件全路径
     *            pdf文件全路径
     * @return void
     */
    public static void office2Pdf(String sourceFile, String destFile) {

        try {
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                System.out.println( -1);// 找不到源文件, 则返回-1
            }

            // 如果目标路径不存在, 则新建该路径
            File pdfFile = new File(destFile);
            if (!pdfFile.getParentFile().exists()) {
                pdfFile.getParentFile().mkdirs();
            }
            // 打开服务
            startService();
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            // 开始转换
            converter.convert(inputFile, pdfFile);
            // 关闭
            stopService();
            System.out.println("运行结束");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void stopService() {
        if (officeManager != null) {
            try {
                officeManager.stop();
            } catch (OfficeException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startService() {
        DefaultOfficeManagerBuilder configuration = new DefaultOfficeManagerBuilder();
        try {
            configuration.setOfficeHome(getOfficeHome());// 设置安装目录
            configuration.setPortNumbers(OPEN_OFFICE_PORT); // 设置端口
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
            officeManager = configuration.build();
            officeManager.start(); // 启动服务
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
    }

    /**
     * openoffice的安装路径
     *
     * @return
     */
    public static String getOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return "/opt/openoffice4/";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "C:/Program Files (x86)/OpenOffice 4/";
        } else if (Pattern.matches("Mac.*", osName)) {
            return "/Applications/OpenOffice.app/Contents/";
        }
        return null;
    }
}