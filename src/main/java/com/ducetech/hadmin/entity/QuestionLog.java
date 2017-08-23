package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;
import org.hibernate.EntityMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 问题管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_question_log")
@Data
public class QuestionLog extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //问题
    @ManyToOne
    private Question question;
    //已选答案
    @ManyToMany
    @JoinTable(name = "big_question_log_select_proper", joinColumns = { @JoinColumn(name = "log_id") }, inverseJoinColumns = { @JoinColumn(name = "proper_id") })
    private List<Proper> selectProper;

    @ManyToOne
    private ExamLog log;

}
