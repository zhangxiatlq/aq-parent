package com.aq.facade.dto.add;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.aq.core.base.BaseValidate;
import com.aq.facade.dto.SellingToolDTO;

import lombok.Data;
/**
 * 
 * @ClassName: AddSellingToolDTO
 * @Description: 卖点工具传输数据
 * @author: lijie
 * @date: 2018年2月11日 下午3:48:51
 */
@Data
public class AddSellingToolDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 网格数据
	 */
	@NotEmpty(message="网格数据不能为空")
	private List<SellingToolDTO> datas;
	/**
	 * 创建人
	 */
	@NotNull(message="创建人不能为空")
	private Integer createrId;
	
}
