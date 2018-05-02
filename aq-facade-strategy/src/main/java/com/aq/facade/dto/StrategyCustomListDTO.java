package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author:zhangxia
 * @createTime:11:20 2018-2-12
 * @version:1.0
 * @desc:策略用户列表查询dto
 */
@Data
public class StrategyCustomListDTO extends BaseValidate{
    private static final long serialVersionUID = -5930194491443271785L;

    /**
     * 客户经理id
     */
    @NotNull(message = "[客户经理id]不能为空")
    private Integer managerId;
    /**
     * 策略id
     */
    @NotNull(message = "[策略id]不能为空")
    private Integer strategyId;

    /**
     * 客户信息【姓名或者手机号】--条件查询
     */
    private String customMessage;

    /**
     * 客户经理分组的id--条件查询
     */
    private Integer groupId;


}
