package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
/**
 * 
 * @ClassName: TrendToolDTO
 * @Description: 趋势化工具数据传输实体
 * @author: lijie
 * @date: 2018年2月11日 下午8:09:18
 */
@Data
@ApiModel(value="趋势化工具数据")
public class TrendToolDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "股票代码", name = "stockCode", required = true)
	@NotBlank(message = "股票代码不能为空")
	private String stockCode;

	@ApiModelProperty(value = "股票名称",name = "stockName",required = true)
	@NotBlank(message = "股票代码名称不能为空")
	private String stockName;
	
}
