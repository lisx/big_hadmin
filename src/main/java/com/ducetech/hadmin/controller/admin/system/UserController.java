package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.PoiUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.entity.Role;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IRoleService;
import com.ducetech.hadmin.service.IUserService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator.Operator;
import com.ducetech.hadmin.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
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
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
	private final IUserService userService;
	private final IRoleService roleService;

    @Autowired
    public UserController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

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
			builder.add("nickName", Operator.likeAll.name(), searchText);
            builder.addOr("userName", Operator.likeAll.name(), searchText);
            builder.addOr("userCode", Operator.likeAll.name(), searchText);
            builder.addOr("telephone", Operator.likeAll.name(), searchText);
            builder.addOr("station", Operator.likeAll.name(), searchText);
            builder.addOr("stationArea", Operator.likeAll.name(), searchText);
            builder.addOr("line", Operator.likeAll.name(), searchText);
            builder.addOr("createdTime", Operator.likeAll.name(), searchText);
		}
        return userService.findAll(builder.generateSpecification(), getPageRequest());
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
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
                List<List<List<String>>> data = PoiUtil.readExcelToList(fileUpload, 0);
                if (null != data && !data.isEmpty()) {
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
                            for (List<String> row : sheet) {
                                String userCode = StringUtil.trim(row.get(0));
                                String userName = StringUtil.trim(row.get(1));
                                //String unit = StringUtil.trim(row.get(2));
                                String telphone = StringUtil.trim(row.get(3));
                                String fwxxkUrl = StringUtil.trim(row.get(4));
                                String zkysgzUrl = StringUtil.trim(row.get(5));
                                String faszUrl = StringUtil.trim(row.get(6));
                                User user = new User();
                                //user.setUnitId(unit);
                                user.setTelephone(telphone);
                                user.setFwxxkUrl(fwxxkUrl);
                                user.setZkysgzUrl(zkysgzUrl);
                                user.setFaszUrl(faszUrl);
                                user.setCreateTime(new Date());
                                user.setIfUse(0);
                                user.setUserCode(userCode);
                                user.setUserName(userName);
                                userService.saveOrUpdate(user);
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
        //创建临时文件夹
        File dirTempFile = new File(BigConstant.upload);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(dirTempFile.getAbsolutePath()+"/"+file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		User user = userService.find(id);
		map.put("user", user);
		return "admin/user/form";
	}

	@RequestMapping(value= {"/edit"} ,method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(User user){
		try {
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
