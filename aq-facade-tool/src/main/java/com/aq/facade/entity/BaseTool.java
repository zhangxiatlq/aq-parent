package com.aq.facade.entity;

import javax.persistence.Column;

import com.aq.core.base.BaseEntity;

import lombok.Data;
/**
 * 
 * @ClassName: BaseTool
 * @Description: 工具共用实体
 * @author: lijie
 * @date: 2018年2月10日 下午3:14:29
 */
@Data
public class BaseTool extends BaseEntity {
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -3963154190915856914L;
	/**
	 * 工具类别编码
	 */
	@Column(name="toolCode")
	private String toolCode;
	/**
	 * 股票代码
	 */
	@Column(name="stockCode")
	private String stockCode;
	/**
	 * 股票代码名称
	 */
	@Column(name="stockName")
	private String stockName;
	/**
	 * 状态：0、待确定 1、工具执行中
	 */
	@Column(name="status")
	private Byte status;
	/**
	 * 是否删除：0、未删除 1、已删除
	 */
	@Column(name="isDelete")
	private Byte isDelete;
	/**
	 * 归属人ID
	 */
	@Column(name="managerId")
	private Integer managerId;
	/**
	 * 创建人类型
	 */
	@Column(name="createrType")
	private Byte createrType;
}
