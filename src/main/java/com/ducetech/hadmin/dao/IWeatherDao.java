package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.WeatherInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IWeatherDao extends IBaseDao<WeatherInfo, Integer> {
    @Query(nativeQuery = true,value = "select a.* from big_weather a ,(select max(o.id) id from big_weather o) b where a.id=b.id")
    WeatherInfo findWeatherInfo();
}
