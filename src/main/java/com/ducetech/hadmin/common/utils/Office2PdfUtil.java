package com.ducetech.hadmin.common.utils;

import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;


/**
 *
 * <ul>
 * <li>日期：2015-11-16-上午10:22:11</li>
 * <li>创建人:lxz</li>
 * <li>jodconverter-3.0</li>
 * </ul>
 */
public class Office2PdfUtil{

    private static Office2PdfUtil office2PdfUtil = new Office2PdfUtil();
    @Autowired
    private static OfficeManager officeManager;
    public static Office2PdfUtil getOffice2PdfUtil() {
        return office2PdfUtil;
    }

    /**
     *
     * office2Pdf 方法
     * @descript：TODO
     * @param inputFile 文件全路径
     * @param pdfFilePath pdf文件全路径
     * @return void
     * @author lxz
     * @return
     */
    public void office2Pdf(String inputFile,String pdfFilePath) {

        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) {
            pdfFile.delete();
        }
        try{
            long startTime = System.currentTimeMillis();
            //打开服务
            startService();
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            //开始转换
            converter.convert(new File(inputFile),new File(pdfFilePath));
            //关闭
            stopService();
            System.out.println("运行结束");
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    public static void stopService() throws OfficeException {
        if (officeManager != null) {
            officeManager.stop();
        }
    }

    public static void startService(){
        try {
            officeManager.start();    //启动服务
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
    }
}