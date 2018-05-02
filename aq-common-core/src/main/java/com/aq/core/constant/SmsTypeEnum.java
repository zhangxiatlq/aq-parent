package com.aq.core.constant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import lombok.Getter;

/**
 * 
 * @ClassName: SmsTypeEnum
 * @Description: 短信枚举
 * @author: lijie
 * @date: 2018年2月9日 下午1:10:59
 */
public enum SmsTypeEnum {

	REGISTER("AQ1000001", "SMS_117522976", "注册", true, false), 
	BACK_PASSWORD("AQ1000002", "SMS_117513060","找回密码", true, true), 
	BACK_PAY_PASSWORD("AQ1000003", "SMS_119080765", "设置支付密码", true, true);
	
	/**
	 * 模板ID
	 */
	@Getter
	private String modelId;
	/**
	 * 类型编码
	 */
	@Getter
	private String typeCode;
	/**
	 * 描述
	 */
	@Getter
	private String desc;
	/**
	 * 是否缓存
	 */
	@Getter
	private boolean cache;
	/**
	 * 是否需要校验用户(不存在则不发送)
	 */
	@Getter
	private boolean check;
	
	private SmsTypeEnum(String typeCode, String modelId, String desc, boolean cache, boolean check) {
		this.typeCode = typeCode;
		this.desc = desc;
		this.modelId = modelId;
		this.cache = cache;
		this.check = check;
	}
	/**
	 * 
	 * @Title: type
	 * @author: lijie 
	 * @Description: TODO
	 * @param type
	 * @return
	 * @return: SmsTypeEnum
	 */
	public static SmsTypeEnum type(String type) {
		SmsTypeEnum result = null;
		if (StringUtils.isNotBlank(type)) {
			for (SmsTypeEnum t : SmsTypeEnum.values()) {
				if (t.getTypeCode().equals(type)) {
					result = t;
					break;
				}
			}
		}
		Assert.notNull(result, "sms type is not exists");
		return result;
	}
}
