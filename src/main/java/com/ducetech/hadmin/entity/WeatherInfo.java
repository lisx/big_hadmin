package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_weather")
@Data
public class WeatherInfo extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //城市
    private String city;
    //温度
    private String temp;
    //低温
    private String temp1;
    //高温
    private String temp2;
    //天气
    private String weather;
    //发布时间
    private String ptime;
    //风向
    private String wd;
    //风级
    private String ws;
    //湿度
    private String sd;

    private String createTime;

}
