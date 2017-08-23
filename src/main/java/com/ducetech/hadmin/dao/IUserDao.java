package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

	User findByUserName(String userName);

    User findByUserCode(String userCode);

    @Query(value="select o from User o where o.station=:station or o.stationArea=:station or o.line=:station")
    List<User> findAllByStation(@Param("station") String station);

}
