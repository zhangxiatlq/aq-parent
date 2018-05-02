package com.aq.facade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: ToolCacheVO
 * @Description: 工具缓存数据
 * @author: lijie
 * @date: 2018年3月6日 下午2:29:33
 */
@Data
@ApiModel(value="实时数据")
public class ToolCacheVO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 7512534292970573714L;
	/**
	 * 工具标识
	 */
	@ApiModelProperty(value="工具唯一标识")
	private String toolIdenty;
	/**
	 * 最新价
	 */
	@ApiModelProperty(value="最新价")
	private String latestPrice;
	/**
	 * 方向：看多、看空、观望
	 */
	@ApiModelProperty(value="方向：看多、看空、观望")
	private String direction;
	
}
