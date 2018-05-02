package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略表
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Table(name = "aq_strategy")
@Data
public class Strategy implements Serializable {

    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 发布者头像
     */
    @Column(name = "publisherPhoto")
    private String publisherPhoto;

    /**
     * 发布者名称
     */
    @Column(name = "publisherName")
    private String publisherName;

    /**
     * 开始时间
     */
    @Column(name = "beginDate")
    private Date beginDate;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "createId")
    private Integer createId;

    /**
     * 策略名称
     */
    @Column(name = "strategyName")
    private String strategyName;

    /**
     * 策略描述
     */
    @Column(name = "strategyDesc")
    private String strategyDesc;

    /**
     * 上传py文件名称
     */
    @Column(name = "fileName")
    private String fileName;

    /**
     * 价格
     */
    @Column(name = "price")
    private BigDecimal price;
}