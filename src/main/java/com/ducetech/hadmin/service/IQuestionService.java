package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.entity.QuestionBank;
import com.ducetech.hadmin.service.support.IBaseService;

import java.util.List;

/**
 * 培训资料
 * @author lisx
 * @create 2017-08-04 17:33
 */
public interface IQuestionService extends IBaseService<Question, Integer> {

	/**
	 * 增加或者修改文章分类
	 */
	void saveOrUpdate(Question question);

    void delete(Integer id);

    void deleteByQuestionBank(Integer id);

}
