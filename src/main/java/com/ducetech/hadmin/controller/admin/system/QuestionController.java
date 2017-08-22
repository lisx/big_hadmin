package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.PoiUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IProperDao;
import com.ducetech.hadmin.dao.IQuestionBankDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.*;
import com.ducetech.hadmin.service.IQuestionService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @RequestMapping("/index")
    public String index() {
        return "admin/question/index";
    }

    /**
     * 查询题库集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/bank" })
    @ResponseBody
    public Page<QuestionBank> bank() {
        logger.info("进入bank");
        SimpleSpecificationBuilder<QuestionBank> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return questionBankDao.findAll(builder.generateSpecification(), getPageRequest());
    }
    /**
     * 查询试题集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<Question> list() {
        SimpleSpecificationBuilder<Question> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        String bank = request.getParameter("bank");
        if(!StringUtil.isBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
            builder.add("bankId", SpecificationOperator.Operator.likeAll.name(), bank);
        }
        return questionService.findAll(builder.generateSpecification(), getPageRequest());
    }

    /**
     * 进上传试题页面
     * @return
     */
    @RequestMapping(value="/uploadQuestion",method=RequestMethod.GET)
    public String uploadQuestion(Model model){
        List<String> areas=stationDao.findLines(6);
        model.addAttribute("areas",areas);
        return "admin/learn/uploadQuestion";
    }


    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(@RequestParam("fileUpload") MultipartFile file,String questionType,String bankName,String area,String station){
        User user=getUser();
        Station s;
        if (file.isEmpty()) {
            return success("文件为空");
        }else{
            QuestionBank bank=questionBankDao.findByName(bankName);
            if(null==bank){
                bank=new QuestionBank();
                bank.setName(bankName);
                bank.setCreateId(user.getId());
                bank.setCreateTime(new Date());
                if(null!=area&&!area.equals("请选择")){
                    s=stationDao.findByNodeName(area);
                    bank.setStation(s);
                }else{
                    s=stationDao.findByNodeName(user.getStationArea());
                    if(null!=s)
                    bank.setStation(s);
                }
                if(null!=station&&!station.equals("请选择")){
                    s=stationDao.findByNodeName(station);
                    bank.setStation(s);
                }
                questionBankDao.save(bank);
            }

            List<List<List<String>>> data = PoiUtil.readExcelToList(file, 2);
            if (null != data && !data.isEmpty()) {
                String A = null,B = null,C = null,D = null,title=null,imgUrl=null;
                for (List<List<String>> sheet : data) {
                    if (null != sheet && !sheet.isEmpty()) {
                        for (List<String> row : sheet) {
                            if(questionType.equals("判断")){
                                if(row.size()==3) {
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
                                }
                            }else if(questionType.equals("单选")){
                                if(row.size()==6) {
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
                                }
                            }else if(questionType.equals("多选")){
                                if(row.size()>3) {
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
                                    String [] arr=A.split("/");
                                    Proper p = null;
                                    for(int i=0;i<arr.length;i++) {
                                        if(!StringUtil.isBlank(arr[i])) {
                                            p = new Proper();
                                            p.setQuestion(question);
                                            p.setName(arr[i]);
                                            properDao.save(p);
                                        }
                                    }
                                    for (int i = 3; i < row.size(); i++) {
                                        if(!StringUtil.isBlank(row.get(i))) {
                                            p = new Proper();
                                            p.setQuestion(question);
                                            p.setName(row.get(i));
                                            properDao.save(p);
                                        }
                                    }
                                }
                            }else if(questionType.equals("排序")){
                                if(row.size()==3) {
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
                                    Proper p = null;
                                    String[] arr = A.split("/");
                                    for (int i = 0; i < arr.length; i++) {
                                        if(!StringUtil.isBlank(arr[i])) {
                                            p = new Proper();
                                            p.setQuestion(question);
                                            p.setName(arr[i]);
                                            properDao.save(p);
                                        }
                                    }
                                }
                            }
                            A = null;B = null;C = null;D = null;title=null;imgUrl=null;
                        }
                    }
                }
            }
        }
        return JsonResult.success("上传成功！");
    }

    private void panduan(String bankName, QuestionBank bank, String title) {
        String proper;
        int xlength=title.indexOf("（×）");
        int ylength=title.indexOf("（√）");
        if(xlength>0){
            proper="错";
            Question question = new Question();
            question.setTitle(title.substring(0,xlength).replaceAll("[\\u00A0]+", "").trim());
            question.setMenuType("判断");
            question.setProper(proper);
            question.setBankId(bankName);
            question.setQuestionBank(bank);
            questionService.saveOrUpdate(question);
        }else if(ylength>0){
            proper="对";
            Question question = new Question();
            question.setTitle(title.substring(0,ylength).replaceAll("[\\u00A0]+", "").trim());
            question.setMenuType("判断");
            question.setProper(proper);
            question.setBankId(bankName);
            question.setQuestionBank(bank);
            questionService.saveOrUpdate(question);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            questionService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    private class Danxuan {
        private String bankName;
        private QuestionBank bank;
        private String a;
        private String b;
        private String c;
        private String d;
        private String proper;
        private String tit;
        private String title;

        public Danxuan(String bankName, QuestionBank bank, String a, String b, String c, String d, String proper, String tit, String title) {
            this.bankName = bankName;
            this.bank = bank;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.proper = proper;
            this.tit = tit;
            this.title = title;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }

        public String getC() {
            return c;
        }

        public String getD() {
            return d;
        }

        public String getProper() {
            return proper;
        }

        public String getTit() {
            return tit;
        }

        public Danxuan invoke() {
            if(title.indexOf("A.")==0||title.indexOf("B.")==0||title.indexOf("C.")==0||title.indexOf("D.")==0){
                if(title.indexOf("A.")==0){
                    if(title.indexOf("B.")>0){
                        a =title.substring(2,title.indexOf("B.")).replaceAll("[\\u00A0]+", "").trim();
                        if(title.indexOf("C.")>0){
                            b =title.substring(title.indexOf("B.")+2,title.indexOf("C.")).replaceAll("[\\u00A0]+", "").trim();
                            if(title.indexOf("D.")>0){
                                c =title.substring(title.indexOf("C.")+2,title.indexOf("D.")).replaceAll("[\\u00A0]+", "").trim();
                                d =title.substring(title.indexOf("D.")+2).replaceAll("[\\u00A0]+", "").trim();
                            }else{
                                c =title.substring(title.indexOf("C.")+2).replaceAll("[\\u00A0]+", "").trim();
                            }
                        }else{
                            b =title.substring(title.indexOf("B.")+2).replaceAll("[\\u00A0]+", "").trim();
                        }
                    }else{
                        a =title.substring(2).replaceAll("[\\u00A0]+", "").trim();
                    }
                }else if(title.indexOf("B.")==0){
                    if(title.indexOf("C.")>0){
                        b =title.substring(2,title.indexOf("C.")).replaceAll("[\\u00A0]+", "").trim();
                        if(title.indexOf("D.")>0){
                            c =title.substring(title.indexOf("C.")+2,title.indexOf("D.")).replaceAll("[\\u00A0]+", "").trim();
                            d =title.substring(title.indexOf("D.")+2).replaceAll("[\\u00A0]+", "").trim();
                        }else{
                            c =title.substring(title.indexOf("C.")+2).replaceAll("[\\u00A0]+", "").trim();
                        }
                    }else{
                        b =title.substring(2).replaceAll("[\\u00A0]+", "").trim();
                    }
                }else if(title.indexOf("C.")==0){
                    if(title.indexOf("D.")>0){
                        c =title.substring(2,title.indexOf("D.")).replaceAll("[\\u00A0]+", "").trim();
                        d =title.substring(title.indexOf("D.")+2).replaceAll("[\\u00A0]+", "").trim();
                    }else{
                        c =title.substring(2).replaceAll("[\\u00A0]+", "").trim();
                    }
                }else if(title.indexOf("D.")==0){
                    d =title.substring(2).replaceAll("[\\u00A0]+", "").trim();
                }
            }else{

                if(title.indexOf("（A）")>-1){
                    title=title.replace("（A）","（ ）");
                    proper="A";
                }else if(title.indexOf("（B）")>-1){
                    title=title.replace("（B）","（ ）");
                    proper="B";
                }else if(title.indexOf("（C）")>-1){
                    title=title.replace("（C）","（ ）");
                    proper="C";
                }else if(title.indexOf("（D）")>-1){
                    title=title.replace("（D）","（ ）");
                    proper="D";
                }
                tit=title;
            }
            if(null!= a &&null!= b &&null!= c &&null!= d &&null!=proper){
                Question question = new Question();
                question.setTitle(tit.replaceAll("[\\u00A0]+", "").trim());
                question.setMenuType("单选");
                if(proper.equals("A"))
                    question.setProper(a.replaceAll("[\\u00A0]+", "").trim());
                else if(proper.equals("B"))
                    question.setProper(b.replaceAll("[\\u00A0]+", "").trim());
                else if(proper.equals("C"))
                    question.setProper(c.replaceAll("[\\u00A0]+", "").trim());
                else
                    question.setProper(d.replaceAll("[\\u00A0]+", "").trim());
                question.setBankId(bankName);
                question.setQuestionBank(bank);
                questionService.saveOrUpdate(question);
                List<Proper> propers=new ArrayList<>();
                Proper pro = new Proper();
                pro.setName(a);
                pro.setQuestion(question);
                properDao.save(pro);
                propers.add(pro);
                pro = new Proper();
                pro.setName(b);
                pro.setQuestion(question);
                properDao.save(pro);
                propers.add(pro);
                pro = new Proper();
                pro.setName(c);
                pro.setQuestion(question);
                properDao.save(pro);
                propers.add(pro);
                pro = new Proper();
                pro.setName(d);
                pro.setQuestion(question);
                properDao.save(pro);
                propers.add(pro);
                question.setPropers(propers);
                questionService.saveOrUpdate(question);
                a = null;
                b = null;
                c = null;
                d = null;proper = null;tit=null;
            }
            return this;
        }
    }
}
