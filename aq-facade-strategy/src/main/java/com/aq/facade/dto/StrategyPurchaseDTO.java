package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 策略购买查询DTO
 *
 * @author 熊克文
 * @date 2018-02-08
 */

@Data
public class StrategyPurchaseDTO implements Serializable {

    /**
     * 策略id
     */
    private Integer strategyId;


    /**
     * 购买人类型 2:客户 3 经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte purchaserType;

    /**
     * 购买人id
     */
    private Integer purchaserId;

    /**
     * 创建时间
     */
    private Date createTime;

}