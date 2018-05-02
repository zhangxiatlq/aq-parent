package com.aq.facade.dto.manage;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime:9:40 2018-2-26
 * @version:1.0
 * @desc:管理员后台添加客户经理dto
 */
@Data
public class WebRegisterDTO extends RegisterBaseDTO {
    private static final long serialVersionUID = -8945678789359921111L;

    /**
     * 余额(客户经理的资产)
     */
    @NotNull(message = "[资产]不能为空")
    private BigDecimal money;

    /**
     * 管理员表id
     */
    @NotNull(message = "[维护人员]不能为空")
    private Integer userId;
    /**
     * 创建人ID
     */
    @NotNull(message = "[创建人ID]不能为空")
    private Integer createrId;
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
}
