package com.aq.facade.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：usermanage_uservip表的实体
 * @作者： 张霞
 * @创建时间： 10:10 2018/1/19
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "usermanage_uservip")
public class UserVip implements Serializable {

    private static final long serialVersionUID = -3925380775074269353L;

    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "vip_end_time")
    private Date vipEndTime;
    /**
     * vip_type添加数据的时候默认就是1
     */
    @Column(name = "vip_type")
    private Integer vipType;

    /**
     * 暂时不填
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "create_date")
    private Date createDate;
}
