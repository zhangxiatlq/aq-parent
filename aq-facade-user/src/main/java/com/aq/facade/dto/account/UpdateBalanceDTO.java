package com.aq.facade.dto.account;

import com.aq.core.base.BaseValidate;
import com.aq.core.constant.TransCodeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改余额DTO
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@Data
public class UpdateBalanceDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -7467789227770357362L;

    /**
     * 客户经理id
     */
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;

    /**
     * 变动的金额
     */
    @NotNull(message = "修改金额不能为空")
    private BigDecimal changeBalance;

    /**
     * 交易编码
     */
    @NotNull(message = "交易编码不能为空")
    private TransCodeEnum transCodeEnum;

    /**
     * 人员角色：1-管理员；2-客户；3-客户经理
     */
    @NotNull(message = "人员角色不能为空")
    private Byte roleType;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 备注
     */
    private String remark;
}
