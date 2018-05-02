package com.aq.controller.pc.tool.dto;

import java.io.Serializable;

import com.aq.facade.dto.GridToolDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: OperGridToolDTO
 * @Description: 操作网格工具传输实体
 * @author: lijie
 * @date: 2018年1月25日 下午4:58:30
 */
@Data
public class OperGridToolDTO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工具数据
	 */
	@ApiModelProperty(value = "网格工具数据",required = true)
	private GridToolDTO gridTool;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID",required = true)
	private Integer userId;
	/**
	 * 工具ID
	 */
	@ApiModelProperty(value = "工具ID：不为空则修改")
	private Integer toolId;
}
