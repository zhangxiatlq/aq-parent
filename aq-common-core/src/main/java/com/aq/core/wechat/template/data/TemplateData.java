package com.aq.core.wechat.template.data;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: TemplateData
 * @Description: TODO
 * @author: lijie
 * @date: 2018年4月17日 上午11:00:40
 */
@Data
public class TemplateData implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 微信用户标识
	 */
	private String openId;
	
}
