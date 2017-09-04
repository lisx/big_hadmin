package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Exam;
import com.ducetech.hadmin.entity.Proper;
import com.ducetech.hadmin.entity.QuestionBank;
import com.ducetech.hadmin.entity.Station;
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
public interface IQuestionBankDao extends IBaseDao<QuestionBank,Integer> {

    QuestionBank findByName(String name);
    @Query(value="select o from QuestionBank o where o.nodeCode like:station ")
    List<QuestionBank> findByStation(@Param("station") String station);
    @Query(value="SELECT COUNT(menuType) FROM Question WHERE questionBank=:bankId and menuType=:menuType")
    Integer findByBank(@Param("bankId") QuestionBank bankId,@Param("menuType") String menuType);
}
