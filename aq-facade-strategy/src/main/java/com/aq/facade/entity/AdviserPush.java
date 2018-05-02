package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾推送记录表
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_push")
@Data
public class AdviserPush implements Serializable {

    private static final long serialVersionUID = 5172650139025369814L;
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 投顾id
     */
    @Column(name = "adviserId")
    private Integer adviserId;

    /**
     * 股票代码
     */
    @Column(name = "sharesCode")
    private String sharesCode;

    /**
     * 股票名称
     */
    @Column(name = "sharesName")
    private String sharesName;

    /**
     * 交易时间
     */
    @Column(name = "tradeTime")
    private Date tradeTime;

    /**
     * 方向 0：卖 1：买
     */
    @Column(name = "direction")
    private Byte direction;

    /**
     * 成交数量
     */
    @Column(name = "tradeNumber")
    private Integer tradeNumber;

    /**
     * 成交单价
     */
    @Column(name = "tradePrice")
    private BigDecimal tradePrice;

    /**
     * 成交额
     */
    @Column(name = "tradeTotalPrice")
    private BigDecimal tradeTotalPrice;

    /**
     * 成交状态 1-已成 2-未成 3-已撤
     */
    @Column(name = "tradeStatus")
    private Byte tradeStatus;

    /**
     * 真实成交单价
     */
    @Column(name = "realPrice")
    private BigDecimal realPrice;
}