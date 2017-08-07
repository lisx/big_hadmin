package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.fastjson.JSONArray;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.dao.IFolderDao;
import com.ducetech.hadmin.entity.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 文件夹标签管理
 *
 * @author lisx
 * @create 2017-08-02 16:19
 **/
@Controller
@RequestMapping("/admin/folder")
public class FolderController {
    @Autowired
    IFolderDao folderDao;
    @RequestMapping("/index")
    @ResponseBody
    public List index(){
        List list = folderDao.findAll();
        System.out.println("||||||||"+list.size());
        return list;
    }
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        Folder folder = folderDao.findOne(id);
        map.put("folder", folder);
        return "admin/learn/form";
    }

    @RequestMapping(value= {"/saveAndFlush"} ,method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(Folder folder){
        try {
            folder.setCreateTime(new Date()+"");
            folderDao.saveAndFlush(folder);
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
