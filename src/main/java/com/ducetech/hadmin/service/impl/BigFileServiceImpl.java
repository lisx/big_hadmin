package com.ducetech.hadmin.service.impl;

import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.service.IBigFileService;
import com.ducetech.hadmin.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-07 09:03
 **/
@Service
public class BigFileServiceImpl extends BaseServiceImpl<BigFile,Integer> implements IBigFileService {
    @Autowired
    private IBigFileDao bigFileDao;
    @Override
    public IBaseDao<BigFile, Integer> getBaseDao() {
        return this.bigFileDao;
    }

    @Override
    public void saveOrUpdate(BigFile file) {
        save(file);
    }
    @Override
    public void delete(Integer id) {
        super.delete(id);
    }

}
