package com.aq.facade.dto.add;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.aq.core.base.BaseValidate;
import com.aq.facade.dto.GridToolDTO;

import lombok.Data;
/**
 * 
 * @ClassName: AddGridToolDTO
 * @Description: 添加网格工具：单个/批量
 * @author: lijie
 * @date: 2018年2月11日 下午3:38:22
 */
@Data
public class AddGridToolDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -5137441931570923647L;
	/**
	 * 网格数据
	 */
	@NotEmpty(message="网格数据不能为空")
	private List<GridToolDTO> datas;
	/**
	 * 创建人
	 */
	@NotNull(message="创建人不能为空")
	private Integer createrId;
}
