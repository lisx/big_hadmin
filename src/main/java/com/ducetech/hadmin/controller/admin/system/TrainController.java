package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.druid.util.StringUtils;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.FileUtil;
import com.ducetech.hadmin.common.utils.PdfUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IFolderDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Folder;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IBigFileService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

/**
 * 培训资料
 *
 * @author lisx
 * @create 2017-08-02 11:07
 **/
@Controller
@RequestMapping("/admin/train")
public class TrainController  extends BaseController {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(TrainController.class);
    @Autowired
    IBigFileService bigFileService;
    @Autowired
    IFolderDao folderDao;

    @RequestMapping("/index")
    public String index() {
        return "admin/learn/index";
    }
    @RequestMapping("/toFolder")
    public String tofolder(String folder,Model map) {
        System.out.println("folder+++"+folder);
        map.addAttribute("folder",folder);
        return "admin/learn/folder";
    }
    /**
     * 查询集合
     * @return Page<User>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<BigFile> list(String folder) {
        System.out.println("list:folder"+folder);
        SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        builder.add("folder", SpecificationOperator.Operator.likeAll.name(), folder);
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return bigFileService.findAll(builder.generateSpecification(), getPageRequest());
    }
    @RequestMapping(value = { "/folder" })
    @ResponseBody
    public Page<Folder> folder() {
        SimpleSpecificationBuilder<Folder> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if(!StringUtil.isBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        return folderDao.findAll(builder.generateSpecification(), getPageRequest());
    }
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(Model map,String folder) {
        System.out.println("++++++"+folder);
        map.addAttribute("folder",folder);
        return "admin/learn/uploadTrain";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "admin/learn/form";
    }
    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request,String chunk,String chunks,String size,String folder){
        List<MultipartFile> files =request.getFiles("file");
        User user=getUser();
        MultipartFile file;
        //创建临时文件夹
        File dirTempFile = new File(BigConstant.TRAIN_PATH);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();

        }
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            String type = null;
            String filePath=BigConstant.TRAIN_PATH+file.getOriginalFilename();
            if (!file.isEmpty()) {
                try {

                    String suffix=StringUtil.suffix(filePath);
                    if(suffix.equals(BigConstant.docx)||suffix.equals(BigConstant.doc)||suffix.equals(BigConstant.xlsx)||suffix.equals(BigConstant.xls)||suffix.equals(BigConstant.ppt)) {
                        filePath=BigConstant.TRAIN_OFFICE_PATH+file.getOriginalFilename();
                        byte[] bytes = file.getBytes();
                        stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                        stream.write(bytes);
                        stream.close();
                        PdfUtil.office2PDF(filePath, filePath + BigConstant.pdf);
                        filePath=BigConstant.getTrainOfficePathUrl(file.getOriginalFilename()+BigConstant.pdf);
                        type="office";
                        BigFile bf=new BigFile();
                        bf.setFileSize(""+Math.round(file.getSize()/1024));
                        bf.setMenuType("3");
                        bf.setFileType(type);
                        bf.setFileName(file.getOriginalFilename());
                        bf.setCreateTime(new Date());
                        bf.setFolder(folder);
                        bf.setFileUrl(filePath);
                        bf.setCreateId(user.getId());
                        bf.setStation(user.getStation());
                        bigFileService.saveOrUpdate(bf);
                    }else if(suffix.equals(BigConstant.png)||suffix.equals(BigConstant.jpeg)||suffix.equals(BigConstant.jpg)){
                        filePath=BigConstant.TRAIN_IMAGE_PATH+file.getOriginalFilename();
                        byte[] bytes = file.getBytes();
                        stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                        stream.write(bytes);
                        stream.close();
                        type="image";
                        filePath=BigConstant.getTrainImagePathUrl(file.getOriginalFilename());
                        BigFile bf=new BigFile();
                        bf.setFileSize(""+Math.round(file.getSize()/1024));
                        bf.setMenuType("3");
                        bf.setFileType(type);
                        bf.setFileName(file.getOriginalFilename());
                        bf.setCreateTime(new Date());
                        bf.setFolder(folder);
                        bf.setFileUrl(filePath);
                        bf.setCreateId(user.getId());
                        bf.setStation(user.getStation());
                        bigFileService.saveOrUpdate(bf);
                    }else{
                        try {
                            byte[] bytes = file.getBytes();
                            stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                            stream.write(bytes);
                            stream.close();
                            //拿到文件对象
                            //第一个参数是目标文件的完整路径
                            //第二参数是webupload分片传过来的文件
                            //FileUtil的这个方法是把目标文件的指针，移到文件末尾，然后把分片文件追加进去，实现文件合并。简单说。就是每次最新的分片合到一个文件里面去。
                            FileUtil.randomAccessFile(BigConstant.TRAIN_VIDEO_PATH+file.getOriginalFilename(), file);
                            //如果文件小与5M的话，分片参数chunk的值是null
                            //5M的这个阈值是在upload3.js中的chunkSize属性决定的，超过chunkSize设置的大小才会进行分片，否则就不分片，不分片的话，webupload传到后台的chunk参数值就是null
                            if(StringUtils.isEmpty(chunk)){
                                //不分片的情况
                                type="video";
                                filePath=BigConstant.getTrainVideoPathUrl(file.getOriginalFilename());
                                BigFile bf=new BigFile();
                                bf.setFileSize(""+Math.round(Integer.parseInt(size)/1024/1024));
                                bf.setMenuType("3");
                                bf.setFileType(type);
                                bf.setFileName(file.getOriginalFilename());
                                bf.setCreateTime(new Date());
                                bf.setFolder(folder);
                                bf.setFileUrl(filePath);
                                bf.setCreateId(user.getId());
                                bf.setStation(user.getStation());
                                bigFileService.saveOrUpdate(bf);
                                logger.debug("success");
                            }else{
                                //分片的情况
                                //chunk 分片索引，下标从0开始
                                //chunks 总分片数
                                if (Integer.valueOf(chunk) == (Integer.valueOf(chunks) - 1)) {
                                    type="video";
                                    filePath=BigConstant.getTrainVideoPathUrl(file.getOriginalFilename());
                                    logger.debug("上传成功");
                                    BigFile bf=new BigFile();
                                    bf.setFileSize(""+Math.round(Integer.parseInt(size)/1024/1024));
                                    bf.setMenuType("3");
                                    bf.setFileType(type);
                                    bf.setFileName(file.getOriginalFilename());
                                    bf.setCreateTime(new Date());
                                    bf.setFolder(folder);
                                    bf.setFileUrl(filePath);
                                    bf.setCreateId(user.getId());
                                    bf.setStation(user.getStation());
                                    bigFileService.saveOrUpdate(bf);
                                } else {
                                    logger.debug("上传中" + file.getOriginalFilename() + " chunk:" + chunk, "");
                                }
                            }
                        } catch (Exception e) {
                            logger.debug("上传失败{}",e.getMessage());
                        }
                    }

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

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            bigFileService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
