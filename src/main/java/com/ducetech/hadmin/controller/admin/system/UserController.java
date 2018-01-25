package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.*;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.dao.IUserDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Role;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IRoleService;
import com.ducetech.hadmin.service.IUserService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author lisx
 * @deprecated 用户管理
 **/
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    IUserService userService;
    @Autowired
    IUserDao userDao;
    @Autowired
    IRoleService roleService;
    @Autowired
    IBigFileDao fileDao;
    @Autowired
    IStationDao stationDao;
    @Autowired
    DucetechProperties properties;

    /**
     * 用户管理初始化页面
     *
     * @return string
     */
    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "admin/user/index";
    }

    /**
     * 获取用户站区
     *
     * @return
     */
    @RequestMapping(value = {"/getUserArea"})
    @ResponseBody
    public String getUserArea() {
        return getUser().getStationArea();
    }

    /**
     * 查询集合
     *
     * @return Page<User>
     */
    @RequestMapping(value = {"/list"})
    @ResponseBody
    public Page<User> list(String nodeCode, Model model) {
        SimpleSpecificationBuilder<User> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if (!StringUtil.isBlank(searchText)) {
            builder.add("userName", Operator.eq.name(), searchText);
            builder.addOr("userCode", Operator.eq.name(), searchText);
            builder.addOr("line", Operator.eq.name(), searchText);
        }
        if(getUser().getRoles().size()>0){
            Set<Role> roles =getUser().getRoles();
            for(Role role:roles)
            {
                if(role.getName().equals("管理员")){
                    if (!StringUtil.isBlank(nodeCode)) {
                        if(nodeCode.equals("运三分公司")){
                            builder.add("stationArea", Operator.eq.name(), nodeCode);
                            builder.addOr("stationArea", Operator.eq.name(), "");
                            builder.addOr("station", Operator.eq.name(), nodeCode);
                            builder.addOr("line", Operator.eq.name(), nodeCode);
                        }else{
                            builder.add("stationArea", Operator.eq.name(), nodeCode);
                            builder.addOr("station", Operator.eq.name(), nodeCode);
                            builder.addOr("line", Operator.eq.name(), nodeCode);
                        }

                    }
                }else{
                    if (!StringUtil.isBlank(nodeCode)) {
                        builder.add("stationArea", Operator.eq.name(), nodeCode);
                        builder.addOr("station", Operator.eq.name(), nodeCode);
                        builder.addOr("line", Operator.eq.name(), nodeCode);
                    } else {
                        nodeCode = getUser().getStationArea();
                        builder.add("stationArea", Operator.eq.name(), nodeCode);
                    }
                }
            }
        }

        model.addAttribute("nodeCode", nodeCode);
        builder.add("ifUse", Operator.eq.name(), 0);
        logger.info("查询人员首页");
        return userDao.findAll(builder.generateSpecification(), getPageRequest());
    }

    /**
     * 添加用户
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        List<String> lines = stationDao.findLines(6);
        model.addAttribute("lines", lines);
        return "admin/user/add";
    }

    /**
     * 保存用户
     *
     * @param user
     * @param photoFile
     * @param fwxxkFile
     * @param zkysgzFile
     * @param faszFile
     * @return
     */
    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult save(User user, @RequestParam("photoFile") MultipartFile photoFile, @RequestParam("fwxxkFile") MultipartFile fwxxkFile, @RequestParam("zkysgzFile") MultipartFile zkysgzFile, @RequestParam("faszFile") MultipartFile faszFile) {
        User selectUser = userDao.findByUserCode(user.getUserCode());
        if (null != selectUser) {
            return JsonResult.failure("该工号已存在");
        }
        JsonResult x = getSaveAndUpdateUser(user, photoFile, fwxxkFile, zkysgzFile, faszFile);
        if (x != null) return x;
        return JsonResult.success();
    }

    private JsonResult getSaveAndUpdateUser(User user, @RequestParam("photoFile") MultipartFile photoFile, @RequestParam("fwxxkFile") MultipartFile fwxxkFile, @RequestParam("zkysgzFile") MultipartFile zkysgzFile, @RequestParam("faszFile") MultipartFile faszFile) {
        try {
            if (null == user.getStationArea() || user.getStationArea().equals("请选择")) {
                user.setStationArea("");
            }
            if (null == user.getStation() || user.getStation().equals("请选择")) {
                user.setStation("");
            }
            if (!faszFile.isEmpty()) {
                String fasz = user.getFaszUrl();
                String path = properties.getUpload() + fasz + BigConstant.jpg;
                if (StringUtil.isBlank(fasz)) {
                    return JsonResult.failure("FAS证未填");
                }
                File dest = new File(path);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                BufferedOutputStream stream;
                byte[] bytes = faszFile.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream(dest));
                stream.write(bytes);
                stream.close();
                BigFile file = new BigFile();
                file.setIfUse(0);
                file.setFileName(fasz + BigConstant.jpg);
                file.setMenuType(BigConstant.User);
                file.setFileUrl(path);
                fileDao.save(file);
            }
            if (!zkysgzFile.isEmpty()) {
                String zkysgz = user.getZkysgzUrl();
                String path = properties.getUpload() + zkysgz + BigConstant.jpg;
                if (StringUtil.isBlank(zkysgz)) {
                    return JsonResult.failure("综控员上岗证未填");
                }
                File dest = new File(path);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                BufferedOutputStream stream;
                byte[] bytes = zkysgzFile.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream(dest));
                stream.write(bytes);
                stream.close();
                BigFile file = new BigFile();
                file.setIfUse(0);
                file.setFileName(zkysgz + BigConstant.jpg);
                file.setMenuType(BigConstant.User);
                file.setFileUrl(path);
                fileDao.save(file);
            }
            if (!fwxxkFile.isEmpty()) {
                String fwxxk = user.getFwxxkUrl();
                String path = properties.getUpload() + fwxxk + BigConstant.jpg;
                if (StringUtil.isBlank(fwxxk)) {
                    fwxxk="fw" + user.getUserCode();
                    path = properties.getUpload() + fwxxk;
                    user.setFwxxkUrl(fwxxk);
                }
                File dest = new File(path);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                BufferedOutputStream stream;
                byte[] bytes = fwxxkFile.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream(dest));
                stream.write(bytes);
                stream.close();
                BigFile file = new BigFile();
                file.setIfUse(0);
                file.setFileName(fwxxk + BigConstant.jpg);
                file.setMenuType(BigConstant.User);
                file.setFileUrl(path);
                fileDao.save(file);
            }
            if (!photoFile.isEmpty()) {
                String path = properties.getUpload() + user.getUserCode() + BigConstant.jpg;
                File dest = new File(path);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                BufferedOutputStream stream;
                byte[] bytes = photoFile.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream(dest));
                stream.write(bytes);
                stream.close();
                BigFile file = new BigFile();
                file.setIfUse(0);
                file.setFileName(user.getUserCode() + BigConstant.jpg);
                file.setMenuType(BigConstant.User);
                file.setFileUrl(path);
                fileDao.save(file);
            }
            user.setPhotoUrl(user.getUserCode());
            user.setIfUse(0);
            user.setCreateTime(new Date());
            user.setCreateId(getUser().getId());
            userService.saveOrUpdate(user);
            // 插入缓存
        } catch (Exception e) {
            return JsonResult.failure("数据异常");
        }
        return null;
    }

    /**
     * 上传用户页面
     *
     * @return
     */
    @RequestMapping(value = "/uploadUser", method = RequestMethod.GET)
    public String uploadUser() {
        return "admin/user/uploadUser";
    }

    /**
     * 保存上传用户
     *
     * @param fileUpload
     * @return
     */
    @RequestMapping(value = "/fileUploadUser", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadUserExcel(@RequestParam("fileUpload") MultipartFile fileUpload) {
        logger.debug("进入上传用户||||||||||||||||||||||");
        try {
            if (fileUpload != null && !fileUpload.isEmpty()) {
                List<List<List<String>>> data = PoiUtil.readExcelToList(fileUpload, 2);
                if (null != data && !data.isEmpty()) {
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            int i=0;
                            for (List<String> row : sheet) {
                                i++;
                                if (row.size() > 8) {
                                    String line = StringUtil.trim(row.get(0));
                                    String stationArea = StringUtil.trim(row.get(1));
                                    String station = StringUtil.trim(row.get(2));
                                    String position = StringUtil.trim(row.get(3));
                                    String userName = StringUtil.trim(row.get(4));
                                    String userCode = StringUtil.trim(row.get(5));
                                    String fwxxkUrl = StringUtil.trim(row.get(6));
                                    String faszUrl = StringUtil.trim(row.get(7));
                                    String zkysgzUrl = StringUtil.trim(row.get(8));
                                    User user = userDao.findByUserCodeOne(userCode);
                                    if (null == user) {
                                        user = new User();
                                    }else{
                                        System.out.println("已存在用户"+i);
                                        continue;
                                    }
                                    if (stationArea.endsWith("站区")) {

                                    } else {
                                        stationArea = stationArea + "站区";
                                    }

                                    Station stationAreaEntity=stationDao.findByNodeName(stationArea);
                                    if(null==stationAreaEntity){
                                        System.out.println("有问题"+i);
                                        continue;
                                    }
                                    if (station.endsWith("站")) {

                                    } else {
                                        station = station + "站";
                                    }
                                    if (station.equals("西直门站") || station.equals("东直门站") || station.equals("建国门站") || station.equals("北土城站") || station.equals("慈寿寺站")) {
                                        station = line + station;
                                    }
                                    Station stationEntity=stationDao.findByNodeName(station);
                                    if(null==stationEntity){
                                        System.out.println("有问题"+i);
                                        continue;
                                    }

                                    user.setUserName(userName);
                                    user.setStationArea(stationArea);
                                    user.setStation(station);
                                    user.setLine(line);
                                    user.setPosition(position);
                                    if (userCode.endsWith(".0")) {
                                        userCode = userCode.substring(0, userCode.length() - 2);
                                    }
                                    user.setUserCode(userCode);
                                    user.setPhotoUrl(userCode);
                                    user.setFwxxkUrl(fwxxkUrl);
                                    user.setZkysgzUrl(zkysgzUrl);
                                    user.setFaszUrl(faszUrl);
                                    user.setCreateTime(new Date());
                                    user.setIfUse(0);
                                    userService.saveOrUpdate(user);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
//            logger.debug(e.getMessage());
            return JsonResult.failure("上传失败！");
        }
        return JsonResult.success("上传成功！");
    }

    /**
     * 上传用户图片
     *
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile() {
        return "admin/user/uploadFile";
    }

    /**
     * 保存上传用户图片
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(HttpServletRequest request) {
        logger.info("进入用户上传图片方法");
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String rotate=request.getParameter("rotation");
        MultipartFile file;
        BufferedOutputStream stream;
        for (MultipartFile file1 : files) {
            file = file1;
            if (!file.isEmpty()) {
                BigFile bf = fileDao.findByFileName(file.getOriginalFilename());
                if (null == bf)
                    bf = new BigFile();
                try {
                    byte[] bytes = file.getBytes();
                    String path = properties.getUpload() + file.getOriginalFilename();
                    File f = new File(path);
                    if(!StringUtil.isBlank(rotate)) {
                        String rotatePath = properties.getUpload() +"rotate"+ file.getOriginalFilename();
                        f = new File(rotatePath);
                        file.transferTo(f);
                        ImageUtil.rotate(f, Integer.parseInt(rotate),path);
                    }else {
                        stream = new BufferedOutputStream(new FileOutputStream(f));
                        stream.write(bytes);
                        stream.close();
                    }
                    bf.setFileSize("" + Math.round(file.getSize() / 1024));
                    bf.setMenuType(BigConstant.User);
                    bf.setFileType(BigConstant.image);
                    bf.setFileName(file.getOriginalFilename());
                    bf.setFileUrl(path);
                    fileDao.saveAndFlush(bf);
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            }
        }
        return JsonResult.success();
    }

    /**
     * 编辑用户
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        User user = userService.find(id);
        List<String> lines = stationDao.findLines(6);
        map.addAttribute("lines", lines);
//        if (null != user.getFwxxkUrl()) {
//            String[] fw = user.getFwxxkUrl().split("=");
//            if (fw.length > 1) {
//                map.put("fwxxkUrl", fw[1]);
//            } else {
//                map.put("fwxxkUrl", fw[0]);
//            }
//        }
//        if (null != user.getZkysgzUrl()) {
//            String[] fw = user.getZkysgzUrl().split("=");
//            if (fw.length > 1) {
//                map.put("zkysgzUrl", fw[1]);
//            } else {
//                map.put("zkysgzUrl", fw[0]);
//            }
//        }
//        if (null != user.getFaszUrl()) {
//            String[] fw = user.getFaszUrl().split("=");
//            if (fw.length > 1) {
//                map.put("faszUrl", fw[1]);
//            } else {
//                map.put("faszUrl", fw[0]);
//            }
//        }
        map.put("user", user);
        return "admin/user/edit";
    }

    /**
     * 查看用户
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Integer id, ModelMap map) {
        User user = userService.find(id);
        logger.info(properties.getUploadChunk() + "||||||" + properties.getHttp());
        user.getImage(properties.getHttp());
        map.put("user", user);
        return "admin/user/show";
    }

    /**
     * 更新用户
     *
     * @param user
     * @param photoFile
     * @param fwxxkFile
     * @param zkysgzFile
     * @param faszFile
     * @return
     */
    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult update(User user, @RequestParam("photoFile") MultipartFile photoFile, @RequestParam("fwxxkFile") MultipartFile fwxxkFile, @RequestParam("zkysgzFile") MultipartFile zkysgzFile, @RequestParam("faszFile") MultipartFile faszFile) {
        JsonResult x = getSaveAndUpdateUser(user, photoFile, fwxxkFile, zkysgzFile, faszFile);
        if (x != null) return x;
        return JsonResult.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    /**
     * 批量删除用户
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/removeAll/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult removeAll(@PathVariable Integer[] ids) {
        try {
            for (int i = 0; i < ids.length - 1; i++) {
                userService.delete(ids[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure("批量删除异常");
        }
        return JsonResult.success();
    }

    /**
     * 选择权限
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/grant/{id}", method = RequestMethod.GET)
    public String grant(@PathVariable Integer id, ModelMap map) {
        User user = userService.find(id);
        map.put("user", user);

        Set<Role> set = user.getRoles();
        List<Integer> roleIds = new ArrayList<>();
        for (Role role : set) {
            roleIds.add(role.getId());
        }
        map.put("roleIds", roleIds);

        List<Role> roles = roleService.findAll();
        map.put("roles", roles);
        return "admin/user/grant";
    }

    /**
     * 保存选择权限
     *
     * @param id
     * @param roleIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
    public JsonResult grant(@PathVariable Integer id, String[] roleIds) {
        try {
            userService.grant(id, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
