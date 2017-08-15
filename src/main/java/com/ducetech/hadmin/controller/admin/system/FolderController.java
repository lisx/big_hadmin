package com.ducetech.hadmin.controller.admin.system;

import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.IFolderDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.Folder;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
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
public class FolderController extends BaseController{
    @Autowired
    IFolderDao folderDao;
    @Autowired
    IStationDao stationDao;
    @RequestMapping("/list")
    @ResponseBody
    public List index(){
        List list = folderDao.findAll();
        return list;
    }
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id,String station, ModelMap map) {
        Folder folder = folderDao.findOne(id);
        map.put("folder", folder);
        map.put("station",station);
        return "admin/learn/form";
    }
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        try {
            folderDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
    @RequestMapping(value= {"/saveAndFlush"} ,method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(Folder folder,String station){
        User user=getUser();
        Station area=stationDao.findByNodeCode(station);
        try {
            folder.setCreateTime(new Date());
            folder.setStation(station);
            folder.setArea(area);
//            if(null!=user)
//                if(null!=user.getStation()) {
//                    folder.setStation(user.getStation());
//                }else if(null!=user.getStationArea()){
//                    folder.setStation(user.getStationArea());
//                }else if(null!=user.getLine()){
//                    folder.setStation(user.getLine());
//                }else{
//                    folder.setStation("");
//                }
                folder.setCreateId(user.getId());
            folderDao.saveAndFlush(folder);
        } catch (Exception e) {
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
