package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.Article;
import com.ducetech.hadmin.service.support.IBaseService;
import com.ducetech.hadmin.vo.Tags;

import java.util.List;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author lisx
 * @since 2016-12-28
 */
public interface IArticleService extends IBaseService<Article, Integer> {

	/**
	 * 增加或者修改文章
	 * @param article
	 */
	void saveOrUpdate(Article article);

    void delete(Integer id);
	java.util.List<Article> findBySortName(String sortName);
	List<Tags> findTags(String  tag);
	List<Article> findAllByLabel(String label);
}
