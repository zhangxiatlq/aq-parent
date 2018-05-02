package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 策略收藏表
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Table(name = "aq_strategy_collection")
@Data
public class StrategyCollection implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 策略id
     */
    @Column(name = "strategyId")
    private Integer strategyId;

    /**
     * 客户经理id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 收藏状态（ 0:收藏,1:取消）
     */
    @Column(name = "collectionState")
    private Byte collectionState;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;
}