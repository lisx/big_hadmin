package com.ducetech.hadmin.common.utils;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import java.io.*;
import java.net.ConnectException;

/**
 * 转换工具
 *
 * @author lisx
 * @create 2017-08-07 10:19
 **/
public class PdfUtil {
    /**
     * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice下载地址为
     * http://www.openoffice.org/
     *
     * <pre>
     * 方法示例:
     * String sourcePath = "F:\\office\\source.doc";
     * String destFile = "F:\\pdf\\dest.pdf";
     * Converter.office2PDF(sourcePath, destFile);
     * </pre>
     *
     * @param sourceFile
     *            源文件, 绝对路径. 可以是Office2003-2007全部格式的文档, Office2010的没测试. 包括.doc,
     *            .docx, .xls, .xlsx, .ppt, .pptx等. 示例: F:\\office\\source.doc
     * @param destFile
     *            目标文件. 绝对路径. 示例: F:\\pdf\\dest.pdf
     * @return 操作成功与否的提示信息. 如果返回 -1, 表示找不到源文件, 或url.properties配置错误; 如果返回 0,
     *         则表示操作成功; 返回1, 则表示转换失败
     */
    public static int office2PDF(String sourceFile, String destFile) {
        try {
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                return -1;// 找不到源文件, 则返回-1
            }

            // 如果目标路径不存在, 则新建该路径
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            // 启动OpenOffice的服务
            //String command = "/opt/openoffice4/program/soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
            //Process pro = Runtime.getRuntime().exec(command);
//            try {
//                startSOfficeService();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            // connect to an OpenOffice.org instance running on port 8100
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(
                    "127.0.0.1", 8100);
            connection.connect();

            // convert
            DocumentConverter converter = new OpenOfficeDocumentConverter(
                    connection);
            System.out.println("inputFile"+inputFile);
            System.out.println("outputFile"+outputFile);
            converter.convert(inputFile, outputFile);

            // close the connection
            connection.disconnect();
            // 关闭OpenOffice服务的进程
            //pro.destroy();
//            killSOfficeProcess();
            return 0;

        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 1;
    }
    public static void startSOfficeService() throws InterruptedException, IOException {
        //First we need to check if the soffice process is running
        String commands = "pgrep soffice";
        Process process = Runtime.getRuntime().exec(commands);
        //Need to wait for this command to execute
        int code = process.waitFor();

        //If we get anything back from readLine, then we know the process is running
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        if (in.readLine() == null) {
            //Nothing back, then we should execute the process
            process = Runtime.getRuntime().exec("/root/soffice.sh");
            code = process.waitFor();
            System.out.println("soffice script "+code+" started");
        } else {
            System.out.println("soffice script is already running");
        }

        in.close();
    }
    public static void killSOfficeProcess() throws IOException {
        if (System.getProperty("os.name").matches(("(?i).*Linux.*"))) {
            Runtime.getRuntime().exec("pkill soffice");
        }
    }
}
