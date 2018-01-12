package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
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
    @ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinTable(name = "big_bank_area", joinColumns = { @JoinColumn(name = "bank_id") }, inverseJoinColumns = { @JoinColumn(name = "area_id") })
    private java.util.Set<Station> stations;

    private String nodeCode;
    private int ifUse=0;
    private String contain;
    @OneToMany
    private List<Exam> exams;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer createId;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer updateId;

}
