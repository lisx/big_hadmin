package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 考试记录
 *
 * @author lisx
 * @create 2017-08-10 18:46
 **/
@Entity
@Table(name = "big_exam_log")
@Data
public class ExamLog extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;

    private int ifUse;
    @JSONField(serialize=false)
    @ManyToOne
    private User user;
    //考试时间
    private Date examTime;
    //结束时间
    private Date endTime;
    //题库
    @ManyToOne
    private QuestionBank bank;
    @ManyToOne
    private Exam exam;
    //试题记录
    @OneToMany
    private List<QuestionLog> questionLogs;
    //分数
    private Integer score;
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
