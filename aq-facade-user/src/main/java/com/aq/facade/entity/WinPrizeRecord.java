package com.aq.facade.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 中奖记录
 * @ClassName: WinPrizeRecord
 * @Description: TODO
 * @author: lijie
 * @date: 2018年1月25日 下午3:14:08
 */
@Data
@Table(name="aq_winprize_record")
public class WinPrizeRecord {
	/**
	 * 主键ID
	 */
	@Id
	private Integer id;
	/**
	 * 用户ID
	 */
	@Column(name="userId")
	private Integer userId;
	/**
	 * 中奖编码
	 */
	@Column(name="winCode")
	private String winCode;
	/**
	 * 中奖值
	 */
	@Column(name="winValue")
	private Integer winValue;
	/**
	 * 奖项编码
	 */
	@Column(name="prizeCode")
	private String prizeCode;
	/**
	 * 创建时间
	 */
	@Column(name="createTime")
	private Date createTime;
	/**
	 * 创建人
	 */
	@Column(name="createrId")
	private Integer createrId;
	/**
	 * 是否有效：0、未领取1、已领取
	 */
	@Column(name="isReceive")
	private Byte isReceive;
}
