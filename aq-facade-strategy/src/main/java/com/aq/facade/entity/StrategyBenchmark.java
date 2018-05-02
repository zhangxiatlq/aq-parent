package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略基准字典表
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Table(name = "aq_strategy_benchmark")
@Data
public class StrategyBenchmark implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 类型
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 基准指数名称
     */
    @Column(name = "benchmarkName")
    private String benchmarkName;

    /**
     * 基准指数值
     */
    @Column(name = "benchmarkNumber")
    private BigDecimal benchmarkNumber;

    /**
     * 来源(用户或其他方式创建)
     */
    @Column(name = "source")
    private Byte source;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 策略id
     */
    @Column(name = "strategyId")
    private Integer strategyId;

}