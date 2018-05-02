package com.aq.facade.dto.update;

import javax.validation.constraints.NotNull;

import com.aq.core.base.BaseValidate;
import com.aq.facade.dto.TrendToolDTO;

import lombok.Data;
/**
 * 
 * @ClassName: UpdateTrendToolDTO
 * @Description: 修改趋势化工具传输数据实体
 * @author: lijie
 * @date: 2018年2月11日 下午4:02:50
 */
@Data
public class UpdateTrendToolDTO extends BaseValidate {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 趋势化工具数据
	 */
	@NotNull(message="趋势化工具数据不能为空")
	private TrendToolDTO data;
	/**
	 * 修改人
	 */
	@NotNull(message="修改人不能为空")
	private Integer updaterId;
	/**
	 * 工具ID
	 */
	@NotNull(message="工具ID不能为空")
	private Integer toolId;
}
