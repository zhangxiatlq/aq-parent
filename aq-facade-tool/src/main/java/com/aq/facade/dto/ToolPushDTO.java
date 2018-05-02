package com.aq.facade.dto;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import io.swagger.annotations.ApiModel;
import lombok.Data;
/**
 * 
 * @ClassName: ToolPushDTO
 * @Description: 工具推送传输实体
 * @author: lijie
 * @date: 2018年2月22日 下午3:33:18
 */
@Data
@ApiModel(value = "工具推送传输实体")
public class ToolPushDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -537189674288612773L;
	/**
	 * 工具标识
	 */
	@NotBlank(message = "工具标识不能为空")
	private String toolIdenty;
	/**
	 * 方向
	 */
	@NotBlank(message = "方向不能为空")
	private String direction;
	/**
	 * 成交价格
	 */
	@NotBlank(message = "价格不能为空")
	private String price;
	/**
	 * 成交数量
	 */
	private Integer number;
	/**
	 * 交易时间
	 */
	@NotBlank(message = "交易时间不能为空")
	private String tradingTime;
	/**
	 * 股票代码
	 */
	@NotBlank(message = "股票代码不能为空")
	private String stockCode;
	/**
	 * 股票代码名称
	 */
	@NotBlank(message = "股票代码名称不能为空")
	private String stockName;
}
