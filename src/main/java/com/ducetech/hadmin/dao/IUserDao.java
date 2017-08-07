package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.User;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

	User findByUserName(String userName);

    User findByUserCode(String userCode);

    List<User> findAllByStation(String station);
}
