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
 * @ClassName: ToolPush
 * @Description: 工具推送实体
 * @author: lijie
 * @date: 2018年2月10日 下午3:14:58
 */
@Data
@Table(name="aq_tool_push")
public class ToolPush implements Serializable {
    /**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 8036305889738953662L;
	/**
     * 主键ID
     */
	@Id
    private Integer id;
    /**
     * 绑定记录ID
     */
	@Column(name="bindId")
    private Integer bindId;
    /**
     * 方向：0：卖 1：买
     */
	@Column(name="direction")
    private Byte direction;
    /**
     * 成交价
     */
	@Column(name="tranPrice")
    private BigDecimal tranPrice;
    /**
     * 数量
     */
	@Column(name="num")
    private Integer num;
    /**
     * 成交额
     */
	@Column(name="totalPrice")
    private BigDecimal totalPrice;
    /**
     * 创建时间
     */
	@Column(name="createTime")
    private Date createTime;
    /**
     * 是否删除：0、否 1、是
     */
	@Column(name="isDelete")
    private Byte isDelete;
	/**
	 * 交易时间
	 */
	@Column(name="tradingTime")
	private Date tradingTime;
}