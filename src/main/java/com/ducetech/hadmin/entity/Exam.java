package com.ducetech.hadmin.entity;

import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 试卷管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_exam")
@Data
public class Exam extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //试卷名称
    private String examName;
    //单选题
    private Integer singleNum;
    //单选分
    private Integer singleScore;
    //多选题
    private Integer multipleNum;
    //多选分
    private Integer multipleScore;
    //判断题
    private Integer judgeNum;
    //判断分
    private Integer judgeScore;
    //排序提
    private Integer rankNum;
    //排序分
    private Integer rankScore;
}
