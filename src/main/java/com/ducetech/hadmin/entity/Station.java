package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.common.utils.StringUtil;
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

    private String nodeName;
    //线路站点编号
    private String nodeCode;
    //创建人
    private long createdId;
    //是否使用/假删除
    private boolean ifUse;
    //创建时间
    private Date createdAt;
    //更新时间
    private Date updatedAt;
    //站区名称
    private String stationArea;
    //所属运营公司
    private String stationCompany;
    //站区联系电话
    private String stationPhone;
    //简写名称
    private String shortName;
    //站点联系电话
    private String sitePhone;
    //简写名称
    private String shortCode;
    //排序
    private String sorting;
    public Station(){}
    public Station(String name, String nodeCode) {
        this.nodeName=name;
        this.nodeCode=nodeCode;
    }

    /**
     * 构造知识树
     * @return
     */
    public static JSONArray createTree(List<Station> nodes ){
        JSONArray array = new JSONArray();
        JSONObject obj = null;
        Station node = null;
        obj = createRoot(null);
        array.add(obj);
        for(int i=0;i<nodes.size();i++){
            node = nodes.get(i);
            obj = new JSONObject();
            obj.put("id", node.nodeCode);
            obj.put("pId",node.nodeCode.substring(0, node.nodeCode.length()-3));
            obj.put("name", node.nodeName);
            array.add(obj);
        }
        return array;
    }
    /**
     * 创建根节点
     * @param name
     * @return
     */
    public static JSONObject createRoot(String name){
        name = StringUtil.trim("车站管理系统");
        JSONObject obj = new JSONObject();
        obj.put("id", "000");
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
