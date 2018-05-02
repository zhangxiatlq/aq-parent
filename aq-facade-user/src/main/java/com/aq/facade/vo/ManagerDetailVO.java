package com.aq.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：后台客户经理获取详情信息
 * @作者： 张霞
 * @创建时间： 23:21 2018/1/21
 * @Copyright @2017 by zhangxia
 */
@Data
public class ManagerDetailVO implements Serializable {
    private static final long serialVersionUID = 6644830795457806000L;

    /**
     * ID号
     */
    private Integer  idCode;

    /**
     * 手机号
     */
    private String telphone;

    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 资产
     */
    private BigDecimal money;

    /**
     * 维护人员姓名
     */
    private String employName;

    /**
     * 维护人员id
     */
    private String employeeID;

    /**
     * 备注
     */
    private String remark;

    /**
     * vip客户经理分润比例
     */
    private Double managerDivideScale;

    /**
     * 是否是员工 1-是，2-不是
     */
    private Byte isEmployee;

    /**
     * 维护人员id
     */
    private Integer userId;

}
