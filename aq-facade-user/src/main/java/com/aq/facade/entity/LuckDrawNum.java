package com.aq.facade.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @ClassName: LuckDrawNum
 * @Description: 抽奖次数统计
 * @author: lijie
 * @date: 2018年1月25日 下午3:10:11
 */
@Data
@Table(name="aq_luckdraw_num")
public class LuckDrawNum implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	/**
	 * 用户ID
	 */
	@Column(name="userId")
	private Integer userId;
	/**
	 * 初始化次数
	 */
	@Column(name="num")
	private Integer num;
	/**
	 * 奖项编码
	 */
	@Column(name="prizeCode")
	private String prizeCode;
}
