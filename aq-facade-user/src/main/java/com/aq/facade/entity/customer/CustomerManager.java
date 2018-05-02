package com.aq.facade.entity.customer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 客户-客户经理表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_customer_manager")
public class CustomerManager implements Serializable {

    private static final long serialVersionUID = -7025514441207001196L;
    @Id
    private Integer id;

    /**
     * 客户id
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 客户经理id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 分组id
     */
    @Column(name = "groupId")
    private Integer groupId;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
}