package com.aq.facade.entity.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 版本
 *
 * @author 熊克文
 * @createDate 2018\2\26 0026
 **/
@Data
@Table(name = "aq_version")
public class Version {

    @Id
    private Integer id;

    /**
     * 版本号
     */
    @Column(name = "versionCode")
    private String versionCode;
}
