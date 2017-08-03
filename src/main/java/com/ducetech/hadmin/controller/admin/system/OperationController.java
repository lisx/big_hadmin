package com.ducetech.hadmin.controller.admin.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 运营图管理
 *
 * @author lisx
 * @create 2017-08-02 11:13
 **/
@Controller
@RequestMapping("/admin/operation")
public class OperationController {
    @RequestMapping("/index")
    public String index() {
        return "admin/operation/index";
    }
}
