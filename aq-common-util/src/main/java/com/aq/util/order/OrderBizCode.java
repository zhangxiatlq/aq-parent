package com.aq.util.order;
/**
 * 
 * @ClassName: OrderBizCode
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月13日 上午9:56:08
 */
public final class OrderBizCode {

	/**
	 * 用于redis存储的键值
	 */
	public static final String INFO = "INFO";

	/**
	 * vip业务编号
	 */
	public static final String CUSTOMR_VIP = "VIP01";

	//客户--策略订单编码--star
	/**
	 * 客户策略续费主订单号编码
	 */
	public static final String CUSTOMR_RENEW_MD = "KXMD1";
	/**
	 * 客户策略续费子订单号编码
	 */
	public static final String CUSTOMR_RENEW_DD = "KXDD1";

	/**
	 * 客户策略购买主订单号编码
	 */
	public static final String CUSTOMR_BUY_MD = "KBMD1";
	/**
	 * 客户策略购买子订单号编码
	 */
	public static final String CUSTOMR_BUY_DD = "KBDD1";
	//客户--策略订单编码--end
	//客户经理--策略订单编码--star
	/**
	 * 客户经理 策略续费主订单号编码
	 */
	public static final String MANAGER_RENEW_MD = "JXMD2";
	/**
	 * 客户经理 策略续费子订单号编码
	 */
	public static final String MANAGER_RENEW_DD = "JXDD2";
	/**
	 * 客户经理 策略购买主订单号编码
	 */
	public static final String MANAGER_BUY_MD = "JBMD2";
	/**
	 * 客户经理 策略购买子订单号编码
	 */
	public static final String MANAGER_BUY_DD = "JBDD2";
	//客户经理--策略订单编码--end

	/**
	 * 客户经理 提现订单号编码
	 */
	public static final String DRAW_CASH_DD = "KHTX1";

	/**
	 * 账户流水单号
	 */
	public static final String ACCOUNT_FLOWER_DD = "ZHLS1";

	//客户--投顾订单编码--star
	/**
	 * 客户投顾续费主订单号编码
	 */
	public static final String CUSTOMER_ADVISER_RENEW_MD = "KXMD3";
	/**
	 * 客户投顾续费子订单号编码
	 */
	public static final String CUSTOMER_ADVISER_RENEW_DD = "KXDD3";

	/**
	 * 客户投顾购买主订单号编码
	 */
	public static final String CUSTOMER_ADVISER_BUY_MD = "KBMD3";
	/**
	 * 客户投顾购买子订单号编码
	 */
	public static final String CUSTOMER_ADVISER_BUY_DD = "KBDD3";
	//客户--投顾订单编码--end

	//客户经理--投顾订单编码--star
	/**
	 * 客户经理 投顾续费主订单号编码
	 */
	public static final String MANAGER_ADVISER_RENEW_MD = "JXMD4";
	/**
	 * 客户经理 投顾续费子订单号编码
	 */
	public static final String MANAGER_ADVISER_RENEW_DD = "JXDD4";
	/**
	 * 客户经理 投顾购买主订单号编码
	 */
	public static final String MANAGER_ADVISER_BUY_MD = "JBMD4";
	/**
	 * 客户经理 投顾购买子订单号编码
	 */
	public static final String MANAGER_ADVISER_BUY_DD = "JBDD4";



	//客户经理--投顾订单编码--end

	/**
	 * 客户经理 投顾购买子订单号编码
	 */
	public static final String MANAGER_ADVISER_DD = "JBDD4";

	/**
	 * 客户经理 投顾购买主订单号编码
	 */
	public static final String MANAGER_ADVISER_MD = "JBMD4";


	/**
	 * 客户投顾购买子订单号编码
	 */
	public static final String CUSTOMER_ADVISER_DD = "KBDD3";

	/**
	 * 客户投顾购买主订单号编码
	 */
	public static final String CUSTOMER_ADVISER_MD = "KBMD3";


}
