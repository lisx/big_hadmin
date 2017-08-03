package com.ducetech.hadmin.controller.admin.system;

import com.alibaba.fastjson.JSONArray;
import com.ducetech.hadmin.common.JsonResult;
import com.ducetech.hadmin.controller.BaseController;
import com.ducetech.hadmin.dao.ILearnDao;
import com.ducetech.hadmin.entity.Learn;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.service.IStationService;
import com.ducetech.hadmin.service.specification.SimpleSpecificationBuilder;
import com.ducetech.hadmin.service.specification.SpecificationOperator.Operator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * 车站信息
 */
@Controller
@RequestMapping("/admin/station")
public class StationController extends BaseController {
	@Autowired
	private IStationService stationService;
    @Autowired
    ILearnDao learnDao;
    /**
     * 树形菜单
     * @return
     */
	@RequestMapping("/tree")
	@ResponseBody
	public JSONArray tree(){
		JSONArray list = stationService.tree(1);
		System.out.println("||||||||"+list.size());
		return list;
	}

	@RequestMapping("/index")
	public String index() {

		return "admin/station/index";
	}

    @RequestMapping("/file")
    public String file() {
        return "admin/station/file";
    }
    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile() {
        return "admin/station/file";
    }

    @RequestMapping(value = "/uploadFilePost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFilePost(HttpServletRequest request){
        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file;
        //创建临时文件夹
        File dirTempFile = new File("/Users/lisx/Ducetech/logs/");
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        Learn learn;
        BufferedOutputStream stream;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    System.out.println("|||||||||||||||");
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(dirTempFile.getAbsolutePath()+"/"+file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                    learn=new Learn();
                    learn.setFileName(file.getOriginalFilename());
                    learn.setFileSize(""+file.getSize()/1000);
                    learn.setCreateTime(new Date());
                    learnDao.save(learn);
                    System.out.println("file.getSize():"+file.getSize()+"|");
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
	@RequestMapping("/list")
	@ResponseBody
	public Page<Station> list() {
		SimpleSpecificationBuilder<Station> builder = new SimpleSpecificationBuilder<Station>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("name", Operator.likeAll.name(), searchText);
		}
		Page<Station> page = stationService.findAll(builder.generateSpecification(),getPageRequest());
		return page;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		List<Station> list = stationService.findAll();
		map.put("list", list);
		return "admin/station/form";
	}


	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		Station station = stationService.find(id);
		map.put("station", station);

		List<Station> list = stationService.findAll();
		map.put("list", list);
		return "admin/station/form";
	}

	@RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Station station, ModelMap map){
		try {
			stationService.saveOrUpdate(station);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id,ModelMap map) {
		try {
			stationService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
}
