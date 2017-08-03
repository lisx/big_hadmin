package com.ducetech.hadmin.entity;

import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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

}
