package com.aq.facade.entity;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：trademodel_boutique精品策略表中对应的实体
 * @作者： 张霞
 * @创建时间： 10:00 2018/1/19
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "trademodel_boutique")
public class Boutique extends BaseEntity {

    private static final long serialVersionUID = 7859464470294324343L;

    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username = "";

    @Column(name = "strategys_id")
    private Integer strategysId;

    @Column(name = "strategy_name")
    private String strategyName = "";

    /**
     *  策略状态 0位系统策略 1为用户自定义策略 2为用户引导式策略，3待定，4客户经理策略
     * {@link com.aq.core.constant.BoutiqueTypeEnum}
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "buytime")
    private Date buytime;

    @Column(name = "endtime")
    private Date endtime;

    @Column(name = "createtime")
    private Date createtime;

    @Column(name = "price")
    private Integer price;

    @Column(name = "headName")
    private String headName = "";

    @Column(name = "head_id")
    private Integer headId;
}
