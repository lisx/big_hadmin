package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IExamDao;
import com.ducetech.hadmin.dao.IQuestionBankDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.Exam;
import com.ducetech.hadmin.entity.QuestionBank;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

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
    IStationDao stationDao;
    @Autowired
    IQuestionBankDao bankDao;

    /**
     * 添加配置试卷
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String index(ModelMap model) {
        logger.info("测试进入exam配置试卷页");
        User user=getUser();
        Station station=stationDao.findByNodeName(user.getStationArea());
        String nodeCode="%000%";
        if(null!=station){
            nodeCode="%"+station.getNodeCode()+"%";
        }
        List<QuestionBank> banks=bankDao.findByStation(nodeCode);
        model.addAttribute("banks",banks);
        return "admin/learn/examForm";
    }

    /**
     * 编辑配置试卷
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Integer id,Model model) {
        logger.info("进入exam编辑配置试卷页");
        if(null!=id) {
            Exam exam = examService.findOne(id);
            model.addAttribute("exam", exam);
            List<String> areas=stationDao.findLines(3);
            model.addAttribute("areas",areas);
        }
        User user=getUser();
        Station station=stationDao.findByNodeName(user.getStationArea());
        String nodeCode="%000%";
        if(null!=station){
            nodeCode="%"+station.getNodeCode()+"%";
        }
        List<QuestionBank> banks=bankDao.findByStation(nodeCode);
        model.addAttribute("banks",banks);
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
        User user=getUser();
        Station station=stationDao.findByNodeName(user.getStationArea());
        String nodeCode="%000%";
        if(null!=station){
            nodeCode="%"+station.getNodeCode()+"%";
        }
        builder.add("nodeCode", SpecificationOperator.Operator.likeAll.name(), nodeCode);
        if(!StringUtil.isBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return examService.findAll(builder.generateSpecification(), getPageRequest());
    }

    @RequestMapping(value = "/saveExam", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult save(Exam exam) {
        User user=getUser();
        QuestionBank bank=bankDao.findOne(exam.getBankId());
        try {
            exam.setCreateId(getUser().getId());
            exam.setCreateTime(new Date());
            exam.setStationName(user.getStation());
            exam.setAreaName(user.getStationArea());
            exam.setNodeCode(bank.getNodeCode());
            exam.setIfUse(0);
            examService.saveAndFlush(exam);
        }catch(Exception e){
            logger.info(e.getMessage());
        }
        return JsonResult.success();
    }
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            Exam exam=examService.findOne(id);
            exam.setIfUse(1);
            examService.saveAndFlush(exam);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @RequestMapping(value = "/getBank/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Exam getBank(@PathVariable Integer id) {
        QuestionBank bank=bankDao.findOne(id);
        int singleNum=bankDao.findByBank(bank,"单选");
        int multipleNum=bankDao.findByBank(bank,"多选");
        int judgeNum=bankDao.findByBank(bank,"判断");
        int rankNum=bankDao.findByBank(bank,"排序");
        Exam exam=new Exam();
        exam.setSingleNum(singleNum);
        exam.setMultipleNum(multipleNum);
        exam.setJudgeNum(judgeNum);
        exam.setRankNum(rankNum);
        return exam;
    }
}
