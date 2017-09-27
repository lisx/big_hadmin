package com.ducetech.hadmin.service;

import com.ducetech.hadmin.service.support.IBaseService;
import com.ducetech.hadmin.entity.User;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author lisx
 * @since 2016-12-28
 */
public interface IUserService extends IBaseService<User, Integer> {

	/**
	 * 根据用户名查找用户
	 * @param username
	 * @return
	 */
	User findByUserName(String username);

	/**
	 * 增加或者修改用户
	 * @param user
	 */
	void saveOrUpdate(User user);

	/**
	 * 给用户分配角色
	 * @param id 用户ID
	 * @param roleIds 角色Ids
	 */
	void grant(Integer id, String[] roleIds);

    User findByUserCode(String username);
}
