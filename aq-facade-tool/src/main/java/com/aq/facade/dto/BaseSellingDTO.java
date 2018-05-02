package com.aq.facade.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: BaseSellingDTO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月13日 下午1:55:54
 */
@Data
public class BaseSellingDTO extends BaseValidate {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 7886486439339900875L;

	/**
	 * 委托数量
	 */
	@ApiModelProperty(value = "委托数量",required = true)
	@NotNull(message="委托数量不能为空")
	private Integer entrustNum;
	/**
	 * 短期均线价格
	 */
	@ApiModelProperty(value = "短期均线天数",required = true)
	@NotNull(message="短期均线天数不能为空")
	private Integer shortDay;
	/**
	 * 长期均线价格
	 */
	@ApiModelProperty(value = "长期均线天数",required = true)
	@NotNull(message="长期均线天数不能为空")
	private Integer longDay;
	/**
	 * 向上偏离值
	 */
	@ApiModelProperty(value = "向上偏离值",required = true)
	@NotBlank(message="向上偏离值不能为空")
	private String topDeviate;
	/**
	 * 向下偏离值
	 */
	@ApiModelProperty(value = "向上偏离值",required = true)
	@NotBlank(message="向下偏离值不能为空")
	private String lowerDeviate;
}
