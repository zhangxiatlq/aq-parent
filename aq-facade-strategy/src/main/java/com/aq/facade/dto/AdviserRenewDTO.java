package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户在微信端或者pc端续费或购买投顾参数
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Data
public class AdviserRenewDTO implements Serializable {

    private static final long serialVersionUID = 5833327689251957413L;

    /**
     * 推荐id
     */
    private Integer recommendId;

    /**
     * 投顾id
     */
    private Integer adviserId;

    /**
     * 购买月数(默认为1)
     */
    private Integer purchaseMonths;
}
