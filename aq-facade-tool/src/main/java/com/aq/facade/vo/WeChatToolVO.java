package com.aq.facade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: WeChatToolVO
 * @Description: 微信工具返回数据
 * @author: lijie
 * @date: 2018年1月20日 下午4:10:25
 */
@Data
@ApiModel(value="工具推荐数据")
public class WeChatToolVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 记录ID
	 */
	@ApiModelProperty(value = "推荐ID")
	private Integer id;
	/**
	 * 工具ID
	 */
	@ApiModelProperty(value = "工具ID")
	private Integer toolId;
	/**
	 * 股票代码
	 */
	@ApiModelProperty(value = "股票代码")
	private String stockCode;
	/**
	 * 股票名称
	 */
	@ApiModelProperty(value = "股票名称")
	private String stockName;
	/**
	 * 委托数量
	 */
	@ApiModelProperty(value = "委托数量")
	private Integer entrustNum;
	/**
	 * 工具类别
	 */
	@ApiModelProperty(value = "工具类别")
	private String categoryCode;
	/**
	 * 工具类别名称
	 */
	@ApiModelProperty(value = "工具类别名称")
	private String categoryDesc;
	
	@ApiModelProperty(value = "状态:0、待确定 1、工具执行中")
	private Byte status;
}
