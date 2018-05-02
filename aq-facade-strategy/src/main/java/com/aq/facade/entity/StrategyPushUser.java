package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 策略推送用户表
 *
 * @author wcyong
 * @date 2018-02-08
 */
@Table(name = "aq_strategy_push_user")
@Data
public class StrategyPushUser {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 策略推荐信息id
     */
    @Column(name = "strategyPushId")
    private Integer strategyPushId;

    /**
     * 推送人openId
     */
    @Column(name = "openId")
    private String openId;

    /**
     * 推送时间
     */
    @Column(name = "pushTime")
    private Date pushTime;

}