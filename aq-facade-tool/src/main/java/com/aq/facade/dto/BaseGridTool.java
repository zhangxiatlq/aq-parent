package com.aq.facade.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: BaseGridTool
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月13日 下午1:51:53
 */
@Data
@ApiModel("网格数据")
public class BaseGridTool extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 5108877550883814123L;

	/**
	 * 委托数量
	 */
	@ApiModelProperty(value = "委托数量",required = true)
	@NotNull(message="委托数量不能为空")
	private Integer entrustNum;
	/**
	 * 基础价
	 */
	@ApiModelProperty(value = "基础价",required = true)
	@NotBlank(message="基础价不能为空")
	private String basePrice;
	/**
	 * 价差
	 */
	@ApiModelProperty(value = "价差",required = true)
	@NotBlank(message="价差不能为空")
	private String differencePrice;
	/**
	 * 上限价
	 */
	@ApiModelProperty(value = "上限价",required = true)
	@NotBlank(message="上限价不能为空")
	private String upperPrice;
	/**
	 * 下限价
	 */
	@ApiModelProperty(value = "下限价",required = true)
	@NotBlank(message="下限价不能为空")
	private String lowerPrice;
}
