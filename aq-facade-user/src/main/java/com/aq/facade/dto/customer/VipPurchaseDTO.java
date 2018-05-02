package com.aq.facade.dto.customer;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: VipPurchaseDTO
 * @Description: vip购买传输实体
 * @author: lijie
 * @date: 2018年2月23日 下午2:46:29
 */
@Data
@ApiModel(value="vip购买传输数据")
public class VipPurchaseDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 3248770257491554355L;
	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格", required = true)
	@NotBlank(message = "价格不能为空")
	private String price;
	/**
	 * 购买数量
	 */
	@ApiModelProperty(value = "购买数量", required = true)
	@NotNull(message = "购买数量不能为空")
	private Integer num;
	/**
	 * 单位：YEAR、MONTH、DAY
	 */
	@ApiModelProperty(value = "单位：year、month、day", hidden = true)
	private String unit;
	/**
	 * 客户ID
	 */
	@ApiModelProperty(value = "客户ID", hidden = true)
	@NotNull(message = "客户ID不能为空")
	private Integer customerId;
}
