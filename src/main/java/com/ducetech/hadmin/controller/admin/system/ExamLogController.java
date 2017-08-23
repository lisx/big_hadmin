package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IExamDao;
import com.ducetech.hadmin.dao.IExamLogDao;
import com.ducetech.hadmin.entity.ExamLog;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
    IExamDao examService;
    @Autowired
    IExamLogDao examLogDao;

    @RequestMapping("/index")
    public String index() {
        logger.debug("测试进入exam首页");
        return "admin/examlog/index";
    }
    /**
     * 查询试题集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<ExamLog> list() {
        SimpleSpecificationBuilder<ExamLog> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("score", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        try {
            Page<ExamLog> list = examLogDao.findAll(builder.generateSpecification(), getPageRequest());
            return list;
        }catch(Exception e){
            logger.debug(e.getMessage());
        }
        return null;
    }
}
