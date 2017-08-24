package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.FileUtil;
import com.ducetech.hadmin.common.utils.PdfUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.slf4j.Logger;
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
 * 消防安全管理
 *  fire safety
 * @author lisx
 * @create 2017-08-15 08:47
 **/
@Controller
@RequestMapping("/admin/fire")
public class FireSafetyController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(FireSafetyController.class);
    @Autowired
    private IStationDao stationDao;
    @Autowired
    IBigFileDao fileDao;
    /**
     * 树形菜单
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public JSONArray tree(){
        logger.info("获取tree数据");
        User user=getUser();
        return Station.getZtrees(user,stationDao);
    }
    /**
     * 查询集合
     * @return Page<BigFile>
     */
    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<BigFile> list(String folder,String nodeCode) {
        User user=getUser();
        SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        nodeCode = Station.getQueryNodeCode(nodeCode, user,stationDao);
        if (!StringUtil.isBlank(nodeCode)&&!nodeCode.equals("undefined")) {
            builder.add("nodeCode", SpecificationOperator.Operator.likeAll.name(), nodeCode);
            builder.addOr("nodeCode", SpecificationOperator.Operator.eq.name(), BigConstant.ADMINCODE);
        }
        if(null!=folder&&!StringUtil.isBlank(folder)) {
            builder.add("folderName", SpecificationOperator.Operator.eq.name(), folder);
        }else {
            builder.add("folderName", SpecificationOperator.Operator.isNull.name(),null);
        }
        builder.add("menuType", SpecificationOperator.Operator.eq.name(), BigConstant.Fire);
        if(!StringUtil.isBlank(searchText)){
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<BigFile> bigFilePage=fileDao.findAll(builder.generateSpecification(), getPageRequest());
        return bigFilePage;
    }
    /**
     * 消防安全首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("获取站点文件全部数据");
        return "admin/fire/index";
    }

    @RequestMapping("/add")
    public String add(String nodeCode,Model map) {
        logger.info("进入消防安全添加文件夹");
        map.addAttribute("nodeCode",nodeCode);
        map.addAttribute("menu","消防安全");
        return "admin/fire/form";
    }

    /**
     * 消防安全新增文件夹
     * @return
     */
    @RequestMapping(value= {"/saveFolder"} ,method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(BigFile folder,String nodeCode,String menu){
        logger.info("新增消防安全文件夹nodeCode{},menu{}",nodeCode,menu);
        User user=getUser();
        Station area;
        if(null!=nodeCode&&!nodeCode.equals("undefined")){
            area=stationDao.findByNodeCode(nodeCode);
        }else{
            area=stationDao.findByNodeName(user.getStationArea());
            if(null!=area)
            nodeCode=area.getNodeCode();
        }
        try {
            folder.setIfFolder(1);
            folder.setCreateTime(new Date());
            folder.setCreateId(user.getId());
            folder.setStationFile(area);
            folder.setNodeCode(nodeCode);
            folder.setMenuType(menu);
            fileDao.saveAndFlush(folder);
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    /**
     * 进入文件夹
     * @param folder
     * @param map
     * @return
     */
    @RequestMapping("/toFolder")
    public String toFolder(String folder,Model map) {
        logger.info("进入消防安全文件夹folder{}",folder);
        map.addAttribute("folder",folder);
        return "admin/fire/folder";
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            fileDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
    /**
     * 进入培训上传页面
     * @param map
     * @param folder
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(Model map,String folder,String nodeCode) {
        System.out.println("++++++"+folder);
        map.addAttribute("folder",folder);
        map.addAttribute("nodeCode",nodeCode);
        return "admin/fire/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, String chunk, String chunks, String size, String folder,String nodeCode){
        logger.info("进入消防安全上传文件");
        List<MultipartFile> files =request.getFiles("file");
        User user=getUser();
        MultipartFile file;
        //创建临时文件夹
        File dirTempFile = new File(BigConstant.upload);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            String type = null;
            String filePath=BigConstant.upload+file.getOriginalFilename();
            if (!file.isEmpty()) {
                try {
                    String suffix=StringUtil.suffix(filePath);
                    if(suffix.equals(BigConstant.docx)||suffix.equals(BigConstant.doc)||suffix.equals(BigConstant.xlsx)||suffix.equals(BigConstant.xls)||suffix.equals(BigConstant.ppt)) {
                        filePath=BigConstant.upload+file.getOriginalFilename();
                        byte[] bytes = file.getBytes();
                        stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                        stream.write(bytes);
                        stream.close();
                        PdfUtil.office2PDF(filePath, filePath + BigConstant.pdf);
                        type="office";
                        BigFile bf=new BigFile();
                        bf.setFileSize(""+Math.round(file.getSize()/1024));
                        bf.setMenuType("消防安全");
                        bf.setFileType(type);
                        bf.setFileName(file.getOriginalFilename());
                        bf.setFileUrl(filePath);
                        stationFolder(folder, nodeCode, bf,user);
                        fileDao.saveAndFlush(bf);
                    }else if(suffix.equals(BigConstant.png)||suffix.equals(BigConstant.jpeg)||suffix.equals(BigConstant.jpg)){
                        byte[] bytes = file.getBytes();
                        stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                        stream.write(bytes);
                        stream.close();
                        type="image";
                        BigFile bf=new BigFile();
                        bf.setFileSize(""+Math.round(file.getSize()/1024));
                        bf.setMenuType("消防安全");
                        bf.setFileType(type);
                        bf.setFileName(file.getOriginalFilename());
                        bf.setFileUrl(filePath);
                        stationFolder(folder, nodeCode, bf,user);
                        fileDao.saveAndFlush(bf);
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
                            FileUtil.randomAccessFile(BigConstant.upload+file.getOriginalFilename(), file);
                            //如果文件小与5M的话，分片参数chunk的值是null
                            //5M的这个阈值是在upload3.js中的chunkSize属性决定的，超过chunkSize设置的大小才会进行分片，否则就不分片，不分片的话，webupload传到后台的chunk参数值就是null
                            if(StringUtils.isEmpty(chunk)) {
                                //不分片的情况
                                type = "video";
                                BigFile bf = new BigFile();
                                bf.setFileSize("" + Math.round(Integer.parseInt(size) / 1024));
                                bf.setMenuType("消防安全");
                                bf.setFileType(type);
                                bf.setFileName(file.getOriginalFilename());
                                bf.setFileUrl(filePath);
                                stationFolder(folder, nodeCode, bf,user);
                                fileDao.saveAndFlush(bf);
                                logger.info("success");
                            }else{
                                //分片的情况
                                //chunk 分片索引，下标从0开始
                                //chunks 总分片数
                                if (Integer.valueOf(chunk) == (Integer.valueOf(chunks) - 1)) {
                                    type="video";
                                    logger.info("上传成功");
                                    BigFile bf=new BigFile();
                                    bf.setFileSize(""+Math.round(Integer.parseInt(size)/1024));
                                    bf.setMenuType("消防安全");
                                    bf.setFileType(type);
                                    bf.setFileName(file.getOriginalFilename());
                                    bf.setFolderName(folder);
                                    bf.setFileUrl(filePath);
                                    stationFolder(folder, nodeCode, bf,user);
                                    fileDao.saveAndFlush(bf);
                                } else {
                                    logger.info("上传中" + file.getOriginalFilename() + " chunk:" + chunk, "");
                                }
                            }
                        } catch (Exception e) {
                            logger.info("上传失败{}",e.getMessage());
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

    private void stationFolder(String folder, String nodeCode, BigFile bf,User user) {
        if(null==folder) {
            Station area;
            if(null!=nodeCode&&!nodeCode.equals("undefined")){
                area=stationDao.findByNodeCode(nodeCode);
            }else{
                area=stationDao.findByNodeName(user.getStationArea());
            }
            if (null != area) {
                nodeCode = area.getNodeCode();
                bf.setNodeCode(nodeCode);
                bf.setStationFile(area);
            }else{
                bf.setNodeCode("0");
            }
        }else{
            bf.setFolderName(folder);
            BigFile folder1=fileDao.findByFileName(folder);
            bf.setFolderFile(folder1);
            Station station = folder1.getStationFile();
            if (null != station){
                bf.setStationFile(station);
                bf.setNodeCode(station.getNodeCode());
            }
        }
        bf.setCreateTime(new Date());
        bf.setCreateId(user.getId());
    }
}
