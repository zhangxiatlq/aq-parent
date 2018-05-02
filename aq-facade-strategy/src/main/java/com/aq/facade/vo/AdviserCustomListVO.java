package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：客户经理推荐投顾时获取客户列表vo
 * @author： 张霞
 * @createTime： 2018/03/08
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "投顾推荐客户列表")
public class AdviserCustomListVO implements Serializable{
    private static final long serialVersionUID = 640989944040640053L;

    /**
     * 推荐记录表（aq_adviser_recommend）id
     */
    @ApiModelProperty(value = "推荐记录表id")
    private Integer id;
    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    /**
     * 客户（客户名称+电话号码后四位）
     */
    @ApiModelProperty(value = "客户")
    private String custom;
    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Integer customerId;
    /**
     * 价格（元）
     */
    @ApiModelProperty(value = "推荐价格 月单价")
    private BigDecimal pushPrice;

    /**
     * 是否已经推送过给客户
     * {@link com.aq.facade.contant.StrategySentEnum}
     */
    @ApiModelProperty(value = "是否已经推送过给客户：0 已推荐；1未推荐")
    private Byte sent;

    /**
     *被推荐人类型
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @ApiModelProperty(value = "被推荐人类型 2-客户；3-客户经理")
    private Byte beRecommendedRoleType;

    /**
     * 微信是否开启推送 0-开启 1-关闭
     * {@link com.aq.facade.contant.PushStateEnum}
     */
    @ApiModelProperty(value = "微信是否开启推送  0-开启 1-关闭")
    private Byte pushState;

    /**
     * 是否关注了微信  0-已关注；1-未关注
     * {@link com.aq.facade.contant.AttentionWchatEnum}
     */
    @ApiModelProperty(value = "是否关注了微信 0-已关注；1-未关注 ")
    private Byte attentionFlag;
}
