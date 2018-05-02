package com.aq.facade.dto.add;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.aq.core.base.BaseValidate;
import com.aq.facade.dto.TrendToolDTO;

import lombok.Data;
/**
 * 
 * @ClassName: AddTrendToolDTO
 * @Description: 趋势化工具传输数据
 * @author: lijie
 * @date: 2018年2月11日 下午4:01:35
 */
@Data
public class AddTrendToolDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -2531475078816482550L;
	/**
	 * 趋势化工具
	 */
	@NotEmpty(message="趋势化工具数据不能为空")
	private List<TrendToolDTO> datas;
	/**
	 * 创建人
	 */
	@NotNull(message="创建人不能为空")
	private Integer createrId;
	/**
	 * 工具类型：1、正常工具 2、专用工具
	 */
	@NotNull(message="工具类型不能为空")
	private Byte toolType;
}
