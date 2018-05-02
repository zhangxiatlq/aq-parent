package com.aq.facade.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: SellingToolDTO
 * @Description: 卖点工具数据
 * @author: lijie
 * @date: 2018年2月12日 下午5:55:31
 */
@Data
@ApiModel(value="卖点工具数据")
public class SellingToolDTO extends BaseSellingDTO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 股票代码
	 */
	@ApiModelProperty(value = "股票代码",required = true)
	@NotBlank(message = "股票代码不能为空")
	private String stockCode;
	/**
	 * 股票代码名称
	 */
	@ApiModelProperty(value = "股票名称",required = true)
	@NotBlank(message = "股票代码名称不能为空")
	private String stockName;
}
