package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    private int singleNum;
    //单选分
    private int singleScore;
    //多选题
    private int multipleNum;
    //多选分
    private int multipleScore;
    //判断题
    private int judgeNum;
    //判断分
    private int judgeScore;
    //排序提
    private int rankNum;
    //排序分
    private int rankScore;
    //归属题库
    private int bankId;
    //归属题库
    private String bankName;
    //归属站区
    private String areaName;
    //归属站点
    private String stationName;

    private String nodeCode;

    private int ifUse;
    @JSONField(serialize = false)
    @ManyToOne
    private QuestionBank questionBank;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private int createId;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private int updateId;

}
