package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.entity.User;
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
public interface IBigFileDao extends IBaseDao<BigFile,Integer> {
    @Query(value="select o from BigFile o where o.stationFile=:station ")
    List<BigFile> findByStation(@Param("station") Station station);
    @Query(value="select o from BigFile o where (o.nodeCode like:station or o.nodeCode=:ys or  o.nodeCode=:area ) and o.menuType=:menuType ")
    List<BigFile> findByStationFileOrStationFileAndMenuType(@Param("station") String station,@Param("ys") String ys,@Param("area") String area,@Param("menuType") String menuType);
    BigFile findByFileName(String folder);
}
