package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Proper;
import com.ducetech.hadmin.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-01 15:46
 **/
@Repository
public interface IProperDao extends IBaseDao<Proper,Integer> {
    List<Proper> findByQuestion(Question question);
}
