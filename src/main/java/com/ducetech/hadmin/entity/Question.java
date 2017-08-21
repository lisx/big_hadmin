package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 问题管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_question")
@Data
public class Question extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //问题
    private String title;
    //正确答案
    private String proper;
    //已选答案
    private String answer;
    //1单选，2多选，3判断，4排序
    private String menuType;
    //是否使用
    private String ifUse;
    //归属问题库
    private String bankId;

    private String imgUrl;

    @OneToMany
    private List<Proper> propers;
    @JSONField(serialize = false)
    @ManyToOne
    private QuestionBank questionBank;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", proper='" + proper + '\'' +
                ", answer='" + answer + '\'' +
                ", menuType='" + menuType + '\'' +
                ", ifUse='" + ifUse + '\'' +
                ", bankId='" + bankId + '\'' +
                ", url='" + imgUrl + '\'' +
                ", propers=" + propers +
                ", questionBank=" + questionBank +
                '}';
    }
}
