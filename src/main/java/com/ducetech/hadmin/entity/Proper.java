package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    @Column(columnDefinition="longText")
    private String name;
    @JSONField(serialize = false)
    @ManyToOne
    private Question question;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer createId;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss",serialize = false)
    private Date updateTime;
    private Integer updateId;

    @Override
    public String toString() {
        return "";
    }
}
