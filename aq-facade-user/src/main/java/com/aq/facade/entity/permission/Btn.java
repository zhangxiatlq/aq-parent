package com.aq.facade.entity.permission;


import com.aq.core.base.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @项目：vul-facade-permission
 * @描述： 按钮实体表
 * @作者： 张霞
 * @创建时间：2018-01-19
 * @Copyright @2018 by zhangxia
 */
@Table(name = "aq_permission_btn")
@Data
public class Btn implements Serializable {

    private static final long serialVersionUID = 3409139139798172232L;

    @Id
    private Integer id;
    /**
     * 按钮名称
     */
    @Column(name = "btnName")
    private String btnName;

    /**
     * 按钮地址
     */
    @Column(name = "btnUrl")
    private String btnUrl;

    /**
     * 按钮code
     */
    @Column(name = "btnCode")
    private String btnCode;

}
