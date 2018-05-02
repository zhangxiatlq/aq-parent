package com.aq.facade.entity.wechat;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 *
 * @ClassName: WhechatTagRecord
 * @Description: 微信标签设置记录表
 * @author: lijie
 * @date: 2018年3月21日 下午2:25:05
 */
@Data
@Table(name="aq_whechat_tag_record")
public class WhechatTagRecord implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 2144858531251431635L;

	@Id
	private Integer id;
	/**
	 * 标签ID
	 */
	@Column(name = "tagId")
	private Integer tagId;
	/**
	 * 微信用户唯一标识
	 */
	@Column(name = "openId")
	private String openId;
	/**
	 * 状态：1：成功、2：失败
	 */
	@Column(name = "status")
	private Byte status;
	/**
	 * 创建时间
	 */
	@Column(name = "createTime")
	private Date createTime;
	/**
	 * 修改时间
	 */
	@Column(name = "updateTime")
	private Date updateTime;
}
