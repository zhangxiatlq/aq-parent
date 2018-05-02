package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾持仓表（列表只显示一支股票最新的持仓记录）
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_position")
@Data
public class AdviserPosition implements Serializable {

    private static final long serialVersionUID = -1243547433938489638L;
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
     * 数量(持仓数量)
     */
    @Column(name = "holdingNo")
    private Integer holdingNo;

    /**
     * 参考成本
     */
    @Column(name = "referenceCost")
    private BigDecimal referenceCost;

    /**
     * 参考盈利
     */
    @Column(name = "referenceProfit")
    private BigDecimal referenceProfit;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 购买数量
     */
    @Column(name = "purchaseNo")
    private Integer purchaseNo;

    /**
     * 盈利比例 (%) 存入时 x 100
     */
    @Column(name = "profitRatio")
    private BigDecimal profitRatio;

}