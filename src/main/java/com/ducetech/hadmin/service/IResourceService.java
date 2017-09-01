package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.Resource;
import com.ducetech.hadmin.service.support.IBaseService;
import com.ducetech.hadmin.vo.ZtreeView;

import java.util.List;

/**
 * <p>
 * 资源服务类
 * </p>
 *
 * @author lisx
 * @since 2016-12-28
 */
public interface IResourceService extends IBaseService<Resource, Integer> {

	/**
	 * 获取角色的权限树
	 * @param roleId
	 * @return
	 */
	List<ZtreeView> tree(int roleId);

	List<Resource> findAll();

	/**
	 * 修改或者新增资源
	 * @param resource
	 */
	void saveOrUpdate(Resource resource);

}
