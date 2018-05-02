package com.aq.facade.vo;

import java.util.Collection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: AdviserPositionAndTradeVO
 * @Description: 投顾指标及持仓信息
 * @author: lijie
 * @date: 2018年3月13日 下午12:40:36
 */
@Data
@ApiModel(value="投顾指标及持仓信息")
public class AdviserPositionAndTradeVO extends AdviserBaseVO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -2668978850457756570L;
	/**
	 * 投顾持仓列表信息
	 */
	@ApiModelProperty(value = "投顾持仓列表信息")
	private Collection<AdviserPositionVO> positions;
}
