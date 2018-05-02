package com.aq.facade.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: QueryToolDTO
 * @Description: 查询工具传输数据
 * @author: lijie
 * @date: 2018年2月11日 下午2:31:52
 */
@Data
@ApiModel(value="查询工具传输数据")
public class QueryToolDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 客户经理ID
	 */
	@NotNull(message = "客户经理ID不能为空")
	@ApiModelProperty(value = "客户经理ID", hidden = true)
	private Integer managerId;
	/**
	 * 类型编码
	 */
	@NotBlank(message="类型编码不能为空")
	@ApiModelProperty(value = "工具类型编码", required = true)
	private String toolCode;
	/**
	 * 账号：手机号/用户名
	 */
	@ApiModelProperty(value = "手机号/用户名")
	private String account;
	/**
	 * 排序字段
	 */
	@ApiModelProperty(value = "排序字段")
	private String sortField;
	/**
	 * 排序类型
	 */
	@ApiModelProperty(value = "排序类型：asc(正序) desc倒序")
	private String orderType;
	/**
	 * 工具类型：1、常规 2、专用
	 */
	@ApiModelProperty(value = "工具类型：1、常规 2、专用工具,备注：查询趋势量化的时候必传")
	private Byte toolType; 
}
