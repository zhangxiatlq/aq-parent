package com.aq.facade.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.aq.core.base.BaseEntity;

import lombok.Data;
/**
 * 
 * @ClassName: Prize
 * @Description: 奖项
 * @author: lijie
 * @date: 2018年1月25日 下午3:11:07
 */
@Data
@Table(name="aq_prize")
public class Prize extends BaseEntity {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1321911946079585844L;
	/**
	 * 奖项描述
	 */
	@Column(name="remark")
	private String remark;
	/**
	 * 奖项值
	 */
	@Column(name="value")
	private String value;
	/**
	 * 奖项编码
	 */
	@Column(name="code")
	private String code;
	/**
	 * 是否有效：0、无效 1、有效
	 */
	@Column(name="isValid")
	private Byte isValid;
	
}
