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
    Logger logeer= LoggerFactory.getLogger(ExamController.class);
    @Autowired
    IExamDao examService;
    @Autowired

    @RequestMapping("/index")
    public String index() {
        logeer.debug("测试进入exam首页");
        return "admin/exam/form";
    }
    @RequestMapping("/examEdit")
    public String examEdit(Integer id,Model model) {
        Exam exam=examService.findOne(id);
        model.addAttribute("exam",exam);
        return "admin/exam/form";
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

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult add(Exam exam) {
        exam.setCreateId(getUser().getId());
        exam.setCreateTime(new Date());
        examService.saveAndFlush(exam);
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
