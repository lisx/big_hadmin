package com.ducetech.hadmin.vo;

/**
 * Created by lisx on 2017/4/20.
 * 自定义tags 标签云
 */
public class Tags implements java.io.Serializable{
    private String name;//标签云名称
    private String color;//背景色

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
