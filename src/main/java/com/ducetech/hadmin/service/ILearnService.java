package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.Learn;
import com.ducetech.hadmin.service.support.IBaseService;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author lisx
 * @since 2016-12-28
 */
public interface ILearnService extends IBaseService<Learn, Integer> {

	/**
	 * 增加或者修改文章分类
	 * @param learn
	 */
	void saveOrUpdate(Learn learn);

    void delete(Integer id);

}
