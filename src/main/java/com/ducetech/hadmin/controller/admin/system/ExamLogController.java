package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IExamDao;
import com.ducetech.hadmin.dao.IExamLogDao;
import com.ducetech.hadmin.dao.IUserDao;
import com.ducetech.hadmin.entity.ExamLog;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
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
    IExamDao examService;
    @Autowired
    IExamLogDao examLogDao;
    @Autowired
    IUserDao userDao;

    @RequestMapping("/index")
    public String index() {
        logger.debug("测试进入exam首页");
        return "admin/examlog/index";
    }
    /**
     * 查询试题集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/user" }, method = RequestMethod.GET)
    @ResponseBody
    public Page<User> user() {
        return userDao.findByScore( getPageRequest());
    }

    @RequestMapping(value = { "/show/{id}" }, method = RequestMethod.GET)
    public String show(@PathVariable Integer id, Model model) {
        User user=userDao.findOne(id);
        List<ExamLog> logs=examLogDao.findByUser(user);
        model.addAttribute("user",user);
        model.addAttribute("logs",logs);
        return "admin/examlog/show";
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
