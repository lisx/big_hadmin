package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 学习园地 培训
 *
 * @author lisx
 * @create 2017-08-02 14:56
 **/
@Entity
@Table(name ="big_file")
@Data
public class Train extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 资源id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String fileName;
    private String fileUrl;
    private String fileSize;
    private String fileType;
    private String folderId;
    private String stationId;
    private String ifUse;
    private String checkStatus;
    private String checkId;
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
