package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IBigFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    IBigFileDao fileDao;

    /**
     * 消防安全首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        logger.info("获取站点文件全部数据");
        return "admin/fire/index";
    }

    /**
     * 进入文件夹
     * @param folder
     * @param map
     * @return
     */
    @RequestMapping("/toFolder")
    public String toFolder(String folder, Integer folderId,String menuType,Model map) {
        logger.info("进入消防安全文件夹folder{}",folder);
        map.addAttribute("folder", folder);
        map.addAttribute("folderId", folderId);
        map.addAttribute("menuType", menuType);
        return "admin/fire/folder";
    }
}
