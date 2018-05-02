package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
/**
 * 
 * @ClassName: BingToolDTO
 * @Description: 推荐工具传输数据
 * @author: lijie
 * @date: 2018年2月11日 下午4:51:49
 */
@Data
@ApiModel(value="推荐工具数据")
public class BingToolDTO extends BaseValidate {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "工具ID:多个时用逗号分隔", required = true)
	@NotNull(message = "工具不能为空")
	private String toolId;
	
	@ApiModelProperty(value = "客户ID：多个时用逗号分隔")
	private String customerIds;

	@ApiModelProperty(value = "创建人ID", hidden = true)
	@NotNull(message = "创建人ID不能为空")
	private Integer createrId;

	@ApiModelProperty(value = "工具类型编码", required = true)
	@NotBlank(message = "工具类型编码不能为空")
	private String categoryCode;
	/**
	 * 客户经理ID
	 */
	@ApiModelProperty(value = "客户经理ID：多个时用逗号分隔")
	private String managerIds;
}
