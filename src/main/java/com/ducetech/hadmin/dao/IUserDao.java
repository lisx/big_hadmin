package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface IUserDao extends IBaseDao<User, Integer> {
	User findByUserName(String userName);
    @Query(value = "select t from User t where t.id=(select max(o.id) from User o where o.userCode=:userCode )")
    User findByUserCodeOne(@Param("userCode")String userCode);

    User findByUserCode(String userCode);
    @Query(value="select o from User o where o.station=:station or o.stationArea=:station ")
    List<User> findAllByStation(@Param("station") String station);
    @Query(nativeQuery = true,
        value = "select u.* from (select user_id,count(score) scount from big_exam_log  GROUP BY user_id) t ,big_user u where t.scount>0 and u.id = t.user_id  \n#pageable \n",
        countQuery = "select count(u.id) from (select user_id,count(score) scount from big_exam_log  GROUP BY user_id) t ,big_user u where t.scount>0 and u.id = t.user_id"
    )
    Page<User> findByScore(@Param("pageable") Pageable pageable);
}
