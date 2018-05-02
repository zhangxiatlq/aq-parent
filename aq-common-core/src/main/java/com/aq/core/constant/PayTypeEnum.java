package com.aq.core.constant;

import lombok.Getter;

/**
 * @项目：phshopping-facade-order
 * @描述：系统中所有支付方式规范
 * @作者： 张霞
 * @创建时间： 13:47 2017/6/15
 * @Copyright @2017 by zhangxia
 */
public enum PayTypeEnum {
    PAY_TYPE_ALIPAY((byte)0,"支付宝支付"),
    PAY_TYPE_WECHATPAY((byte)1,"微信支付"),
    PAY_TYPE_BALANCE((byte)2,"余额支付"),
    PAY_TYPE_CREDIT((byte)3,"赊账"),
    PAY_TYPE_GZ((byte)4,"贵州银联"),
    PAY_TYPE_BJ((byte)5,"北京通用支付"),
    PAY_TYPE_SWIFT((byte)6,"威富通支付"),
    PAY_TYPE_SX((byte)7,"随行付"),
    /**
     * 信用额度支付
     * @author Mr.Chang
     * @date 2017.12.27
     * @version 2.1
     */
    PAY_TYPE_CREDIT_BALANCE((byte)8,"信用额度")
    ;
    /**
     * 标识值:支付方式
     */
	@Getter
    private byte payType;
    /**
     *标识描述信息
     */
	@Getter
    private String desc;

    PayTypeEnum(byte payType, String desc) {
        this.payType = payType;
        this.desc = desc;
    }
    
	public static PayTypeEnum getPayType(final Byte type) {
		if (null != type) {
			PayTypeEnum[] types = PayTypeEnum.values();
			for (PayTypeEnum ty : types) {
				if (type.equals(ty.getPayType())) {
					return ty;
				}
			}
		}
		return null;
	}
}
