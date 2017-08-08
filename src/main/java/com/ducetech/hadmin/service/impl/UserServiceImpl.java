package com.ducetech.hadmin.service.impl;

import com.ducetech.hadmin.common.utils.MD5Utils;
import com.ducetech.hadmin.dao.IUserDao;
import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Role;
import com.ducetech.hadmin.entity.User;
import com.ducetech.hadmin.service.IRoleService;
import com.ducetech.hadmin.service.IUserService;
import com.ducetech.hadmin.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户账户表  服务实现类
 * </p>
 *
 * @author lisx
 * @since 2016-12-28
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IRoleService roleService;

	@Override
	public IBaseDao<User, Integer> getBaseDao() {
		return this.userDao;
	}

	@Override
	public User findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Override
	public void saveOrUpdate(User user) {
		if(user.getId() != null){
			User dbUser = find(user.getId());
			dbUser.setNickName(user.getNickName());
			dbUser.setTelephone(user.getTelephone());
			update(dbUser);
		}else{
			user.setPassword(MD5Utils.md5("123456"));
			save(user);
		}
	}



	@Override
	public void delete(Integer id) {
		User user = find(id);
		//Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能删除");
		super.delete(id);
	}

	@Override
	public void grant(Integer id, String[] roleIds) {
		User user = find(id);
		Assert.notNull(user, "用户不存在");
		//Assert.state(!"admin".equals(user.getUserName()),"超级管理员用户不能修改管理角色");
		Role role;
		Set<Role> roles = new HashSet<Role>();
		if(roleIds != null){
			for (int i = 0; i < roleIds.length; i++) {
				Integer rid = Integer.parseInt(roleIds[i]);
				role = roleService.find(rid);
				roles.add(role);
			}
		}
		user.setRoles(roles);
		update(user);
	}

    @Override
    public User findByUserCode(String userCode) {
        return userDao.findByUserCode(userCode);
    }

}
