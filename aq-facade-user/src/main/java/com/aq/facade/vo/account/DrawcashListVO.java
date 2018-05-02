package com.aq.facade.vo.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime:16:13 2018-2-23
 * @version:1.0
 * @desc:后台提现审核列表vo
 */
@Data
public class DrawcashListVO implements Serializable{
    private static final long serialVersionUID = 3691689123064200889L;

    /**
     *提现申请表id
     */
    private Integer id;
    /**
     * 姓名
     */
    private String realName;

    /**
     * 电话号码
     */
    private String telphone;

    /**
     * 提现金额
     */
    private BigDecimal price;

    /**
     * 提现银行
     */
    private String bankName;

    /**
     * 开户网点
     */
    private String openingAddress;
    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 提现状态(1=审核中；2=已通过；3=未通过)
     * {@link com.aq.core.constant.CashAuthStatusEnum}
     */
    private Byte status;
}
