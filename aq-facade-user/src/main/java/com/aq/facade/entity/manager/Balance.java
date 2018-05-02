package com.aq.facade.entity.manager;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 客户经理余额表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_balance")
public class Balance extends BaseEntity {


    private static final long serialVersionUID = 2732295537436214163L;

    /**
     * 客户经理或者管理员id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 余额
     */
    @Column(name = "money")
    private BigDecimal money;

    /**
     * 人员角色：1-管理员；2-客户；3-客户经理
     */
    @Column(name = "roleType")
    private Byte roleType;

    /**
     * 累计进账
     */
    @Column(name = "totalRevenue")
    private BigDecimal totalRevenue;

    /**
     * 累计结算
     */
    @Column(name = "totalSettlement")
    private BigDecimal totalSettlement;
}