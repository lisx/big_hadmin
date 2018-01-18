package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.DucetechProperties;
import com.ducetech.hadmin.common.utils.MD5Utils;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IUserDao;
import com.ducetech.hadmin.entity.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 用户接口
 *
 * @author lisx
 * @create 2017-08-07 17:39
 **/
@RestController
@RequestMapping("/interface/user")
public class UserInterface extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserInterface.class);


    @Autowired
    IUserDao userDao;
    @Autowired
    DucetechProperties properties;
    @ApiOperation(value="用户登录", notes="根据用户工号及密码登录")
    @RequestMapping(value="/login", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="用户工号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="password",value="用户密码",dataType="string", paramType = "query")})
    public JSONObject login(String code,String password){
        logger.info("进入登录接口code:{},password:{}",code,password);
        int state;
        String msg;
        JSONObject obj=new JSONObject();
        if(StringUtil.isBlank(code)){
            obj.put("state",BigConstant.state_error);
            obj.put("msg",BigConstant.user_state_4);
            obj.put("data",null);
            return obj;
        }
        User user=userDao.findByUserCodeOne(code);

        if(null!=user){
            if(user.getPassword().equals(MD5Utils.md5(password))){
                user.setRoles(null);
                msg=BigConstant.state_1;
                state=BigConstant.state_success;
            }else{
                msg=BigConstant.user_state_2;
                state=BigConstant.state_error;
            }
        }else{
            msg=BigConstant.user_state_3;
            state=BigConstant.state_error;
        }
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",user);
        return obj;
    }
    @ApiOperation(value="获取用户列表", notes="根据站点站区或线路获取用户")
    @RequestMapping(value={"/list"}, method=RequestMethod.GET)
    @ApiImplicitParam(name="station",value="线路，站点，站区",dataType="string", paramType = "query")
    public JSONObject getUserList(String station) {
        logger.info("进入获取用户列表接口station:{}"+station);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        List<User> r;
        r=userDao.findAllByStation(station);
        for(int i=0;i<r.size();i++){
            User user;
            user=r.get(i);
            user.getImage(properties.getHttp());
            r.set(i,user);
        }
        if(null==r){
            msg=BigConstant.state_2;
        }else{
            msg=BigConstant.state_1;
        }
        obj.put("msg",msg);
        obj.put("state",state);
        obj.put("data",r);
        return obj;
    }
    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID",  dataType = "Integer", paramType = "query")
    @RequestMapping(value="/detail", method=RequestMethod.GET)
    public JSONObject getUser(Integer id) {
        logger.info("进入获取用户详情接口id:{}",id);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        User user=userDao.findOne(id);
        user.getImage(properties.getHttp());
        if(null==user){
            msg=BigConstant.user_state_5;
            state=BigConstant.state_error;
        }else{
            msg=BigConstant.state_1;
        }
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",user);
        return obj;
    }

    @ApiOperation(value="修改用户密码", notes="修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="用户工号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="oldPassword",value="用户原密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="newPassword",value="用户新密码",dataType="string", paramType = "query")
    })
    @RequestMapping(value="/setUserPassword", method=RequestMethod.GET)
    public JSONObject setUserPassword(String code,String oldPassword,String  newPassword) {
        logger.info("进入获取用户详情接口code:{},oldPassword:{},newPassword:{}",code,oldPassword,newPassword);
        int state=BigConstant.state_success;
        String msg;
        JSONObject obj=new JSONObject();
        User user=userDao.findByUserCode(code);
        if(null!=user){
            if(user.getPassword().equals(MD5Utils.md5(oldPassword))){
                user.setPassword(MD5Utils.md5(newPassword));
                userDao.save(user);
                msg=BigConstant.user_state_6;
            }else{
                msg=BigConstant.user_state_2;
                state=BigConstant.state_error;
            }
        }else{
            msg=BigConstant.user_state_3;
            state=BigConstant.state_error;
        }
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",null);
        return obj;
    }
}
