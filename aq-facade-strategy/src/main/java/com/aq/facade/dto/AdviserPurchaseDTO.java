package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 投顾购买查询DTO
 *
 * @author 郑朋
 * @create 2018/3/2
 */

@Data
public class AdviserPurchaseDTO implements Serializable {

    private static final long serialVersionUID = -7251563464438429503L;

    /**
     * 策略id
     */
    private Integer adviserId;


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