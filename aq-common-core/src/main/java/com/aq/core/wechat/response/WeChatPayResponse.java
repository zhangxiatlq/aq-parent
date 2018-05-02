package com.aq.core.wechat.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.pay.WXPayConstants.SignType;
import com.aq.core.wechat.util.WXPayUtil;

import lombok.Data;
/**
 * 
 * @ClassName: PayResponse
 * @Description: 支付响应结果数据
 * @author: lijie
 * @date: 2018年1月29日 下午4:33:59
 */
@Data
public class WeChatPayResponse implements Serializable, SignResponse {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 最终结果
	 */
	private boolean success;
	/**-- 以下字段在return_code为SUCCESS的时候有返回 ---*/
	/**
	 * 公众账号ID
	 */
	private String appId;
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 * 随机字符串
	 */
	private String nonceStr;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 业务结果
	 */
	private String resultCode;
	/**
	 * 错误代码
	 */
	private String errCode;
	/**
	 * 错误代码描述
	 */
	private String errCodeDes;
	/**
	 * 用户标识
	 */
	private String openId;
	/**
	 * 付款银行
	 */
	private String bankType;
	/**
	 * 订单金额
	 */
	private Integer totalFee;
	/**
	 * 现金支付金额
	 */
	private Integer cashFee;
	/**
	 * 商户订单号
	 */
	private String outTradeNo;
	/**
	 * 支付完成时间
	 */
	private String timeEnd;
	/**
	 * 微信支付订单号
	 */
	private String transactionId;
	/**
	 * 交易类型
	 */
	private String tradeType;
	/**
	 * 二维码链接
	 */
	private String codeUrl;
	/**
	 * 预支付交易会话标识
	 */
	private String prepayId;
	
	
	@Override
	public Map<String, String> getJsApiResult(SignType signType) throws Exception {
		if (null == signType) {
			signType = SignType.MD5;
		}
		Map<String, String> result = new HashMap<String, String>(16);
		result.put("appId", this.appId);
		result.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
		result.put("nonceStr", this.nonceStr);
		result.put("package", "prepay_id=" + prepayId);
		result.put("signType", signType.name());
		result.put("paySign", WXPayUtil.generateSignature(result, WeChatConfig.API_KEY, signType));
		return result;
	}
}
