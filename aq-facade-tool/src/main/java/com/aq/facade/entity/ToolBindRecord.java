package com.aq.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
* @ClassName: ToolBindRecord 
* @Description: 工具绑定记录实体 
* @author lijie
* @date 2018年1月19日 下午5:14:35 
*
 */
@Data
@Table(name = "aq_tool_bind")
public class ToolBindRecord implements Serializable {
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	/**
	 * 工具ID
	 */
	@Column(name="toolId")
	private Integer toolId;
	/**
	 * 用户ID
	 */
	@Column(name="userId")
	private Integer userId;
	/**
	 * 创建时间
	 */
	@Column(name="createTime")
	private Date createTime;
	/**
	 * 创建人ID
	 */
	@Column(name="createrId")
	private Integer createrId;
	/**
	 * 工具类别编码
	 */
	@Column(name="toolCategoryCode")
	private String toolCategoryCode;
	/**
	 * 是否删除：0、否 1、是
	 */
	@Column(name="isDelete")
	private Byte isDelete;
	/**
	 * 是否推送：0、否 1、是
	 */
	@Column(name="isPush")
	private Byte isPush;
	/**
	 * 收益
	 */
	@Column(name="profit")
	private BigDecimal profit;
	/**
	 * 是否同步：0、否 1、是
	 */
	@Column(name="isSynchro")
	private Byte isSynchro;
	/**
	 * 3、客户经理 2、客户
	 */
	@Column(name="userType")
	private Byte userType;
	/**
	 * 是否同步删除:0、否 1、是
	 */
	@Column(name="isSynchroDelete")
	private Byte isSynchroDelete;
	/**
	 * 修改时间
	 */
	@Column(name="updateTime")
	private Date updateTime;
}
