package com.aq.facade.dto.order;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import lombok.Data;
/**
 * 
 * @ClassName: UpdateVipOrderDTO
 * @Description: 修改vip订单数据
 * @author: lijie
 * @date: 2018年2月23日 下午7:09:58
 */
@Data
public class UpdateVipOrderDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -2639733377573815898L;
	/**
	 * 修改人ID
	 */
	@NotNull(message = "修改人不能为空")
	private Integer updaterId;
	/**
	 * 订单编号
	 */
	@NotBlank(message = "订单编号不能为空")
	private String orderNo;
	/**
	 * 支付方式
	 */
	@NotNull(message = "支付方式不能为空")
	private Byte payType;
	/**
	 * 支付状态
	 */
	@NotNull(message = "支付状态不能为空")
	private Byte status;
	
	private String thirdOrderNo;
}
