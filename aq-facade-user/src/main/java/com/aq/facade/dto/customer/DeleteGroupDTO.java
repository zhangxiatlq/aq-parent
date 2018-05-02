package com.aq.facade.dto.customer;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 删除分组DTO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "删除分组参数")
public class DeleteGroupDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -8239863560194877064L;

    @ApiModelProperty(value = "删除分组id集合",required = true)
    @NotEmpty(message = "删除分组id集合不能为空")
    private List<Integer> list;

    @ApiModelProperty(value = "客户经理id", hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;
}
