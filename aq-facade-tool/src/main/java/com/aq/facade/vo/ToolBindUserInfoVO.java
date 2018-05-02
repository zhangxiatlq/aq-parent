package com.aq.facade.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: ToolBindUserInfoVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月25日 上午1:13:45
 */
@Data
public class ToolBindUserInfoVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 4815132039033415466L;
	/**
	 * 
	 */
	private String openId;
	/**
	 * 工具类别编码
	 */
	private String toolCategoryCode;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 工具类型
	 */
	private Byte toolType;
}
