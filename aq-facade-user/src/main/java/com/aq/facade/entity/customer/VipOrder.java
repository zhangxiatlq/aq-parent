package com.aq.facade.entity.customer;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;

import com.aq.core.base.BaseEntity;

import lombok.Data;
/**
 * 
 * @ClassName: VipOrder
 * @Description: vip订单表
 * @author: lijie
 * @date: 2018年2月23日 下午3:05:07
 */
@Data
@Table(name="aq_vip_order")
public class VipOrder extends BaseEntity {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -936231419826644757L;
	/**
	 * 客户id
	 */
	@Column(name = "customerId")
	private Integer customerId;
	/**
	 * 支付金额
	 */
	@Column(name = "price")
	private BigDecimal price;
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Byte status;
	/**
	 * 订单编号
	 */
	@Column(name = "orderNo")
	private String orderNo;
	/**
	 * 支付方式
	 */
	@Column(name = "payType")
	private Byte payType;
	/**
	 * 是否删除：0、否 1、是
	 */
	@Column(name = "isDelete")
	private Byte isDelete;
	/**
	 * 购买周期
	 */
	@Column(name = "month")
	private Integer month;

	/**
	 * 客户经理分润比
	 */
	@Column(name = "managerDivideScale")
	private Double managerDivideScale;

	/**
	 * AQ VIP 分成比例
	 */
	@Column(name = "divideScale")
	private Double divideScale;

	/**
	 * 客户经理id
	 */
	@Column(name = "managerId")
	private Integer managerId;

	/**
	 * 员工id
	 */
	@Column(name = "userId")
	private Integer userId;

}
