package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import com.aq.facade.contant.CollectionStateEnum;
import com.aq.facade.contant.PurchaseStateEnum;
import com.aq.facade.contant.RecommendStateEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾查询dto
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@Data
public class AdviserQueryDTO extends BaseValidate{

    private static final long serialVersionUID = -3840196709720016684L;

    /**
     * 经理id
     */
    @NotNull(message = "经理id不能为空")
    private Integer managerId;

    /**
     * 用户类型
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "登录用户类型不能为空")
    private Byte collectionerType;


    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 购买状态
     * {@link PurchaseStateEnum}
     */
    private Byte purchaseState;

    /**
     * 收藏状态
     * {@link CollectionStateEnum}
     */
    private Byte collectionState;

    /**
     * 推荐状态
     * {@link RecommendStateEnum}
     */
    private Byte recommendState;

    /**
     * 排序类型 asc-正序，desc-倒序
     */
    private String orderType;
    /**
     * 排序字段:1=年化收益(annualIncome),2=开始时间(indexTime),3=今日收益,4=累计收益
     */
    private Byte orderByContent;


}
