package com.ducetech.hadmin.bigInterfacel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.ducetech.hadmin.common.utils.MD5Utils;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IUserDao;
import com.ducetech.hadmin.entity.User;
import com.sun.star.util.DateTime;
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
    private ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if(v==null||v.equals(null)){
                return "";
            }
            return v;
        }
    };
    @Autowired
    IUserDao userDao;
    @ApiOperation(value="用户登录", notes="根据用户工号及密码登录")
    @RequestMapping(value="/login", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="用户工号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="password",value="用户密码",dataType="string", paramType = "query")})
    public JSONObject login(String code,String password){
        logger.debug("进入登录接口");
        User user=userDao.findByUserCode(code);
        JSONObject obj=new JSONObject();
        int state=0;
        String msg;
        if(null!=user){
            if(user.getPassword().equals(MD5Utils.md5(password))){
                user.setRoles(null);
                msg="登录成功！";
                state=1;
            }else{
                msg="密码错误！";
            }
        }else{
            msg="工号错误！";
        }
        obj.put("state",state);
        obj.put("msg",msg);
        obj.put("data",user);
        return JSONObject.parseObject(JSONObject.toJSONString(obj, filter));
    }
    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());
    @ApiOperation(value="获取用户列表", notes="根据站点站区或线路获取用户")
    @RequestMapping(value={"/list"}, method=RequestMethod.GET)
    @ApiImplicitParam(name="station",value="线路，站点，站区",dataType="string", paramType = "query")
    public JSONObject getUserList(String station) {
        JSONObject obj=new JSONObject();
        int state=0;
        String msg;
        System.out.println("|||||||||||||"+station);
        List<User> r = userDao.findAllByStation(station);
        obj.put("state",state);
        if(r!=null)
        obj.put("msg","查询成功！");
        obj.put("data",r);
        String json=JSONObject.toJSONString(obj,filter);
        return JSONObject.parseObject(json);
    }
    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public User getUser(@PathVariable Integer id) {
        return userDao.findOne(id);
    }
    @ApiOperation(value="更新用户详细信息", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public String putUser(@PathVariable String id, @RequestBody User user) {
        User u = users.get(id);
        u.setUserName(user.getUserName());
       // users.put(id, u);
        return "success";
    }
    @ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        users.remove(id);
        return "success";
    }
}
