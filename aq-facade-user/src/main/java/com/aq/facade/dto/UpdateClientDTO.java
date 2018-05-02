package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：
 * @作者： 张霞
 * @创建时间： 19:14 2018/1/20
 * @Copyright @2017 by zhangxia
 */
@Data
public class UpdateClientDTO implements Serializable {
    private static final long serialVersionUID = -6241332997458719625L;

    /**
     * 客户id
     */
    private Integer id;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 资产
     */
    private BigDecimal assets;

    /**
     * 维护人id
     *//*
    private Integer employeeId;*/

    /**
     * 客户经理id
     */
    private Integer managerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 客户电话
     */
    private String telphone;

}
