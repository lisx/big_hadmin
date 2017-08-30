package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_notice")
@Data
public class Notice extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //通知标题
    private String title;
    //通知内容
    private String content;
    //文件集
    @OneToMany(mappedBy = "notice")
    private List<BigFile> bigFiles;
    //站点
    private String stationName;
    //是否使用
    private int ifUse;

    private String sendPostion;
    private String sendPerson;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    private Integer createId;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss",serialize = false)
    private Date updateTime;
    @JSONField(serialize = false)
    private Integer updateId;


}
