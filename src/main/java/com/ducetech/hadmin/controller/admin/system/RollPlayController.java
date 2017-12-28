package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页滚播图管理
 *  fire safety
 * @author lisx
 * @create 2017-08-15 08:47
 **/
@Controller
@RequestMapping("/admin/rollPlay")
public class RollPlayController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(RollPlayController.class);
    @Autowired
    IBigFileDao fileDao;
    /**
     * 首页滚播图首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("首页滚播图管理首页");
        return "admin/rollPlay/index";
    }

}
