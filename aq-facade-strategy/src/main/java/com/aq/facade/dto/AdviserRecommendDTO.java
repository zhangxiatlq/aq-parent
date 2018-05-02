package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：客户经理推荐投顾给客户dto
 * @author： 张霞
 * @createTime： 2018/03/08
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "客户经理推荐投顾给客户参数dto")
public class AdviserRecommendDTO extends BaseValidate{

    private static final long serialVersionUID = 1134913402360342234L;

    /**
     * 客户经理id
     */
    @ApiModelProperty(value = "客户经理id",hidden = true)
    @NotNull(message = "[客户经理id]不能为空")
    private Integer managerId;

    /**
     * 推荐的投顾id
     */
    @ApiModelProperty(value = "推荐的投顾id",required = true)
    @NotNull(message = "[推荐的投顾id]不能为空")
    private Integer adviserId;



    /**
     * 客户推荐给客户的信息
     */
    private List<AdviserRecommendCustomDTO> adviserRecommendCustomDTOS;

}
