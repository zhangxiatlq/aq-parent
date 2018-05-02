package com.aq.facade.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: WechatBindFailVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年3月22日 下午2:20:18
 */
@Data
public class WechatBindFailVO implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -6749787416139656400L;
	/**
	 * 手机号
	 */
	private String telphone;
	/**
	 * 用户昵称
	 */
	private String nickname;
}
