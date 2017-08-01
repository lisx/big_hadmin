package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface IArticleDao extends IBaseDao<Article, Integer> {
    java.util.List<Article> findBySortName(String sortName);

}
