package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IExamDao;
import com.ducetech.hadmin.entity.Exam;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
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

import java.util.Date;

/**
 * 配置考试类型
 *
 * @author lisx
 * @create 2017-08-09 11:07
 **/
@Controller
@RequestMapping("/admin/exam")
public class ExamController extends BaseController {
    Logger logger= LoggerFactory.getLogger(ExamController.class);
    @Autowired
    IExamDao examService;
    @Autowired

    @RequestMapping("/add")
    public String index() {
        logger.debug("测试进入exam配置试卷页");
        return "admin/learn/examForm";
    }
    @RequestMapping("/edit")
    public String edit(Integer id,Model model) {
        if(null!=id) {
            Exam exam = examService.findOne(id);
            model.addAttribute("exam", exam);
        }
        return "admin/learn/examForm";
    }
    /**
     * 查询集合
     * @return Page<User>
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Exam> list() {
        SimpleSpecificationBuilder<Exam> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return examService.findAll(builder.generateSpecification(), getPageRequest());
    }

    @RequestMapping(value = "/saveExam", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult save(Exam exam) {
        try {
            exam.setCreateId(getUser().getId());
            exam.setCreateTime(new Date());
            examService.saveAndFlush(exam);
        }catch(Exception e){
            logger.debug(e.getMessage());
        }
        return JsonResult.success();
    }
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            examService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
