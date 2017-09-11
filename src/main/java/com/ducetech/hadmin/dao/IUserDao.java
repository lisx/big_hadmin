package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.User;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityResult;
import java.util.List;
@Repository
public interface IUserDao extends IBaseDao<User, Integer> {
	User findByUserName(String userName);
	@Cacheable(value = "findByUserCode")
    User findByUserCode(String userCode);
    @Cacheable(value = "findAllByStation")
    @Query(value="select o from User o where o.station=:station or o.stationArea=:station ")
    List<User> findAllByStation(@Param("station") String station);
    @Cacheable(value = "findByScore")
    @Query(nativeQuery = true,
        value = "select u.* from (select user_id,count(score) scount from big_exam_log  GROUP BY user_id) t ,big_user u where t.scount>0 and u.id = t.user_id  \n#pageable \n",
        countQuery = "select count(u.id) from (select user_id,count(score) scount from big_exam_log  GROUP BY user_id) t ,big_user u where t.scount>0 and u.id = t.user_id"
    )
    Page<User> findByScore(@Param("pageable") Pageable pageable);
    @Cacheable(value = "findUserAll")
    @Override
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
