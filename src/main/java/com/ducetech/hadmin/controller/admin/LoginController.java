package com.ducetech.hadmin.controller.admin;

import com.ducetech.hadmin.common.utils.MD5Utils;
import com.ducetech.hadmin.controller.BaseController;

import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController extends BaseController {
    @Autowired
    IUserService userService;
	@RequestMapping(value = { "/admin/login","/" }, method = RequestMethod.GET)
	public String login() {
        User user=userService.findByUserName("运三管理员");
        if(null==user){
            user=new User();
            user.setUserName("运三管理员");
            user.setStationArea("运三分公司");
            user.setUserCode("123456");
            user.setPassword(MD5Utils.md5("123456"));
            userService.saveOrUpdate(user);
        }
		return "admin/login";
	}
	@RequestMapping(value = { "/admin/login" }, method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,ModelMap model
			) {
		try {
			 Subject subject = SecurityUtils.getSubject();
			 UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
			return redirect("/admin/index");
		} catch (AuthenticationException e) {
			model.put("message", e.getMessage());
		}
		return "admin/login";
	}

	@RequestMapping(value = { "/admin/logout" }, method = RequestMethod.GET)
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return redirect("admin/login");
	}

}
