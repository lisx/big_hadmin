package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
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
public interface IStationDao extends IBaseDao<Station,Integer> {
    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM big_station WHERE parent_id = :id")
    void deleteGrant(@Param("id") Integer id);

    Set<Station> findAllByIfUse(Integer ifUse);

    Station findByNodeCode(String nodeCode);

    Station findByNodeName(String nodeName);

    List<Station> findByNodeCodeStartingWith(String nodeCode);

    Station save(Station station);

    @Query(value = "SELECT a from Station a where length(a.nodeCode)=:nodeLength order by a.nodeCode")
    List <Station> findByStationArea(@Param("nodeLength") int nodeLength);
    @Query(value = "SELECT a.nodeName from Station a where length(a.nodeCode)=:nodeLength order by a.nodeCode")
    List<String> findLines(@Param("nodeLength") int nodeLength);

    @Query(value = "SELECT a from Station a where a.nodeCode like :parentCode and length(a.nodeCode)=:nodeLength order by a.nodeCode")
    List<Station> querySubNodesByCode(@Param("parentCode") String parentCode,@Param("nodeLength") int nodeLength);

    @Query(value = "SELECT a.nodeName from Station a where length(a.nodeCode)=:nodeLength and a.nodeCode like :nodeCode order by a.nodeCode")
    List<String> findStations(@Param("nodeLength") int nodeLength,@Param("nodeCode")String nodeCode);
    @Query(value = "SELECT a.nodeName from Station a where length(a.nodeCode)>:nodeLength and a.nodeCode like :nodeCode order by a.nodeCode")
    List<String> findByTreeStations(@Param("nodeLength") int nodeLength,@Param("nodeCode")String nodeCode);
}
