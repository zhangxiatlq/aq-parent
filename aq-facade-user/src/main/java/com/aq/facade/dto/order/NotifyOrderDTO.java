package com.aq.facade.dto.order;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import lombok.Data;
/**
 * 
 * @ClassName: UpdateOrderDTO
 * @Description: 订单修改
 * @author: lijie
 * @date: 2018年1月29日 下午6:02:51
 */
@Data
public class NotifyOrderDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 6020147897281719540L;
	/**
	 * 订单号
	 */
	@NotBlank(message="订单号不能为空")
	private String orderNo;
	/**
	 * 订单状态
	 */
	@NotBlank(message="订单状态不能为空")
	private String orderStatus;
	/**
	 * 第三方交易号
	 */
	@NotBlank(message="第三方交易号不能为空")
	private String zfpayNo;
	/**
	 * 支付完成时间
	 */
	@NotBlank(message="支付完成时间不能为空")
	private Date payTime;
	/**
	 * 支付类型
	 */
	@NotBlank(message="支付类型不能为空")
	private String payType;
}
