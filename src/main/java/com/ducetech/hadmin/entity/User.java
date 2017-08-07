package com.ducetech.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户表
 *
 */
@Entity
@Table(name = "big_user")
@Data
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 用户名
	 */
	private String userName;
    /**
     * 员工工号
     */
    private String userCode;
	/**
	 * 姓名
	 */
	private String nickName;

	/**
	 * 用户密码
	 */
	private String password;

	/**
	 * 单位 站点 站区 线路
	 */

	private String station;
    private String stationArea;
    private String line;

	/**
	 * 逻辑删除状态 0 未删除 1 删除
	 */
	private Integer ifUse=0;

	/**
	 * 电话
	 */
	private String telephone;

	/**
	 * 服务信息卡
	 */
	private String fwxxkUrl;

	/**
	 * 综控员上岗证
	 */
	private String zkysgzUrl;

	/**
	 * FAS证
	 */
	private String faszUrl;

	/**
	 * 头像
	 */
	private String photoUrl;

    @JSONField(serialize = false)
	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "big_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private java.util.Set<Role> roles;

}
