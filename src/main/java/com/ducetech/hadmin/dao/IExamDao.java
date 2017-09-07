package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Exam;
import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.entity.QuestionBank;
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
public interface IExamDao extends IBaseDao<Exam,Integer> {
    Exam findByExamName(String examName);

    @Query(value = "select o from Exam o where o.nodeCode like:nodeCode or o.nodeCode='000'")
    List<Exam> findByStation(@Param("nodeCode") String nodeCode);

    List<Exam> findByQuestionBank(QuestionBank bank);
}

