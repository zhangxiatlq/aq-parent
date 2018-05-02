package com.aq.facade.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 策略推送实体
 * @date 2018/1/26
 * @copyright by xkw
 */
@Data
@Table(name = "weixin_pushlist")
@AllArgsConstructor
public class WeixinPushlist {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 策略id
     */
    @Column(name = "strategy_id")
    private Integer strategyId;

    /**
     * 策略名称
     */
    @Column(name = "strategy_name")
    private String strategyName;

    /**
     * 推送人名
     */
    @Column(name = "user_name")
    private String userName;
}
