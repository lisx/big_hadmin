package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.PdfUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IBigFileService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 培训资料
 *
 * @author lisx
 * @create 2017-08-02 11:07
 **/
@Controller
@RequestMapping("/admin/train")
public class TrainController  extends BaseController {
    @Autowired
    IBigFileService bigFileService;

    @RequestMapping("/index")
    public String index() {
        return "admin/learn/index";
    }

    /**
     * 查询集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<BigFile> list() {
        SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return bigFileService.findAll(builder.generateSpecification(), getPageRequest());
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile() {
        return "admin/learn/uploadTrain";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "admin/learn/form";
    }
    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request){
        System.out.println((String) request.getAttribute("uid"));
        List<MultipartFile> files =request.getFiles("file");
        User user=getUser();
        MultipartFile file;
        //创建临时文件夹
        File dirTempFile = new File(BigConstant.TRAIN_PATH);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();

        }
        BufferedOutputStream stream;
        String docx=".docx";
        String doc=".doc";
        String xlsx=".xlsx";
        String xls=".xls";
        String ppt=".ppt";
        String pdf=".pdf";
        String jpeg=".jepg";
        String jpg=".jpg";
        String png=".png";
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            String type = null;
            String filePath=BigConstant.TRAIN_PATH+file.getOriginalFilename();
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                    stream.write(bytes);
                    stream.close();
                    String suffix=StringUtil.suffix(filePath);
                    if(suffix.equals(docx)||suffix.equals(doc)||suffix.equals(xlsx)||suffix.equals(xls)||suffix.equals(ppt)) {
                        PdfUtil.office2PDF(filePath, filePath + pdf);
                        filePath = filePath+pdf;
                        type="office";
                    }else if(suffix.equals(png)||suffix.equals(jpeg)||suffix.equals(jpg)){
                        type="image";
                    }else{
                        type="video";
                    }
                    BigFile bf=new BigFile();
                    bf.setMenuType("3");
                    bf.setFileType(type);
                    bf.setFileName(file.getOriginalFilename());
                    bf.setFileSize(""+Math.round(file.getSize()/1024));
                    bf.setCreateTime(new Date());
                    bf.setFileUrl(filePath);
                    bf.setCreateId(user.getId()+"");
                    bigFileService.saveOrUpdate(bf);
                } catch (Exception e) {
                    //stream =  null;
                    return JsonResult.success("You failed to upload " + i + " =>" + e.getMessage());
                }
            } else {
                return JsonResult.success("You failed to upload " + i + " becausethe file was empty.");
            }
        }
        return JsonResult.success();
    }


}
