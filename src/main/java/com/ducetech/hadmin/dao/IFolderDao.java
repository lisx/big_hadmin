package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Folder;

import java.util.List;

/**
 * 文件夹标签
 *
 * @author lisx
 * @create 2017-08-02 17:33
 **/
public interface IFolderDao extends IBaseDao<Folder,Integer> {
    List<Folder> findByStation(String station);

}
