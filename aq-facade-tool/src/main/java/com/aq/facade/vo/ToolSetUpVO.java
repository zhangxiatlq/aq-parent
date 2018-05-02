package com.aq.facade.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: ToolSetUpVO
 * @Description: 工具设置数据
 * @author: lijie
 * @date: 2018年2月11日 下午5:21:57
 */
@Data
public class ToolSetUpVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -3951247168905488640L;
	/**
	 * 设置的数据
	 */
	private Integer num;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 客户名称
	 */
	private String realName;
}
