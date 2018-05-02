package com.aq.core.wechat.response;

import java.util.Map;

import com.aq.core.wechat.pay.WXPayConstants.SignType;
/**
 * 
 * @ClassName: SignResponse
 * @Description: 签名相应数据
 * @author: lijie
 * @date: 2018年1月29日 下午9:10:06
 */
public interface SignResponse {
	
	/**
	 * 
	 * @Title: getJsApiResult
	 * @author: lijie 
	 * @Description: TODO
	 * @param signType
	 * @return
	 * @throws Exception
	 * @return: Map<String,String>
	 */
	Map<String, String> getJsApiResult(SignType signType) throws Exception ;
}
