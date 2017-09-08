package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.FileUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator;
import org.apache.commons.io.FileUtils;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 应急预案管理
 *  fire safety
 * @author lisx
 * @create 2017-08-15 08:47
 **/
@Controller
@RequestMapping("/admin/emergency")
public class EmergencyController  extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EmergencyController.class);
    @Autowired
    private IStationDao stationDao;
    @Autowired
    IBigFileDao fileDao;

    /**
     * 树形菜单
     *
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public JSONArray tree() {
        logger.info("获取tree数据");
        User user = getUser();
        return Station.getZtrees(user, stationDao);
    }


    /**
     * 应急预案首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("获取站点文件全部数据");
        return "admin/emergency/index";
    }

    /**
     * 查询集合
     *
     * @return Page<User>
     */
    @RequestMapping(value = {"/list"})
    @ResponseBody
    public Page<BigFile> list(String folder, String nodeCode) {
        logger.info("list:folder" + folder);
        SimpleSpecificationBuilder<BigFile> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        User user = getUser();
        nodeCode = Station.getQueryNodeCode(nodeCode, user, stationDao);
        if (!StringUtil.isBlank(nodeCode) && !nodeCode.equals("undefined")) {
            builder.add("nodeCode", SpecificationOperator.Operator.likeAll.name(), nodeCode);
            builder.addOr("nodeCode", SpecificationOperator.Operator.eq.name(), "000");
        }
        if (null != folder && !StringUtil.isBlank(folder)) {
            builder.add("folderName", SpecificationOperator.Operator.eq.name(), folder);
        } else {
            builder.add("folderName", SpecificationOperator.Operator.isNull.name(), null);
        }
        builder.add("menuType", SpecificationOperator.Operator.eq.name(), BigConstant.Emergency);
        if (!StringUtil.isBlank(searchText)) {
            builder.add("fileName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<BigFile> bigFilePage = fileDao.findAll(builder.generateSpecification(), getPageRequest());
        return bigFilePage;
    }

    @RequestMapping("/add")
    public String add(String nodeCode, Model map) {
        logger.info("进入应急预案添加文件夹");
        map.addAttribute("nodeCode", nodeCode);
        map.addAttribute("menu", "应急预案");
        return "admin/emergency/form";
    }

    /**
     * 应急预案新增文件夹
     *
     * @return
     */
    @RequestMapping(value = {"/saveFolder"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(BigFile folder, String nodeCode, String menu) {
        logger.info("新增应急预案文件夹nodeCode{},menu{}", nodeCode, menu);
        User user = getUser();
        Station area;
        if (null != nodeCode && !nodeCode.equals("undefined")) {
            area = stationDao.findByNodeCode(nodeCode);
        } else {
            area = stationDao.findByNodeName(user.getStationArea());
            if (null != area)
                nodeCode = area.getNodeCode();
        }
        try {
            folder.setIfFolder(1);
            folder.setIfUse(0);
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
     *
     * @param folder
     * @param map
     * @return
     */
    @RequestMapping("/toFolder")
    public String toFolder(String folder, Model map) {
        logger.info("进入应急预案文件夹folder{}", folder);
        map.addAttribute("folder", folder);
        return "admin/emergency/folder";
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
     *
     * @param map
     * @param folder
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile(Model map, String folder, String nodeCode) {
        System.out.println("++++++" + folder);
        map.addAttribute("folder", folder);
        map.addAttribute("nodeCode", nodeCode);
        return "admin/emergency/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(MultipartHttpServletRequest request, Integer chunk, Integer chunks, Integer size, String folder, String nodeCode, String guid) {
        logger.info("进入培训资料上传文件");
        List<MultipartFile> files = request.getFiles("file");
        User user = getUser();
        MultipartFile file;
        BufferedOutputStream stream;
        for (int i = 0; i < files.size(); ++i) {
            long flag = new Date().getTime();
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String suffix = StringUtil.suffix(file.getOriginalFilename());
                    try {
                        if (null == chunks) {
                            logger.info("不分片的情况");
                            //不分片的情况
                            if (suffix.equals(BigConstant.docx) || suffix.equals(BigConstant.doc) || suffix.equals(BigConstant.xlsx) || suffix.equals(BigConstant.xls) || suffix.equals(BigConstant.ppt) || suffix.equals(BigConstant.pdf)) {
                                BigFile.saveFile(properties.getUpload(),folder, nodeCode, user, file, BigConstant.office, BigConstant.Emergency, flag, fileDao, stationDao);
                            } else if (suffix.equals(BigConstant.png) || suffix.equals(BigConstant.jpeg) || suffix.equals(BigConstant.jpg)) {
                                BigFile.saveFile(properties.getUpload(),folder, nodeCode, user, file, BigConstant.image, BigConstant.Emergency, flag, fileDao, stationDao);
                            } else {
                                BigFile.saveFile(properties.getUpload(),folder, nodeCode, user, file, BigConstant.video, BigConstant.Emergency, flag, fileDao, stationDao);
                            }
                        } else {
                            logger.info("分片的情况");
                            String tempFileDir = properties.getUploadChunk() + guid + "/";
                            String realname = file.getOriginalFilename();
                            // 临时目录用来存放所有分片文件
                            File parentFileDir = new File(tempFileDir + realname + "/");
                            if (!parentFileDir.exists()) {
                                parentFileDir.mkdirs();
                            }
                            // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
                            File tempPartFile = new File(parentFileDir, chunk + "");
                            byte[] bytes = file.getBytes();
                            stream = new BufferedOutputStream(new FileOutputStream(tempPartFile));
                            stream.write(bytes);
                            stream.close();
                            // 是否全部上传完成
                            // 所有分片都存在才说明整个文件上传完成
                            boolean uploadDone = true;
                            for (int c = 0; c < chunks; c++) {
                                File partFile = new File(parentFileDir, c + "");
                                if (!partFile.exists()) {
                                    uploadDone = false;
                                    break;
                                }
                            }
                            // 所有分片文件都上传完成
                            // 将所有分片文件合并到一个文件中
                            logger.info("|||||||" + uploadDone);
                            if (uploadDone) {
                                File[] array = parentFileDir.listFiles();
                                List<Integer> fileNames = new ArrayList<>();
                                for (int a = 0; a < array.length; a++) {
                                    logger.info("arr" + array[a].getName());
                                    fileNames.add(Integer.parseInt(array[a].getName()));
                                }
                                Collections.sort(fileNames);
                                //     得到 destTempFile 就是最终的文件
                                FileUtil.merge(properties.getUpload(), fileNames, realname, guid, flag);
                                // 删除临时目录中的分片文件
                                FileUtils.deleteDirectory(parentFileDir);
                                if (suffix.equals(BigConstant.docx) || suffix.equals(BigConstant.doc) || suffix.equals(BigConstant.xlsx) || suffix.equals(BigConstant.xls) || suffix.equals(BigConstant.ppt) || suffix.equals(BigConstant.pdf)) {
                                    BigFile.saveFile(properties.getUpload(),size, folder, nodeCode, user, file, BigConstant.office, BigConstant.Emergency, flag, fileDao, stationDao);
                                } else if (suffix.equals(BigConstant.png) || suffix.equals(BigConstant.jpeg) || suffix.equals(BigConstant.jpg)) {
                                    BigFile.saveFile(properties.getUpload(),size, folder, nodeCode, user, file, BigConstant.image, BigConstant.Emergency, flag, fileDao, stationDao);
                                } else {
                                    BigFile.saveFile(properties.getUpload(),size, folder, nodeCode, user, file, BigConstant.video, BigConstant.Emergency, flag, fileDao, stationDao);
                                }
                            } else {
                                logger.info("上传中 chunks" + chunks + " chunk:" + chunk, "");
                            }
                        }
                    } catch (Exception e) {
                        logger.info("上传失败{}", e.getMessage());
                    }

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
