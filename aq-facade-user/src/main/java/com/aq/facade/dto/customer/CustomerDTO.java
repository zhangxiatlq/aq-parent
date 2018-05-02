package com.aq.facade.dto.customer;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 客户DTO
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
@Data
@ApiModel(value = "客户数据")
public class CustomerDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 1708683005258526731L;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "客户手机号")
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "请输入正确的手机号")
    private String telphone;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "客户姓名")
    @NotNull(message = "姓名不能为空")
    private String realName;
}
