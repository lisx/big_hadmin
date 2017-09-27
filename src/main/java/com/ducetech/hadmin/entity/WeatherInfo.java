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
@Table(name = "big_weather")
@Data
public class WeatherInfo extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;

    private String code;

    private String txt;

    private String deg;

    private String dir;

    private String sc;

    private String spd;

    private String fl;

    private String hum;

    private String pcpn;

    private String pres;

    private String tmp;

    private String vis;

    private String aqi;

    private String co;

    private String no2;

    private String o3;

    private String pm10;

    private String pm25;

    private String qlty;

    private String so2;

    private String url;

    private String createTime;

    public WeatherInfo() {
    }

    public WeatherInfo(String code, String txt, String deg, String dir, String sc, String spd, String fl, String hum, String pcpn, String pres, String tmp, String vis, String aqi, String co, String no2, String o3, String pm10, String pm25, String qlty, String so2) {
        this.code = code;
        this.txt = txt;
        this.deg = deg;
        this.dir = dir;
        this.sc = sc;
        this.spd = spd;
        this.fl = fl;
        this.hum = hum;
        this.pcpn = pcpn;
        this.pres = pres;
        this.tmp = tmp;
        this.vis = vis;
        this.aqi = aqi;
        this.co = co;
        this.no2 = no2;
        this.o3 = o3;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.qlty = qlty;
        this.so2 = so2;
    }
}
