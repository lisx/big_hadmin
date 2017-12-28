package com.ducetech.hadmin.service;

import com.alibaba.fastjson.JSONArray;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.service.support.IBaseService;

/**
 * 车站信息类
 *
 * @author lisx
 * @create 2017-08-01 16:00
 **/
public interface IStationService extends IBaseService<Station, Integer> {
    /**
     * 获取角色的权限树
     * @param ifUse
     * @return
     */
    JSONArray tree(int ifUse);

    Station findByNodeName(String nodeName);

    /**
     * 修改或者新增资源
     * @param station
     */
    Station saveOrUpdate(Station station);
}
