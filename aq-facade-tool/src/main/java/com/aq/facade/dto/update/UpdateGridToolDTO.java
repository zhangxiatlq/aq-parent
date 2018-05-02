package com.aq.facade.dto.update;

import javax.validation.constraints.NotNull;

import com.aq.core.base.BaseValidate;
import com.aq.facade.dto.BaseGridTool;

import lombok.Data;
/**
 * 
 * @ClassName: UpdateGridToolDTO
 * @Description: 网格工具传输数据
 * @author: lijie
 * @date: 2018年2月11日 下午3:55:23
 */
@Data
public class UpdateGridToolDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -5544236370505498296L;
	/**
	 * 网格工具数据
	 */
	@NotNull(message="网格工具数据不能为空")
	private BaseGridTool data;
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
