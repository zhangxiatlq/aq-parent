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
 * 导入客户DTO
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
@Data
@ApiModel(value = "批量导入客户")
public class ImportCustomerDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 5397598010464955484L;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;


    @ApiModelProperty(value = "客户集合",required = true)
    @NotEmpty(message = "导入客户不能为空")
    List<CustomerDTO> list;

}
