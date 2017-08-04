package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.Train;
import com.ducetech.hadmin.service.support.IBaseService;

/**
 * 培训资料
 * @author lisx
 * @create 2017-08-04 17:33
 */
public interface ITrainService extends IBaseService<Train, Integer> {

	/**
	 * 增加或者修改文章分类
	 * @param learn
	 */
	void saveOrUpdate(Train learn);

    void delete(Integer id);

}
