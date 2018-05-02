package com.aq.facade.entity;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 投顾表
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser")
@Data
public class Adviser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4827205424799571743L;

    /**
     * 价格
     */
    @Column(name = "price")
    private BigDecimal price;


    /**
     * 客户经理ID
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 是否删除：1-是  2-否
     */
    @Column(name = "isDelete")
    private Byte isDelete;


    /**
     * 总资产
     */
    @Column(name = "totalPrice")
    private BigDecimal totalPrice;

    /**
     * 投顾简介
     */
    @Column(name = "adviserDesc")
    private String adviserDesc;


    /**
     * 是否导入 1-是 2-否
     */
    @Column(name = "isImport")
    private Byte isImport;

    /**
     * 投顾名称
     */
    @Column(name = "adviserName")
    private String adviserName;

    /**
     * 是否显示 1-是 2-否
     */
    @Column(name = "isVisible")
    private Byte isVisible;

}