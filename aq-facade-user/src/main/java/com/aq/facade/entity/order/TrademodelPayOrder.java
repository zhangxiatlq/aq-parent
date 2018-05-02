package com.aq.facade.entity.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @ClassName: TrademodelPayOrder
 * @Description: 订单实体
 * @author: lijie
 * @date: 2018年1月29日 下午3:02:34
 */
@Data
@Table(name="trademodel_pay_order")
public class TrademodelPayOrder implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	/**
	 * 订单号
	 */
	@Column(name="order_id")
	private String orderId;
	/**
	 * 商品订单号
	 */
	@Column(name="commodity_id")
	private String commodityId;
	/**
	 * 商品名称
	 */
	@Column(name="name")
	private String name;
	/**
	 * 价格
	 */
	@Column(name="price")
	private Double price;
	/**
	 * 付款价格
	 */
	@Column(name="pay_amount")
	private Double payAmount;
	/**
	 * 数量
	 */
	@Column(name="number")
	private Integer number;
	/**
	 * 购买人
	 */
	@Column(name="puchaser")
	private String puchaser;
	/**
	 * 订单状态：
	 */
	@Column(name="order_status")
	private String orderStatus;
	/**
	 * 创建时间
	 */
	@Column(name="create_time")
	private Date createTime;
	/**
	 * 支付方式
	 */
	@Column(name="pay_type")
	private String payType;
	/**
	 * 商品类型
	 */
	@Column(name="commodity_type")
	private String commodityType;
	/**
	 * 随机加密字符串
	 */
	@Column(name="voucher")
	private String voucher;
	/**
	 * 支付时间
	 */
	@Column(name="pay_time")
	private Date payTime;
	/**
	 * 三方支付流水号
	 */
	@Column(name="zfpay_no")
	private String zfpayNo;
}
