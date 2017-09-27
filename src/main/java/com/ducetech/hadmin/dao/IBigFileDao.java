package com.ducetech.hadmin.dao;

import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.BigFile;
import com.ducetech.hadmin.entity.Notice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-01 15:46
 **/
@Repository
public interface IBigFileDao extends IBaseDao<BigFile,Integer> {
//    @Query(value="select o from BigFile o where o.nodeCode =:nodeCode  ")
//    List<BigFile> findByStation(@Param("nodeCode") String nodeCode);
    @Query(nativeQuery = true,value = "insert into big_file(id,file_name,if_use,if_folder,menu_type)VALUES(1,'题库',0,1,'培训资料'),(2,'文档资料',0,1,'培训资料'),(3,'信号平面图',0,1,'培训资料'),(4,'案例库',0,1,'培训资料');")
    int initTrain();
    @Query(value="select o from BigFile o where (o.nodeCode like:station or o.nodeCode=:ys or  o.nodeCode=:area ) and o.ifUse=0 and o.menuType=:menuType ")
    List<BigFile> findByStationFileOrStationFileAndMenuType(@Param("station") String station,@Param("ys") String ys,@Param("area") String area,@Param("menuType") String menuType);
    @Query(value="select o from BigFile o where (o.nodeCode like:station or o.nodeCode=:ys or  o.nodeCode=:area ) and o.ifUse=0  and o.menuType=:menuType and o.fileName like:name")
    List<BigFile> findByStationFileOrStationFileAndMenuTypeAndFileName(@Param("station") String station,@Param("ys") String ys,@Param("area") String area,@Param("menuType") String menuType,@Param("name") String name);

    @Query(value = "select t from BigFile t where t.id=(select max(o.id) from BigFile  o where o.fileName like:folder)")
    BigFile findByFileName(@Param("folder") String folder);

    BigFile findByFileNameAndMenuType(String folder,String menuType);

    List<BigFile> findByFolderFile(BigFile file);

    List<BigFile> findByMenuType(String menuType);

    List<BigFile> findByNotice(Notice notice);

}
