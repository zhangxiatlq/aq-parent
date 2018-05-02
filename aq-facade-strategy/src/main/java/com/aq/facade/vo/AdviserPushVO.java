package com.aq.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾定时任务撤单VO
 *
 * @author 郑朋
 * @create 2018/3/8 0008
 **/
@Data
public class AdviserPushVO implements Serializable {
    private static final long serialVersionUID = 52679150950064160L;

    private Integer managerId;

    private Integer pushId;

    private Integer adviserId;

    /**
     * 股票代码
     */
    private String sharesCode;

    /**
     * 股票名称
     */
    private String sharesName;

    /**
     * 交易时间
     */
    private Date tradeTime;

    /**
     * 方向 0：卖 1：买
     */
    private Byte direction;

    /**
     * 成交数量
     */
    private Integer tradeNumber;

    /**
     * 成交单价
     */
    private BigDecimal tradePrice;

    /**
     * 成交额
     */
    private BigDecimal tradeTotalPrice;

    /**
     * 成交状态 1-已成 2-未成 3-已撤
     */
    private Byte tradeStatus;
}
