package com.aq.facade.dto.update;

import javax.validation.constraints.NotNull;

import com.aq.core.base.BaseValidate;
import com.aq.facade.dto.BaseSellingDTO;

import lombok.Data;
/**
 * 
 * @ClassName: UpdateSellingToolDTO
 * @Description: 修改卖点工具传输数据
 * @author: lijie
 * @date: 2018年2月11日 下午3:54:09
 */
@Data
public class UpdateSellingToolDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 5670285569602956132L;
	/**
	 * 卖点工具数据
	 */
	@NotNull(message="卖点工具数据不能为空")
	private BaseSellingDTO data;
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
