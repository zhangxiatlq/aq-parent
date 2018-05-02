package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 投顾 订单修改 DTO
 *
 * @author 郑朋
 * @create 2018/3/28
 **/
@Data
public class AdviserAddOrderDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -5247985535167337844L;

    /**
     * 购买人类型 2 客户  3 客户经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "[购买人类型]不能为空")
    private Byte purchaserType;

    /**
     * 购买人id
     */
    @ApiModelProperty(value = "购买人id（客户id或者客户经理id）", required = true)
    private Integer purchaserId;


    @NotEmpty(message = "请选择购买投顾信息")
    private List<AdviserRenewDTO> adviserRenewDTOS;
}
