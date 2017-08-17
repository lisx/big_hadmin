package com.ducetech.hadmin.entity;

import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_running")
@Data
public class Running extends BaseEntity {
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
    //归属时间类型
    private String dateType;
    //是否使用
    private String ifUse;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //线路
    private String lineName;
    public void setFileSize(String fileSize) {
        Double size=Double.parseDouble(fileSize);
        DecimalFormat df = new DecimalFormat("#.00");
        if(size>1024){
            Double m=size/1024;
            if(m>1024){
                this.fileSize=df.format(m/1024)+"G";
            }else {
                this.fileSize = df.format(m)+ "M";
            }
        }else{
            this.fileSize = fileSize+"KB";
        }
    }

}
