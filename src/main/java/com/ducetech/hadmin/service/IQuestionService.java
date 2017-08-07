package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.service.support.IBaseService;

/**
 * 培训资料
 * @author lisx
 * @create 2017-08-04 17:33
 */
public interface IQuestionService extends IBaseService<Question, Integer> {

	/**
	 * 增加或者修改文章分类
	 * @param learn
	 */
	void saveOrUpdate(Question learn);

    void delete(Integer id);

}
