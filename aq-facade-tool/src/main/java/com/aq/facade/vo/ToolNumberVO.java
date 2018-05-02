package com.aq.facade.vo;

import java.io.Serializable;
/**
 * 
 * @ClassName: ToolNumberVO
 * @Description: 工具数量数据
 * @author: lijie
 * @date: 2018年3月1日 下午2:54:49
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="工具数量数据")
public class ToolNumberVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -4028438904628941493L;
	/**
	 * 剩余工具数据
	 */
	@ApiModelProperty(value="剩余工具申请数量")
	private Integer surplusNumber;
	/**
	 * 最大数量
	 */
	@ApiModelProperty(value="最大申请工具数量")
	private Integer maxNumber;
}
