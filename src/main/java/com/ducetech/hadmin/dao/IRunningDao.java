package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Running;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-01 15:46
 **/
@Repository
public interface IRunningDao extends IBaseDao<Running,Integer> {
    public List<Running> findByLineName(String line);
}
