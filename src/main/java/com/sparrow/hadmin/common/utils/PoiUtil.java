package com.sparrow.hadmin.common.utils;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author lisx
 * @create 2017-07-31 10:59
 **/
public class PoiUtil {
    private final static Logger logger = LoggerFactory.getLogger(PoiUtil.class);
    /**
     * 将excel表格转换为List<List<List<String>>>
     * @param excelFile
     * @param ignoreLine
     * @return
     */
    public static List<List<List<String>>> readExcelToList(MultipartFile excelFile, int ignoreLine) {
        List<List<List<String>>> allData = new ArrayList<>();
        try{
            Workbook workbook = createWorkbook(excelFile.getInputStream());
            //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
            int sheetNumber = workbook.getNumberOfSheets();
            //读取每个工作表
            if (sheetNumber > 0) {
                //对每个工作表进行循环，读取每个工作表
                for (int i=0; i<sheetNumber; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    if (null == sheet) {
                        continue;
                    }
                    List<List<String>> sheetList = new ArrayList<>();
                    //遍历每一行，跳过忽略行数
                    int r = 0;
                    Iterator<Row> rows = sheet.iterator();
                    while(rows.hasNext()) {
                        r++;
                        Row row = rows.next();
                        List<String>rowList = new ArrayList<>();
                        if (null == row || r <= ignoreLine) {
                            continue;
                        }
                        //得到当前行的所有单元格
                        if (null != row) {
                            Iterator<Cell> cells = row.iterator();
                            //对每个单元格进行循环
                            while (cells.hasNext()) {
                                Cell cell = cells.next();
                                //读取当前单元格的值
                                String cellStr ="";
                                if (null == cell) {
                                    continue;
                                }
                                if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    cellStr = ((Double)cell.getNumericCellValue()).toString();
                                    BigDecimal b = new BigDecimal(cellStr);
                                    cellStr = b.toPlainString();
                                } else if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    cellStr = cell.getStringCellValue();
                                }
                                rowList.add(cellStr);
                            }
                        }
                        sheetList.add(rowList);
                    }
                    allData.add(sheetList);
                }
            }
        }catch(Exception e){
            logger.error("读取excel文件失败" + e.getMessage(), e);
        }
        return allData;
    }
    /**
     * 根据xls和xlsx不同返回不同的workbook
     * @param inp
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook createWorkbook(InputStream inp) throws IOException, InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            return new HSSFWorkbook(inp);
        }
        if (POIXMLDocument.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        }
        throw new IllegalArgumentException("无法解析的excel版本");
    }
}
