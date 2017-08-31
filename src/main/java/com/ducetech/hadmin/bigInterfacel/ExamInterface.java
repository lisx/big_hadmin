package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.DateUtil;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        logger.info("获取试卷题库");
        List<QuestionBank> banks=questionBankDao.findAll();
        List<Exam> exams=examDao.findAll();
        obj=new JSONObject();
        ValueFilter filter = new ValueFilter() {
            @Override
            public Object process(Object obj, String s, Object v) {
                if(v==null){
                    return 0;
                }
                if(s.equals("createTime")||s.equals("updateTime")){
                    return ((Date) v).getTime();
                }
                return v;
            }
        };
        JSONObject o=new JSONObject();
        o.put("banks",banks);
        o.put("exams",exams);
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",o);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, filter));
    }


    @ApiOperation(value="获取练习题", notes="获取练习题")
    @RequestMapping(value="/findExamQuestion", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId", value = "题库Id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "试题类型", dataType = "String", paramType = "query")
    })
    public JSONObject findExamQuestion(Integer bankId,String type){
        logger.info("获取练习题");
        QuestionBank bank=questionBankDao.findOne(bankId);
        List<Question> questions=questionDao.findByQuestionBankAndMenuType(bank,type);
        obj=new JSONObject();
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
        logger.info("获取试卷类型examId{}||bankId{}||userId{}",examId,bankId,userId);
        User user=null;
        ExamLog log=new ExamLog();
        if(null!=userId){
            user=userDao.findOne(userId);
            if(null!=user){
                log.setUser(user);
                logger.info("user{}",user.getUserCode());
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
                    if(singles.size()>0)
                    for(int i=0;i<exam.getSingleNum();i++) {
                        int l = rand.nextInt(singles.size());
                        Question q=singles.get(l);

                        if(!questions.contains(q)&&null!=q){
                            logger.info(q.getPropers().size()+"单选");
                            List<Proper> pros=properDao.findByQuestion(q);
                            logger.info("pros||||||{}|||||{}|||"+pros.size(),q.getId());
                            q.setPropers(pros);
                            questions.add(q);
                        }else{
                            i--;
                        }
                    }
                }
                if(null!=exam.getMultipleNum()&&exam.getMultipleNum()>0){
                    multiples = questionDao.findByQuestionBankAndMenuType(bank,"多选");
                    if(multiples.size()>0)
                    for(int i=0;i<exam.getMultipleNum();i++) {
                        int l = rand.nextInt(multiples.size());
                        Question q=multiples.get(l);
                        if(!questions.contains(q)&&null!=q){
                            List<Proper> pros=properDao.findByQuestion(q);
                            logger.info("pros||||||{}|||||{}|||"+pros.size(),q.getId());
                            q.setPropers(pros);
                            questions.add(q);
                        }else{
                            i--;
                        }
                    }
                }
                if(null!=exam.getJudgeNum()&&exam.getJudgeNum()>0){
                    judges = questionDao.findByQuestionBankAndMenuType(bank,"判断");
                    if(judges.size()>0)
                    for(int i=0;i<exam.getJudgeNum();i++) {
                        int l = rand.nextInt(judges.size());
                        Question q=judges.get(l);
                        if(!questions.contains(q)&&null!=q){
                            questions.add(q);
                        }else{
                            i--;
                        }
                    }
                }
                if(null!=exam.getRankNum()&&exam.getRankNum()>0){
                    ranks = questionDao.findByQuestionBankAndMenuType(bank,"排序");
                    if(ranks.size()>0)
                    for(int i=0;i<exam.getRankNum();i++) {
                        int l = rand.nextInt(ranks.size());
                        Question q=ranks.get(l);
                        if(!questions.contains(q)&&null!=q){
                            logger.info(q.getPropers().size()+"排序");
                            List<Proper> pros=properDao.findByQuestion(q);
                            logger.info("pros||||||{}|||||{}|||"+pros.size(),q.getId());
                            q.setPropers(pros);
                            questions.add(q);
                        }else{
                            i--;
                        }
                    }
                }
            }
            logger.info("|+|+|"+questions.size());
            questions.removeAll(Collections.singleton(null));
            log.setCreateTime(new Date());
            log.setBank(bank);
            log.setExam(exam);
            log.setIfUse(0);
            examLogDao.save(log);
            for(Question question:questions){
                QuestionLog qlog=new QuestionLog();
                qlog.setQuestion(question);
                qlog.setLog(log);
                questionLogDao.save(qlog);
            }
            JSONObject o=new JSONObject();
            o.put("log",log);
            o.put("questions",questions);
            obj=new JSONObject();
            obj.put("state",state);
            obj.put("msg","获取成功");
            obj.put("data",o);
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
        logger.info("进入设置试卷");
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
            @ApiImplicitParam(name = "logId", value = "考试记录Id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "questionId", value = "问题Id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "properIds", value = "答案Id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "交卷时间", dataType = "String", paramType = "query"),
    })
    public JSONObject questionExamLog(Integer logId,Integer questionId,String properIds,Date endTime){
        logger.info("获取练习题logId:{}|questionId:{}|properIds:{}",logId,questionId,properIds);
        ExamLog examLog=examLogDao.findOne(logId);
        Question question=questionDao.findOne(questionId);
        List<Proper> propers=new ArrayList<>();
        QuestionLog log=questionLogDao.findByQuestionAndLog(question,examLog);
        Exam exam=examLog.getExam();
        //分数
        int score=0;
        if(question.getMenuType().equals("判断")){
            if(question.getProper().equals(properIds)){
                score=exam.getJudgeScore();
            }
        }else if(question.getMenuType().equals("单选")){
            Proper proper = properDao.findOne(Integer.parseInt(properIds));
            propers.add(proper);
            if(question.getProper().equals(proper.getName())){
                score=exam.getSingleScore();
            }
        }else if(question.getMenuType().equals("排序")){
            if(properIds.equals("1")){

            }else{
                score=exam.getRankScore();
            }
//            String[] ids = properIds.split(",");
//            if (ids.length > 0) {
//                String answer="";
//                for (String id : ids) {
//                    Proper proper = properDao.findOne(Integer.parseInt(id));
//                    if(null!=proper) {
//                        propers.add(proper);
//                        answer = answer + proper.getName() + "/";
//                    }
//                }
//                answer=answer.substring(0, answer.length()-1);
//                if(question.getProper().equals(answer)){
//                    score=exam.getRankScore();
//                }
//            }
        }else{
            String[] ids = properIds.split(",");
            if (ids.length > 0) {
                List<String> list=Arrays.asList(question.getProper().split("/"));
                List<String> answers=new ArrayList<>(list);
                for (String id : ids) {
                    Proper proper = properDao.findOne(Integer.parseInt(id));
                    propers.add(proper);
                    String temp=proper.getName();
                    for(int i=0;i<answers.size();i++) {
                        if(answers.get(i).equals(temp)) {
                            answers.remove(i);
                        }
                    }
                }
                if(answers.size()==0){
                    score=exam.getMultipleScore();
                }
            }
        }
        log.setSelectProper(propers);
        questionLogDao.save(log);
        if(score!=0) {
            msg="答对";
        }else{
            msg="答错";
        }
        JSONObject o=new JSONObject();
        if(null!=examLog.getScore()) {
                o.put("score",score);
                score = score + examLog.getScore();
                o.put("total",score);
        }else {
            o.put("score",score);
            o.put("total",score);
        }
        examLog.setScore(score);
        if(null!=endTime){
            examLog.setEndTime(endTime);
        }
        examLogDao.saveAndFlush(examLog);

        obj=new JSONObject();
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",o);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }
}
