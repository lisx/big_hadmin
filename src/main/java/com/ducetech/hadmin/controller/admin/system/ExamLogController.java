package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.DateUtil;
import com.ducetech.hadmin.common.utils.PoiUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IExamLogDao;
import com.ducetech.hadmin.dao.IUserDao;
import com.ducetech.hadmin.entity.ExamLog;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 配置考试类型
 *
 * @author lisx
 * @create 2017-08-09 11:07
 **/
@Controller
@RequestMapping("/admin/examlog")
public class ExamLogController extends BaseController {
    Logger logger= LoggerFactory.getLogger(ExamLogController.class);
    @Autowired
    IExamLogDao examLogDao;
    @Autowired
    IUserDao userDao;

    @RequestMapping("/index")
    public String index() {
//        logger.debug("测试进入exam首页");
        return "admin/examlog/index";
    }
    /**
     * 查询试题集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/user" }, method = RequestMethod.GET)
    @ResponseBody
    public Page<User> user() {
        Page<User> users=null;
        try {
            users = userDao.findByScore(getPageRequest());
        }catch(Exception e){
            logger.debug(e.getMessage());
        }
        return users;
    }

    @RequestMapping(value = { "/show/{id}" }, method = RequestMethod.GET)
    public String show(@PathVariable Integer id, Model model) {
        User user=userDao.findOne(id);
        List<ExamLog> logs=examLogDao.findByUser(user);
        model.addAttribute("user",user);
        model.addAttribute("logs",logs);
        return "admin/examlog/show";
    }
    @RequestMapping(value = { "/exportLog/{id}" }, method = RequestMethod.GET)
    public void exportLog(@PathVariable Integer id) throws IOException {
        User user=userDao.findOne(id);
        List<ExamLog> logs=examLogDao.findByUser(user);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(user.getUserName() + "的考试记录表格");
        HSSFRow row;
        HSSFCellStyle style = PoiUtil.getDefaultHssfCellStyle(workbook);
        row = sheet.createRow(0);
        Cell cell=row.createCell(0);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell=row.createCell(1);
        cell.setCellValue(user.getUserName());
        cell.setCellStyle(style);
        cell=row.createCell(2);
        cell.setCellValue("工号");
        cell.setCellStyle(style);
        cell=row.createCell(3);
        cell.setCellValue(user.getUserCode());
        cell.setCellStyle(style);
        cell=row.createCell(4);
        cell.setCellValue("站区");
        cell.setCellStyle(style);
        cell=row.createCell(5);
        cell.setCellValue(user.getStationArea());
        cell.setCellStyle(style);
        cell=row.createCell(6);
        cell.setCellValue("车站");
        cell.setCellStyle(style);
        cell=row.createCell(7);
        cell.setCellValue(user.getStation());
        cell.setCellStyle(style);
        row=sheet.createRow(1);
        cell=row.createCell(0);
        cell.setCellValue("考试时间");
        cell.setCellStyle(style);
        cell=row.createCell(1);
        cell.setCellStyle(style);
        CellRangeAddress region=new CellRangeAddress(1, 1, 0, 1);
        sheet.addMergedRegion(region);
        cell=row.createCell(2);
        cell.setCellValue("题库名称");
        cell.setCellStyle(style);
        cell=row.createCell(3);
        cell.setCellStyle(style);
        region=new CellRangeAddress(1, 1, 2, 3);
        sheet.addMergedRegion(region);
        cell=row.createCell(4);
        cell.setCellValue("试卷类型");
        cell.setCellStyle(style);
        cell=row.createCell(5);
        cell.setCellStyle(style);
        region=new CellRangeAddress(1, 1, 4, 5);
        sheet.addMergedRegion(region);
        cell=row.createCell(6);
        cell.setCellValue("用时");
        cell.setCellStyle(style);
        cell=row.createCell(7);
        cell.setCellValue("分数");
        cell.setCellStyle(style);
        for(int i=0;i<logs.size();i++){
            row=sheet.createRow(i+2);
            for(int j=0;j<8;j++) {
                cell = row.createCell(j);
                cell.setCellStyle(style);
            }
            ExamLog log=logs.get(i);
            String createTime= DateUtil.dateFormat(log.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
            row.getCell(0).setCellValue(createTime);
            region=new CellRangeAddress(i+2, i+2, 0, 1);
            sheet.addMergedRegion(region);
            row.getCell(2).setCellValue(log.getBank().getName());
            region=new CellRangeAddress(i+2, i+2, 2, 3);
            sheet.addMergedRegion(region);
            row.getCell(4).setCellValue(log.getExam().getExamName());
            region=new CellRangeAddress(i+2, i+2, 4, 5);
            sheet.addMergedRegion(region);
            row.getCell(6).setCellValue(log.getEndTime());
            row.getCell(7).setCellValue(log.getScore()==null?"":log.getScore()+"");
        }
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(user.getUserName()+"的考试记录.xls", "UTF-8"));// 设置文件名
        OutputStream os = response.getOutputStream();
        try {
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping(value = { "/userLog/{id}" }, method = RequestMethod.GET)
    @ResponseBody
    public List<ExamLog>  userLog(@PathVariable Integer id) {
        User user=userDao.findOne(id);
        List<ExamLog> logs=examLogDao.findByUser(user);
        return logs;
    }
    @RequestMapping(value = { "/list" }, method = RequestMethod.GET)
    @ResponseBody
    public Page<ExamLog> list() {
        SimpleSpecificationBuilder<ExamLog> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("score", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        builder.add("ifUse", SpecificationOperator.Operator.eq.name(), 0);
        try {
            Page<ExamLog> list = examLogDao.findAll(builder.generateSpecification(), getPageRequest());
            return list;
        }catch(Exception e){
            logger.debug(e.getMessage());
        }
        return null;
    }
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            ExamLog log=examLogDao.findOne(id);
            log.setIfUse(1);
            examLogDao.saveAndFlush(log);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
