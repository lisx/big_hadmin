package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.entity.QuestionBank;
import org.springframework.data.jpa.repository.Modifying;
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
public interface IQuestionDao extends IBaseDao<Question,Integer> {
    List<Question> findByQuestionBankAndMenuType(QuestionBank bank,String type);

    @Modifying
    @Query(nativeQuery = true,value = "DELETE FROM big_question WHERE question_bank_id = :id")
    void deleteByQuestionBank(@Param("id")Integer id);
}
