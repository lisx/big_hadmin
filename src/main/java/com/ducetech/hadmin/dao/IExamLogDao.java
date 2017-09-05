package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Exam;
import com.ducetech.hadmin.entity.ExamLog;
import com.ducetech.hadmin.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 考试记录
 *
 * @author lisx
 * @create 2017-08-11 10:38
 **/
public interface IExamLogDao extends IBaseDao<ExamLog,Integer> {
    List<ExamLog> findByUser(@Param("user")User user);
}
