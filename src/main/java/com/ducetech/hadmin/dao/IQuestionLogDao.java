package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.ExamLog;
import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.entity.QuestionLog;
import org.springframework.stereotype.Repository;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-01 15:46
 **/
@Repository
public interface IQuestionLogDao extends IBaseDao<QuestionLog,Integer> {
    QuestionLog findByQuestionAndLog(Question question, ExamLog log);
}
