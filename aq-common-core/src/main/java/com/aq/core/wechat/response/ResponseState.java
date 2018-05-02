package com.aq.core.wechat.response;

import java.io.Serializable;

/**
 * 
 * @ClassName: ResponseState
 * @Description: 响应状态
 * @author: lijie
 * @date: 2018年2月23日 上午11:00:51
 */
public interface ResponseState extends Serializable {
	/**
	 * 
	* @Title: success  
	* @Description: 是否成功
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	boolean isSuccess();
}
