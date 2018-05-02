package com.aq.facade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: ToolBindVO
 * @Description: 工具绑定记录
 * @author: lijie
 * @date: 2018年2月11日 下午8:24:12
 */
@Data
@ApiModel(value="工具绑定记录")
public class ToolBindVO implements Serializable,Comparable<ToolBindVO> {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -37846811747023472L;
	/**
	 * 工具唯一标识
	 */
	@ApiModelProperty(value = "工具唯一标识")
	private String toolIdenty;
	/**
	 * 工具推荐ID
	 */
	@ApiModelProperty(value = "工具推荐ID")
	private Integer bindId;
	/**
	 * 工具类别代码
	 */
	@ApiModelProperty(value = "工具类别代码")
	private String categoryCode;
	/**
	 * 股票代码
	 */
	@ApiModelProperty(value = "股票代码")
	private String stockCode;
	/**
	 * 股票名称
	 */
	@ApiModelProperty(value = "股票名称")
	private String stockName;
	/**
	 * 用户类型
	 */
	@ApiModelProperty(value = "用户类型", hidden = true)
	private Integer userType;
	
	@Override
	public int compareTo(ToolBindVO o) {
		if (null != o.getUserType() && null != userType) {
			if (o.getUserType() - userType > 0) {
				return 1;
			}
			if (o.getUserType() - userType < 0) {
				return -1;
			}
		}
		return 0;
	}
}
