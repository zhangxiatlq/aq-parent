package com.aq.facade.dto.permission;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * VIP 设置 DTO
 *
 * @author 郑朋
 * @create 2018/3/21
 **/
@Data
public class SetVIPDTO extends BaseValidate implements Serializable {


    private Integer id;

    /**
     * 分润比例（包含：vip购买），默认0
     */
    @NotNull(message = "分成比例不能为空")
    @Max(value = 1, message = "最大值不能超过1")
    @Min(value = 0, message = "最小值不能小于0")
    private Double divideScale;

}
