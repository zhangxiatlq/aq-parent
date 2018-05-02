package com.aq.facade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: StockInfoVO
 * @Description: 股票信息数据
 * @author: lijie
 * @date: 2018年3月8日 下午2:37:58
 */
@Data
@ApiModel(value="股票信息数据")
public class StockInfoVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -471198937672885648L;
	/**
	 * 股票代码
	 */
	@ApiModelProperty(value="股票代码")
	private String code;
	/**
	 * 股票名称
	 */
	@ApiModelProperty(value="股票名称")
	private String name;
	/**
	 * 价格
	 */
	@ApiModelProperty(value="价格")
	private String price;
	/**
	 * 方向
	 */
	@ApiModelProperty(value="方向")
	private String direction;
}
