package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.*;
import com.ducetech.hadmin.entity.*;
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
import java.util.Date;
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
    private static Logger logger = LoggerFactory.getLogger(ExamInterface.class);

    int state=1;
    String msg;
    JSONObject obj;
    @Autowired
    IExamDao examDao;
    @Autowired
    IQuestionBankDao questionBankDao;
    @Autowired
    IQuestionDao questionDao;
    @Autowired
    IQuestionLogDao questionLogDao;
    @Autowired
    IUserDao userDao;
    @Autowired
    IProperDao properDao;
    @Autowired
    IExamLogDao examLogDao;

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

    @ApiOperation(value="获取练习题", notes="获取练习题")
    @RequestMapping(value="/findExamQuestion", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId", value = "题库Id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "试题类型", dataType = "String", paramType = "query")
    })
    public JSONObject findExamQuestion(Integer bankId,String type){
        logger.debug("获取练习题");
        QuestionBank bank=questionBankDao.findOne(bankId);
        List<Question> questions=questionDao.findByQuestionBankAndMenuType(bank,type);
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",questions);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }


    @ApiOperation(value="获取试卷及考试题", notes="获取试卷及考试题")
    @RequestMapping(value="/findQuestionById", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="examId",value="试卷id",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="bankId",value="题库id",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="userId",value="用户id",dataType="Integer", paramType = "query")
    })
    public JSONObject findQuestionById(Integer examId,Integer bankId,Integer userId){
        logger.debug("获取试卷类型examId{}||bankId{}||userId{}",examId,bankId,userId);
        User user=null;
        ExamLog log=new ExamLog();
        if(null!=userId){
            user=userDao.findOne(userId);
            if(null!=user){
                log.setUser(user);
                logger.debug("user{}",user.getUserCode());
            }
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
                        int l = rand.nextInt(singles.size());
                        Question q=singles.get(l);
                        System.out.println(singles.size()+"|||||空"+q.getId());
                        if(null==q){
                            System.out.println(singles.size()+"|||||空"+l);
                        }
                        questions.add(q);
                    }
                }
                if(null!=exam.getMultipleNum()&&exam.getMultipleNum()>0){
                    multiples = questionDao.findByQuestionBankAndMenuType(bank,"多选");
                    for(int i=0;i<exam.getMultipleNum();i++) {
                        int l = rand.nextInt(multiples.size());
                        questions.add(multiples.get(l));
                    }
                }
                if(null!=exam.getJudgeNum()&&exam.getJudgeNum()>0){
                    judges = questionDao.findByQuestionBankAndMenuType(bank,"判断");
                    for(int i=0;i<exam.getJudgeNum();i++) {
                        int l = rand.nextInt(judges.size());
                        Question q=judges.get(l);
                        System.out.println(judges.size()+"|||||空"+q.getId());
                        if(null==q){
                            System.out.println(judges.size()+"|||||空"+l);
                        }
                        questions.add(q);
                    }
                }
                if(null!=exam.getRankNum()&&exam.getRankNum()>0){
                    ranks = questionDao.findByQuestionBankAndMenuType(bank,"排序");
                    for(int i=0;i<exam.getRankNum();i++) {
                        int l = rand.nextInt(ranks.size());
                        questions.add(ranks.get(l));
                    }
                }
            }
            logger.debug("|+|+|"+questions.size());
            log.setExamTime(new Date());
            log.setQuestions(questions);
            log.setBank(bank);
            examLogDao.save(log);
            obj=new JSONObject();
            obj.put("state",state);
            obj.put("msg",msg);
            obj.put("data",log);
        }else{
            obj=new JSONObject();
            obj.put("state",state);
            obj.put("msg","缺少用户");
            obj.put("data","");
        }
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));

    }

    @ApiOperation(value="设置试卷", notes="设置试卷")
    @RequestMapping(value="/setExam", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="examName",value="试卷名",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="singleNum",value="单选题数量",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="singleScore",value="单选题分数",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="multipleNum",value="多选题数量",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="multipleScore",value="多选题分数",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="judgeNum",value="判断题数量",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="judgeScore",value="判断题分数",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="rankNum",value="排序提数量",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="rankScore",value="排序提分数",dataType="Integer", paramType = "query"),
    })
    public JSONObject setExam(String examName,Integer singleNum,Integer singleScore,Integer multipleNum,
            Integer multipleScore,Integer judgeNum,Integer judgeScore,Integer rankNum,Integer rankScore){
        logger.debug("进入设置试卷");
        Exam exam=examDao.findByExamName(examName);
        if(null==exam) {
            exam = new Exam();
            exam.setExamName(examName);
            exam.setSingleNum(singleNum);
            exam.setSingleScore(singleScore);
            exam.setMultipleNum(multipleNum);
            exam.setMultipleScore(multipleScore);
            exam.setJudgeNum(judgeNum);
            exam.setJudgeScore(judgeScore);
            exam.setRankNum(rankNum);
            exam.setRankScore(rankScore);
            examDao.save(exam);
        }
        obj=new JSONObject();
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",exam);
        return obj;
    }

    @ApiOperation(value="设置考试记录", notes="设置考试记录")
    @RequestMapping(value="/questionExamLog", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "logId", value = "考试记录Id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "questionId", value = "问题Id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "properId", value = "答案Id", dataType = "Integer", paramType = "query")
    })
    public JSONObject questionExamLog(Integer logId,Integer questionId,Integer properId){
        logger.debug("获取练习题");
        ExamLog examLog=examLogDao.findOne(logId);
        Question question=questionDao.findOne(questionId);
        Proper proper=properDao.findOne(properId);
        QuestionLog log=new QuestionLog();
        log.setQuestion(question);
        log.setSelectProper(proper);
        obj=new JSONObject();
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data","");
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}