package com.aq.facade.vo.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime:18:53 2018-2-23
 * @version:1.0
 * @desc:提现审核结果详情
 */
@Data
public class DrawcashAuthDetailVO implements Serializable{
    private static final long serialVersionUID = 2638268726951149382L;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 提现金额
     */
    private BigDecimal price;
    /**
     * 审核不通过描述
     */
    private String authDesc;
}
