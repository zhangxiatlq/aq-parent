package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：客户经理收藏投顾dto
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "收藏者收藏投顾参数")
public class AdviserCollectionDTO extends BaseValidate{

    private static final long serialVersionUID = 3361400166648930059L;
    /**
     * 收藏者id
     */
    @ApiModelProperty(value = "收藏者id", hidden = true)
    @NotNull(message = "[收藏者id]不能为空")
    private Integer collectionerId;
    /**
     * 策略id
     */
    @NotNull(message = "[投顾]不能为空")
    @ApiModelProperty(value = "投顾id", required = true)
    private Integer adviserId;

    /**
     * 收藏人类型 2-客户；3-客户经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "[收藏人类型]不能为空")
    @ApiModelProperty(value = "收藏人类型", hidden = true)
    private Byte collectionerType;
}
