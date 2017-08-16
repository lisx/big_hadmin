package com.ducetech.hadmin.entity;

import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_file")
@Data
public class BigFile extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //文件名字
    private String fileName;
    //文件地址
    private String fileUrl;
    //文件大小
    private String fileSize;
    //文件类型
    private String fileType;
    //归属文件夹
    @ManyToOne
    private Folder folderFile;
    private String folderName;
    //归属类型 1站点 2站区 3线路 4总公司
    private String affiliation;
    //归属菜单类型
    //1人员文件 2车站文件 3培训文件 4练习考试 5规章制度 6运行图 7通知 8消防安全 9首页滚动
    private String menuType;
    //是否使用
    private String ifUse;
    //审核状态
    private String checkStatus;
    //审核ID
    private String checkId;
    @ManyToOne
    private Station stationFile;
    private String nodeCode;

}
