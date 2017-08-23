package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 答案集
 *
 * @author lisx
 * @create 2017-08-09 14:59
 **/
@Entity
@Table(name ="big_proper")
@Data
public class Proper extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 资源id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String name;
    @ManyToOne
    private Question question;
}
