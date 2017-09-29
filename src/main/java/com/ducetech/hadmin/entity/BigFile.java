package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.common.utils.BigConstant;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.dao.IBigFileDao;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    //md5值
    private String md5;
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
    @ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinTable(name = "big_station_file", joinColumns = { @JoinColumn(name = "file_id") }, inverseJoinColumns = { @JoinColumn(name = "station_id") })
    private List<Station> stations;
    @JSONField(serialize = false)
    @Column(columnDefinition="longText")
    private String nodeCode;
    @ManyToOne
    @JSONField(serialize = false)
    private Notice notice;
    public static void returnFile(String path, OutputStream out) throws FileNotFoundException, IOException {
        // A FileInputStream is for bytes
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] buf = null;
            if(fis.available() > 4*1024){
                buf = new byte[4*1024]; // 4K buffer
            }else {
                buf = new byte[fis.available()];
            }
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.flush();

        } finally {
            if(out!=null)
                out.close();
            if (fis != null)
                fis.close();
        }
    }
    public void initData(Integer uId,String nodeCode,String menu){
        this.setIfFolder(1);
        this.setIfUse(0);
        this.setCreateTime(new Date());
        this.setCreateId(uId);
        this.setNodeCode(nodeCode);
        this.setMenuType(menu);
    }
    public static BigFile saveFile(String md5, String upload, Integer folderId, String nodeCode, User user, MultipartFile file, String fileType, String menuType, long flag, IBigFileDao fileDao, IStationDao stationDao) throws IOException {
        String filePath;
        BufferedOutputStream stream;
        BigFile bf = new BigFile();
        try {
            filePath = upload + flag + file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(bytes);
            stream.close();
            bf.setFileSize("" + Math.round(file.getSize() / 1024));
            bf.setMenuType(menuType);
            bf.setMd5(md5);
            bf.setFileType(fileType);
            if(menuType.equals(BigConstant.Question)) {
                bf.setFileName(user.getStationArea()+file.getOriginalFilename());
            }else{
                bf.setFileName(file.getOriginalFilename());
            }
            bf.setFileUrl(filePath);
            bf.setByteSize(file.getSize()+"");
            bf.setIfUse(0);
            stationFolder(folderId, nodeCode, bf, user,fileDao,stationDao);
            fileDao.saveAndFlush(bf);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return bf;
    }
    public static BigFile saveFile(String md5,String upload,Integer size,Integer folderId, String nodeCode, User user, MultipartFile file, String fileType, String menuType, long flag, IBigFileDao fileDao, IStationDao stationDao) throws IOException {
        BigFile bf = new BigFile();
        try {
            String filePath;
            filePath=upload+file.getOriginalFilename();
            File oldFile=new File(filePath);
            filePath=upload+flag+file.getOriginalFilename();
            File newFile=new File(filePath);
            oldFile.renameTo(newFile);
            bf.setMd5(md5);
            bf.setFileSize("" + Math.round(size/ 1024));
            bf.setByteSize(size+"");
            bf.setMenuType(menuType);
            bf.setFileType(fileType);
            bf.setFileName(file.getOriginalFilename());
            bf.setFileUrl(filePath);
            bf.setIfUse(0);
            stationFolder(folderId, nodeCode, bf, user,fileDao,stationDao);
            fileDao.saveAndFlush(bf);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return bf;
    }

    /**
     * 保存站点及文件夹
     * @param folderId
     * @param nodeCode
     * @param bf
     * @param user
     * @param fileDao
     * @param stationDao
     */
    public static void stationFolder(Integer folderId, String nodeCode, BigFile bf, User user, IBigFileDao fileDao, IStationDao stationDao) {
        if(null!=folderId) {
            BigFile folder = fileDao.findOne(folderId);
            String folderName = folder.getFolderName();
            bf.setFolderName(folderName);
            bf.setFolderFile(folder);
            bf.setNodeCode(folder.getNodeCode());
            bf.setMenuType(folder.getMenuType());
        }
        List<Station> stations=new ArrayList<>();
        Station area=null;
        if(null!=nodeCode&&!nodeCode.equals("undefined")){
            String [] node=nodeCode.split(",");
            if(null!=node&&node.length>0){
                String str="";
                for(int i=0;i<node.length;i++){
                    area=stationDao.findByNodeCode(node[i]);
                    stations.add(area);
                    List<String> list=stationDao.findNodeCode(node[i]+"%");
                    str=str+StringUtil.join(list.toArray(),",");
                }
                bf.setNodeCode(str);
                bf.setStations(stations);
            }
        }else{
            if(null!=folderId){

            }else {
                area = stationDao.findByNodeName(user.getStationArea());
                if(null!=area) {
                    stations.add(area);
                    bf.setStations(stations);
                    bf.setNodeCode(area.getNodeCode());
                }
            }
        }
        bf.setCreateTime(new Date());
        bf.setCreateId(user.getId());
    }
}
