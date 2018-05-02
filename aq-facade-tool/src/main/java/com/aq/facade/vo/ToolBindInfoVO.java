package com.aq.facade.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: ToolBindInfoVO
 * @Description: 绑定的工具详情
 * @author: lijie
 * @date: 2018年2月11日 下午8:46:08
 */
@Data
@ApiModel(value="工具详情")
public class ToolBindInfoVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 9157983172900735006L;
	
	/**
	 * 工具推荐ID
	 */
	@ApiModelProperty(value = "工具推荐ID")
	private Integer bindId;
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
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private Integer userId;
	/**
	 * 用户类型
	 */
	@ApiModelProperty(value = "用户类型：3、客户经理 2、客户")
	private Byte userType;
	/**
	 * 客户经理ID
	 */
	@ApiModelProperty(value = "客户经理ID")
	private Integer managerId;
	/**
	 * 客户/客户经理姓名
	 */
	@ApiModelProperty(value = "客户/客户经理姓名")
	private String realName;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 状态：0、待确定 1、工具执行中
	 */
	@ApiModelProperty(value = "状态：0、待确定 1、工具执行中")
	private Byte status;
	/**
	 * 创建人类型：3、客户经理 2、客户
	 */
	@ApiModelProperty(value = "创建人类型：3、客户经理 2、客户")
	private Byte createrType;
	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	private Integer createrId;
	
	/**
	 * 委托数量
	 */
	@ApiModelProperty(value = "委托数量")
	private Integer entrustNum;
	/**
	 * 基础价
	 */
	@ApiModelProperty(value = "基础价")
	private String basePrice;
	/**
	 * 价差
	 */
	@ApiModelProperty(value = "价差")
	private String differencePrice;
	/**
	 * 上限价
	 */
	@ApiModelProperty(value = "上限价")
	private String upperPrice;
	/**
	 * 下限价
	 */
	@ApiModelProperty(value = "下限价")
	private String lowerPrice;
	/**
	 * 短期均线天数
	 */
	@ApiModelProperty(value = "短期均线天数")
	private Integer shortDay;
	/**
	 * 长期均线天数
	 */
	@ApiModelProperty(value = "长期均线天数")
	private Integer longDay;
	/**
	 * 向上偏离值
	 */
	@ApiModelProperty(value = "向上偏离值")
	private String topDeviate;
	/**
	 * 向下偏离值
	 */
	@ApiModelProperty(value = "向下偏离值")
	private String lowerDeviate;
	/**
	 * 工具交易记录
	 */
	@ApiModelProperty(value = "工具交易记录")
	private List<ToolPushVO> pushs;

	/**
	 * 1、常规 2、专用
	 */
	@ApiModelProperty(hidden = true)
	private Byte toolType;
}
