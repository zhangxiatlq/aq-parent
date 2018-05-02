package com.aq.facade.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: RealTimeAdviserVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年4月13日 下午3:56:41
 */
@Data
@ApiModel(value="投顾指标及持仓实时数据信息")
public class RealTimeAdviserVO extends AdviserBaseVO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "投顾持仓实时信息")
	private List<AdviserPositionBaseVO> positions;
}
