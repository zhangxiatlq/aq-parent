package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import com.aq.facade.contant.PurchaseStateEnum;
import lombok.Data;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：微信端  获取策略列表dto
 * @author： 张霞
 * @createTime： 2018/03/29
 * @Copyright @2017 by zhangxia
 */
@Data
public class WechatStrategyQueryDTO extends BaseValidate{
    private static final long serialVersionUID = 478448651866473350L;

    /**
     * 微信端 的手机绑定微信时，微信分配的openid
     */
    private String openId;
    /**
     * 购买状态
     * {@link PurchaseStateEnum}
     */
    private Byte purchaseState;
}
