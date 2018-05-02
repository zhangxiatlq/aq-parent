package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 投顾时间配置
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_config")
@Data
public class AdviserConfig implements Serializable {
    private static final long serialVersionUID = 4926223518475185511L;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 交易所类型 1-交易所 2-深圳交易所
     */
    @Column(name = "stockType")
    private Byte stockType;

    /**
     * 类型 1-交易时间 2-竞价时间 3-委托交易时间 4-撤单时间
     */
    @Column(name = "type")
    private Byte type;

    /**
     * 开始月
     */
    @Column(name = "startMonth")
    private String startMonth;

    /**
     * 开始日
     */
    @Column(name = "startDay")
    private String startDay;

    /**
     * 开始小时
     */
    @Column(name = "startHour")
    private String startHour;

    /**
     * 开始分钟
     */
    @Column(name = "startMinute")
    private String startMinute;


    /**
     * 结束月
     */
    @Column(name = "endMonth")
    private String endMonth;

    /**
     * 结束日
     */
    @Column(name = "endDay")
    private String endDay;


    /**
     * 结束小时
     */
    @Column(name = "endHour")
    private String endHour;

    /**
     * 结束分钟
     */
    @Column(name = "endMinute")
    private String endMinute;

}