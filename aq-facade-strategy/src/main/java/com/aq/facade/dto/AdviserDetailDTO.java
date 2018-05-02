package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾详情查询DTO
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdviserDetailDTO extends BaseValidate{

    private static final long serialVersionUID = -3354676608965489799L;

    /**
     * 投顾id
     */
    @NotNull(message = "投顾id不能为空")
    private Integer adviserId;


    /**
     * 购买人类型 2:客户 3 经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "购买人类型不存在")
    private Byte purchaserType;

    /**
     * 购买人id
     */
    @NotNull(message = "购买人id不能为空")
    private Integer purchaserId;

    /**
     * 创建时间
     */
    private Date createTime;
}
