package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 投顾购买表
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_purchase")
@Data
public class AdviserPurchase implements Serializable {

    private static final long serialVersionUID = -8740490467200236902L;
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 经理id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 投顾id
     */
    @Column(name = "adviserId")
    private Integer adviserId;

    /**
     * 到期时间
     */
    @Column(name = "expiryTime")
    private Date expiryTime;

    /**
     * 购买人id
     */
    @Column(name = "purchaserId")
    private Integer purchaserId;

    /**
     * 购买人类型 2:客户 3 经理
     */
    @Column(name = "purchaserType")
    private Byte purchaserType;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

}