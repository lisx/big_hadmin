package com.ducetech.hadmin.entity;

import com.ducetech.hadmin.entity.support.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 问题库管理
 *
 * @author lisx
 * @create 2017-08-04 14:52
 **/
public class QuestionBank  extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //问题库名称
    private String name;

}
