package com.aq.facade.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @ClassName: TrendTool
 * @Description: 趋势化工具
 * @author: lijie
 * @date: 2018年2月10日 下午3:05:23
 */
@Data
@Table(name="aq_trend_tool")
public class TrendTool extends BaseTool {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	/**
	 * 工具类型：1、常规 2、专用
	 */
	@Column(name="toolType")
	private Byte toolType;
}
