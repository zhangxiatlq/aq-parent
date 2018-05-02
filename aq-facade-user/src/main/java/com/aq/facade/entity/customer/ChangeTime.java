package com.aq.facade.entity.customer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * vip续费时间记录表
 * 
 * @author zhengpeng
 * 
 * @date 2018-02-07
 */
@Table(name = "aq_change_time")
@Data
public class ChangeTime implements Serializable {
    private static final long serialVersionUID = -6625766270121021201L;
    @Id
    private Integer id;

    /**
     * 客户id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 变化前时间
     */
    @Column(name = "beforeTime")
    private Date beforeTime;

    /**
     * 变化后时间
     */
    @Column(name = "afterTime")
    private Date afterTime;
}