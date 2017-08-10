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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        logger.debug("进入bank");
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
    public String uploadQuestion(){
        return "admin/learn/uploadQuestion";
    }


    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(@RequestParam("fileUpload") MultipartFile file,String questionType,String bankName,String area,String station){
        User user=getUser();
        Station s=null;
        if(null!=station){
            s=stationDao.findByNodeName(station);
        }else if(null!=area){
            s=stationDao.findByNodeName(area);
        }

        if (file.isEmpty()) {
            return success("文件为空");
        }else{
            QuestionBank bank=questionBankDao.findByName(bankName);
            if(null==bank){
                bank=new QuestionBank();
                bank.setName(bankName);
                bank.setCreateId(user.getId());
                bank.setCreateTime(new Date());
                bank.setStation(s);
                questionBankDao.save(bank);
            }
            List<List<List<String>>> data = PoiUtil.readExcelToList(file, 1);
            if (null != data && !data.isEmpty()) {
                String A = null,B = null,C = null,D = null,proper = null,tit=null;
                for (List<List<String>> sheet : data) {
                    if (null != sheet && !sheet.isEmpty()) {
                        for (List<String> row : sheet) {
                            if(questionType.equals("判断")){
                                String title = StringUtil.trim(row.get(0));
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
                            }else if(questionType.equals("单选")){
                                String title = StringUtil.trim(row.get(0));

                                if(title.indexOf("A.")==0||title.indexOf("B.")==0||title.indexOf("C.")==0||title.indexOf("D.")==0){
                                    if(title.indexOf("A.")==0){
                                        if(title.indexOf("B.")>0){
                                            A=title.substring(2,title.indexOf("B.")).replaceAll("[\\u00A0]+", "").trim();
                                            if(title.indexOf("C.")>0){
                                                B=title.substring(title.indexOf("B.")+2,title.indexOf("C.")).replaceAll("[\\u00A0]+", "").trim();
                                                if(title.indexOf("D.")>0){
                                                    C=title.substring(title.indexOf("C.")+2,title.indexOf("D.")).replaceAll("[\\u00A0]+", "").trim();
                                                    D=title.substring(title.indexOf("D.")+2).replaceAll("[\\u00A0]+", "").trim();
                                                }else{
                                                    C=title.substring(title.indexOf("C.")+2).replaceAll("[\\u00A0]+", "").trim();
                                                }
                                            }else{
                                                B=title.substring(title.indexOf("B.")+2).replaceAll("[\\u00A0]+", "").trim();
                                            }
                                        }else{
                                            A=title.substring(2).replaceAll("[\\u00A0]+", "").trim();
                                        }
                                    }else if(title.indexOf("B.")==0){
                                        if(title.indexOf("C.")>0){
                                            B=title.substring(2,title.indexOf("C.")).replaceAll("[\\u00A0]+", "").trim();
                                            if(title.indexOf("D.")>0){
                                                C=title.substring(title.indexOf("C.")+2,title.indexOf("D.")).replaceAll("[\\u00A0]+", "").trim();
                                                D=title.substring(title.indexOf("D.")+2).replaceAll("[\\u00A0]+", "").trim();
                                            }else{
                                                C=title.substring(title.indexOf("C.")+2).replaceAll("[\\u00A0]+", "").trim();
                                            }
                                        }else{
                                            B=title.substring(2).replaceAll("[\\u00A0]+", "").trim();
                                        }
                                    }else if(title.indexOf("C.")==0){
                                        if(title.indexOf("D.")>0){
                                            C=title.substring(2,title.indexOf("D.")).replaceAll("[\\u00A0]+", "").trim();
                                            D=title.substring(title.indexOf("D.")+2).replaceAll("[\\u00A0]+", "").trim();
                                        }else{
                                            C=title.substring(2).replaceAll("[\\u00A0]+", "").trim();
                                        }
                                    }else if(title.indexOf("D.")==0){
                                        D=title.substring(2).replaceAll("[\\u00A0]+", "").trim();
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
                                if(null!=A&&null!=B&&null!=C&&null!=D&&null!=proper){
                                    Question question = new Question();
                                    question.setTitle(tit.replaceAll("[\\u00A0]+", "").trim());
                                    question.setMenuType("单选");
                                    if(proper.equals("A"))
                                        question.setProper(A.replaceAll("[\\u00A0]+", "").trim());
                                    else if(proper.equals("B"))
                                        question.setProper(B.replaceAll("[\\u00A0]+", "").trim());
                                    else if(proper.equals("C"))
                                        question.setProper(C.replaceAll("[\\u00A0]+", "").trim());
                                    else
                                        question.setProper(D.replaceAll("[\\u00A0]+", "").trim());
                                    question.setBankId(bankName);
                                    question.setQuestionBank(bank);
                                    questionService.saveOrUpdate(question);
                                    List<Proper> propers=new ArrayList<>();
                                    Proper pro = new Proper();
                                    pro.setName(A);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
                                    propers.add(pro);
                                    pro = new Proper();
                                    pro.setName(B);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
                                    propers.add(pro);
                                    pro = new Proper();
                                    pro.setName(C);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
                                    propers.add(pro);
                                    pro = new Proper();
                                    pro.setName(D);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
                                    propers.add(pro);
                                    question.setPropers(propers);
                                    questionService.saveOrUpdate(question);
                                    A = null;B = null;C = null;D = null;proper = null;tit=null;
                                }
                            }
                        }
                    }
                }
            }
        }
        return JsonResult.success("上传成功！");
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
}
