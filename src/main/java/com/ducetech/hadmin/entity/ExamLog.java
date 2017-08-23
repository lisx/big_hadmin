package com.ducetech.hadmin.entity;

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
    @ManyToOne
    private User user;
    //考试时间
    private Date examTime;
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

}
