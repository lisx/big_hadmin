package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
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
public interface IQuestionBankDao extends IBaseDao<QuestionBank,Integer> {

    QuestionBank findByNameAndIfUse(String name,Integer ifUse);

    @Query(value="select o from QuestionBank o where (o.nodeCode like:station or o.nodeCode like:area or o.nodeCode='000') and o.ifUse=0")
    List<QuestionBank> findByStation(@Param("station") String station,@Param("area") String area);

    @Query(value="SELECT COUNT(menuType) FROM Question WHERE questionBank=:bankId and menuType=:menuType and ifUse=0")
    Integer findByBank(@Param("bankId") QuestionBank bankId,@Param("menuType") String menuType);
}
