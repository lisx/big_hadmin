package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.PoiUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IProperDao;
import com.ducetech.hadmin.dao.IQuestionBankDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.*;
import com.ducetech.hadmin.service.IQuestionService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

import static com.ducetech.hadmin.common.JsonResult.*;

/**
 * 试题管理
 *
 * @author lisx
 * @create 2017-08-02 11:07
 **/
@Controller
@RequestMapping("/admin/question")
public class QuestionController extends BaseController {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    IQuestionService questionService;
    @Autowired
    IProperDao properDao;
    @Autowired
    IQuestionBankDao questionBankDao;
    @Autowired
    IStationDao stationDao;
    @Autowired
    IBigFileDao fileDao;

    @RequestMapping("/index")
    public String index(Integer id,Model model) {
        model.addAttribute("id",id);
        return "admin/question/index";
    }

    /**
     * 查询题库集合
     *
     * @return Page<User>
     */
    @RequestMapping(value = {"/bank"})
    @ResponseBody
    public Page<QuestionBank> bank() {
        logger.info("进入bank查询Page");
        SimpleSpecificationBuilder<QuestionBank> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        User user=getUser();
        Station station=stationDao.findByNodeName(user.getStationArea());
        String nodeCode="%000%";
        if(null!=station){
            nodeCode="%"+station.getNodeCode()+"%";
        }
        builder.add("nodeCode", SpecificationOperator.Operator.likeAll.name(), nodeCode);
        builder.addOr("nodeCode", SpecificationOperator.Operator.eq.name(), "000");
        builder.add("ifUse", SpecificationOperator.Operator.eq.name(), 0);
        if (!StringUtil.isBlank(searchText)) {
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return questionBankDao.findAll(builder.generateSpecification(), getPageRequest());
    }

    /**
     * 查询试题集合
     *
     * @return Page<User>
     */
    @RequestMapping(value = {"/list"})
    @ResponseBody
    public Page<Question> list(Integer id) {
        SimpleSpecificationBuilder<Question> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        QuestionBank bank = questionBankDao.findOne(id);
        builder.add("questionBank", SpecificationOperator.Operator.eq.name(), bank);
        builder.add("ifUse", SpecificationOperator.Operator.eq.name(), 0);
        if (!StringUtil.isBlank(searchText)) {
            builder.add("title", SpecificationOperator.Operator.likeAll.name(), searchText);
            builder.add("questionBank", SpecificationOperator.Operator.eq.name(), bank);
        }

        return questionService.findAll(builder.generateSpecification(), getPageRequest());
    }

    /**
     * 进上传试题页面
     *
     * @return
     */
    @RequestMapping(value = "/uploadQuestion", method = RequestMethod.GET)
    public String uploadQuestion(Model model) {
        User user=getUser();
        List<String> areas=new ArrayList<>();
        if(user.getStationArea().equals(BigConstant.ADMIN)) {
           areas = stationDao.findLines(9);
        }else{
            areas.add(user.getStationArea());
        }
        model.addAttribute("areas", areas);
        return "admin/learn/uploadQuestion";
    }


    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(@RequestParam("radioFile") MultipartFile radioFile,@RequestParam("multipleFile") MultipartFile multipleFile,@RequestParam("opinionFile") MultipartFile opinionFile,@RequestParam("sortFile") MultipartFile sortFile, String questionType, String bankName, String area, String station) {
        User user = getUser();
        Station s;
        Set<String> sets=new HashSet();
        if (radioFile.isEmpty()&&multipleFile.isEmpty()&&opinionFile.isEmpty()&&sortFile.isEmpty()) {
            return JsonResult.failure(1,"文件为空,必须上传至少一个文件");
        } else {
            QuestionBank bank = questionBankDao.findByName(bankName);
            List<String> contain = new ArrayList<>();
            if (null == bank) {
                bank = new QuestionBank();
                bank.setName(bankName);
                bank.setCreateId(user.getId());
                bank.setCreateTime(new Date());
                if (null != area && !area.equals("全部")) {
                    s = stationDao.findByNodeName(area);
                    bank.setStation(s);
                    bank.setNodeCode(s.getNodeCode());
                } else {
                    s = stationDao.findByNodeName(user.getStationArea());
                    if (null != s) {
                        bank.setStation(s);
                        bank.setNodeCode(s.getNodeCode());
                    }
                }
                if (null != station && !station.equals("全部")) {
                    s = stationDao.findByNodeName(station);
                    bank.setStation(s);
                    bank.setNodeCode(s.getNodeCode());
                }
                questionBankDao.save(bank);
            }
            if (!opinionFile.isEmpty()) {//判断
                contain.add("判断");
                List<List<List<String>>> data = PoiUtil.readExcelToList(opinionFile, 2);
                if (null != data && !data.isEmpty()) {
                    String A = null, B = null, C = null, D = null, title = null, imgUrl = null;
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            for (List<String> row : sheet) {
                                if (row.size() == 3) {
                                    title = StringUtil.trim(row.get(0));
                                    imgUrl = StringUtil.trim(row.get(1));
                                    A = StringUtil.trim(row.get(2));
                                    Question question = new Question();
                                    question.setTitle(title);
                                    question.setProper(A);
                                    question.setImgUrl(imgUrl);
                                    question.setQuestionBank(bank);
                                    question.setCreateId(user.getId());
                                    question.setCreateTime(new Date());
                                    question.setMenuType("判断");
                                    questionService.saveOrUpdate(question);
                                    sets.add("判断");
                                }else{
                                    return JsonResult.failure(1,"上传的判断文件格式错误");
                                }
                            }
                        }
                    }
                }
            }
            if (!sortFile.isEmpty()) {//排序
                contain.add("排序");
                List<List<List<String>>> data = PoiUtil.readExcelToList(sortFile, 2);
                if (null != data && !data.isEmpty()) {
                    String A = null, B = null, C = null, D = null, title = null, imgUrl = null;
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            for (List<String> row : sheet) {
                                if (row.size() == 3) {
                                    title = StringUtil.trim(row.get(0));
                                    imgUrl = StringUtil.trim(row.get(1));
                                    A = StringUtil.trim(row.get(2));
                                    Question question = new Question();
                                    question.setTitle(title);
                                    question.setProper(A);
                                    question.setImgUrl(imgUrl);
                                    question.setQuestionBank(bank);
                                    question.setCreateId(user.getId());
                                    question.setCreateTime(new Date());
                                    question.setMenuType("排序");
                                    questionService.saveOrUpdate(question);
                                    sets.add("排序");
                                    Proper p = null;
                                    String[] arr = A.split("/");
                                    for (int i = 0; i < arr.length; i++) {
                                        if (!StringUtil.isBlank(arr[i])) {
                                            p = new Proper();
                                            p.setQuestion(question);
                                            p.setName(arr[i]);
                                            properDao.save(p);
                                        }
                                    }
                                }else{
                                    return JsonResult.failure(1,"上传的排序文件格式错误");
                                }

                            }
                        }
                    }
                }
            }
            if (!multipleFile.isEmpty()) {//多选
                contain.add("多选");
                List<List<List<String>>> data = PoiUtil.readExcelToList(multipleFile, 2);
                if (null != data && !data.isEmpty()) {
                    String A = null, B = null, C = null, D = null, title = null, imgUrl = null;
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            for (List<String> row : sheet) {
                                if (row.size() > 3) {
                                    title = StringUtil.trim(row.get(0));
                                    imgUrl = StringUtil.trim(row.get(1));
                                    A = StringUtil.trim(row.get(2));
                                    Question question = new Question();
                                    question.setTitle(title);
                                    question.setProper(A);
                                    question.setImgUrl(imgUrl);
                                    question.setQuestionBank(bank);
                                    question.setCreateId(user.getId());
                                    question.setCreateTime(new Date());
                                    question.setMenuType("多选");
                                    questionService.saveOrUpdate(question);
                                    sets.add("多选");
                                    String[] arr = A.split("/");
                                    Proper p = null;
                                    for (int i = 0; i < arr.length; i++) {
                                        if (!StringUtil.isBlank(arr[i])) {
                                            p = new Proper();
                                            p.setQuestion(question);
                                            p.setName(arr[i]);
                                            properDao.save(p);
                                        }
                                    }
                                    for (int i = 3; i < row.size(); i++) {
                                        if (!StringUtil.isBlank(row.get(i))) {
                                            p = new Proper();
                                            p.setQuestion(question);
                                            p.setName(row.get(i));
                                            properDao.save(p);
                                        }
                                    }
                                }else{
                                    return JsonResult.failure(1,"上传的多选文件格式错误");
                                }
                            }
                        }
                    }
                }
            }
            if (!radioFile.isEmpty()) {//单选
                contain.add("单选");
                List<List<List<String>>> data = PoiUtil.readExcelToList(radioFile, 2);
                if (null != data && !data.isEmpty()) {
                    String A = null, B = null, C = null, D = null, title = null, imgUrl = null;
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            for (List<String> row : sheet) {
                                if (row.size() == 6) {
                                    title = StringUtil.trim(row.get(0));
                                    imgUrl = StringUtil.trim(row.get(1));
                                    A = StringUtil.trim(row.get(2));
                                    B = StringUtil.trim(row.get(3));
                                    C = StringUtil.trim(row.get(4));
                                    D = StringUtil.trim(row.get(5));
                                    if (StringUtil.isBlank(title)) {
                                        break;
                                    }
                                    Question question = new Question();
                                    question.setTitle(title);
                                    question.setProper(A);
                                    question.setImgUrl(imgUrl);
                                    question.setQuestionBank(bank);
                                    question.setCreateId(user.getId());
                                    question.setCreateTime(new Date());
                                    question.setMenuType("单选");
                                    questionService.saveOrUpdate(question);
                                    sets.add("单选");
                                    Proper p = new Proper();
                                    p.setQuestion(question);
                                    p.setName(A);
                                    properDao.save(p);
                                    p = new Proper();
                                    p.setQuestion(question);
                                    p.setName(B);
                                    properDao.save(p);
                                    p = new Proper();
                                    p.setQuestion(question);
                                    p.setName(C);
                                    properDao.save(p);
                                    p = new Proper();
                                    p.setQuestion(question);
                                    p.setName(D);
                                    properDao.save(p);
                                }else{
                                    return JsonResult.failure(1,"上传的单选文件格式错误");
                                }
                            }
                        }
                    }
                }
            }
            bank.setContain(StringUtils.join(sets.toArray(),","));
            questionBankDao.saveAndFlush(bank);
        }
        return JsonResult.success("上传成功！");
    }

    /**
     * 进上传试题页面图片
     *
     * @return
     */
    @RequestMapping(value = "/uploadImage", method = RequestMethod.GET)
    public String uploadImage(Model model) {
        List<String> areas = stationDao.findLines(6);
        model.addAttribute("areas", areas);
        return "admin/learn/uploadImage";
    }

    @RequestMapping(value = "/uploadQuestionPost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadQuestionPost(MultipartHttpServletRequest request, String folder, String nodeCode) {
        logger.info("进入练习考试上传文件");
        List<MultipartFile> files = request.getFiles("file");
        User user = getUser();
        MultipartFile file;
        for (int i = 0; i < files.size(); ++i) {
            long flag = new Date().getTime();
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    BigFile.saveFile(folder, nodeCode, user, file, BigConstant.image, BigConstant.Question, flag, fileDao, stationDao);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            } else {
                return JsonResult.failure("You failed to upload " + i + " becausethe file was empty.");
            }
        }
        return JsonResult.success();
    }
}
