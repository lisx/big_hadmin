package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.PoiUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IProperDao;
import com.ducetech.hadmin.dao.IQuestionBankDao;
import com.ducetech.hadmin.entity.Proper;
import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.entity.QuestionBank;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IQuestionService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public JsonResult uploadFilePost(@RequestParam("fileUpload") MultipartFile file,String questionType,String bankName){
        User user=getUser();
        if (file.isEmpty()) {
            return success("文件为空");
        }else{
            QuestionBank bank=questionBankDao.findByName(bankName);
            if(null==bank){
                bank=new QuestionBank();
                bank.setName(bankName);
                bank.setCreateId(user.getId());
                bank.setCreateTime(new Date());
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
                                    question.setTitle(title.substring(0,xlength).trim());
                                    question.setMenuType("判断");
                                    question.setProper(proper);
                                    question.setBankId(bankName);
                                    question.setQuestionBank(bank);
                                    questionService.saveOrUpdate(question);
                                }else if(ylength>0){
                                    proper="对";
                                    Question question = new Question();
                                    question.setTitle(title.substring(0,ylength).trim());
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
                                            A=title.substring(2,title.indexOf("B.")).trim();
                                            if(title.indexOf("C.")>0){
                                                B=title.substring(title.indexOf("B.")+2,title.indexOf("C.")).trim();
                                                if(title.indexOf("D.")>0){
                                                    C=title.substring(title.indexOf("C.")+2,title.indexOf("D.")).trim();
                                                    D=title.substring(title.indexOf("D.")+2).trim();
                                                }else{
                                                    C=title.substring(title.indexOf("C.")+2).trim();
                                                }
                                            }else{
                                                B=title.substring(title.indexOf("B.")+2).trim();
                                            }
                                        }else{
                                            A=title.substring(2).trim();
                                        }
                                    }else if(title.indexOf("B.")==0){
                                        if(title.indexOf("C.")>0){
                                            B=title.substring(2,title.indexOf("C.")).trim();
                                            if(title.indexOf("D.")>0){
                                                C=title.substring(title.indexOf("C.")+2,title.indexOf("D.")).trim();
                                                D=title.substring(title.indexOf("D.")+2).trim();
                                            }else{
                                                C=title.substring(title.indexOf("C.")+2).trim();
                                            }
                                        }else{
                                            B=title.substring(2).trim();
                                        }
                                    }else if(title.indexOf("C.")==0){
                                        if(title.indexOf("D.")>0){
                                            C=title.substring(2,title.indexOf("D.")).trim();
                                            D=title.substring(title.indexOf("D.")+2).trim();
                                        }else{
                                            C=title.substring(2).trim();
                                        }
                                    }else if(title.indexOf("D.")==0){
                                        D=title.substring(2).trim();
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
                                    question.setTitle(tit.trim());
                                    question.setMenuType("单选");
                                    if(proper.equals("A"))
                                        question.setProper(A.trim());
                                    else if(proper.equals("B"))
                                        question.setProper(B.trim());
                                    else if(proper.equals("C"))
                                        question.setProper(C.trim());
                                    else
                                        question.setProper(D.trim());
                                    question.setBankId(bankName);
                                    question.setQuestionBank(bank);
                                    questionService.saveOrUpdate(question);
                                    Proper pro = new Proper();
                                    pro.setName(A);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
                                    pro = new Proper();
                                    pro.setName(B);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
                                    pro = new Proper();
                                    pro.setName(C);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
                                    pro = new Proper();
                                    pro.setName(D);
                                    pro.setQuestion(question);
                                    properDao.save(pro);
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

}
