package com.ducetech.hadmin.entity;

import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;

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
    //文件名字
    private String title;
    //文件地址
    private String proper;
    //文件大小
    private String answer;
    //1单选，2多选，3判断，4排序
    private String menuType;
    //是否使用
    private String ifUse;
    //归属问题库
    private String bankId;

}
