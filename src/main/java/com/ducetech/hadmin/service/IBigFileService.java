package com.ducetech.hadmin.service;

import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.service.support.IBaseService;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-07 09:01
 **/
public interface IBigFileService extends IBaseService<BigFile, Integer> {
    void saveOrUpdate(BigFile file);

    void delete(Integer id);
}
