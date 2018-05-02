package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：编辑客户经理dto
 * @作者： 张霞
 * @创建时间： 9:22 2018/1/22
 * @Copyright @2017 by zhangxia
 */
@Data
public class UpdateManagerDTO implements Serializable {
    private static final long serialVersionUID = -6363434281963090714L;

    /**
     * 客户经理id
     */
    private Integer id;
    /**
     * 真实姓名
     */
    private String name;

    /**
     * 资产
     */
    private BigDecimal accout;

    /**
     * 维护人id
     */
    private Integer employeeId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新人id
     */
    private Integer updaterId;

    /**
     * 手机号
     */
    private String telphone;

    /**
     * 分润比例（0-1之间）
     */
    private Double managerDivideScale;

    /**
     * 是否是员工枚举 1-是，2-不是
     * @see  com.aq.core.constant.IsEmployeeEnum
     */
    private Byte editIsEmployee;

}
