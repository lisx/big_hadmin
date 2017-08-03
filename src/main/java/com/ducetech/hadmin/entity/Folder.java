package com.ducetech.hadmin.entity;

import com.ducetech.hadmin.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 文件夹标签
 *
 * @author lisx
 * @create 2017-08-02 16:12
 **/
@Entity
@Table(name ="big_folder")
@Data
public class Folder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 资源id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String name;

}
