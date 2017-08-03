package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Learn;
import com.ducetech.hadmin.entity.Station;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 车站信息
 *
 * @author lisx
 * @create 2017-08-01 15:46
 **/
@Repository
public interface ILearnDao extends IBaseDao<Learn,Integer> {

}
