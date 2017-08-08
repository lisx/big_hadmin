package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Train;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-01 15:46
 **/
@Repository
public interface ITrainDao extends IBaseDao<Train,Integer> {
    @Query(value = "select o from BigFile o where o.station =:station or o.station=:area")
    List<BigFile> findByStationBetween(@Param("station") String station,@Param("area") String area);
}
