package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.Office2PdfUtil;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 文件管理
 *
 * @author lisx
 * @create 2017-08-04 13:54
 **/
@Entity
@Table(name = "big_file")
@Data
public class BigFile extends BaseEntity {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Integer id;
    //文件名字
    private String fileName;
    //文件地址
    @JSONField(serialize = false)
    private String fileUrl;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer createId;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss",serialize = false)
    private Date updateTime;
    @JSONField(serialize = false)
    private Integer updateId;

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

    //文件大小
    private String fileSize;
    private String byteSize;
    //文件类型
    private String fileType;
    //归属文件夹
    @ManyToOne
    private BigFile folderFile;
    private String folderName;
    //是否为文件夹 1是 0否
    private Integer ifFolder;
    //归属类型 1站点 2站区 3线路 4总公司
    @JSONField(serialize = false)
    private String affiliation;
    //归属菜单类型
    //1人员文件 2车站文件 3培训文件 4练习考试 5规章制度 6运行图 7通知 8消防安全 9首页滚动
    @JSONField(serialize = false)
    private String menuType;
    //是否使用
    private Integer ifUse;
    //审核状态
    @JSONField(serialize = false)
    private String checkStatus;
    //审核ID
    @JSONField(serialize = false)
    private String checkId;
    @ManyToOne
    //@JSONField(serialize = false)
    private Station stationFile;
    @JSONField(serialize = false)
    private String nodeCode;
    @ManyToOne
    @JSONField(serialize = false)
    private Notice notice;
    public static boolean saveFile(String upload,String folder, String nodeCode, User user, MultipartFile file, String fileType, String menuType, long flag, IBigFileDao fileDao, IStationDao stationDao) throws IOException {
        String filePath;
        BufferedOutputStream stream;
        try {
            String suffix= StringUtil.suffix(file.getOriginalFilename());
            filePath = upload + flag + file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(bytes);
            stream.close();
            if (fileType.equals(BigConstant.office)&&!suffix.equals(BigConstant.pdf)) {
                Office2PdfUtil.office2Pdf(filePath, filePath + BigConstant.pdf);
            }
            BigFile bf = new BigFile();
            bf.setFileSize("" + Math.round(file.getSize() / 1024));
            bf.setMenuType(menuType);
            bf.setFileType(fileType);
            if(menuType.equals(BigConstant.Question)) {
                bf.setFileName(user.getStationArea()+file.getOriginalFilename());
            }else{
                bf.setFileName(file.getOriginalFilename());
            }
            bf.setFileUrl(filePath);
            bf.setByteSize(file.getSize()+"");
            bf.setIfUse(0);
            stationFolder(folder, nodeCode, bf, user,fileDao,stationDao);
            fileDao.saveAndFlush(bf);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public static boolean saveFile(String upload,Integer size,String folder, String nodeCode, User user, MultipartFile file, String fileType, String menuType, long flag, IBigFileDao fileDao, IStationDao stationDao) throws IOException {
        try {
            String filePath;
            filePath=upload+file.getOriginalFilename();
            File oldFile=new File(filePath);
            filePath=upload+flag+file.getOriginalFilename();
            File newFile=new File(filePath);
            oldFile.renameTo(newFile);
            BigFile bf = new BigFile();
            bf.setFileSize("" + Math.round(size/ 1024));
            bf.setByteSize(size+"");
            bf.setMenuType(menuType);
            bf.setFileType(fileType);
            bf.setFileName(file.getOriginalFilename());
            bf.setFileUrl(filePath);
            bf.setIfUse(0);
            stationFolder(folder, nodeCode, bf, user,fileDao,stationDao);
            fileDao.saveAndFlush(bf);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static void stationFolder(String folder, String nodeCode, BigFile bf, User user, IBigFileDao fileDao, IStationDao stationDao) {
        if(null==folder||folder.equals(BigConstant.trainFolder1)||folder.equals(BigConstant.trainFolder2)||folder.equals(BigConstant.trainFolder3)||folder.equals(BigConstant.trainFolder4)){
            Station area;
            if(null!=nodeCode&&!nodeCode.equals("undefined")){
                area=stationDao.findByNodeCode(nodeCode);
            }else{
                area=stationDao.findByNodeName(user.getStationArea());
            }
            if(null!=folder){
                bf.setFolderName(folder);
                BigFile folderd=fileDao.findByFileName(folder);
                bf.setFolderFile(folderd);
            }
            if (null != area) {
                nodeCode = area.getNodeCode();
                bf.setNodeCode(nodeCode);
                bf.setStationFile(area);
            }else{
                bf.setNodeCode("000");
            }
        }else{
            bf.setFolderName(folder);
            BigFile folderd=fileDao.findByFileName(folder);
            bf.setFolderFile(folderd);
            Station station = folderd.getStationFile();
            if (null != station&&!folder.equals(BigConstant.trainFolder1)&&!folder.equals(BigConstant.trainFolder2)&&!folder.equals(BigConstant.trainFolder3)&&!folder.equals(BigConstant.trainFolder4)){
                bf.setStationFile(station);
                bf.setNodeCode(station.getNodeCode());
            }else{
                station=stationDao.findByNodeName(user.getStationArea());
                bf.setStationFile(station);
                bf.setNodeCode(station.getNodeCode());
            }
        }
        bf.setCreateTime(new Date());
        bf.setCreateId(user.getId());
    }
}
