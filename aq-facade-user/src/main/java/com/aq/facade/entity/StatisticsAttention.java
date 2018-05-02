package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：统计客户经理每天的微信关注情况记录
 * @author： 张霞
 * @createTime： 2018/03/06
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "aq_statistics_attention")
public class StatisticsAttention implements Serializable{
    private static final long serialVersionUID = 2214712656693013914L;

    /**
     * 表id
     */
    @Id
    private Integer id;


    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 当天关注微信的客户数量
     */
    @Column(name = "attentionNum")
    private Integer attentionNum;

    /**
     * 当天取消的微信关注的客户数量
     */
    @Column(name = "cancelAttentionNum")
    private Integer cancelAttentionNum;

    /**
     * 客户经理id
     */
    @Column(name = "managerId")
    private Integer managerId;
}
