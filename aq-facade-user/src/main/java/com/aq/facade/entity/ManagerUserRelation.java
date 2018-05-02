package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户经理和客户之前的关联关系
 * @作者： 张霞
 * @创建时间： 14:04 2018/1/20
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "usermanage_userrelation")
public class ManagerUserRelation{
    private static final long serialVersionUID = 2742420759754737896L;
    private Integer id;
    /**
     * 客户经理id
     */
    @Column(name = "headid")
    private Integer headId;
    /**
     * 客户id
     */
    @Column(name = "userid_id")
    private Integer userId;

    /**
     * 创建时间
     */
    @Column(name = "createtime")
    private Date createTime;
}
