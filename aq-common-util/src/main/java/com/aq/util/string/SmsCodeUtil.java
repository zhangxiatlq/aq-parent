package com.aq.util.string;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 
 * @ClassName: SmsCodeUtil
 * @Description: 短信验证码工具
 * @author: lijie
 * @date: 2018年2月9日 上午11:30:35
 */
public class SmsCodeUtil {

	/**
	 * 
	 * @Title: getSmsCode
	 * @author: lijie 
	 * @Description: 获取随机验证码
	 * @return
	 * @return: String
	 */
	public static String getSmsCode() {
		return RandomStringUtils.random(6, "0123456789");
	}

}
