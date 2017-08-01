package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Station;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 车站信息
 *
 * @author lisx
 * @create 2017-08-01 15:46
 **/
public interface IStationDao extends IBaseDao<Station,Integer> {
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM big_station WHERE parent_id = :id")
    void deleteGrant(@Param("id") Integer id);
}
