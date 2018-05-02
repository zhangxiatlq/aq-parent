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
 * 删除导入失败的客户DTO
 *
 * @author 郑朋
 * @create 2018/2/10 0010
 **/
@ApiModel(value = "删除导入失败的客户记录")
@Data
public class DeleteImportDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 7789134550795822345L;


    @ApiModelProperty(value = "删除失败的客户id集合",required = true)
    @NotEmpty(message = "删除失败的客户id集合不能为空")
    private List<Integer> list;

    @ApiModelProperty(value = "客户经理id", hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;
}
