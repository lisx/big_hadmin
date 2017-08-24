package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.common.utils.StringUtil;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车站表
 * </p>
 *
 * @author lisx
 * @since 2017-7-1
 */
@Entity
@Table(name ="big_line_station")
@Data
public class Station extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 资源id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;
    //线路站点名称
    private String nodeName;
    //线路站点编号
    private String nodeCode;
    //是否使用/假删除
    private boolean ifUse;
    //排序
    private String sorting;
    //1人员文件 2车站文件 3培训文件 4练习考试 5规章制度 6运行图 7通知 8消防安全 9首页滚动
    private String menuType;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer createId;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer updateId;

    @OneToMany
    private List<QuestionBank> banks;
    public static JSONArray getZtrees(User user, IStationDao stationDao) {
        List<Station> stations=null;
        if(user.getStationArea().equals("运三分公司")){
            stations=stationDao.findAll();
        }else{
            Station s=stationDao.findByNodeName(user.getStationArea());
            String station=s.getNodeCode();
            stations= stationDao.findByNodeCodeStartingWith(station);
        }
        if(!user.getStationArea().equals("运三分公司")) {
            return Station.createTree(stations);
        }else{
            return Station.createRootTree(stations);
        }
    }
    /**
     * 构造知识树
     * @return
     */
    public static JSONArray createTree(List<Station> nodes ){
        JSONArray array = new JSONArray();
        JSONObject obj = null;
        Station node = null;
        for(int i=0;i<nodes.size();i++){
            node = nodes.get(i);
            obj = new JSONObject();
            obj.put("id", node.nodeCode);
            if(null!=node.nodeCode)
            obj.put("pId",node.nodeCode.substring(0, node.nodeCode.length()-3));
            obj.put("name", node.nodeName);
            array.add(obj);
        }
        return array;
    }
    public static JSONArray createRootTree(List<Station> nodes ){
        JSONArray array = new JSONArray();
        JSONObject obj = null;
//        obj = createRoot(null);
//        array.add(obj);
        for (Station node : nodes) {
            if(null==node.nodeCode){
                obj = new JSONObject();
                obj.put("id", "");
                obj.put("pId", "-1");
                obj.put("name", "运三分公司");
            }else {
                obj = new JSONObject();
                obj.put("id", node.nodeCode);
                obj.put("pId", node.nodeCode.substring(0, node.nodeCode.length() - 3));
                obj.put("name", node.nodeName);
                array.add(obj);
            }
        }
        return array;
    }
    /**
     * 创建根节点
     * @param name
     * @return
     */
    public static JSONObject createRoot(String name){
        name = StringUtil.trim("运三分公司");
        JSONObject obj = new JSONObject();
        obj.put("id", "");
        obj.put("pId", "-1");
        obj.put("name", name);
        return obj;

    }

    /**
     * 获取新的节点编号
     * @param parentCode
     * @return
     */
    public  static String getNodeCode(List<Station> nodes,String parentCode){
        String newCode="001";
        if(!nodes.isEmpty()){
            Station node = nodes.get(nodes.size()-1);
            String nodeCode = node.nodeCode;
            String subCode = nodeCode.substring(nodeCode.length()-3, nodeCode.length());
            int current = Integer.parseInt(subCode);
            current = current+1;
            newCode = StringUtil.leftJoin(current, 3, "0");
        }
        return parentCode+newCode;
    }

}
