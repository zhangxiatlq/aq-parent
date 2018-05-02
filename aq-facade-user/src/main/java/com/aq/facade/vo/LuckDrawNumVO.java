package com.aq.facade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: LuckDrawNumVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年1月25日 下午8:23:18
 */
@Data
@ApiModel(value="抽奖次数返回数据")
public class LuckDrawNumVO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 2755137099989201695L;
	/**
	 * 奖项编码
	 */
	@ApiModelProperty(value="奖项编码:P1000001(赠送vip天数),P1000002(买工具赠送vip天数)")
	private String prizeCode;
	/**
	 * 抽奖次数
	 */
	@ApiModelProperty(value="抽奖次数")
	private Integer num;
}
