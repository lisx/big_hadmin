package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 问题库管理
 *
 * @author lisx
 * @create 2017-08-04 14:52
 **/
@Entity
@Table(name = "big_question_bank")
@Data
public class QuestionBank  extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private int id;
    //问题库名称
    private String name;
    @OneToMany
    private List<Question> questionList;
    @ManyToOne
    private Station station;

}
