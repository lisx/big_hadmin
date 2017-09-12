package com.ducetech.hadmin.controller;

import com.ducetech.hadmin.common.DateEditor;
import com.ducetech.hadmin.common.utils.DucetechProperties;
import com.ducetech.hadmin.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class BaseController {
	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Autowired
    protected DucetechProperties properties;

	@InitBinder
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        webDataBinder.registerCustomEditor(Date.class, new DateEditor(true));
    }

	/**
     * 带参重定向
     *
     * @param path
     * @return
     */
    protected String redirect(String path) {
        return "redirect:" + path;
    }

    /**
     * 不带参重定向
     *
     * @param response
     * @param path
     * @return
     */
    protected String redirect(HttpServletResponse response, String path) {
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取分页请求
     * @return
     */
    protected PageRequest getPageRequest(){
    	int page = 1;
    	int size = 10;
    	Sort sort = null;
    	try {
    		String sortName = request.getParameter("sortName");
    		String sortOrder = request.getParameter("sortOrder");
    		if(StringUtils.isNoneBlank(sortName) && StringUtils.isNoneBlank(sortOrder)){
    			if(sortOrder.equalsIgnoreCase("desc")){
    			    String[] arr=sortName.split(",");
    			    if(arr.length>1){
                        sort = new Sort(Direction.DESC, arr[0]).and(new Sort(Direction.DESC,arr[1]));
                    }else {
                        sort = new Sort(Direction.DESC, sortName);
                    }
    			}else{
    				sort = new Sort(Direction.ASC, sortName);
    			}
    		}
    		if(!org.springframework.util.StringUtils.isEmpty(request.getParameter("pageNumber"))){
				page = Integer.parseInt(request.getParameter("pageNumber")) - 1;
				size = Integer.parseInt(request.getParameter("pageSize"));
			}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	PageRequest pageRequest = new PageRequest(page, size, sort);
    	return pageRequest;
    }

    /**
     * 获取分页请求
     * @param sort 排序条件
     * @return
     */
    protected PageRequest getPageRequest(Sort sort){
    	int page = 0;
    	int size = 10;
    	try {
    		if (null==sort) {
				String sortName = request.getParameter("sortName");
				String sortOrder = request.getParameter("sortOrder");
				if (StringUtils.isNoneBlank(sortName) && StringUtils.isNoneBlank(sortOrder)) {
					if (sortOrder.equalsIgnoreCase("desc")) {
						sort.and(new Sort(Direction.DESC, sortName));
					} else {
						sort.and(new Sort(Direction.ASC, sortName));
					}
				}
			}
			if(!org.springframework.util.StringUtils.isEmpty(request.getParameter("pageNumber"))){
				page = Integer.parseInt(request.getParameter("pageNumber")) - 1;
				size = Integer.parseInt(request.getParameter("pageSize"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	PageRequest pageRequest = new PageRequest(page, size, sort);
    	return pageRequest;
    }
    /**
     * 获取用户登录信息
     *
     *org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY ; com.hncxhd.bywl.entity.manual.User@533752b2
     */
    public User getUser(){
        //boolean status = false;
        String sessionID =request.getSession().getId();
        SessionKey key = new WebSessionKey(sessionID,request,response);
        try{
            Session se = SecurityUtils.getSecurityManager().getSession(key);
            Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            //org.apache.shiro.subject.SimplePrincipalCollection cannot be cast to com.hncxhd.bywl.entity.manual.User
            SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
            return (User)coll.getPrimaryPrincipal();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
        }
        return null;
    }

}
