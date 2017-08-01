package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.ArticleSort;
import com.ducetech.hadmin.service.support.IBaseService;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author lisx
 * @since 2016-12-28
 */
public interface IArticleSortService extends IBaseService<ArticleSort, Integer> {

	/**
	 * 增加或者修改文章分类
	 * @param article
	 */
	void saveOrUpdate(ArticleSort article);

    void delete(Integer id);

}
