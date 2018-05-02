package com.aq.facade.entity.wechat;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 *
 * @ClassName: WechatPosition
 * @Description: 微信用户上报的地理位置
 * @author: lijie
 * @date: 2018年3月21日 下午2:21:21
 */
@Data
@Table(name="aq_wechat_position")
public class WechatPosition implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -5925698462902642643L;

	@Id
	private Integer id;
	/**
	 * 微信用户标识
	 */
	@Column(name = "openId")
	private String openId;
	/**
	 * 地理位置维度
	 */
	@Column(name = "latitude")
	private Double latitude;
	/**
	 * 地理位置经度
	 */
	@Column(name = "longitude")
	private Double longitude;
	/**
	 * 地理位置精度
	 */
	@Column(name = "precisions")
	private Double precisions;
	/**
	 * 创建时间
	 */
	@Column(name = "createTime")
	private Date createTime;
}
