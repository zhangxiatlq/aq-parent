package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import com.aq.facade.contant.CollectionStateEnum;
import com.aq.facade.contant.PurchaseStateEnum;
import com.aq.facade.contant.RecommendStateEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 精品策略列表查询DTO
 *
 * @author 熊克文
 * @createDate 2018\2\8 0008
 **/
@Data
public class BoutiqueStrategyQueryDTO extends BaseValidate {

    /**
     * 经理id
     */
    @NotNull(message = "经理id不能为空")
    private Integer userId;

    /**
     * 用户类型
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "登录用户类型不能为空")
    private Byte userRoleCode;


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
