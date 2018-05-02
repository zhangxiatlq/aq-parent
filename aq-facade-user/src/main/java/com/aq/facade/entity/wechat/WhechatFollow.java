package com.aq.facade.entity.wechat;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 *
 * @ClassName: WhechatFollow
 * @Description: 微信关注信息表
 * @author: lijie
 * @date: 2018年3月21日 下午2:22:14
 */
@Data
@Table(name="aq_whechat_follow")
public class WhechatFollow implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	/**
	 * 微信用户标识
	 */
	@Column(name = "openId")
	private String openId;
	/**
	 * 关注状态:1、未关注 2、已关注
	 */
	@Column(name = "status")
	private Byte status;
	/**
	 * 关注时间
	 */
	@Column(name = "followTime")
	private Date followTime;
	/**
	 * 取消关注时间
	 */
	@Column(name = "unfollowTime")
	private Date unfollowTime;
	/**
	 * 创建时间
	 */
	@Column(name = "createTime")
	private Date createTime;
}
