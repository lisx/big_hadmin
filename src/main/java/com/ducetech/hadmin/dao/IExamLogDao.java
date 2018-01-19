package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.ExamLog;
import com.ducetech.hadmin.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * 考试记录
 *
 * @author lisx
 * @create 2017-08-11 10:38
 **/
public interface IExamLogDao extends IBaseDao<ExamLog,Integer> {
    List<ExamLog> findByUser(@Param("user")User user);
    @Query(value ="select count(o.score) as logSize from ExamLog o where o.user=:user and o.score>=0")
    int findByUserLogSize(@Param("user")User user);
    @Query(value = "select o from ExamLog o where o.user=:user and  createTime <:start and createTime >:end ")
    List<ExamLog> findByUserAndCreateTimeBetween(@Param("user")User user,@Param("start") Date start,@Param("end") Date end);
}
