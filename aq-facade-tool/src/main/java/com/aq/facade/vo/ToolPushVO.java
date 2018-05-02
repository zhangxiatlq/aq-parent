package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @ClassName: ToolPushVO
 * @Description: 工具推送返回数据
 * @author: lijie
 * @date: 2018年2月11日 下午8:56:42
 */
@Data
@ApiModel(value="工具交易数据")
public class ToolPushVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 7409856821434665415L;

	@ApiModelProperty(value = "表id",hidden = true)
	private Integer id;
	/**
	 * 交易时间
	 */
	@ApiModelProperty(value = "交易时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 方向：0：卖 1：买
	 */
	@ApiModelProperty(value = "方向：0：卖 1：买")
	private Byte direction;
	/**
	 * 成交价
	 */
	@ApiModelProperty(value = "成交价")
	private String tranPrice;
	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer num;
	/**
	 * 成交额
	 */
	@ApiModelProperty(value = "成交额")
	private String totalPrice;
}
