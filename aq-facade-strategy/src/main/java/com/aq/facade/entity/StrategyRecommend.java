package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略推荐记录表（客户-经理）
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Table(name = "aq_strategy_recommend")
@Data
public class StrategyRecommend implements Serializable {
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 策略id
     */
    @Column(name = "strategyId")
    private Integer strategyId;

    /**
     * 推荐id 0为平台推荐 其余则是客户经理id
     */
    @Column(name = "recommendedId")
    private Integer recommendedId;

    /**
     * 被推荐人类型 {@link com.aq.core.constant.RoleCodeEnum}
     */
    @Column(name = "beRecommendedRoleType")
    private Byte beRecommendedRoleType;

    /**
     * 被推荐id(如果推荐id为0 是客户经理id 其余是 客户id)
     */
    @Column(name = "beRecommendedId")
    private Integer beRecommendedId;

    /**
     * 微信是否开启推送( 0:开启 1:关闭)
     */
    @Column(name = "pushState")
    private Byte pushState;

    /**
     * 推荐时间
     */
    @Column(name = "pushDate")
    private Date pushDate;

    /**
     * 推荐价格  月单价
     */
    @Column(name = "pushPrice")
    private BigDecimal pushPrice;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    /**
     * 删除状态( 1:是 2:否)
     */
    @Column(name = "isDelete")
    private Byte isDelete;

}