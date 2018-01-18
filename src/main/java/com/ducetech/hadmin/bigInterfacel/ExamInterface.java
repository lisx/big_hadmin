package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.*;
import com.ducetech.hadmin.entity.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    IExamDao examDao;
    @Autowired
    IQuestionBankDao bankDao;
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
    @Autowired
    IStationDao stationDao;

    @ApiOperation(value="获取用户所属站区试卷和题库", notes="获取用户所属站区试卷和题库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "station", value = "站点", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型0是考试1是练习", dataType = "String", paramType = "query")
    })
    @RequestMapping(value="/findQuestionBankAll", method = RequestMethod.GET)
    public JSONObject findQuestionBankAll(String station,String type){//type=0是考试1是练习
        logger.info("获取用户所属站区试卷和题库type0是考试1是练习station:{},type:{}",station,type);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        List<QuestionBank> banks=new ArrayList<>();
        List<Exam> exams=null;
        Station sub=stationDao.findByNodeName(station);
        String nodeCode="%000%";
        String area="";
        if(null!=sub){
            msg=BigConstant.state_1;
            nodeCode="%"+sub.getNodeCode()+"%";
            area=sub.getNodeCode().substring(0,sub.getNodeCode().length()-3);
            area="%"+area+"%";
        }else{
            msg=BigConstant.state_2;
        }
        String[] menuTypes={"单选","多选","排序","判断"};
        //考试
        if(null!=type&&type.equals("0")){
            exams=examDao.findByStationAndIfUse(nodeCode,area);
            if(null==exams){
                msg=BigConstant.state_2;
            }
            for(int i=0;i<exams.size();i++){QuestionBank bank=bankDao.findOne(exams.get(i).getBankId());
                Set<String> sets=new HashSet<>();
                for(String menuType:menuTypes) {
                    List<Question> questions = questionDao.findByQuestionBankAndMenuTypeAndIfUse(bank,menuType,0 );
                    if(null!=questions&&questions.size()>0){
                        sets.add(menuType);
                    }
                }
                bank.setContain(StringUtils.join(sets.toArray(),","));
                if(StringUtil.isBlank(bank.getContain())){
                    continue;
                }
                banks.add(bank);
            }
            for (int i = 0; i < banks.size(); i++) {
                exams = examDao.findByQuestionBankAndIfUse(banks.get(i), 0);
                banks.get(i).setExams(exams);
            }
        }else {//练习

            banks=bankDao.findByStation(nodeCode,area+"%");
            if(null==banks){
                msg=BigConstant.state_2;
            }
            for (int i = 0; i < banks.size(); i++) {
                Set<String> sets=new HashSet<>();
                exams = examDao.findByQuestionBankAndIfUse(banks.get(i), 0);
                banks.get(i).setExams(exams);
                for (String menuType : menuTypes) {
                    List<Question> questions = questionDao.findByQuestionBankAndMenuTypeAndIfUse(banks.get(i), menuType, 0);
                    if (null != questions && questions.size() > 0) {
                        sets.add(menuType);
                    }
                }
                banks.get(i).setContain(StringUtils.join(sets.toArray(), ","));
                if(StringUtil.isBlank(banks.get(i).getContain())){
                    banks.remove(i);
                }
            }
        }
        JSONObject o=new JSONObject();
        o.put("banks",banks);
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",o);
        return obj;
    }


    @ApiOperation(value="获取练习题", notes="获取练习题")
    @RequestMapping(value="/findExamQuestion", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId", value = "题库Id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "试题类型", dataType = "String", paramType = "query")
    })
    public JSONObject findExamQuestion(Integer bankId,String type){
        logger.info("获取练习题bankId:{},type:{}",bankId,type);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        QuestionBank bank=bankDao.findOne(bankId);
        List<Question> questions=questionDao.findByQuestionBankAndMenuTypeAndIfUse(bank,type,0);
        for (int i=0;i<questions.size();i++){
            List<Proper> propers=properDao.findByQuestion(questions.get(i));
            Collections.shuffle(propers);
            questions.get(i).setPropers(propers);
            String [] imgs=null;
            if(!StringUtil.isBlank(questions.get(i).getImgUrl())){
                imgs=questions.get(i).getImgUrl().split("=");
            }
            String url="";
            if(null!=imgs&&imgs.length>0) {
                url=imgs[imgs.length - 1];
            }else{
                url=questions.get(i).getImgUrl();
            }
            if(StringUtil.isBlank(url)||url.equals("null")){
                questions.get(i).setImgUrl("");
            }else {
                questions.get(i).setImgUrl(properties.getHttp() + url);
            }
        }
        Collections.shuffle(questions);
        if(null==questions){
            msg=BigConstant.state_2;
        }else{
            msg=BigConstant.state_1;
        }
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",questions);
        return obj;
        //return JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
    }


    @ApiOperation(value="获取试卷及考试题", notes="获取试卷及考试题")
    @RequestMapping(value="/findQuestionById", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="examId",value="试卷id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="bankId",value="题库id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="userId",value="用户id",dataType="String", paramType = "query")
    })
    public JSONObject findQuestionById(Integer examId,Integer bankId,Integer userId){
        logger.info("获取试卷及考试题examId:{},bankId:{},userId:{}",examId,bankId,userId);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        User user=null;
        ExamLog log=new ExamLog();
        JSONObject o=new JSONObject();
        if(null!=userId){
            user=userDao.findOne(userId);
            if(null!=user){
                log.setUser(user);
                logger.info("user{}",user.getUserCode());
                QuestionBank bank=bankDao.findOne(bankId);
                Exam exam=examDao.findOne(examId);
                List<Question> questions=new ArrayList<Question>();
                if(null!=exam) {
                    List<Question> singles;
                    List<Question> multiples;
                    List<Question> judges;
                    List<Question> ranks;
                    Random rand = new Random();
                    if(null!=exam.getSingleNum()&&exam.getSingleNum()>0){
                        singles = questionDao.findByQuestionBankAndMenuTypeAndIfUse(bank,"单选",0);
                        if(singles.size()>0)
                        for(int i=0;i<exam.getSingleNum();i++) {
                            int l = rand.nextInt(singles.size());
                            Question q=singles.get(l);

                            if(!questions.contains(q)&&null!=q){
                                logger.info(q.getPropers().size()+"单选");
                                List<Proper> pros=properDao.findByQuestion(q);
                                logger.info("pros||||||{}|||||{}|||"+pros.size(),q.getId());
                                Collections.shuffle(pros);
                                q.setPropers(pros);
                                questions.add(q);
                            }else{
                                i--;
                            }
                        }
                    }
                    if(null!=exam.getMultipleNum()&&exam.getMultipleNum()>0){
                        multiples = questionDao.findByQuestionBankAndMenuTypeAndIfUse(bank,"多选",0);
                        if(multiples.size()>0)
                        for(int i=0;i<exam.getMultipleNum();i++) {
                            int l = rand.nextInt(multiples.size());
                            Question q=multiples.get(l);
                            if(!questions.contains(q)&&null!=q){
                                List<Proper> pros=properDao.findByQuestion(q);
                                logger.info("pros||||||{}|||||{}|||"+pros.size(),q.getId());
                                Collections.shuffle(pros);
                                q.setPropers(pros);
                                questions.add(q);
                            }else{
                                i--;
                            }
                        }
                    }
                    if(null!=exam.getJudgeNum()&&exam.getJudgeNum()>0){
                        judges = questionDao.findByQuestionBankAndMenuTypeAndIfUse(bank,"判断",0);
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
                        ranks = questionDao.findByQuestionBankAndMenuTypeAndIfUse(bank,"排序",0);
                        if(ranks.size()>0)
                        for(int i=0;i<exam.getRankNum();i++) {
                            int l = rand.nextInt(ranks.size());
                            Question q=ranks.get(l);
                            if(!questions.contains(q)&&null!=q){
                                logger.info(q.getPropers().size()+"排序");
                                List<Proper> pros=properDao.findByQuestion(q);
                                Collections.shuffle(pros);
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
                    String [] imgs=null;
                    if(!StringUtil.isBlank(question.getImgUrl())){
                        imgs=question.getImgUrl().split("=");
                    }
                    String url="";
                    if(null!=imgs&&imgs.length>0) {
                        url=imgs[imgs.length - 1];
                    }else{
                        url=question.getImgUrl();
                    }
                    if(StringUtil.isBlank(url)||url.equals("null")){
                        question.setImgUrl("");
                    }else {
                        question.setImgUrl(properties.getHttp() + url);
                    }
                }

                o.put("log",log);
                o.put("questions",questions);
                obj.put("state",state);
                obj.put("msg",BigConstant.state_1);
                obj.put("data",o);
            }else{
                obj.put("state",state);
                obj.put("msg",BigConstant.state_3);
                obj.put("data",null);
            }
        }else{
            obj=new JSONObject();
            obj.put("state",state);
            obj.put("msg",BigConstant.state_3);
            obj.put("data",null);
        }
        return obj;

    }

    @ApiOperation(value="设置考试记录", notes="设置考试记录")
    @RequestMapping(value="/setExamLog", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "logId", value = "考试记录Id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "score", value = "分数", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "交卷时间", dataType = "String", paramType = "query"),
    })
    public JSONObject setExamLog(Integer logId,Integer score,String endTime){
        logger.info("设置考试记录logId:{}|score{}|endTime{}",logId,score,endTime);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        ExamLog examLog=examLogDao.findOne(logId);
        examLog.setScore(score);
        examLog.setEndTime(endTime);
        examLogDao.saveAndFlush(examLog);
        obj.put("state",state);
        obj.put("msg","完成考试");
        obj.put("data","完成考试");
        return obj;
//        JSONObject jobj=JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
//        logger.info(jobj.toJSONString());
//        return jobj;
    }

    @ApiOperation(value="设置考试记录", notes="设置考试记录")
    @RequestMapping(value="/questionExamLog", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "logId", value = "考试记录Id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "questionId", value = "问题Id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "properIds", value = "答案Id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "交卷时间", dataType = "String", paramType = "query"),
    })
    public JSONObject questionExamLog(Integer logId,Integer questionId,String properIds,String endTime){
        logger.info("设置考试记录:{}|questionId:{}|properIds:{}|endTime{}",logId,questionId,properIds,endTime);
        int state=0;
        String msg;
        JSONObject obj=new JSONObject();
        ExamLog examLog=examLogDao.findOne(logId);
        Question question=questionDao.findOne(questionId);
        List<Proper> propers=new ArrayList<>();
        QuestionLog log=null;
        Exam exam=examLog.getExam();
        //分数
        int score=0;
        if(null!=question) {
            log = questionLogDao.findByQuestionAndLog(question, examLog);

            if (question.getMenuType().equals("判断")) {
                if (question.getProper().equals(properIds)) {
                    score = exam.getJudgeScore();
                }
                if(log.getSelectProper().size()>0&&log.getScore()>0){
                    score -= exam.getJudgeScore();
                }

            } else if (question.getMenuType().equals("单选")) {
                Proper proper = properDao.findOne(Integer.parseInt(properIds));
                propers.add(proper);
                if (question.getProper().equals(proper.getName())) {
                    score = exam.getSingleScore();
                }
                if(log.getSelectProper().size()>0&&log.getScore()>0){
                    score -= exam.getSingleScore();
                }
            } else if (question.getMenuType().equals("排序")) {
                if (properIds.equals("0")) {

                } else {
                    score = exam.getRankScore();
                }
                if(log.getSelectProper().size()>0&&log.getScore()>0){
                    score = -exam.getRankScore();
                }
            } else {
                String[] ids = properIds.split(",");
                if (ids.length > 0) {
                    List<String> list = Arrays.asList(question.getProper().split("/"));
                    List<String> answers = new ArrayList<>(list);
                    for (String id : ids) {
                        Proper proper = properDao.findOne(Integer.parseInt(id));
                        propers.add(proper);
                        String temp = proper.getName();
                        for (int i = 0; i < answers.size(); i++) {
                            if (answers.get(i).equals(temp)) {
                                answers.remove(i);
                            }
                        }
                    }
                    if (answers.size() == 0) {
                        score = exam.getMultipleScore();
                    }
                    if(log.getSelectProper().size()>0&&log.getScore()>0){
                        score -= exam.getMultipleScore();
                    }
                }
            }
        }
        if(null!=log&&null!=propers) {
            log.setSelectProper(propers);
            log.setScore(score);
            if(null!=log)
                questionLogDao.save(log);
        }
        if(score!=0&&score>0) {
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
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",o);
        return obj;
//        JSONObject jobj=JSONObject.parseObject(JSONObject.toJSONString(obj, BigConstant.filter));
//        logger.info(jobj.toJSONString());
//        return jobj;
    }
}
