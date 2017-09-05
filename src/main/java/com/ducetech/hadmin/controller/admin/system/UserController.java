package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.MD5Utils;
import com.ducetech.hadmin.common.utils.PoiUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Role;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IRoleService;
import com.ducetech.hadmin.service.IUserService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator.Operator;
import com.ducetech.hadmin.common.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *@deprecated  用户管理
 *@author lisx
 *
 **/
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
	IUserService userService;
    @Autowired
	IRoleService roleService;
	@Autowired
    IBigFileDao fileDao;
	@Autowired
    IStationDao stationDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
	 * 用户管理初始化页面
	 * @return string
	 */
	@RequestMapping(value = { "/", "/index" })
	public String index() {
		System.out.println("++++++"+request.getSession());
	    return "admin/user/index";
	}

    /**
     * 查询集合
     * @return Page<User>
     */
	@RequestMapping(value = { "/list" })
	@ResponseBody
	public Page<User> list() {
		SimpleSpecificationBuilder<User> builder = new SimpleSpecificationBuilder<>();
		String searchText = request.getParameter("searchText");
		if(!StringUtil.isBlank(searchText)){
            builder.addOr("userName", Operator.likeAll.name(), searchText);
            builder.addOr("userCode", Operator.likeAll.name(), searchText);
            builder.addOr("station", Operator.likeAll.name(), searchText);
            builder.addOr("stationArea", Operator.likeAll.name(), searchText);
            builder.addOr("line", Operator.likeAll.name(), searchText);
		}
        return userService.findAll(builder.generateSpecification(), getPageRequest());
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
        List<String> areas = stationDao.findLines(9);
        model.addAttribute("areas", areas);
		return "admin/user/form";
	}

    @RequestMapping(value = "/uploadUser", method = RequestMethod.GET)
    public String uploadUser() {
        return "admin/user/uploadUser";
    }

    @RequestMapping(value = "/fileUploadUser", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadUserExcel(@RequestParam("fileUpload") MultipartFile fileUpload) {
        try {
            if (fileUpload != null && !fileUpload.isEmpty()) {
                List<List<List<String>>> data = PoiUtil.readExcelToList(fileUpload, 2);
                if (null != data && !data.isEmpty()) {
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            for (List<String> row : sheet) {
                                String userName = StringUtil.trim(row.get(0));
                                String stationArea = StringUtil.trim(row.get(1));
                                String station = StringUtil.trim(row.get(2));
                                String line = StringUtil.trim(row.get(3));
                                String position = StringUtil.trim(row.get(4));
                                String userCode = StringUtil.trim(row.get(5));
                                String fwxxkUrl = StringUtil.trim(row.get(6));
                                String faszUrl = StringUtil.trim(row.get(7));
                                String zkysgzUrl = StringUtil.trim(row.get(8));
                                if(stationArea.endsWith("站区")){

                                }else{
                                    stationArea=stationArea+"站区";
                                }
                                if(stationArea.equals("西直门站区")||stationArea.equals("东直门站区")||stationArea.equals("建国门站区")||stationArea.equals("北土城站区")||stationArea.equals("慈寿寺站区")){
                                    stationArea=line+stationArea;
                                }
                                if(station.endsWith("站")){

                                }else{
                                    station=station+"站";
                                }
                                if(station.equals("西直门站")||station.equals("东直门站")||station.equals("建国门站")||station.equals("北土城站")||station.equals("慈寿寺站")){
                                    station=line+station;
                                }
                                User user=userService.findByUserCode(userCode);
                                if(null==user) {
                                    user = new User();
                                }
                                user.setUserName(userName);
                                user.setStationArea(stationArea);
                                user.setStation(station);
                                user.setLine(line);
                                user.setPosition(position);
                                user.setUserCode(userCode);
                                user.setPhotoUrl(userCode);
                                user.setFwxxkUrl(fwxxkUrl);
                                user.setZkysgzUrl(zkysgzUrl);
                                user.setFaszUrl(faszUrl);
                                user.setCreateTime(new Date());
                                user.setIfUse(0);
                                userService.saveOrUpdate(user);
                                String redisValue = stringRedisTemplate.opsForValue().get("user"+user.getId());
                                if (StringUtils.isEmpty(redisValue)) {
                                    stringRedisTemplate.opsForValue().set("user"+user.getId(), user.getUserName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success("上传成功！");
    }
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile() {
        return "admin/user/uploadFile";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(HttpServletRequest request){
        logger.info("进入上传方法");
        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file;
        BufferedOutputStream stream;
        for (MultipartFile file1 : files) {
            file = file1;
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    String path = BigConstant.upload + file.getOriginalFilename();
                    File f = new File(path);
                    stream = new BufferedOutputStream(new FileOutputStream(f));
                    stream.write(bytes);
                    stream.close();
                    BigFile bf = fileDao.findByFileName(file.getOriginalFilename());
                    if (null == bf)
                        bf = new BigFile();
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		User user = userService.find(id);
        List<String> areas = stationDao.findLines(9);
        map.addAttribute("areas", areas);
        if(null!=user.getFwxxkUrl()) {
            String[] fw=user.getFwxxkUrl().split("=");
            if(fw.length>1) {
                map.put("fwxxkUrl", fw[1]);
            }else{
                map.put("fwxxkUrl", fw[0]);
            }
        }
        if(null!=user.getFwxxkUrl()) {
            String[] fw=user.getZkysgzUrl().split("=");
            if(fw.length>1) {
                map.put("zkysgzUrl", fw[1]);
            }else{
                map.put("zkysgzUrl", fw[0]);
            }
        }
        if(null!=user.getFwxxkUrl()) {
            String[] fw=user.getFaszUrl().split("=");
            if(fw.length>1) {
                map.put("faszUrl", fw[1]);
            }else{
                map.put("faszUrl", fw[0]);
            }
        }
		map.put("user", user);
		return "admin/user/form";
	}
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Integer id,ModelMap map) {
        User user = userService.find(id);
        map.put("user", user);
        return "admin/user/show";
    }

	@RequestMapping(value= {"/edit"} ,method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(User user){
		try {
		    user.setPassword(MD5Utils.md5(user.getPassword()));
		    user.setIfUse(0);
		    user.setCreateTime(new Date());
		    user.setCreateId(getUser().getId());
			userService.saveOrUpdate(user);
            // 插入缓存
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

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

	@ResponseBody
	@RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
	public JsonResult grant(@PathVariable Integer id,String[] roleIds) {
		try {
			userService.grant(id,roleIds);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
}
