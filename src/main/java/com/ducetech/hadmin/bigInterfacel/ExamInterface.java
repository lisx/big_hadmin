package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IExamDao;
import com.ducetech.hadmin.dao.IProperDao;
import com.ducetech.hadmin.dao.IQuestionBankDao;
import com.ducetech.hadmin.dao.IQuestionDao;
import com.ducetech.hadmin.entity.Exam;
import com.ducetech.hadmin.entity.Proper;
import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.entity.QuestionBank;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 试卷接口
 *
 * @author lisx
 * @create 2017-08-09 11:10
 **/
@RestController
@RequestMapping("/interface/exam")
public class ExamInterface  extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserInterface.class);

    int state=0;
    String msg;
    JSONObject obj;
    @Autowired
    IExamDao examDao;
    @Autowired
    IQuestionBankDao questionBankDao;
    @Autowired
    IQuestionDao questionDao;
    @Autowired
    IProperDao properDao;

    @ApiOperation(value="获取试卷题库", notes="获取试卷题库")
    @RequestMapping(value="/findQuestionBankAll", method = RequestMethod.GET)
    public JSONObject findQuestionBankAll(){
        logger.debug("获取试卷题库");
        List<QuestionBank> banks=questionBankDao.findAll();
        List<Exam> exams=examDao.findAll();
        obj=new JSONObject();
        JSONObject o=new JSONObject();
        o.put("banks",banks);
        o.put("exams",exams);
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",o);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }

    @ApiOperation(value="获取试卷类型", notes="获取试卷类型")
    @RequestMapping(value="/findQuestionById", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="examId",value="试卷id",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="bankId",value="题库id",dataType="Integer", paramType = "query")})
    public JSONObject findQuestionById(Integer examId,Integer bankId){
        logger.debug("获取试卷类型");
        QuestionBank bank=questionBankDao.findOne(bankId);
        Exam exam=examDao.findOne(examId);
        List<Question> questions=new ArrayList<Question>();
        if(null!=exam) {
            List<Question> singles;
            List<Question> multiples;
            List<Question> judges;
            List<Question> ranks;
            Random rand = new Random();
            if(null!=exam.getSingleNum()&&exam.getSingleNum()>0){
                singles = questionDao.findByQuestionBankAndMenuType(bank,"单选");
                for(int i=0;i<exam.getSingleNum();i++) {
                    int l = rand.nextInt(exam.getSingleNum());
                    logger.debug(singles.get(l).getPropers().size()+"|||");
                    questions.add(singles.get(l));
                }
            }
            if(null!=exam.getMultipleNum()&&exam.getMultipleNum()>0){
                multiples = questionDao.findByQuestionBankAndMenuType(bank,"多选");
                for(int i=0;i<exam.getMultipleNum();i++) {
                    int l = rand.nextInt(exam.getMultipleNum());
                    questions.add(multiples.get(l));
                }
            }
            if(null!=exam.getJudgeNum()&&exam.getJudgeNum()>0){
                judges = questionDao.findByQuestionBankAndMenuType(bank,"判断");
                for(int i=0;i<exam.getJudgeNum();i++) {
                    int l = rand.nextInt(exam.getJudgeNum());
                    logger.debug(judges.get(l).getPropers().size()+"|||");
                    questions.add(judges.get(l));
                }
            }
            if(null!=exam.getRankNum()&&exam.getRankNum()>0){
                ranks = questionDao.findByQuestionBankAndMenuType(bank,"排序");
                for(int i=0;i<exam.getRankNum();i++) {
                    int l = rand.nextInt(exam.getRankNum());
                    questions.add(ranks.get(l));
                }
            }

        }
        obj=new JSONObject();
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",questions);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }

    @ApiOperation(value="获取试题答案", notes="获取试题答案")
    @RequestMapping(value="/findProperById", method = RequestMethod.GET)
    @ApiImplicitParam(name="questionId",value="试题id",dataType="Integer", paramType = "query")
    public JSONObject findProperById(Integer questionId) {
        logger.debug("获取试题答案");
        Question question=questionDao.findOne(questionId);
        List<Proper> propers=properDao.findByQuestion(question);
        obj=new JSONObject();
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",propers);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
